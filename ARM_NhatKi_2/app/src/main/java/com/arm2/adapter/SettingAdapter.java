package com.arm2.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arm2.model.Font;
import com.arm2.model.Setting;
import com.arm2.nhatki2.BackgroundActivity;
import com.arm2.nhatki2.KhoaActivity;
import com.arm2.nhatki2.MemoActivity;
import com.arm2.nhatki2.R;

import static com.arm2.model.Const.BAT_DAU_SET_PASS;
import static com.arm2.model.Const.BUTTON_PRESS;
import static com.arm2.model.Const.arrFont;
import static com.arm2.model.Const.saveValue;


/**
 * Created by ARM on 24-Apr-17.
 */

public class SettingAdapter extends ArrayAdapter<Setting>{

    Activity context;
    int resource;
    FontAdapter fontAdapter;
    Font font;

    public SettingAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource, null);

        LinearLayout lnListSetting = (LinearLayout) view.findViewById(R.id.lnListSetting);
        ImageView imgHinhSetting = (ImageView) view.findViewById(R.id.imgHinhSetting);
        TextView txtTenSetting = (TextView) view.findViewById(R.id.txtTenSetting);

        Setting setting = getItem(position);
        imgHinhSetting.setImageResource(setting.getHinhSetting());
        txtTenSetting.setText(setting.getTenSetting());
        txtTenSetting.setTextColor(this.context.getResources().getColor(R.color.text_color));
        lnListSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySetting(position);
            }
        });


        return view;
    }

    private void xuLySetting(int position) {
        switch (position)
        {
            case 0:
                moSetPass();
                break;
            case 1:
                chonFont();
                break;
            case 2:
                chonBackGround();
                break;
            case 3:
                managerMemo();


        }
    }

    private void managerMemo() {
        Intent intent = new Intent(this.context, MemoActivity.class);
        this.context.startActivity(intent);
    }

    //----------------mở màn hình set pass -------------------
    private void moSetPass() {
        Intent intentSetPass = new Intent(this.context,KhoaActivity.class);
        intentSetPass.putExtra(BAT_DAU_SET_PASS,BUTTON_PRESS);
        this.context.startActivity(intentSetPass);
    }

//----------------------------chọn font---------------------
    private void chonFont() {

        fontAdapter = new FontAdapter(this.context, R.layout.list_fonts);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        //lấy list cảm xúc có sẵn
        for (int i = 0; i < arrFont.length; i++) {

            font = new Font();

            font.setTenFont(arrFont[i]);

            Typeface typeface1 = Typeface.createFromAsset(this.context
                    .getAssets(), "fonts/"
                    + font.getTenFont()
                    + ".ttf");
            font.setKieuFont(typeface1);
            fontAdapter.add(font);
        }


        builder.setAdapter(fontAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                font = fontAdapter.getItem(which);
                //save tên font vào SharedPreferences
                saveValue.setSaved_Font_String(font.getTenFont());
                ///thêm save
                capNhatFont();
            }
        });
        builder.show();
    }

//---------------------------------------------------------------------------------
    private void capNhatFont() {
        Intent i = this.context.getPackageManager()
                .getLaunchIntentForPackage(this.context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.context.finish();
        this.context.startActivity(i);


    }

//---------------------------chọn background------------------------
    private void chonBackGround() {

        Intent intent = new Intent(this.context, BackgroundActivity.class);
        this.context.startActivity(intent);

    }


}
