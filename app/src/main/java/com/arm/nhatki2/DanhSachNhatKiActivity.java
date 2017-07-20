package com.arm.nhatki2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arm.adapter.ChiTietNhatKiAdapter;
import com.arm.model.ChiTietNhatKi;
import com.arm.model.SaveValue;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm.nhatki2.MainActivity.arrCamXuc;
import static com.arm.nhatki2.MainActivity.arrHinhBackground;
import static com.arm.nhatki2.MainActivity.arrHinhCamXuc;
import static com.arm.nhatki2.MainActivity.database;

public class DanhSachNhatKiActivity extends AppCompatActivity {


    ChiTietNhatKiAdapter chiTietNhatKiAdapter;
    ListView lvDSNhatKiSave;
    LinearLayout lndsNhatKi,activity_danh_sach_nhat_ki;
    SaveValue saveValue;
    TextView txtAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nhat_ki);
        addControl();
        addEvent();
    }

//------------------hiện toàn bộ nhật kí---------------------------------
    private void addControl() {
        saveValue = new SaveValue(this);
        txtAdd = (TextView) findViewById(R.id.txtAdd);
        //nếu không có nhật kí thì hiện text bảo thêm
        if (tongSoDongAnhHuong("SELECT * FROM dsNhatKi WHERE date= ? "
                ,new String[] {saveValue.getSaved_Date_String()}) == 0)
        {
            txtAdd.setText(getResources().getString(R.string.text_add));
            txtAdd.setVisibility(View.VISIBLE);
        }

        lndsNhatKi = (LinearLayout) findViewById(R.id.activity_danh_sach_nhat_ki);
        activity_danh_sach_nhat_ki = (LinearLayout) findViewById(R.id.activity_danh_sach_nhat_ki);

        lvDSNhatKiSave = (ListView) findViewById(R.id.lvDSNhatKiSave);
        chiTietNhatKiAdapter = new ChiTietNhatKiAdapter(DanhSachNhatKiActivity.this,R.layout.list_item_nhat_ki);

        hienThiNhatKi("SELECT * FROM dsNhatKi WHERE date= ? "
                ,new String[] {saveValue.getSaved_Date_String()}
        );
        lvDSNhatKiSave.setAdapter(chiTietNhatKiAdapter);
    }
//-------------------------------------------------------------------------
    private void addEvent() {
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VietNhatKiActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

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

            ChiTietNhatKi nhatKi = new ChiTietNhatKi();
            nhatKi.setHinhCamXuc(arrHinhCamXuc.getResourceId(camXuc,-1));
            nhatKi.setHinhDinhKem(R.drawable.ic_calendar);

            String noiDungCamXuc_dsNhatKi = "";
            for (int i = 0; i<=arrCamXuc.length; i++)
            {
                if (camXuc == i)
                {
                    noiDungCamXuc_dsNhatKi = arrCamXuc[i];
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
        hienThiNhatKi("SELECT * FROM dsNhatKi WHERE date= ? "
                ,new String[] {saveValue.getSaved_Date_String()}
        );
        //nếu có nhật kí thì ẩn textView đi
        if (tongSoDongAnhHuong("SELECT * FROM dsNhatKi WHERE date= ? "
                ,new String[] {saveValue.getSaved_Date_String()}) != 0)
        {
            txtAdd.setVisibility(View.GONE);
        }
        else
        {
            txtAdd.setVisibility(View.VISIBLE);
        }
        //set Background
        int bg = saveValue.getSaved_Last_Background_Integer();

            activity_danh_sach_nhat_ki
                    .setBackgroundResource(arrHinhBackground.getResourceId(bg,-1));

    }


//set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
