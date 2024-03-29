package com.example.sybildrawable;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sybildrawable.model.BusLine;
import com.example.sybildrawable.model.BusPositions;
import com.example.sybildrawable.model.BusStop;
import com.example.sybildrawable.model.Vector2f;
import com.example.sybildrawable.widget.SynopticView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    View moreView;
    View lessView;
    SynopticView busLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputStream rawLine =  getResources().openRawResource(R.raw.line20);
        InputStream rawPositions =  getResources().openRawResource(R.raw.positions20);
        Reader rdLine = new BufferedReader(new InputStreamReader(rawLine));
        Reader rdPositions = new BufferedReader(new InputStreamReader(rawPositions));

        Gson gson = new Gson();
//        Type connectionType = new TypeToken<BusPositions>(){}.getType();
        BusLine connections = gson.fromJson(rdLine, BusLine.class);
        BusPositions positions = gson.fromJson(rdPositions, BusPositions.class);
        imageView = findViewById(R.id.image_view);
        moreView = findViewById(R.id.text_more);
        lessView = findViewById(R.id.text_less);
        busLineView = findViewById(R.id.bus_line_view);
        busLineView.updateWith(connections);
        busLineView.setOnSelectStopListener(new SynopticView.SelectionListener() {
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
