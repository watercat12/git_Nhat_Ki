package com.arm2.nhatki2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class XemHinhActivity extends AppCompatActivity {

    ImageView imgXemHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_hinh);
        getSupportActionBar().hide();
        addControl();
    }

    private void addControl() {
        imgXemHinh = (ImageView) findViewById(R.id.imgXemHinh);
        Intent intent = getIntent();
        String pathHinh = intent.getStringExtra("hinh");
        Bitmap bitmap = BitmapFactory.decodeFile(pathHinh);
        imgXemHinh.setImageBitmap(bitmap);
    }
}
