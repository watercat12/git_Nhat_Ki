package com.arm2.nhatki2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arm2.adapter.MaterialCalendarAdapter;
import com.arm2.adapter.SavedEventsAdapter;
import com.arm2.model.Const;
import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static com.arm2.model.Const.BAT_DAU_SET_PASS;
import static com.arm2.model.Const.BUTTON_PRESS;
import static com.arm2.model.Const.arrDataHinhCamXuc;
import static com.arm2.model.Const.arrDataTenCamXuc;
import static com.arm2.model.Const.arrHinhBackground_3;
import static com.arm2.model.Const.arrHinhCamXuc;
import static com.arm2.model.Const.arrHinhTheme;
import static com.arm2.model.Const.saveValue;
import static com.arm2.nhatki2.MainActivity.database;
import static com.arm2.nhatki2.MaterialCalendar.getmCurrentDay;

/**
 * Created by Maximilian on 9/1/14.
 */
public class MaterialCalendarFragment extends Fragment implements View.OnClickListener, GridView.OnItemClickListener{
    // Variables

    ImageView mSavedEventImageView;
    //Views
    ImageView mPrevious;
    ImageView mNext;
    TextView mMonthName;
    TextView txtNhatKiGanDayToDay, txtNhatKiGanDayTieuDe, txtNhatKiGanDayCamXuc
            ,txtNhatKiGanDayNoiDung, txtNhatKiGanDayTime;
    ImageView imgNhatKiGanDayCamXuc;
    GridView mCalendar;

    ImageView imgTimeLineClick,imgVietNhatKiNow, imgSetting;
    ImageView imgShare, imgDanhGia;
    LinearLayout lnBackGround;
    private PlusOneButton mPlusOneButton;

    // Calendar Adapter
    private MaterialCalendarAdapter mMaterialCalendarAdapter;

    // Saved Events Adapter
    protected static SavedEventsAdapter mSavedEventsAdapter;
    protected static ListView mSavedEventsListView;

    public static ArrayList<HashMap<String, Integer>> mSavedEventsPerDay;
    public static ArrayList<Integer> mSavedEventDays;

    public static int mNumEventsOnDay = 0;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    //----------------mở màn hình set pass -------------------
    private void moSetPass() {
        Intent intentSetPass = new Intent(getActivity(),KhoaActivity.class);
        intentSetPass.putExtra(BAT_DAU_SET_PASS,BUTTON_PRESS);
        startActivity(intentSetPass);
    }

    //---------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        Log.d("tao_view","view đây");

        if (rootView != null) {
            // Get Calendar info
            MaterialCalendar.getInitialCalendarInfo();
            getSavedEventsForCurrentMonth();

            imgShare = (ImageView) rootView.findViewById(R.id.imgShare);
            imgShare.setOnClickListener(this);
            imgDanhGia = (ImageView) rootView.findViewById(R.id.imgDanhGia);
            imgDanhGia.setOnClickListener(this);
            lnBackGround = (LinearLayout) rootView.findViewById(R.id.lnBackGround);

            imgTimeLineClick = (ImageView) rootView.findViewById(R.id.imgTimeLineClick);
            imgTimeLineClick.setOnClickListener(this);

            imgVietNhatKiNow = (ImageView) rootView.findViewById(R.id.imgVietNhatKiNow);
            imgVietNhatKiNow.setOnClickListener(this);

            imgSetting = (ImageView) rootView.findViewById(R.id.imgSetting);
            imgSetting.setOnClickListener(this);

            /*txtNhatKiGanDayToDay =
                    (TextView) rootView.findViewById(R.id.txtNhatKiGanDayToDay);
            txtNhatKiGanDayTieuDe =
                    (TextView) rootView.findViewById(R.id.txtNhatKiGanDayTieuDe);
            txtNhatKiGanDayCamXuc =
                    (TextView) rootView.findViewById(R.id.txtNhatKiGanDayCamXuc);
            txtNhatKiGanDayNoiDung =
                    (TextView) rootView.findViewById(R.id.txtNhatKiGanDayNoiDung);
            txtNhatKiGanDayTime =
                    (TextView) rootView.findViewById(R.id.txtNhatKiGanDayTime);
            imgNhatKiGanDayCamXuc =
                    (ImageView) rootView.findViewById(R.id.imgNhatKiGanDayCamXuc);*/

            mPlusOneButton = (PlusOneButton) rootView.findViewById(R.id.plus_one_button);
            mPlusOneButton.initialize("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName(), 0);
            /*txtToDay.setText(getmCurrentDay()
                    +"/"+(MaterialCalendar.getmCurrentMonth() + 1)
                    +"/"+MaterialCalendar.getmCurrentYear());*/
            //hienThiNhatKiCuoiCung();

            // Previous ImageView
            mPrevious = (ImageView) rootView.findViewById(R.id.material_calendar_previous);
            if (mPrevious != null) {
                mPrevious.setOnClickListener(this);
            }

            // Month name TextView
            mMonthName = (TextView) rootView.findViewById(R.id.material_calendar_month_name);
            if (mMonthName != null) {
                Calendar cal = Calendar.getInstance();
                if (cal != null) {
                mMonthName.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
                        Locale.getDefault()) + " " + cal.get(Calendar.YEAR));
                }
            }

