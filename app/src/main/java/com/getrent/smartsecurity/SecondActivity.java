package com.getrent.smartsecurity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getrent.smartsecurity.modelClass.ImageList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.getrent.smartsecurity.helper.Apis.baseUrl;
import static com.getrent.smartsecurity.helper.Apis.imageList;
import static com.getrent.smartsecurity.helper.Apis.imgList;

public class SecondActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvLevelName;
    private String levelName;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initComponents();

        requestQueue = Volley.newRequestQueue(SecondActivity.this);
        getImageList();
        imageList.clear();
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImageListAdapter(this, imageList);
        recyclerView.setAdapter(adapter);


        levelName = getIntent().getStringExtra("LevelName");
        tvLevelName.setText(levelName);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
        recyclerView = findViewById(R.id.rvList);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getImageList() {
        imageList.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, imgList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2  = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("itemsList");
                    baseUrl = object2.getString("staus");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String dateTime = object.getString("dateTime");
                        String img = object.getString("img_mane");

                        imageList.add(new ImageList(id,dateTime,img));
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("VOLLEY", e.toString());
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
        requestQueue.getCache().clear();
        requestQueue.add(request);

    }
}
