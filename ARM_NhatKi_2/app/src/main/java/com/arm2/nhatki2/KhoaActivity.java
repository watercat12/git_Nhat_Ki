package com.arm2.nhatki2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arm2.model.SaveValue;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.BAT_DAU_SET_PASS;
import static com.arm2.model.Const.arrFont;
import static com.arm2.model.Const.arrHinhBackground_1;
import static com.arm2.model.Const.arrHinhBackground_2;
import static com.arm2.model.Const.arrHinhBackground_3;
import static com.arm2.model.Const.arrHinhCamXuc;
import static com.arm2.model.Const.arrHinhChonBackground;
import static com.arm2.model.Const.arrHinhSetting;
import static com.arm2.model.Const.arrHinhTheme;
import static com.arm2.model.Const.arrTenSetting;
import static com.arm2.model.Const.saveValue;

public class KhoaActivity extends AppCompatActivity {
    String pass = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);
        getSupportActionBar().hide();

        arrTenSetting = getResources()
                .getStringArray(R.array.setting_array);
        arrHinhCamXuc = getResources()
                .obtainTypedArray(R.array.hinh_cam_xuc_array);
        arrHinhChonBackground = getResources()
                .obtainTypedArray(R.array.hinh_chon_background_array);
        arrHinhTheme = getResources()
                .obtainTypedArray(R.array.hinh_theme_array);
        arrHinhBackground_1 = getResources()
                .obtainTypedArray(R.array.hinh_background_1_array);
        arrHinhBackground_2 = getResources()
                .obtainTypedArray(R.array.hinh_background_2_array);
        arrHinhBackground_3 = getResources()
                .obtainTypedArray(R.array.hinh_background_2_array);
        arrHinhSetting = getResources()
                .obtainTypedArray(R.array.hinh_setting_array);


        arrFont = getResources().getStringArray(R.array.fonts_array);
        saveValue = new SaveValue(this);
//thiết lập font chung
        if (saveValue.getSaved_Font_String() == ""
                || saveValue.getSaved_Font_String() == null)
        {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/Roboto.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
        else
        {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/"+saveValue.getSaved_Font_String()+".ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }

        intent = new Intent();
        intent = getIntent();
        int kt = intent.getIntExtra(BAT_DAU_SET_PASS,0);


        pass = saveValue.getSaved_Pass_String();

        /*if (pass == "" || pass == null)
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerKhoa, new NumLockFragment())
                    .commit();
        }*/
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerKhoa, new NumLockFragment())
                .commit();
    }
//set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
