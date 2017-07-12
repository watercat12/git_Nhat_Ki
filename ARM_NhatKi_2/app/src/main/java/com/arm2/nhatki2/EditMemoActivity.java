package com.arm2.nhatki2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arm2.adapter.EditMemoAdapter;
import com.arm2.model.CamXuc;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.CAM_XUC_EDIT;
import static com.arm2.model.Const.INTEN_EDIT_CAM_XUC;
import static com.arm2.model.Const.arrDaTaHinhCamXucGoc;
import static com.arm2.model.Const.arrDataHinhCamXuc;
import static com.arm2.model.Const.arrDataTenCamXuc;
import static com.arm2.model.Const.arrHinhBackground_2;
import static com.arm2.model.Const.arrHinhCamXuc;
import static com.arm2.model.Const.saveValue;
import static com.arm2.nhatki2.MainActivity.database;

public class EditMemoActivity extends AppCompatActivity {

    TextView txtTitleEditMemo;
    EditText edTenMemoEditMemo;
    LinearLayout lnChooseImgMemoEditMemo;
    RelativeLayout activity_edit_memo;
    ImageView imgChooseImgMemoEditMemo;
    ImageView imgXoaEditMemo;
    Button btnCloseEditMemo;
    Button btnSaveEditMemo;

    EditMemoAdapter editMemoAdapter;
    CamXuc camXucBundle;
    CamXuc camXuc;
    Integer hinhMemoInDSMemo;
    Intent intent;
    Bundle bundle;
    String tenCamXuc;
    int hinhCamXuc,idCamXuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        getSupportActionBar().hide();
        addControl();
        addEvent();
    }

//----------------------------------------------------------------
    private void addControl() {


        activity_edit_memo = (RelativeLayout) findViewById(R.id.activity_edit_memo);


        txtTitleEditMemo = (TextView) findViewById(R.id.txtTitleEditMemo);
        edTenMemoEditMemo = (EditText) findViewById(R.id.EdTenMemoEditMemo);
        lnChooseImgMemoEditMemo = (LinearLayout) findViewById(R.id.lnChooseImgMemoEditMemo);
        imgChooseImgMemoEditMemo = (ImageView) findViewById(R.id.imgChooseImgMemoEditMemo);

        btnSaveEditMemo = (Button) findViewById(R.id.btnSaveEditMemo);


        intent = getIntent();
        if (intent.getAction() == INTEN_EDIT_CAM_XUC && intent != null)
        {
            camXucBundle = new CamXuc();
            bundle = intent.getExtras();
            camXucBundle = (CamXuc) bundle.getSerializable(CAM_XUC_EDIT);
            tenCamXuc = camXucBundle.getNoiDungCamXuc();
            hinhCamXuc = camXucBundle.getHinhCamXuc();
            idCamXuc = camXucBundle.getId();
            hienThiInfoCamXuc(intent);
        }

    }
//----------------------------------------------------------------
    private void addEvent() {
        lnChooseImgMemoEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonHinhMemo();
            }
        });
        btnSaveEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.getAction() == INTEN_EDIT_CAM_XUC && intent != null)
                {
                    xuLyUpdateCamXuc(idCamXuc);
                    addVaoArrayTenCamXuc("SELECT * FROM dsMemo ",null);
                }
                else
                {
                    xuLyAddMemo();
                    addVaoArrayTenCamXuc("SELECT * FROM dsMemo ",null);
                }

            }
        });
    }

//------------------------------------------------------------------------

    /**
     * sửa giá trị tại dòng có id đã nhập
     * @param id id của dòng muốn sửa trong database
     */
    private void xuLyUpdateCamXuc(int id) {
        ContentValues values = new ContentValues();
        values.put("ten_memo",edTenMemoEditMemo.getText().toString());
        //khi có chọn cảm xúc
        if (hinhMemoInDSMemo != null)
        {
            values.put("hinh_memo",hinhMemoInDSMemo);
        }
        else
        {
            values.put("hinh_memo",1);
        }


        if (database.update("dsMemo",values,"id = ? ",new String[]{String.valueOf(id)}) == -1)
        {
            Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.edit), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


//----------------------------------------------------------------
    private void xuLyChonHinhMemo() {
        editMemoAdapter = new EditMemoAdapter(this,R.layout.list_cam_xuc);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //lấy list cảm xúc có sẵn
        for (int i =0; i< arrDaTaHinhCamXucGoc.size(); i++)
        {

            camXuc = new CamXuc();

            camXuc.setCamXuc(i);
            camXuc.setHinhCamXuc(arrHinhCamXuc.getResourceId(arrDaTaHinhCamXucGoc.get(i),-1));
            editMemoAdapter.add(camXuc);
        }


        builder.setAdapter(editMemoAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                camXuc = editMemoAdapter.getItem(which);
                hinhMemoInDSMemo = which + 1;
                saveValue.setSaved_Last_CamXuc_String(camXuc.getNoiDungCamXuc());
                imgChooseImgMemoEditMemo.setImageResource(camXuc.getHinhCamXuc());

            }
        });

        builder.show();
    }

//----------------------------------------------------------------

    private void addVaoArrayTenCamXuc(String sql,String []arr) {
        Cursor cursor = database.rawQuery(sql,arr);

        arrDataTenCamXuc.clear();
        arrDataHinhCamXuc.clear();
        while (cursor.moveToNext())
        {

            String tenDataCamXuc = cursor.getString(1);
            arrDataTenCamXuc.add(tenDataCamXuc);
            int hinhDataCamXuc = cursor.getInt(2);
            arrDataHinhCamXuc.add(hinhDataCamXuc -1 );

        }
        cursor.close();
    }



//------------------------------------------------------------------------------
    //lấy text từ control để lưu vào database
    private void xuLyAddMemo() {
        ContentValues values = new ContentValues();
        String titleMemo = edTenMemoEditMemo.getText().toString();

        if (hinhMemoInDSMemo != null)
        {
            values.put("hinh_memo",hinhMemoInDSMemo);
        }
        else
        {
            values.put("hinh_memo",1);
        }

        values.put("goc",1);

        if (titleMemo != null || titleMemo != "")
        {
            values.put("ten_memo",edTenMemoEditMemo.getText().toString());
            if (database.insert("dsMemo",null,values) == -1)
            {
                Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, getResources().getString(R.string.add), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {

        }


    }

//------------------------------------------------------------------------------------
    private void hienThiInfoCamXuc(Intent intent) {

        String tieuDe = camXucBundle.getNoiDungCamXuc();
        Integer hinhCamXuc = camXucBundle.getHinhCamXuc();

        edTenMemoEditMemo.setText(tieuDe+"");
        imgChooseImgMemoEditMemo.setImageResource(hinhCamXuc);
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

        activity_edit_memo.setBackgroundResource(arrHinhBackground_2.getResourceId(bg,-1));
    }
}
