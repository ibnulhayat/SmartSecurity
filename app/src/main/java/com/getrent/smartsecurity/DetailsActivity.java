package com.getrent.smartsecurity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import static com.getrent.smartsecurity.helper.Apis.baseUrl;
import static com.getrent.smartsecurity.helper.Apis.imageList;
import static com.getrent.smartsecurity.helper.Apis.imgDelet;

public class DetailsActivity extends AppCompatActivity {

    private LinearLayout layoutTop;
    private LinearLayout layoutBottom;
    private ImageView ivBack;
    private ImageView ivMainImage;
    private ImageView ivShare;
    private ImageView ivDownload;
    private ImageView ivDelete;
    private TextView tvLevelName;
    private String position,id,imageName, imageUrl, isShow="show";
    private static final int PERMISSION_CODE = 1000;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initComponents();
        requestQueue = Volley.newRequestQueue(DetailsActivity.this);

        position = getIntent().getStringExtra("Position");
        id = getIntent().getStringExtra("ID");
        imageName = getIntent().getStringExtra("ImageName");
        imageUrl = getIntent().getStringExtra("ImageUrl");
        tvLevelName.setText(imageName);
        if (!imageUrl.equals("null")) {
            Picasso.get().load(baseUrl+imageUrl).placeholder(R.drawable.gallery).fit().into(ivMainImage);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow.equals("show")) {
                    layoutTop.setVisibility(View.VISIBLE);
                    layoutBottom.setVisibility(View.VISIBLE);
                    isShow = "hide";
                } else {
                    layoutTop.setVisibility(View.GONE);
                    layoutBottom.setVisibility(View.GONE);
                    isShow = "show";
                }
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("image/*");
                String shareBody = baseUrl+imageUrl;
                myIntent.putExtra(Intent.EXTRA_STREAM, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share Image"));
            }
        });
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permisssion = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permisssion,PERMISSION_CODE);
                    }else {
                        startDownload();
                    }
                }else {
                    startDownload();
                }
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDelete(id,imageUrl);
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
        ivBack = findViewById(R.id.ivBack);
        tvLevelName = findViewById(R.id.tvLevelName);
        ivMainImage = findViewById(R.id.ivMainImage);
        ivShare = findViewById(R.id.ivShare);
        ivDownload = findViewById(R.id.ivDownload);
        ivDelete = findViewById(R.id.ivDelete);
        layoutTop = findViewById(R.id.layoutTop);
        layoutBottom = findViewById(R.id.layoutBottom);
    }

    private void startDownload() {
        String name = String.valueOf(System.currentTimeMillis());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(baseUrl+imageUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(name+".jpeg");
        request.setDescription("Image Downloading.....");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+".jpeg");
        DownloadManager manager =(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


    private void imageDelete(String id,String name) {
        String url = imgDelet+"id="+id+"&imageName="+name;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    if (response.equals("Delete Successfully")){

                        Toast.makeText(DetailsActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        imageList.remove(Integer.parseInt(position));
                        onBackPressed();
                    }else {
                        Toast.makeText(DetailsActivity.this, response, Toast.LENGTH_LONG).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.toString());
            }
        });
        requestQueue.getCache().clear();
        requestQueue.add(request);

    }
}
