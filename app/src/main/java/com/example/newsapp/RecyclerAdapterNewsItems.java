package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterNewsItems extends RecyclerView.Adapter<RecyclerAdapterNewsItems.ViewHolder> {

    Context context;
    List<NewsPojo> newsPojoList = new ArrayList<>();
    ProgressBar progressBar;

    public RecyclerAdapterNewsItems(Context context, List<NewsPojo> newsPojoList,ProgressBar progressBar) {
        this.context = context;
        this.newsPojoList = newsPojoList;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_list, parent, false);
        return new RecyclerAdapterNewsItems.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.headline.setText(newsPojoList.get(position).getTitle());
        holder.publishDate.setText(newsPojoList.get(position).getPublished());
        holder.source.setText(newsPojoList.get(position).getSource());
        Picasso.get().load(newsPojoList.get(position).getImageLink()).into(holder.imageView);

        if (position == newsPojoList.size() - 1) {          // It used to check when we reach the last item of recyclerView in this case it is 10
            progressBar.setVisibility(View.VISIBLE);
            apiCall();
        }

        final String imageLink = newsPojoList.get(position).getImageLink();
        final String headline = newsPojoList.get(position).getTitle();
        final String description = newsPojoList.get(position).getDescription();
        final String source = newsPojoList.get(position).getSource();
        final String published = newsPojoList.get(position).getPublished();

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,NewsDetail.class);
                intent.putExtra("imageLink",imageLink);
                intent.putExtra("headline",headline);
                intent.putExtra("description",description);
                intent.putExtra("source",source);
                intent.putExtra("published",published);
                context.startActivity(intent);

            }
        });
    }
    public void apiCall(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?country=in&apiKey=89640794f4d74222be4fddb4e60902d0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("status").equals("ok")){

                        JSONArray jsonArray = jsonObject.getJSONArray("articles");
                        //MainActivity.counter++;
                        for (int i =  MainActivity.counter * 10 ; i < MainActivity.counter*10 +10 ; i++){

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
                        notifyDataSetChanged();
                        MainActivity.counter++;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return newsPojoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView headline,source,publishDate;
        ImageView imageView;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            headline = itemView.findViewById(R.id.headline);
            source = itemView.findViewById(R.id.source);
            publishDate = itemView.findViewById(R.id.publishDate);
            imageView =  itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.constraintView);

        }
    }
}
