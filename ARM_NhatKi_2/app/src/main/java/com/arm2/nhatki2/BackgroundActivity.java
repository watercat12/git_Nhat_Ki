package com.arm2.nhatki2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.arm2.adapter.BackgroundAdapter;
import com.arm2.model.Background;

import java.util.ArrayList;
import java.util.List;

import static com.arm2.model.Const.arrHinhBackground_1;
import static com.arm2.model.Const.arrHinhChonBackground;
import static com.arm2.model.Const.saveValue;


public class BackgroundActivity extends AppCompatActivity {

    private List<Background> backgrounds = new ArrayList<>();
    private RecyclerView recyclerView;
    private BackgroundAdapter mAdapter;
    LinearLayout activity_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        getSupportActionBar().hide();
        addControl();
    }

    private void addControl() {
        activity_background = (LinearLayout) findViewById(R.id.activity_background);
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

    @Override
    protected void onResume() {
        super.onResume();
        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();

        activity_background.setBackgroundResource(arrHinhBackground_1.getResourceId(bg,-1));
    }
}
