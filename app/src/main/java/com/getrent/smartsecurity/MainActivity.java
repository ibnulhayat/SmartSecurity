package com.getrent.smartsecurity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutLive;
    private LinearLayout layoutImage;
    private LinearLayout layoutVideo;
    private LinearLayout layoutSetting;
    private LinearLayout layoutMain;
    private WebView webView;
    private ImageView ivPlay;
    private ProgressBar progressBar;
    private String message = "Under Construction";
    private String webUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initComponents();
        FirebaseMessaging.getInstance().subscribeToTopic("global");

        layoutLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage(message);
            }
        });

        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("LevelName","Save Images List");
                startActivity(intent);

            }
        });
        layoutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
//                intent.putExtra("LevelName","Save Video List");
//                startActivity(intent);
                showMessage(message);
            }
        });

        layoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("LevelName","Camera Settings");
                startActivity(intent);
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webUrl.isEmpty()){
                    showDialog();
                }else {
                    webView();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponents() {
        layoutLive = findViewById(R.id.layoutLive);
        layoutImage = findViewById(R.id.layoutImage);
        layoutVideo = findViewById(R.id.layoutVideo);
        layoutSetting = findViewById(R.id.layoutSetting);
        layoutMain = findViewById(R.id.layoutMain);
        webView = findViewById(R.id.webView);
        ivPlay = findViewById(R.id.ivPlay);
        progressBar = findViewById(R.id.progressBar);

    }

    private void showMessage(String message){
        Snackbar snackbar = Snackbar.make(layoutMain, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.ip_address);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText etMessage = dialog.findViewById(R.id.etMessage);
        Button okButton = dialog.findViewById(R.id.okButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);


        // dialog ok button
        okButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String mess = etMessage.getText().toString().trim();
                dialog.dismiss();
                etMessage.clearFocus();
                etMessage.setShowSoftInputOnFocus(false);
                webUrl="http://"+mess+":8000/index.html";
                webView();
            }
        });

        // dialog cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void webView() {

        //kProgressHUD.show();
        if (webUrl != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyWebviewClient());
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);

                    if (newProgress==100){
                        progressBar.setVisibility(View.GONE);
                    }
                    super.onProgressChanged(view, newProgress);
                }
            });
            webView.loadUrl(webUrl);
            progressBar.setProgress(0);
        } else {
            Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public class MyWebviewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ivPlay.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            ivPlay.setVisibility(View.GONE);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ivPlay.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            ivPlay.setVisibility(View.VISIBLE);
        }
    }

}
