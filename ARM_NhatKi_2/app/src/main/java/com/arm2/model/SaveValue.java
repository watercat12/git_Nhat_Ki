package com.arm2.model;

import android.content.Context;
import android.content.SharedPreferences;

import static com.arm2.model.Const.arrHinhTheme;

/**
 * Created by ARM on 21-Mar-17.
 */

public class SaveValue {
    private String dateHienTai = "NGAY_HIEN_TAI";//giá trị trong file
    private String passWord = "PA";//giá trị trong file
    private String camXuc = "PA_1";
    private String lastId = "LAST_ID";
    private String isAdd = "isAdd";
    private String font = "FONT";
    private String lastTheme = "THEME";
    private String timeDem = "TIME_DEM";
    private String isLock = "LOCK";
    private String firstRun = "first_Run";
    private String save = "SaveValue";//tên file SharedPreferences
    Context context;

    public SaveValue(Context context) {
        this.context = context;
    }
//------------------------get và set date hiện tại----------------------------------------
    public void setSaved_Date_String(String s)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putString(this.dateHienTai, s);
        localEditor.commit();
    }

    public String getSaved_Date_String()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getString(this.dateHienTai,"error");
    }
//--------------------- get và set PassWord final ---------------------------
    public void setSaved_Pass_String(String s)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putString(this.passWord, s);
        localEditor.commit();
    }

    public String getSaved_Pass_String()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getString(this.passWord,"");
    }
//-----------------------------------------------------------------------
    public void setSaved_Last_CamXuc_String(String s)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putString(this.camXuc, s);
        localEditor.commit();
    }

    public String getSaved_Last_CamXuc_String()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getString(this.camXuc,"");
    }

//-------------------------------------------------------------------------
    public void setSaved_Font_String(String s)

    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putString(this.font, s);
        localEditor.commit();
    }

    public String getSaved_Font_String()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getString(this.font,"");
    }

//-------------------------------------------------------------------------
    public void setSaved_Last_Id_Database_Integer(Integer i)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putInt(this.lastId, i);
        localEditor.commit();
    }

    public Integer getSaved_Last_Id_Database_Integer()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getInt(this.lastId,0);
    }


//-------------------------------------------------------------------------
    public void setSaved_Add_Database_Integer(Integer i)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putInt(this.isAdd, i);
        localEditor.commit();
    }

    public Integer getSaved_Add_Database_Integer()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getInt(this.isAdd,0);
    }

//-------------------------------------------------------------------------
    public void setSaved_Time_Pass_Long(Long i)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putLong(this.timeDem, i);
        localEditor.commit();
    }

    public Long getSaved_Time_Pass_Long()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getLong(this.timeDem,0);
    }

//-------------------------------------------------------------------------
    public void setSaved_Is_Khoa_Boolean(Boolean is)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putBoolean(this.isLock, is);
        localEditor.commit();
    }

    public Boolean getSaved_Is_Khoa_Boolean()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getBoolean(this.isLock,false);
    }

//-------------------------------------------------------------------------
    public void setSaved_First_Run_Boolean(Boolean run)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putBoolean(this.firstRun, run);
        localEditor.commit();
    }

    public Boolean getSaved_First_Run_Boolean()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getBoolean(this.firstRun,false);
    }
//-------------------------------------------------------------------------
    public void setSaved_Last_Theme_Integer(Integer i)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putInt(this.lastTheme, i);
        localEditor.commit();
    }

    public Integer getSaved_Last_Theme_Integer()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getInt(this.lastTheme
                        , (arrHinhTheme.length() -1));
    }



}
