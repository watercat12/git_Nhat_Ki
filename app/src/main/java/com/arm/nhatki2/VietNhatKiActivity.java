package com.arm.nhatki2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.adapter.CamXucAdapter;
import com.arm.model.CamXuc;
import com.arm.model.ChiTietNhatKi;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm.nhatki2.KhoaActivity.saveValue;

import static com.arm.nhatki2.LaunchActivity.vietLuon;
import static com.arm.nhatki2.MainActivity.EDIT_NHAT_KI;
import static com.arm.nhatki2.MainActivity.NHAT_KI_DOI_TUONG_BUNDLE;
import static com.arm.nhatki2.MainActivity.arrCamXuc;
import static com.arm.nhatki2.MainActivity.arrHinhBackground;
import static com.arm.nhatki2.MainActivity.arrHinhCamXuc;
import static com.arm.nhatki2.MainActivity.database;
import static com.arm.nhatki2.MaterialCalendarFragment.daySelect;
import static com.arm.nhatki2.MaterialCalendarFragment.namSelect;
import static com.arm.nhatki2.MaterialCalendarFragment.thangSelect;
import static com.arm.nhatki2.NumLockFragment.HOM_NAY;

public class VietNhatKiActivity extends AppCompatActivity {


    EditText txtEditTieuDe,txtEditNoiDung;
    TextView txtEditCamXuc;
    ImageView imgHinhCamXucVietNhayKi;
    LinearLayout activity_viet_nhat_ki;
    Button btnSave,btnXoa;
    String gioHienTai;
    String date,tieuDe,noiDung,time;
    String action;
    Intent intent;
    ChiTietNhatKi chiTietNhatKi;
    CamXuc camXuc;

    CamXucAdapter camXucAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viet_nhat_ki);
        addControl();
        addEvent();
    }

    private void addControl() {

        intent = getIntent();
        action = intent.getAction();

        txtEditTieuDe = (EditText) findViewById(R.id.txtEditTieuDe);
        txtEditNoiDung = (EditText) findViewById(R.id.txtEditNoiDung);
        txtEditCamXuc = (TextView) findViewById(R.id.txtEditCamXuc);
        imgHinhCamXucVietNhayKi = (ImageView) findViewById(R.id.imgHinhCamXucVietNhatKi);
        activity_viet_nhat_ki = (LinearLayout) findViewById(R.id.activity_viet_nhat_ki);
        //btnSave = (Button) findViewById(R.id.btnSave);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnXoa.setVisibility(View.GONE);
        if (action != null && action.equals(EDIT_NHAT_KI))
            btnXoa.setVisibility(View.VISIBLE);
        gioHienTai = getTimeHienTai();//giờ hiện tại
        date = saveValue.getSaved_Date_String();//lấy trong SharedPreferences
        //nếu bấm vào 1 nhật kí nào đó thì sẽ hiển thị nội dung của nhật kí đó lên
        //để người ta sửa
        if (action == EDIT_NHAT_KI)
        {
            hienThiNhatKiDaLuu(intent);
        }

        if (vietLuon == 2)
        {
            vietLuon = 3;
        }
    }
//------------------------------------------------------------------------------------
    private void hienThiNhatKiDaLuu(Intent intent) {

        Bundle bundle = intent.getExtras();
        chiTietNhatKi = (ChiTietNhatKi) bundle.getSerializable(NHAT_KI_DOI_TUONG_BUNDLE);
        String tieuDe = chiTietNhatKi.getTieuDe();
        String noiDung = chiTietNhatKi.getNoiDung();
        String noiDungCamXuc = "";
        for (int j = 0; j<= arrCamXuc.length; j++)
        {

            if (chiTietNhatKi.getCamXuc() == j)
            {
                noiDungCamXuc = arrCamXuc[j];
                break;
            }
        }
        txtEditTieuDe.setText(tieuDe+"");
        String mystring=new String(noiDung);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        txtEditNoiDung.setText(content);
        //txtEditNoiDung.setText(noiDung);
        txtEditCamXuc.setText(noiDungCamXuc);
        imgHinhCamXucVietNhayKi.setImageResource(chiTietNhatKi.getHinhCamXuc());
    }

//----------------------------trả về giờ phút giây hiện tại----------------------------
    private String getTimeHienTai() {
        Calendar now = Calendar.getInstance();
        //Muốn xuất Giờ:Phút:Giây AM (PM)
        String strDateFormat12 = "hh:mm:ss a";
        String strDateFormat24 = "HH:mm:ss";
        SimpleDateFormat sdf =null;
        //Tạo đối tượng SimpleDateFormat với định dạng 12
        sdf= new SimpleDateFormat(strDateFormat24);
        return sdf.format(now.getTime());
    }

//-----------------------------------------------------------------------------------
    private void addEvent() {
        intent = getIntent();
        action = intent.getAction();
        //__________________SAVE___________________________________
        /*btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });*/
        txtEditCamXuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonCamXuc();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXoaNhatKi(String.valueOf(chiTietNhatKi.getId()));
            }
        });
    }
