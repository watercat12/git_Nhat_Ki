package com.arm.nhatki2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arm.adapter.BackgroundAdapter;
import com.arm.model.Background;

import java.util.ArrayList;
import java.util.List;

import static com.arm.nhatki2.MainActivity.arrHinhChonBackground;

public class BackgroundActivity extends AppCompatActivity {

    private List<Background> backgrounds = new ArrayList<>();
    private RecyclerView recyclerView;
    private BackgroundAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        addControl();
    }

    private void addControl() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mAdapter = new BackgroundAdapter(backgrounds,this);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();
    }

    private void prepareMovieData() {
        for (int i =0; i < arrHinhChonBackground.length() ; i++)
        {
            Background background = new Background();
            background.setHinhBackground(arrHinhChonBackground.getResourceId(i,-1));
            backgrounds.add(background);
            //mAdapter.notifyDataSetChanged();//dữ liệu thay đổi
        }
    }
}