            // Next ImageView
            mNext = (ImageView) rootView.findViewById(R.id.material_calendar_next);
            if (mNext != null) {
                mNext.setOnClickListener(this);
            }

            // GridView for custom Calendar
            mCalendar = (GridView) rootView.findViewById(R.id.material_calendar_gridView);
            if (mCalendar != null)
            {
                mCalendar.setOnItemClickListener(this);
                mMaterialCalendarAdapter = new MaterialCalendarAdapter(getActivity());
                mCalendar.setAdapter(mMaterialCalendarAdapter);


                // Set current day to be auto selected when first opened
                if (getmCurrentDay() != -1
                        && MaterialCalendar.getmFirstDay() != -1)
                {
                    int startingPosition = 6 + MaterialCalendar.getmFirstDay();
                    int currentDayPosition = startingPosition
                            + getmCurrentDay();

                    Log.d("SELECTED_POSITION", String.valueOf(currentDayPosition));
                    mCalendar.setItemChecked(currentDayPosition, true);

                    if (mMaterialCalendarAdapter != null) {
                        mMaterialCalendarAdapter.notifyDataSetChanged();
                    }
                }
            }

            // ListView for saved events in calendar
        }

        return rootView;
    }
//---------------------------------------------------------------------------
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mSavedEventsListView != null) {
            mSavedEventsAdapter = new SavedEventsAdapter(getActivity());
            mSavedEventsListView.setAdapter(mSavedEventsAdapter);
            mSavedEventsListView.setOnItemClickListener(this);
            Log.d("EVENTS_ADAPTER", "set adapter");

            // Show current day saved events on load
            int today = getmCurrentDay() + 6 + MaterialCalendar.getmFirstDay();
            showSavedEventsListView(today);
        }

    }
//---------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.material_calendar_previous:
                    MaterialCalendar.previousOnClick(mPrevious, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.material_calendar_next:
                    MaterialCalendar.nextOnClick(mNext, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;
                case R.id.imgTimeLineClick:
                    hienTimeLine();
                    break;
                case R.id.imgVietNhatKiNow:
                    vietNow();
                    break;
                case R.id.imgSetting:
                    settingShow();
                    break;
                case R.id.imgShare:
                    xuLyShare();
                    break;
                case R.id.imgDanhGia:
                    xuLyRate();
                default:
                    break;
            }
        }
    }

    private void xuLyRate() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_rate, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                startActivity(
                        new Intent(Intent.ACTION_VIEW
                                , Uri.parse("market://details?id="
                                + getContext().getPackageName())));
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

    private void xuLyShare() {
        String application = "Bần tăng chưa có link";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, application);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    //-----------------------------------------------------------------------------
    private void settingShow() {
        Intent intent = new Intent(getActivity(),SettingActivity.class);
        startActivity(intent);
    }

//-----------------------------------------------------------------------------
    private void vietNow() {

       saveValue.setSaved_Date_String(MaterialCalendar.getmCurrentDay()
               +"/"+(MaterialCalendar.getmCurrentMonth() + 1)
               +"/"+MaterialCalendar.getmCurrentYear());
        Intent intent = new Intent(getActivity(),DanhSachNhatKiActivity.class);
        //lưu date hiện tại vào file SharedPreferences SaveValue.xml
        startActivity(intent);
    }

//-----------------------------------------------------------------------------
    private void hienTimeLine() {
        Intent intent = new Intent(getActivity(),DanhSachNhatKiActivity.class);
        intent.setAction(Const.HIEN_DS_NHAT_KI_TIME_LINE);
        startActivity(intent);
    }