//----------------------------------------------------------------------------

    /**
     * xóa nhật kí có id
     * @param id  truyền vào id để xóa trong database
     */
    private void xuLyXoaNhatKi(final String id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.delete_note));
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (database.delete("dsNhatKi","id = ? ",new String[]{id}) == -1)
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

//---------------------nếu không nhập gì thì sẽ false-------------------------------------
    private boolean textIsEmtype() {
        if (txtEditTieuDe.getText().toString() == ""
                && txtEditNoiDung.getText().toString() == ""
                && txtEditCamXuc.getText().toString() == "")
        {
            return true;
        }
        return false;
    }

//------------------------------------------------------------------------

    /**
     * sửa giá trị tại dòng có id đã nhập
     * @param id id của dòng muốn sửa trong database
     */
    private void xuLyEditNhatKi(String id) {
        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("tieu_de",txtEditTieuDe.getText().toString());
        values.put("noi_dung",txtEditNoiDung.getText().toString());
        //khi có chọn cảm xúc
        if (camXuc != null)
        {
            values.put("cam_xuc",camXuc.getCamXuc());
        }

        values.put("time",gioHienTai);

        if (database.update("dsNhatKi",values,"id = ? ",new String[]{id}) == -1)
        {
            Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.edit), Toast.LENGTH_SHORT).show();
            saveValue.setSaved_Last_Id_Database_Integer(chiTietNhatKi.getId());
            finish();
        }
    }

//--------------------AlertDialog để chọn cảm xúc----------------------------------

    private void xuLyChonCamXuc() {
        camXucAdapter = new CamXucAdapter(this,R.layout.list_cam_xuc);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //lấy list cảm xúc có sẵn
        for (int i =0; i< arrCamXuc.length; i++)
        {
            camXuc = new CamXuc();
            camXuc.setNoiDungCamXuc(arrCamXuc[i]);
            camXuc.setCamXuc(i);
            camXuc.setHinhCamXuc(arrHinhCamXuc.getResourceId(i,-1));
            camXucAdapter.add(camXuc);
        }


        builder.setAdapter(camXucAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                camXuc = camXucAdapter.getItem(which);
                txtEditCamXuc.setText(camXuc.getNoiDungCamXuc());

                saveValue.setSaved_Last_CamXuc_String(camXuc.getNoiDungCamXuc());
                imgHinhCamXucVietNhayKi.setImageResource(camXuc.getHinhCamXuc());
            }
        });

        builder.show();
    }
//------------------------------------------------------------------------------
    //lấy text từ control để lưu vào database
    private void xuLyAddNhatKi() {

        Calendar calendar = Calendar.getInstance();
        int ngayHomNay = calendar.get(Calendar.DAY_OF_MONTH);
        int thangHomNay = calendar.get(Calendar.MONTH)+1;
        int namHomNay = calendar.get(Calendar.YEAR);
        //đưa vào để xét, nếu ngày hôm nay không viết gì thì sẽ hiện thông báo
        try {
            if (ngayHomNay == daySelect
                    && thangHomNay == thangSelect
                    && namHomNay == namSelect)
            {
                saveValue.setSaved_Have_Note_ToDay_Integer(1);
            }
            else
            {
                saveValue.setSaved_Have_Note_ToDay_Integer(0);
            }
        }
        catch (Exception e)
        {

        }
        //khi gọi dc gọi từ Notification
        try {
            if (vietLuon == 3)
            {
                date = ngayHomNay+"/"+thangHomNay+"/"+namHomNay;
                vietLuon = 0;
            }
        }
        catch (Exception e)
        {
            Log.e("loi","loi HOM_NAY");
        }

        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("tieu_de",txtEditTieuDe.getText().toString());
        values.put("noi_dung",txtEditNoiDung.getText().toString());
        //khi có chọn cảm xúc
        if (camXuc != null)
        {
            values.put("cam_xuc",camXuc.getCamXuc());
        }

        values.put("time",gioHienTai);

        if (database.insert("dsNhatKi",null,values) == -1)
        {
            Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
        }
        else 
        {
            Toast.makeText(this, getResources().getString(R.string.add), Toast.LENGTH_SHORT).show();
            finish();
        }

    }
//----------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.save_nhat_ki:
                save();
        }
        return super.onOptionsItemSelected(item);
    }
//------------------------------------------------------------------------------------
    private void save() {
        //___________________sửa nhật kí__________________________
        if (action == EDIT_NHAT_KI)
        {
            if (!textIsEmtype())
            {
                xuLyEditNhatKi(String.valueOf(chiTietNhatKi.getId()));
                saveValue.setSaved_Add_Database_Integer(0);
            }

            else
            {

            }

        }
        //______________________thêm nhật kí__________________________
        else
        {
            if (!textIsEmtype())
            {
                xuLyAddNhatKi();
                saveValue.setSaved_Add_Database_Integer(1);
            }

            else
            {

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set Background
        int bg = saveValue.getSaved_Last_Background_Integer();
            activity_viet_nhat_ki.setBackgroundResource(arrHinhBackground.getResourceId(bg,-1));

    }

    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
