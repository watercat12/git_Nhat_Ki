package com.arm2.nhatki2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.arm2.adapter.SettingAdapter;
import com.arm2.model.Setting;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.arrHinhBackground_1;
import static com.arm2.model.Const.arrHinhSetting;
import static com.arm2.model.Const.arrTenSetting;
import static com.arm2.model.Const.saveValue;

public class SettingActivity extends AppCompatActivity {

    SettingAdapter settingAdapter;
    ListView lvSetting;
    RelativeLayout activity_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        addControl();

    }

    private void addControl() {
        activity_setting = (RelativeLayout) findViewById(R.id.activity_setting);
        lvSetting = (ListView) findViewById(R.id.lvSetting);
        settingAdapter = new SettingAdapter(this,R.layout.list_setting);
        lvSetting.setAdapter(settingAdapter);

        xuLyAddSetting();
    }

    private void xuLyAddSetting() {
        for (int i = 0; i<=arrHinhSetting.length()-1; i++)
        {
            Setting setting = new Setting();
            setting.setHinhSetting(arrHinhSetting.getResourceId(i,0));
            setting.setTenSetting(arrTenSetting[i]);
            settingAdapter.add(setting);
        }
    }

//------------------------------------------------------------------------------
    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
//------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();

        activity_setting.setBackgroundResource(arrHinhBackground_1.getResourceId(bg,-1));
    }

}
