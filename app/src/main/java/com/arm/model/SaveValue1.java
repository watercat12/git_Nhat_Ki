package com.arm.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ARM on 08-Jun-17.
 */

public class SaveValue1 {
    private String lastDayCursor = "drive_id";//giá trị trong file
    private String save = "ID";//tên file SharedPreferences
    Context context;

    public SaveValue1(Context context) {
        this.context = context;
    }
    //------------------------get và set date hiện tại----------------------------------------
    public void setSaved_Date_String(String s)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putString(this.lastDayCursor, s);
        localEditor.commit();
    }

    public String getSaved_Date_String()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getString(this.lastDayCursor,"");
    }
}
