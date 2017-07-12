package com.arm2.nhatki2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arm2.adapter.CamXucAdapter;
import com.arm2.adapter.SelectHinhAdapter;
import com.arm2.model.CamXuc;
import com.arm2.model.ChiTietNhatKi;
import com.arm2.model.Hinh;
import com.arm2.model.SelectHinh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.EDIT_NHAT_KI;
import static com.arm2.model.Const.NHAT_KI_DOI_TUONG_BUNDLE;
import static com.arm2.model.Const.PICK_IMAGE_REQUEST;
import static com.arm2.model.Const.arrDataHinhCamXuc;
import static com.arm2.model.Const.arrDataTenCamXuc;
import static com.arm2.model.Const.arrHinhBackground_2;
import static com.arm2.model.Const.arrHinhCamXuc;
import static com.arm2.model.Const.saveValue;
import static com.arm2.nhatki2.MainActivity.database;

public class VietNhatKiActivity extends AppCompatActivity {


    EditText txtEditTieuDe,txtEditNoiDung;
    TextView txtEditCamXuc,txtDay;
    ImageView imgHinhCamXucVietNhatKi, imgChupHinh, imgTest;
    LinearLayout activity_viet_nhat_ki;
    Button btnSave,btnXoa;
    String gioHienTai;
    String date,tieuDe,noiDung,time;
    String action;
    Intent intent;
    ChiTietNhatKi chiTietNhatKi;
    CamXuc camXuc;

    String mCurrentPhotoPath;
    static String pathFileHinh, tenFileHinh;

    ArrayList<Hinh> arrayPath;
    ArrayList<Hinh> arrayPathFull;

    LinearLayout lnAddHinh;

    CamXucAdapter camXucAdapter;
    SelectHinhAdapter hinhAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viet_nhat_ki);
        getSupportActionBar().hide();
        addControl();
        addEvent();
    }

    private void addControl() {

        arrayPath = new ArrayList<>();
        arrayPathFull = new ArrayList<>();

        intent = getIntent();
        action = intent.getAction();

        imgChupHinh = (ImageView) findViewById(R.id.imgChupHinh);


        txtEditTieuDe = (EditText) findViewById(R.id.txtEditTieuDe);
        txtEditNoiDung = (EditText) findViewById(R.id.txtEditNoiDung);
        txtEditCamXuc = (TextView) findViewById(R.id.txtEditCamXuc);
        txtDay = (TextView) findViewById(R.id.txtDay);
        imgHinhCamXucVietNhatKi = (ImageView) findViewById(R.id.imgHinhCamXucVietNhatKi);
        activity_viet_nhat_ki = (LinearLayout) findViewById(R.id.activity_viet_nhat_ki);
        lnAddHinh = (LinearLayout) findViewById(R.id.lnAddHinh);
        btnSave = (Button) findViewById(R.id.btnLuu);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnXoa.setVisibility(View.GONE);
        txtDay.setText(""+saveValue.getSaved_Date_String());
        if (action != null && action.equals(EDIT_NHAT_KI))
        {
            btnXoa.setVisibility(View.VISIBLE);
        }

        /*if (action != null && action.equals(Const.THEM_NK_NGAY_HIEN_TAI))
        {

        }*/

        gioHienTai = getTimeHienTai();//giờ hiện tại
        date = saveValue.getSaved_Date_String();//lấy trong SharedPreferences
        //nếu bấm vào 1 nhật kí nào đó thì sẽ hiển thị nội dung của nhật kí đó lên
        //để người ta sửa
        if (action == EDIT_NHAT_KI)
        {
            hienThiNhatKiDaLuu(intent);
        }

    }
