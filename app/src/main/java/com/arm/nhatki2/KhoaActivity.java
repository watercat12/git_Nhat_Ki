package com.arm.nhatki2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.arm.model.SaveValue;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm.nhatki2.MainActivity.BAT_DAU_SET_PASS;

public class KhoaActivity extends AppCompatActivity {
    String pass = "";
    Intent intent;
    public static SaveValue saveValue;
    public static String arrFont[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);

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
        intent = getIntent();
        int kt = intent.getIntExtra(BAT_DAU_SET_PASS,0);


        saveValue = new SaveValue(this);
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