//---------------------khi bấm chọn ngày hoặc bấm chọn trong list event---------------------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //khi bấm chọn ngày
        switch (parent.getId()) {
            case R.id.material_calendar_gridView:
                MaterialCalendar.selectCalendarDay(mMaterialCalendarAdapter, position);

                // Reset event list
                mNumEventsOnDay = -1;

                showSavedEventsListView(position);
                //cần mở 1 Activity để hiện list nhật kí
                //Toast.makeText(parent.getContext(), position+"", Toast.LENGTH_SHORT).show();
                int daySelect = position - (6 + MaterialCalendar.getmFirstDay());
                Log.d("position",position+"");
                String thoiGianDaChon = daySelect+"/"
                        +   (MaterialCalendar.getmMonth()+1)+"/"
                        +   MaterialCalendar.getmYear();
                Toast.makeText(getActivity()
                        , thoiGianDaChon, Toast.LENGTH_SHORT).show();
                Log.d("time_hien_tai",thoiGianDaChon);
                if (daySelect > 0)//nếu bấm chọn vào ô có ngày thì daySelect > 0
                {
                    Intent intent = new Intent(parent.getContext(),DanhSachNhatKiActivity.class);
                    //lưu date hiện tại vào file SharedPreferences SaveValue.xml
                    saveValue.setSaved_Date_String(thoiGianDaChon);
                    startActivity(intent);
                    //activity này có nút thêm nhật kí mới
                    //nhấn lâu vào nhật kí sẵn có để xóa
                }

                
                break;

            default:
                break;
        }

    }
//---------------------------------------------------------------------------
    // dùng để chọn ngày nào có event và có bao nhiu event trong ngày
    protected static void getSavedEventsForCurrentMonth() {
        /**
         *  -- IMPORTANT --
         *  This is where you get saved event info
         */

        // -- Ideas on what could be done here --
        // Probably pull from some database
        // cross check event dates with current calendar month and year

        // For loop adding each event date to ArrayList
        // Also get ArrayList<SavedEvents>

        mSavedEventsPerDay = new ArrayList<HashMap<String, Integer>>();


        mSavedEventDays = new ArrayList<Integer>();

        Random random = new Random();
        int thangTrongLich = (MaterialCalendar.getmMonth()) + 1;
        int namTrongLich = MaterialCalendar.getmYear();
        String sql = "SELECT * FROM dsNhatKi WHERE date LIKE ? ";
        String arr[] = {"%"+thangTrongLich+"/"+namTrongLich};
        Cursor cursor=database.rawQuery(sql,arr);
        while (cursor.moveToNext())
        {
            int vt = cursor.getString(1).indexOf("/");
            String day = cursor.getString(1).substring(0,vt);
            int ngayCoEvent = Integer.parseInt(day);

            HashMap<String, Integer> dayInfo = new HashMap<String, Integer>();
            dayInfo.put("day" + ngayCoEvent,0);

            mSavedEventDays.add(ngayCoEvent);
            mSavedEventsPerDay.add(dayInfo);
            Log.d("sl",ngayCoEvent+"");

        }
        cursor.close();
        /*for (int i = 0; i < soNgayCoEvent; i++) {
            int ngayCoEvent = random.nextInt(MaterialCalendar.getmNumDaysInMonth() - 1) + 1;
            int soEventTrongNgay = random.nextInt(5 - 1) + 1;

            HashMap<String, Integer> dayInfo = new HashMap<String, Integer>();
            dayInfo.put("day" + ngayCoEvent, soEventTrongNgay);

            mSavedEventDays.add(ngayCoEvent);
            mSavedEventsPerDay.add(dayInfo);

            Log.d("EVENTS_PER DAY", String.valueOf(dayInfo));
        }*/

        Log.d("SAVED_EVENT_DATES", String.valueOf(mSavedEventDays));
    }
//-------------------------------------------------------------------------
    private void hienThiNhatKiCuoiCung() {
        int id= saveValue.getSaved_Last_Id_Database_Integer();
        String sql = "SELECT * FROM dsNhatKi WHERE id= ? ";
        Cursor cursor=database.rawQuery(sql,new String[]{String.valueOf(id)} );
        while (cursor.moveToNext())
        {
            String date = cursor.getString(1);
            String tieu_de = cursor.getString(2);
            String noi_dung = cursor.getString(3);
            int camXuc = cursor.getInt(4);
            String time = cursor.getString(5);

            txtNhatKiGanDayToDay.setText(date);
            txtNhatKiGanDayTieuDe.setText(tieu_de.trim());
            txtNhatKiGanDayNoiDung.setText(noi_dung.trim());
            txtNhatKiGanDayCamXuc.setText(saveValue.getSaved_Last_CamXuc_String());
            txtNhatKiGanDayTime.setText(getResources().getString(R.string.write_at)+time);
            imgNhatKiGanDayCamXuc
                    .setImageResource(arrHinhCamXuc
                            .getResourceId(arrDataHinhCamXuc.get(camXuc),-1));
        }
        cursor.close();
    }


