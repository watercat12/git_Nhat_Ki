package com.arm2.nhatki2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arm2.adapter.FontAdapter;
import com.arm2.model.Const;
import com.arm2.model.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arm2.model.Const.arrDaTaHinhCamXucGoc;
import static com.arm2.model.Const.arrDataHinhCamXuc;
import static com.arm2.model.Const.arrDataTenCamXuc;
import static com.arm2.model.Const.arrDataTenCamXucGoc;
import static com.arm2.model.Const.arrFont;
import static com.arm2.model.Const.arrHinhTheme;
import static com.arm2.model.Const.saveValue;



public class MainActivity extends AppCompatActivity {
    public static String DATABASE_NAME = "data.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;


    FontAdapter fontAdapter;
    Font font;
    LinearLayout linearLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        processCopy();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        addControl();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_container
                    , new MaterialCalendarFragment()).commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private void addControl() {



        requestPermissionCamera();

        if (saveValue.getSaved_First_Run_Boolean()) {
            dialogHuongDan();
        }


        linearLayout = (LinearLayout) findViewById(R.id.activity_main);
        addVaoArrayTenCamXuc("SELECT * FROM dsMemo ",null);

    }


    public boolean requestPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.storage_permission_request_title))
                    .setMessage(getString(R.string.storage_permission_request_summary))
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //mở màn hình của android để yêu cầu đồng ý quyền
                            //gửi cái requestCode Const.EXTDIR_REQUEST_CODE đến onRequestPermissionsResult
                            ActivityCompat
                                    .requestPermissions(MainActivity.this,
                                            new String[]{
                                                    Manifest
                                                    .permission
                                                    .CAMERA},
                                            Const.CAMERA_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false);

            alert.create().show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Const.CAMERA_REQUEST_CODE:
                if ((grantResults.length > 0) &&
                        (grantResults[0] != PackageManager.PERMISSION_GRANTED))
                {
                    /**
                     * nếu không có quyền bộ nhớ thì thoát
                     */
                    finish();
                }

        }
    }

//----------------------------------------------------------------

    private void addVaoArrayTenCamXuc(String sql,String []arr) {
        arrDataTenCamXuc = new ArrayList<>();
        arrDataHinhCamXuc = new ArrayList<>();
        arrDaTaHinhCamXucGoc = new ArrayList<>();
        arrDataTenCamXucGoc = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,arr);

        while (cursor.moveToNext())
        {
            Integer goc = 0;

            if (cursor.getInt(3) == goc)
            {

                String tenDataCamXucGoc = cursor.getString(1);
                arrDataTenCamXucGoc.add(tenDataCamXucGoc);
                int hinhDataCamXucGoc = cursor.getInt(2);
                arrDaTaHinhCamXucGoc.add(hinhDataCamXucGoc -1 );
            }
            String tenDataCamXucGoc = cursor.getString(1);
            arrDataTenCamXuc.add(tenDataCamXucGoc);
            int hinhDaTaCamXuc = cursor.getInt(2);
            arrDataHinhCamXuc.add(hinhDaTaCamXuc -1);

        }
        cursor.close();
    }

    //----------------------------hướng dẫn------------------------------------
    private void dialogHuongDan() {
        LayoutInflater inflater1 = LayoutInflater.from(this);
        View view = inflater1.inflate(R.layout.dialog_huong_dan, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(false);
        Button btnOKConDe = (Button) view.findViewById(R.id.btnOkConDe);
        TextView txtHuongDan = (TextView) view.findViewById(R.id.txtHuongDan);
        txtHuongDan.setText(getResources().getString(R.string.huong_dan));
        builder1.setView(view);
        final AlertDialog DialogHD = builder1.create();
        btnOKConDe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveValue.setSaved_First_Run_Boolean(false);
                DialogHD.dismiss();
            }
        });

        DialogHD.show();
        //builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.font_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.rate:
                createDialogRate();
                break;
            case R.id.share:
                share();
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------
    private void share() {
        String application = "Bần tăng chưa có link";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, application);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    //---------------------------chọn background------------------------
    private void chonBackGround() {

        Intent intent = new Intent(this, BackgroundActivity.class);
        startActivity(intent);

    }

    //----------------------------chọn font---------------------
    private void chonFont() {

        fontAdapter = new FontAdapter(this, R.layout.list_fonts);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //lấy list cảm xúc có sẵn
        for (int i = 0; i < arrFont.length; i++) {

            font = new Font();

            font.setTenFont(arrFont[i]);

            Typeface typeface1 = Typeface.createFromAsset(this
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
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);


    }


    private void createDialogRate() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_rate, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        Button btnOK = (Button) view.findViewById(R.id.btnRate);
        Button btnCancel = (Button) view.findViewById(R.id.btnrateCancel);
        RatingBar rating = (RatingBar) view.findViewById(R.id.ratingBar1);
        rating.setProgress(10);
        Drawable drawable = rating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFFDEC00"), PorterDuff.Mode.SRC_ATOP);

        builder.setView(view);
        final AlertDialog DialogRate = builder.create();
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogRate.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogRate.dismiss();
            }
        });
        DialogRate.show();
        //builder.show();
    }

    //----------------coppy database vào bộ nhớ máy------------------------------------
    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;

            myInput = getAssets().open(DATABASE_NAME);


            // Path to the just created empty db
            String outFileName = getDatabasePath();

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();
        linearLayout.setBackgroundResource(arrHinhTheme.getResourceId(bg, -1));

    }

    //-------------------------set font-----------------------------------------------
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }
}