//------------------------------------------------------------------------------------
    private void hienThiNhatKiDaLuu(Intent intent) {

        Bundle bundle = intent.getExtras();
        chiTietNhatKi = (ChiTietNhatKi) bundle.getSerializable(NHAT_KI_DOI_TUONG_BUNDLE);
        String tieuDe = chiTietNhatKi.getTieuDe();
        String noiDung = chiTietNhatKi.getNoiDung();
        String noiDungCamXuc = "";
        String arrHinhDinhKem [] = chiTietNhatKi.getHinhDinhKem();
        String arrHinhDinhKemFull [] = chiTietNhatKi.getHinhDinhKemFull();

        if (arrHinhDinhKem.length != 0 && arrHinhDinhKem != null)
        {
            for (int i = 0; i < arrHinhDinhKem.length ; i++) {
                Hinh hinh = new Hinh();
                String s = arrHinhDinhKem[i];
                hinh.setPath(s);
                String s2 = arrHinhDinhKemFull[i];
                hinh.setPathFull(s2);
                //arrayPath.add(hinh);
                xuLyAddHinh(hinh);
            }

        }

        for (int j = 0; j<= arrDataTenCamXuc.size(); j++)
        {

            if (chiTietNhatKi.getCamXuc() == j)
            {
                noiDungCamXuc = arrDataTenCamXuc.get(j);
                break;
            }
        }
        txtEditTieuDe.setText(tieuDe+"");
        String mystring=new String(noiDung);
        //làm gạch chân chữ
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        txtEditNoiDung.setText(content);
        //txtEditNoiDung.setText(noiDung);
        txtEditCamXuc.setText(noiDungCamXuc);
        imgHinhCamXucVietNhatKi.setImageResource(chiTietNhatKi.getHinhCamXuc());
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
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
        //__________________CHỤP HÌNH___________________________________
        imgChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chupHinh();
                chonHinhBang();

                //dispatchTakePictureIntent();
            }
        });
    }

    private void chonHinhBang() {
        hinhAdapter = new SelectHinhAdapter(this,R.layout.list_phuong_thuc);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //lấy list cảm xúc có sẵn
        for (int i =0; i< 2; i++)
        {
            SelectHinh selectHinh;
            selectHinh = new SelectHinh();
        switch (i)
        {
            case 0:
                selectHinh.setTenPhuongThuc(
                        getApplicationContext().getResources()
                                .getString(R.string.chup));

                break;
            case 1:
                selectHinh.setTenPhuongThuc(
                        getApplicationContext().getResources()
                                .getString(R.string.chon_tu_bo_nho));

        }



            hinhAdapter.add(selectHinh);
        }


        builder.setAdapter(hinhAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which)
                {
                    case 0:
                        dispatchTakePictureIntent();
                    break;
                    case 1:
                        chonTuBoNho();
                }
                /*txtEditCamXuc.setText(camXuc.getNoiDungCamXuc());

                saveValue.setSaved_Last_CamXuc_String(camXuc.getNoiDungCamXuc());
                imgHinhCamXucVietNhatKi.setImageResource(camXuc.getHinhCamXuc());*/
            }
        });

        builder.show();
    }


    //----------------------------------------------------------------------------
    private void chupHinh() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 9);

    }
//----------------------------------------------------------------------------

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


//----------------------------------------------------------------------------
    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        tenFileHinh = imageFileName;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = getCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );





        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        pathFileHinh = mCurrentPhotoPath;
        return image;
    }

//----------------------------------------------------------------------------
    private void chonTuBoNho() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
//----------------------------------------------------------------------------
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "hinh_tao_lao",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 99);
            }
        }
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

        StringBuilder builder = new StringBuilder();
        for (Hinh s : arrayPath) {
            builder.append(s.getPath()+"@");
        }
        String s1 = builder.toString();
        values.put("hinh",s1);

        StringBuilder builder2 = new StringBuilder();
        for (Hinh s : arrayPathFull) {
            builder2.append(s.getPathFull()+"@");
        }
        String s2 = builder2.toString();
        values.put("hinh_full",s2);
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
        for (int i =0; i< arrDataTenCamXuc.size(); i++)
        {
            Log.d("arm",arrDataTenCamXuc.size()+"");
            camXuc = new CamXuc();
            camXuc.setNoiDungCamXuc(arrDataTenCamXuc.get(i));
            camXuc.setCamXuc(i);
            camXuc.setHinhCamXuc(arrHinhCamXuc.getResourceId(arrDataHinhCamXuc.get(i),-1));
            camXucAdapter.add(camXuc);
        }


        builder.setAdapter(camXucAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                camXuc = camXucAdapter.getItem(which);
                txtEditCamXuc.setText(camXuc.getNoiDungCamXuc());

                saveValue.setSaved_Last_CamXuc_String(camXuc.getNoiDungCamXuc());
                imgHinhCamXucVietNhatKi.setImageResource(camXuc.getHinhCamXuc());
            }
        });

        builder.show();
    }


    /*private void getIntegerHinhCamXuc(String sql,String []arr) {
        arrDataTenCamXuc = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,arr);

        while (cursor.moveToNext())
        {

            String tenDataCamXuc = cursor.getString(2);
            arrDataTenCamXuc.add(tenDataCamXuc);

        }
        cursor.close();
    }*/
