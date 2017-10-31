package com.mobile.hoozoo;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;



import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

public class LoginActivity extends AppCompatActivity {

    final String TAG = getClass().getName();

    /* Hoozoo's api key, client secret, redirect callback URL, respectively */
    private static String clientId = "78kan9pe2wmpif";
    private String clientSecret = "P7heMfqljkbVGvKg";
    private static String redirectUrl = "http://com.amalbit.redirecturl";
    private static final String STATE = "E3ZYKC1T6H2yP4z";

    //These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    ProgressDialog webProgDialog;
    OAuth20Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //hide status bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        TextView urlText = (TextView) findViewById(R.id.auth_url);

        //get the webView from the layout
        final WebView webView = (WebView) findViewById(R.id.login_webview);
        webView.requestFocus(View.FOCUS_DOWN);

        //Show a progress dialog to the user
        webProgDialog = ProgressDialog.show(this, "", "loading",true);

        //Set a custom web view client
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //Then only we do is dismiss the progressDialog, in case we are showing any.

                if (webProgDialog != null)
                    webProgDialog.dismiss();

            }

            @SuppressWarnings("deprecation") /* Suppressed to support all versions of Android */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {

                Log.d(TAG, "OverrideUrl: " + authorizationUrl);

                webView.loadUrl(authorizationUrl);
                Log.d(TAG,"");
                return true;
            }

        });


        service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope("r_basicprofile r_emailaddress")
                .callback(redirectUrl)
                .build(LinkedInApi20.instance());



        /*
        *   I need to go to the URL and authorize, I will get a code.
        *   Then I trade the code for the token; use the token to get protected resources
        * */
        // Obtain the Authorization URL
        Log.d(TAG, "Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();
        Log.d(TAG, "Got the Authorization URL!");
        Log.d(TAG, "Now go and authorize ScribeJava here:");
        Log.d(TAG, "Auth URL: " + authorizationUrl);
        Log.d(TAG, "And paste the authorization code here");


        //webView.loadUrl(getAuthorizationUrl());
        webView.loadUrl(authorizationUrl);

        /*// Trade the Request Token and Verfier for the Access Token
        Log.d(TAG, "Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        Log.d(TAG, "Got the Access Token!");
        Log.d(TAG, "(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");*/

    }

    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+clientId
                +AMPERSAND+STATE_PARAM+EQUALS+STATE
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+redirectUrl;
    }


}
