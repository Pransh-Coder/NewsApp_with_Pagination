package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    RequestQueue requestQueue;
    ProgressBar progressBar;
    List<NewsPojo> newsPojoList = new ArrayList<>();

    public static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.dialog);

        requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?country=in&apiKey=89640794f4d74222be4fddb4e60902d0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("status").equals("ok")){

                        JSONArray jsonArray = jsonObject.getJSONArray("articles");
                        counter++;
                        for (int i=0;i<10;i++){

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            JSONObject jsonObject2 = jsonObject1.getJSONObject("source");

                            NewsPojo newsPojo = new NewsPojo();

                            for (int j=0;j<jsonObject2.length();j++){

                                String id = jsonObject2.getString("id");
                                String name = jsonObject2.getString("name");

                                Log.e("retrived",id+"  --->"+name);

                                //newsPojo.setSource(jsonObject2.getString("name"));

                            }
                            String title = jsonObject1.getString("title");
                            String publishedAt = jsonObject1.getString("publishedAt");
                            String urlImage = jsonObject1.getString("urlToImage");
                            String description = jsonObject1.getString("description");

                            newsPojo.setTitle(jsonObject1.getString("title"));
                            newsPojo.setPublished(jsonObject1.getString("publishedAt"));
                            newsPojo.setImageLink(jsonObject1.getString("urlToImage"));
                            newsPojo.setDescription(jsonObject1.getString("description"));
                            newsPojo.setSource(jsonObject2.getString("name"));

                            Log.e("keyvalues",title+" **** "+publishedAt+" ---- "+urlImage);

                            newsPojoList.add(newsPojo);

                        }
                        adapter = new RecyclerAdapterNewsItems(MainActivity.this,newsPojoList,progressBar);
                        recyclerView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}