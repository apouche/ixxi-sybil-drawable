package com.example.sybildrawable;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sybildrawable.model.BusLine;
import com.example.sybildrawable.model.BusStop;
import com.example.sybildrawable.model.Vector2f;
import com.example.sybildrawable.widget.BusLineView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    View moreView;
    View lessView;
    BusLineView busLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputStream raw =  getResources().openRawResource(R.raw.line169);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        Gson gson = new Gson();
//        Type connectionType = new TypeToken<BusPositions>(){}.getType();
        BusLine connections = gson.fromJson(rd, BusLine.class);
        imageView = findViewById(R.id.image_view);
        moreView = findViewById(R.id.text_more);
        lessView = findViewById(R.id.text_less);
        busLineView = findViewById(R.id.bus_line_view);
        busLineView.updateWith(connections);
        busLineView.setOnSelectStopListener(new BusLineView.Listener() {
            @Override
            public void onClick(BusStop busStop, Vector2f coordinates) {
                Toast.makeText(MainActivity.this, busStop.mnemo, Toast.LENGTH_LONG).show();
            }
        });

        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busLineView.increaseScale();
            }
        });

        lessView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busLineView.decreaseScale();
            }
        });
    }
}
