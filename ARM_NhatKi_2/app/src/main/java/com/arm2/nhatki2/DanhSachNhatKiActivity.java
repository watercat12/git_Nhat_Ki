package com.arm2.nhatki2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arm2.adapter.ChiTietNhatKiAdapter;
import com.arm2.model.ChiTietNhatKi;
import com.arm2.model.Const;
import com.arm2.model.SaveValue;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.arrDataHinhCamXuc;
import static com.arm2.model.Const.arrDataTenCamXuc;
import static com.arm2.model.Const.arrHinhBackground_1;
import static com.arm2.model.Const.arrHinhCamXuc;
import static com.arm2.model.Const.saveValue;
import static com.arm2.nhatki2.MainActivity.database;

public class DanhSachNhatKiActivity extends AppCompatActivity {


    ChiTietNhatKiAdapter chiTietNhatKiAdapter;
    ListView lvDSNhatKiSave;
    LinearLayout lndsNhatKi,activity_danh_sach_nhat_ki,lnVietThemNk;
    TextView txtDay,txtTimeLineOrDay;
    ImageView imgTimeOrDay;
    String actionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nhat_ki);
        getSupportActionBar().hide();
        addControl();
        addEvent();
    }

//------------------hiện toàn bộ nhật kí---------------------------------
    private void addControl() {
        actionIntent = getIntent().getAction();

        imgTimeOrDay = (ImageView) findViewById(R.id.imgTimeLineOrDay);
        txtDay = (TextView) findViewById(R.id.txtDay);
        txtTimeLineOrDay = (TextView) findViewById(R.id.txtTimeLineOrDay);
        // setText là ngày tháng năm khi bấm vào lịch
        txtDay.setText(saveValue.getSaved_Date_String());

        saveValue = new SaveValue(this);

        //nếu không có nhật kí thì hiện text bảo thêm


        lndsNhatKi = (LinearLayout) findViewById(R.id.activity_danh_sach_nhat_ki);
        activity_danh_sach_nhat_ki = (LinearLayout) findViewById(R.id.activity_danh_sach_nhat_ki);

        lnVietThemNk = (LinearLayout) findViewById(R.id.lnVietThemNK);

        lvDSNhatKiSave = (ListView) findViewById(R.id.lvDSNhatKiSave);
        chiTietNhatKiAdapter = new
                ChiTietNhatKiAdapter(DanhSachNhatKiActivity.this,R.layout.list_item_nhat_ki);


        lvDSNhatKiSave.setAdapter(chiTietNhatKiAdapter);

        //nếu dc mở bởi TimeLine sẽ hiển thị nhật kí dc xếp theo id giảm dần
        if (actionIntent == Const.HIEN_DS_NHAT_KI_TIME_LINE)
        {
            lnVietThemNk.setVisibility(View.GONE);
            imgTimeOrDay.setImageResource(R.drawable.iconvietnhatky);
            txtDay.setText(getString(R.string.time_line));
            txtTimeLineOrDay.setText(getString(R.string.entries));
            String sql = "SELECT * FROM dsNhatKi ORDER BY id DESC ";
            hienThiNhatKi(sql,null);
        }
        else
        {
            lnVietThemNk.setVisibility(View.VISIBLE);
            hienThiNhatKi("SELECT * FROM dsNhatKi WHERE date= ? "
                    ,new String[] {saveValue.getSaved_Date_String()}
            );
        }
    }
//-------------------------------------------------------------------------
    private void addEvent() {

        lnVietThemNk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VietNhatKiActivity.class);
                intent.setAction(Const.THEM_NK_NGAY_HIEN_TAI);
                startActivity(intent);
            }
        });


    }
//-------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }
//-------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_add:
                Intent intent = new Intent(this,VietNhatKiActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


//---------------------------------------------------------------------------------
    /*
    database đã mở kết nối bên MainActivity rồi
    phương thức này sẽ trong database các dòng có ngày tháng năm hợp lý
    và add vào đối tượng ChiTietNhatKi sau đó add vào ChiTietNhatKiAdapter
    để hiển thị lên ListView trong
    activity_danh_sach_nhat_ki.xml
     */
    private void hienThiNhatKi(String sql,String []arr) {

        Cursor cursor = database.rawQuery(sql,arr);
        chiTietNhatKiAdapter.clear();
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            String time = cursor.getString(5);
            String tieuDe = cursor.getString(2);
            String noiDung = cursor.getString(3);
            int camXuc = cursor.getInt(4);
            String hinhDinhKem = cursor.getString(6);
            String hinhDinhKemFull = cursor.getString(7);

            ChiTietNhatKi nhatKi = new ChiTietNhatKi();
            //phải lấy ra dc id bên bảng dsMemo, để lấy ten và hình memo

            nhatKi.setHinhCamXuc(arrHinhCamXuc.getResourceId(arrDataHinhCamXuc.get(camXuc),-1));
            if (hinhDinhKem != null)
            {
                String arrHinhString[] = hinhDinhKem.split("@");
                String arrHinhFullString [] = hinhDinhKemFull.split("@");
            /*for (String s: arrHinhString) {
                Bitmap bmp = BitmapFactory.decodeFile(s);
                //resize lại hình cho nhẹ
                Bitmap resized = Bitmap
                        .createScaledBitmap(bmp, 50, 50, true);

            }*/
                nhatKi.setHinhDinhKem(arrHinhString);
                nhatKi.setHinhDinhKemFull(arrHinhFullString);
            }


            String noiDungCamXuc_dsNhatKi = "";
            for (int i = 0; i<=arrDataTenCamXuc.size(); i++)
            {
                if (camXuc == i)
                {
                    noiDungCamXuc_dsNhatKi = arrDataTenCamXuc.get(i);
                    break;
                }

            }
            nhatKi.setId(id);
            nhatKi.setNoiDungCamXuc(noiDungCamXuc_dsNhatKi);
            nhatKi.setCamXuc(camXuc);
            nhatKi.setTieuDe(tieuDe.trim());
            nhatKi.setNoiDung(noiDung.trim());
            nhatKi.setDateNgayThangNam(date);
            nhatKi.setTime(time);
            chiTietNhatKiAdapter.add(nhatKi);
        }
        cursor.close();
    }

//--------------------trả về số dòng có kết quả khớp--------------------------
    public int tongSoDongAnhHuong(String sql,String []arr) {
        Cursor cursor = database.rawQuery(sql,arr);
        int totalRows = cursor.getCount();
        cursor.close();
        return totalRows;
    }



//---------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        if (actionIntent == Const.HIEN_DS_NHAT_KI_TIME_LINE)
        {
            lnVietThemNk.setVisibility(View.GONE);
            String sql = "SELECT * FROM dsNhatKi ORDER BY id DESC ";
            hienThiNhatKi(sql,null);
        }
        else
        {
            lnVietThemNk.setVisibility(View.VISIBLE);
            hienThiNhatKi("SELECT * FROM dsNhatKi WHERE date= ? "
                    ,new String[] {saveValue.getSaved_Date_String()}
            );
        }


        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();

            activity_danh_sach_nhat_ki
                    .setBackgroundResource(arrHinhBackground_1.getResourceId(bg,-1));

    }


//set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
