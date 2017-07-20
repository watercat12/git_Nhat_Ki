package com.arm.nhatki2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.adapter.FontAdapter;
import com.arm.model.Font;
import com.arm.model.NotifiReciever;
import com.arm.model.SaveValue1;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.id;
import static com.arm.nhatki2.Driver_utils.api;
import static com.arm.nhatki2.Driver_utils.driveId;
import static com.arm.nhatki2.Driver_utils.idForDrive;
import static com.arm.nhatki2.Driver_utils.preferences_driverId;
import static com.arm.nhatki2.KhoaActivity.arrFont;
import static com.arm.nhatki2.KhoaActivity.saveValue;
import static com.arm.nhatki2.LaunchActivity.vietLuon;
import static com.arm.nhatki2.R.id.download;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public static String DATABASE_NAME = "NhatKi2.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static String arrCamXuc[];
    public static TypedArray arrHinhCamXuc;
    public static TypedArray arrHinhChonBackground;
    public static TypedArray arrHinhBackground;
    public static String EDIT_NHAT_KI = "EDIT_NHAT_KI";
    public static String CAM_XUC_SELECT = "CAM_XUC_SELECT";
    public static String NHAT_KI_DOI_TUONG_BUNDLE = "NHAT_KI_DOI_TUONG_BUNDLE";
    public static String BAT_DAU_SET_PASS = "BAT_DAU_SET_PASS";
    public static Integer BUTTON_PRESS = 1;
    String GOOGLE_FILE_NAME = "com.arm.nhatki";

    FontAdapter fontAdapter;
    Font font;
    LinearLayout linearLayout;
    DriveId driveId_1;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private static GoogleApiClient api_1;
    Gson gson;
    SharedPreferences.Editor editor_drive;
    Driver_utils driver_utils;

    private static final String TAG = "MainActivity";
    private boolean mResolvingError = false;
    private DriveFile mfile;
    private static final int DIALOG_ERROR_CODE =100;
    private static final String GOOGLE_DRIVE_FILE_NAME = "data_chitieu.sqlite";

    int upload_db = 0;
    int download_db = 0;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        //if (saveValue.getSaved_First_Run_Boolean())
        xuLyThongBao();

        //wrtieFileOnInternalStorage(this,"arm","eo");



        addControl();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_container
                    , new MaterialCalendarFragment()).commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        api_1 = new GoogleApiClient
                .Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }



    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void xuLyThongBao() {
        Intent alarmIntent = new Intent(this, NotifiReciever.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //bắt đầu từ 20h
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        // lặp lại sau 24h
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void addControl() {

        Driver_utils.ctxs = this;
        gson = new Gson();
        driver_utils = new Driver_utils();
        preferences_driverId = getSharedPreferences("ID",MODE_PRIVATE);

        if (saveValue.getSaved_First_Run_Boolean()) {
            dialogHuongDan();
        }


        linearLayout = (LinearLayout) findViewById(R.id.activity_main);


        arrCamXuc = getApplicationContext().getResources()
                .getStringArray(R.array.cam_xuc_array);
        arrHinhCamXuc = getApplicationContext().getResources()
                .obtainTypedArray(R.array.hinh_cam_xuc_array);
        arrHinhChonBackground = getApplicationContext().getResources()
                .obtainTypedArray(R.array.hinh_chon_background_array);
        arrHinhBackground = getApplicationContext().getResources()
                .obtainTypedArray(R.array.hinh_background_array);


        if (vietLuon == 1)
        {
            Log.e("vietluon","vao`");
            Intent intent =
                    new Intent(this
                            , VietNhatKiActivity.class);
            startActivity(intent);
            vietLuon = 2;
        }

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
            case R.id.font_select:
                chonFont();
                break;
            case R.id.background_select:
                chonBackGround();
                break;
            case R.id.rate:
                createDialogRate();
                break;
            case R.id.share:
                share();
                break;
            case R.id.upload:
                if (!api_1.isConnected())
                {
                    api_1.connect();
                    upload_db = 1;
                }


                break;
            case download:
                if (!api_1.isConnected())
                {
                    api_1.connect();

                    download_db = 1;

                    /*File arm = new File(Environment.getDataDirectory().getPath()
                            +"/data/"
                            +getPackageName()
                            +"/databases/"
                            +"arm.db");*/

                }
                //download();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*private void download() {
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{"application/zip"})
                .build(api);
        try {

            startIntentSenderForResult(

                    intentSender, 1000, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {

            Log.w(TAG, e.getMessage());
        }
    }*/
    private static void DownloadFile(final DriveId driveId, final File filename) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!filename.exists()) {
                    try {
                        filename.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                DriveFile file = Drive.DriveApi.getFile(
                        api_1, driveId);
                file.getMetadata(api_1)
                        .setResultCallback(metadataRetrievedCallback);
                DriveApi.DriveContentsResult driveContentsResult = file.open(
                        api_1,
                        DriveFile.MODE_READ_ONLY, null).await();
                DriveContents driveContents = driveContentsResult
                        .getDriveContents();
                InputStream inputstream = driveContents.getInputStream();

                try {
                    FileOutputStream fileOutput = new FileOutputStream(filename);

                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;
                    while ((bufferLength = inputstream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                    }
                    fileOutput.close();
                    inputstream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
            }

        };
        task.execute();
    }

    private static ResultCallback<DriveResource.MetadataResult> metadataRetrievedCallback =
            new ResultCallback<DriveResource.MetadataResult>() {
        @Override
        public void onResult(DriveResource.MetadataResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            Metadata metadata = result.getMetadata();
        }
    };
    private void upload(DriveId driveId_1) {

        //upload file data lên
        File directorys = new File(getDatabasePath(DATABASE_NAME).getPath());
        if (directorys.exists()) {



            //Update file already stored in Drive
            Driver_utils.trash(driveId_1, api_1);
            // Create the Drive API instance
            Driver_utils.creatBackupDrive(this, api_1);
            //Driver_utils.create_backup(this);
            //generateNoteOnSD(this,"water",idForDrive);



            Toast.makeText(getBaseContext()
                    , getResources().getString(R.string.backup)
                    , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext()
                    , "cạn lời", Toast.LENGTH_LONG).show();
        }

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

    private String getDatabasePath1() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;

            myInput = getAssets().open(DATABASE_NAME);


            // Path to the just created empty db
            String outFileName = getDatabasePath1();

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
        int bg = saveValue.getSaved_Last_Background_Integer();
        linearLayout.setBackgroundResource(arrHinhBackground.getResourceId(bg, -1));

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
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        api_1.disconnect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "Connection failed");

       /* if(mResolvingError) { // If already in resolution state, just return.
            return;
        } else */if(result.hasResolution()) { // Error can be resolved by starting an intent with user interaction
            //mResolvingError = true;
            try {
                result.startResolutionForResult(this, DIALOG_ERROR_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DIALOG_ERROR_CODE) {
            // kết nối
            //mResolvingError = false;
            if(resultCode == RESULT_OK) { // Error was resolved, now connect to the api if not done so.
                if(!api_1.isConnecting() && !api_1.isConnected()) {
                    api_1.connect();
                }
            }
        }

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            DriveId mSelectedFileDriveId = data.getParcelableExtra(
                    OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            Log.e("DriveID ---", mSelectedFileDriveId + "");
            Gson gson = new Gson();
            String json = gson.toJson(mSelectedFileDriveId); // myObject - instance of MyObject
            editor_drive = preferences_driverId.edit();
            editor_drive.putString("drive_id", json).commit();
            Log.e(TAG, "driveId this 1-- " + mSelectedFileDriveId);
            if (1 ==1 ) {
                //restore Drive file to SDCArd
                Driver_utils.restoreDriveBackup(this, api_1, GOOGLE_DRIVE_FILE_NAME, preferences_driverId, mfile);
                //Driver_utils.restore(this);

            }
        }

    }

    private ResultCallback<DriveApi.MetadataBufferResult> metadataCallback1 =
            new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {

                }
            };

    private ResultCallback<DriveApi.MetadataBufferResult> metadataCallback2 =
            new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(final DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {

                        return;
                    }
                    String id = result
                            .getMetadataBuffer()
                            .get(0)
                            .getDriveId()
                            .getResourceId();
                    Log.e("id",id);
                    File arm = new File(getDatabasePath(DATABASE_NAME).getPath());
                    DriveId driveId1 = DriveId.zzdD(id);
                    DownloadFile(driveId1,arm);


                    Toast.makeText(MainActivity.this
                            ,getResources().getString(R.string.khoi_phuc)
                            , Toast.LENGTH_SHORT).show();

                }
            };

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "Connected successfully");
        if (upload_db == 1)
        {
            Query query = new Query.Builder()
                    .addFilter(Filters
                            .contains(SearchableField.TITLE, GOOGLE_FILE_NAME))
                    .build();
            Drive.DriveApi.query(api_1, query)
                    .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi
                                .MetadataBufferResult metadataBufferResult) {
                            if (!metadataBufferResult.getStatus().isSuccess()) {
                                Log.e("upload","lỗi");
                                return;
                            }
                            //nếu query không ra gì
                            if (metadataBufferResult.getMetadataBuffer().getCount() == 0)
                            {
                                String json = "";

                                if (preferences_driverId == null)
                                {
                                    SaveValue1 saveValue1 = new SaveValue1(getBaseContext());
                                    json = saveValue1.getSaved_Date_String();
                                }
                                else
                                {
                                    json = preferences_driverId.getString("drive_id", "");
                                }
                                driveId_1 = gson.fromJson(json, DriveId.class);
                            }
                            else
                            {
                                Log.e("upload"
                                        ,metadataBufferResult
                                                .getMetadataBuffer()
                                                .getCount()+"");
                                driveId_1 = metadataBufferResult
                                        .getMetadataBuffer()
                                        .get(0)
                                        .getDriveId();
                            }
                            upload(driveId_1);
                        }
                    });


            upload_db = 0;
        }
        if (download_db == 1)
        {



            Query query = new Query.Builder()
                    .addFilter(Filters.contains(SearchableField.TITLE, GOOGLE_FILE_NAME))
                    .build();
            Drive.DriveApi.query(api_1, query)
                    .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(
                                @NonNull DriveApi
                                        .MetadataBufferResult metadataBufferResult) {
                            if (!metadataBufferResult.getStatus().isSuccess()) {

                                return;
                            }
                            if (metadataBufferResult.getMetadataBuffer().getCount() == 0)
                            {
                                Toast.makeText(MainActivity.this
                                        , getResources().getString(R.string.file_not_found)
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String id = metadataBufferResult
                                        .getMetadataBuffer()
                                        .get(0)
                                        .getDriveId()
                                        .getResourceId();
                                Log.e("id",id);
                                File arm =
                                        new File(getDatabasePath(DATABASE_NAME)
                                                .getPath());
                                DriveId driveId1 = DriveId.zzdD(id);
                                DownloadFile(driveId1,arm);


                                Toast.makeText(MainActivity.this
                                        ,getResources().getString(R.string.khoi_phuc)
                                        , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

            download_db = 0;

        }
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Connection suspended");

    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }
}


