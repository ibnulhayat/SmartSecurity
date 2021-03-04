package com.getrent.smartsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.getrent.smartsecurity.helper.Apis.imageList;
import static com.getrent.smartsecurity.helper.Apis.settingGet;
import static com.getrent.smartsecurity.helper.Apis.settingPost;

public class SettingActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvLevelName;
    private EditText etRotation;
    private EditText etRegulation;
    private EditText etBrightness;
    private Button buttonSubmit;
    private ProgressBar progressBar;
    private String levelName;
    private String id;
    private String video_rec;
    private String rotation;
    private String regulation;
    private String brightness;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    @Override
    protected void onStart() {
        super.onStart();
        initComponents();

        requestQueue = Volley.newRequestQueue(SettingActivity.this);
        getImageList();
        imageList.clear();

        levelName = getIntent().getStringExtra("LevelName");
        tvLevelName.setText(levelName);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotation = etRotation.getText().toString().trim();
                regulation = etRegulation.getText().toString().trim();
                brightness = etBrightness.getText().toString().trim();

                if (rotation.isEmpty()) {
                    rotation = "180";
                }
                if (regulation.isEmpty()) {
                    regulation = "720x480";
                }
                if (brightness.isEmpty()) {
                    brightness = "50";
                }

                dataPost();
                buttonSubmit.setTextColor(getResources().getColor(R.color.ofWhite));
                progressBar.setVisibility(View.VISIBLE);
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
        etRotation = findViewById(R.id.etRotation);
        etRegulation = findViewById(R.id.etRegulation);
        etBrightness = findViewById(R.id.etBrightness);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        progressBar = findViewById(R.id.progressBar);

    }

    private void getImageList() {
        StringRequest request = new StringRequest(Request.Method.GET, settingGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object  = new JSONObject(response);

                    id = object.getString("id");
                    video_rec = object.getString("video_record");
                    rotation = object.getString("rotation");
                    regulation = object.getString("regulation");
                    brightness = object.getString("brightness");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("VOLLEY", e.toString());

                }

                etRotation.setText(rotation);
                etRegulation.setText(regulation);
                etBrightness.setText(brightness);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.toString());
            }
        });
        requestQueue.add(request);
        requestQueue.getCache().clear();

    }


    private void dataPost(){

        StringRequest request = new StringRequest(Request.Method.POST, settingPost, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        if (response.equals("Update Successfully")){
                            Toast.makeText(SettingActivity.this, "Post Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SettingActivity.this, MainActivity.class));
                            progressBar.setVisibility(View.GONE);
                        }
                    }}, 3000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", id);
                MyData.put("video_record", video_rec);
                MyData.put("rotation", rotation);
                MyData.put("regulation", regulation);
                MyData.put("brightness", brightness);
                return MyData;
            }
        };

        requestQueue.add(request);
    }

}
