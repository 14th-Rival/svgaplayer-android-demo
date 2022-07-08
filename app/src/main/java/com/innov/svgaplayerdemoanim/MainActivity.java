package com.innov.svgaplayerdemoanim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.innov.svgaplayerdemoanim.Adapters.SVGAAdapter;
import com.innov.svgaplayerdemoanim.Models.AssetsModel;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<AssetsModel> assetsModelList = new ArrayList<>();
    private SVGAImageView svgaImageView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        svgaImageView = findViewById(R.id.svga_viewer);
        mTextView = findViewById(R.id.tv_filename);
        RecyclerView mRecyclerAssets = findViewById(R.id.rv_assets_svga);
        mRecyclerAssets.setHasFixedSize(true);

        SVGAAdapter svgaAdapter =
                new SVGAAdapter(
                        getListofAssets(),
                        getBaseContext(),
                        MainActivity.this
                );

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(
                        getBaseContext(),
                        RecyclerView.HORIZONTAL,
                        false
                );

        mRecyclerAssets.setLayoutManager(mLayoutManager);
        mRecyclerAssets.setAdapter(svgaAdapter);

        Button btn_play = findViewById(R.id.btn_play);
        Button btn_stop = findViewById(R.id.btn_stop);

        btn_play.setOnClickListener(v -> startAnim());
        btn_stop.setOnClickListener(v -> stopAnim());
    }

    private List<AssetsModel> getListofAssets(){

        try {
            String[] str = getAssets().list("");
            if (assetsModelList.isEmpty()){
                for (String s : str) {
                    if (s.endsWith("svga")) {
                        Log.d("svgafiles", s);
                        AssetsModel assetsModel = new AssetsModel(s);
                        assetsModelList.add(assetsModel);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetsModelList;
    }

    private void startAnim(){
        svgaImageView.startAnimation();
    }

    private void stopAnim(){
        svgaImageView.stopAnimation();
    }

    public void setViewAsset(String s){
        SVGAParser svgaParser = new SVGAParser(getApplicationContext());
        svgaParser.decodeFromAssets(s, new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                SVGADrawable svgaDrawable = new SVGADrawable(svgaVideoEntity);
                svgaImageView.setImageDrawable(svgaDrawable);
                svgaImageView.startAnimation();
            }

            @Override
            public void onError() {

            }
        }, null);
        mTextView.setText("FileName: "+s);
    }
}