//-------------------------------------------------------------------------
    private void hienThiNhatKiMoiThem() {
        int id= saveValue.getSaved_Last_Id_Database_Integer();
        String sql = "SELECT * FROM dsNhatKi ORDER BY id DESC LIMIT 1 ";
        Cursor cursor=database.rawQuery(sql,null);
        while (cursor.moveToNext())
        {
            String date = cursor.getString(1);
            String tieu_de = cursor.getString(2);
            String noi_dung = cursor.getString(3);
            int camXuc = cursor.getInt(4);
            String time = cursor.getString(5);

            txtNhatKiGanDayToDay.setText(date);
            txtNhatKiGanDayTieuDe.setText(tieu_de.trim());
            txtNhatKiGanDayNoiDung.setText(noi_dung.trim());
            txtNhatKiGanDayCamXuc.setText(arrDataTenCamXuc.get(camXuc));
            txtNhatKiGanDayTime.setText(getResources().getString(R.string.write_at)+time);
            imgNhatKiGanDayCamXuc
                    .setImageResource(arrHinhCamXuc
                            .getResourceId(arrDataHinhCamXuc.get(camXuc),-1));
        }
        cursor.close();
    }

//----------------------------------------------------------------------------------
    protected static void showSavedEventsListView(int position) {
        Boolean savedEventsOnThisDay = false;
        int selectedDate = -1;

        if (MaterialCalendar.getmFirstDay() != -1 && mSavedEventDays != null && mSavedEventDays.size
                () > 0) {
            selectedDate = position - (6 + MaterialCalendar.getmFirstDay());
            Log.d("SELECTED_SAVED_DATE", String.valueOf(selectedDate));

            for (int i = 0; i < mSavedEventDays.size(); i++) {
                if (selectedDate == mSavedEventDays.get(i)) {
                    savedEventsOnThisDay = true;
                }
            }
        }

        Log.d("SAVED_EVENTS_BOOL", String.valueOf(savedEventsOnThisDay));

        if (savedEventsOnThisDay) {
            Log.d("POS", String.valueOf(selectedDate));
            if (mSavedEventsPerDay != null && mSavedEventsPerDay.size() > 0) {
                for (int i = 0; i < mSavedEventsPerDay.size(); i++) {
                    HashMap<String, Integer> x = mSavedEventsPerDay.get(i);
                    if (x.containsKey("day" + selectedDate)) {
                        mNumEventsOnDay = mSavedEventsPerDay.get(i).get("day" + selectedDate);
                        Log.d("NUM_EVENT_ON_DAY", String.valueOf(mNumEventsOnDay));
                    }
                }
            }
        } else {
            mNumEventsOnDay = -1;
        }

        if (mSavedEventsAdapter != null && mSavedEventsListView != null) {
            mSavedEventsAdapter.notifyDataSetChanged();

            // Scrolls back to top of ListView before refresh
            mSavedEventsListView.setSelection(0);
        }
    }
//------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();

        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();

        lnBackGround.setBackgroundResource(arrHinhTheme.getResourceId(bg,-1));
        imgTimeLineClick.setBackgroundResource(arrHinhBackground_3.getResourceId(bg,-1));
        imgSetting.setBackgroundResource(arrHinhBackground_3.getResourceId(bg,-1));
        imgVietNhatKiNow.setBackgroundResource(arrHinhBackground_3.getResourceId(bg,-1));
        //tải lại lịch
        MaterialCalendar.refreshCalendar1(mMonthName,mCalendar,mMaterialCalendarAdapter);
        if (saveValue.getSaved_Add_Database_Integer() == 0)
        {
            //hiển thị nhật kí viết cuối cùng
            //hienThiNhatKiCuoiCung();
        }
        else
        {
            //hienThiNhatKiMoiThem();
        }

        //set lại text cho nút pass
        /*if (saveValue.getSaved_Pass_String() == ""
                || saveValue.getSaved_Pass_String() == null)
        {
            btnSetPass.setText(getResources().getString(R.string.set_pass));
            txtSetPass.setText(getResources().getString(R.string.set_pass));
        }
        else
        {
            btnSetPass.setText(getResources().getString(R.string.edit_pass));
            txtSetPass.setText(getResources().getString(R.string.edit_pass));

        }*/
    }


}


