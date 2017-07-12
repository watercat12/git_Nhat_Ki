package com.arm2.nhatki2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arm2.adapter.CamXucAdapter;
import com.arm2.model.CamXuc;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.arrHinhBackground_1;
import static com.arm2.model.Const.arrHinhCamXuc;
import static com.arm2.model.Const.saveValue;
import static com.arm2.nhatki2.MainActivity.database;

public class MemoActivity extends AppCompatActivity {

    ListView lvAddMemo;
    CamXucAdapter camXucAdapter;
    CamXuc camXuc;

    TextView txtEditCamXuc;
    ImageView imgHinhCamXucAdd;
    LinearLayout lnButtonAdd;
    RelativeLayout activity_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        getSupportActionBar().hide();
        addControl();
        addEvent();
    }

//---------------------------------------------------------------------------
    private void addControl() {
        activity_memo = (RelativeLayout) findViewById(R.id.activity_memo);
        lvAddMemo = (ListView) findViewById(R.id.lvAddMemo);
        camXucAdapter = new CamXucAdapter(this,R.layout.list_cam_xuc);
        lnButtonAdd = (LinearLayout) findViewById(R.id.lnButtonAdd);

        lvAddMemo.setAdapter(camXucAdapter);

    }
//---------------------------------------------------------------------------
    private void addEvent() {
        lnButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyAddMemo();
            }
        });
    }
//---------------------------------------------------------------------------
    private void xuLyAddMemo() {
        Intent intent = new Intent(this,EditMemoActivity.class);
        startActivity(intent);
    }

//-------------------------------------------------------------------------
    private void hienThiAllMemo() {
        int id= saveValue.getSaved_Last_Id_Database_Integer();
        String sql = "SELECT * FROM dsMemo WHERE goc = 1 ";
        Cursor cursor=database.rawQuery(sql,null);
        while (cursor.moveToNext())
        {
            camXuc = new CamXuc();



            camXuc.setNoiDungCamXuc(cursor.getString(1));
            //camXuc.setCamXuc(i);
            camXuc.setHinhCamXuc(arrHinhCamXuc.getResourceId(cursor.getInt(2)-1,-1));
            camXuc.setId(cursor.getInt(0));
            camXucAdapter.add(camXuc);
        }
        cursor.close();
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
        camXucAdapter.clear();
        hienThiAllMemo();
        /*for (int i =0; i< arrDataTenCamXuc.size(); i++)
        {


        }*/
        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();

        activity_memo.setBackgroundResource(arrHinhBackground_1.getResourceId(bg,-1));
    }
}
