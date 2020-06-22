package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetail extends AppCompatActivity {

    ImageView imageView;
    TextView title,description,sourc,publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        imageView = findViewById(R.id.imageVIEW);
        title = findViewById(R.id.main_heading);
        description = findViewById(R.id.description);
        sourc = findViewById(R.id.source2);
        publish = findViewById(R.id.publishDate2);


        Intent intent = getIntent();

        String headline = intent.getStringExtra("headline");
        String imgLink = intent.getStringExtra("imageLink");
        String des = intent.getStringExtra("description");
        String source = intent.getStringExtra("source");
        String published = intent.getStringExtra("published");

        Picasso.get().load(imgLink).into(imageView);
        title.setText(headline);
        description.setText(des);
        sourc.setText(source);
        publish.setText(published);

    }
}