//------------------------------------------------------------------------------
    //lấy text từ control để lưu vào database
    private void xuLyAddNhatKi() {
        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("tieu_de",txtEditTieuDe.getText().toString());
        values.put("noi_dung",txtEditNoiDung.getText().toString());

        StringBuilder builder = new StringBuilder();
        for (Hinh s : arrayPath) {
            builder.append(s.getPath()+"@");
        }
        String s1 = builder.toString();
        values.put("hinh",s1);
        StringBuilder builder2 = new StringBuilder();
        for (Hinh s : arrayPathFull) {
            builder2.append(s.getPathFull()+"@");
        }
        String s2 = builder2.toString();
        values.put("hinh_full",s2);
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

        }
        //______________________thêm nhật kí__________________________
        else
        {
            if (!textIsEmtype())
            {
                xuLyAddNhatKi();
                saveValue.setSaved_Add_Database_Integer(1);
            }
        }

    }

//------------------------------------------------------------------------------------
    private void xuLyAddHinh(final Hinh hinh) {


        arrayPath.add(hinh);
        arrayPathFull.add(hinh);
        Bitmap bmp = BitmapFactory.decodeFile(hinh.getPath());
        //resize lại hình cho nhẹ

        ImageView imgAddHinh = new ImageView(this);
        imgAddHinh.setImageBitmap(bmp);
        imgAddHinh.setLayoutParams(new LinearLayout.LayoutParams(100,200));

        ImageView imgXoaHinh = new ImageView(this);
        imgXoaHinh.setImageResource(R.drawable.icondelete);
        imgXoaHinh.setLayoutParams(new LinearLayout.LayoutParams(40,40));


        final RelativeLayout rlHinh = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_RIGHT,RelativeLayout.TRUE);
        //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        rlHinh.setLayoutParams(params);
        rlHinh.addView(imgAddHinh);
        rlHinh.addView(imgXoaHinh);
        //xóa hình
        imgXoaHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnAddHinh.removeView(rlHinh);
                arrayPath.remove(hinh);
                arrayPathFull.remove(hinh);
            }
        });
        imgAddHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),XemHinhActivity.class);
                /*intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + hinh.getPathFull()), "image*//*");*/
                intent.putExtra("hinh",hinh.getPathFull());
                startActivity(intent);
            }
        });
        lnAddHinh.setVisibility(View.VISIBLE);
        lnAddHinh.addView(rlHinh);
    }

//------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();
            activity_viet_nhat_ki.setBackgroundResource(arrHinhBackground_2.getResourceId(bg,-1));

    }

//------------------------------------ có hính đã chụp trong intent data------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //___________________lấy hình từ camera_____________________________
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            final String path = pathFileHinh;

            Hinh hinh = new Hinh();

            File fileNen;
            File fileGoc = new File(path);

            // Compress image in main thread using custom Compressor
            fileNen = new Compressor.Builder(this)
                    .setMaxWidth(100)
                    .setMaxHeight(100)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    //lưu vào /data/data/name
                    .setDestinationDirectoryPath(getFilesDir().getAbsolutePath())
                    .build()
                    .compressToFile(fileGoc);
            String duongDanFileNen = fileNen.getAbsolutePath();
            hinh.setPath(duongDanFileNen);
            hinh.setPathFull(path);

            xuLyAddHinh(hinh);
            //saveToInternalStorage(photo);
        }
        //______________________lấy hình từ bộ nhớ_________________________________
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            try {


                Hinh hinh = new Hinh();

                File fileNen;
                File xemFull = FileUtil.from(this, data.getData());
                File fileGoc = FileUtil.from(this, data.getData());

                // Compress image in main thread using custom Compressor
                fileNen = new Compressor.Builder(this)
                        .setMaxWidth(100)
                        .setMaxHeight(100)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.PNG)
                        //lưu vào /data/data/name
                        .setDestinationDirectoryPath(getFilesDir().getAbsolutePath())
                        .build()
                        .compressToFile(fileGoc);
                String duongDanFileNen = fileNen.getAbsolutePath();
                hinh.setPath(duongDanFileNen);
                hinh.setPathFull(xemFull.getAbsolutePath());

                xuLyAddHinh(hinh);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //------------------------------------------------------------------------------------
    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
