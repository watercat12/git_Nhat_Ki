package com.arm.nhatki2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.adapter.MaterialCalendarAdapter;
import com.arm.adapter.SavedEventsAdapter;
import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static com.arm.nhatki2.KhoaActivity.saveValue;
import static com.arm.nhatki2.MainActivity.BAT_DAU_SET_PASS;
import static com.arm.nhatki2.MainActivity.BUTTON_PRESS;
import static com.arm.nhatki2.MainActivity.arrCamXuc;
import static com.arm.nhatki2.MainActivity.arrHinhCamXuc;
import static com.arm.nhatki2.MainActivity.database;

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
    TextView txtToDay,txtNhatKiGanDayToDay, txtNhatKiGanDayTieuDe, txtNhatKiGanDayCamXuc
            ,txtNhatKiGanDayNoiDung, txtNhatKiGanDayTime;
    ImageView imgNhatKiGanDayCamXuc;
    GridView mCalendar;
    Button btnSetPass;
    TextView txtSetPass;
    private PlusOneButton mPlusOneButton;
    static int daySelect, thangSelect, namSelect;

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


        if (rootView != null) {
            // Get Calendar info
            // Get Calendar info
            MaterialCalendar.getInitialCalendarInfo();
            getSavedEventsForCurrentMonth();
            txtToDay = (TextView) rootView.findViewById(R.id.txtToDay);
            txtNhatKiGanDayToDay = (TextView) rootView.findViewById(R.id.txtNhatKiGanDayToDay);
            txtNhatKiGanDayTieuDe = (TextView) rootView.findViewById(R.id.txtNhatKiGanDayTieuDe);
            txtNhatKiGanDayCamXuc = (TextView) rootView.findViewById(R.id.txtNhatKiGanDayCamXuc);
            txtNhatKiGanDayNoiDung = (TextView) rootView.findViewById(R.id.txtNhatKiGanDayNoiDung);
            txtNhatKiGanDayTime = (TextView) rootView.findViewById(R.id.txtNhatKiGanDayTime);
            imgNhatKiGanDayCamXuc = (ImageView) rootView.findViewById(R.id.imgNhatKiGanDayCamXuc);

            mPlusOneButton = (PlusOneButton) rootView.findViewById(R.id.plus_one_button);
            mPlusOneButton.initialize("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName(), 0);
            txtToDay.setText(MaterialCalendar.getmCurrentDay()
                    +"/"+(MaterialCalendar.getmCurrentMonth() + 1)
                    +"/"+MaterialCalendar.getmCurrentYear());
            hienThiNhatKiCuoiCung();
            btnSetPass = (Button) rootView.findViewById(R.id.btnSetPass);
            btnSetPass.setOnClickListener(this);
            txtSetPass = (TextView) rootView.findViewById(R.id.txtSetPass);
            txtSetPass.setOnClickListener(this);
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
                if (MaterialCalendar.getmCurrentDay() != -1 && MaterialCalendar.getmFirstDay() != -1)
                {
                    int startingPosition = 6 + MaterialCalendar.getmFirstDay();
                    int currentDayPosition = startingPosition + MaterialCalendar.getmCurrentDay();


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


            // Show current day saved events on load
            int today = MaterialCalendar.getmCurrentDay() + 6 + MaterialCalendar.getmFirstDay();
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
                case R.id.btnSetPass:
                    moSetPass();
                    break;
                case R.id.txtSetPass:
                    moSetPass();
                    break;
                default:
                    break;
            }
        }
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
                daySelect = position - (6 + MaterialCalendar.getmFirstDay());
                thangSelect = (MaterialCalendar.getmMonth()+1);
                namSelect = MaterialCalendar.getmYear();
                String thoiGianHienTai = daySelect+"/"
                        +   (MaterialCalendar.getmMonth()+1)+"/"
                        +   MaterialCalendar.getmYear();

                if (daySelect > 0)
                {
                    Intent intent = new Intent(parent.getContext()
                            ,DanhSachNhatKiActivity.class);
                    //lưu date hiện tại vào file SharedPreferences SaveValue.xml
                    saveValue.setSaved_Date_String(thoiGianHienTai);
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
            imgNhatKiGanDayCamXuc.setImageResource(arrHinhCamXuc.getResourceId(camXuc,-1));
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
            txtNhatKiGanDayCamXuc.setText(arrCamXuc[camXuc]);
            txtNhatKiGanDayTime.setText(getResources().getString(R.string.write_at)+time);
            imgNhatKiGanDayCamXuc.setImageResource(arrHinhCamXuc.getResourceId(camXuc,-1));
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

            for (int i = 0; i < mSavedEventDays.size(); i++) {
                if (selectedDate == mSavedEventDays.get(i)) {
                    savedEventsOnThisDay = true;
                }
            }
        }



        if (savedEventsOnThisDay) {

            if (mSavedEventsPerDay != null && mSavedEventsPerDay.size() > 0) {
                for (int i = 0; i < mSavedEventsPerDay.size(); i++) {
                    HashMap<String, Integer> x = mSavedEventsPerDay.get(i);
                    if (x.containsKey("day" + selectedDate)) {
                        mNumEventsOnDay = mSavedEventsPerDay.get(i).get("day" + selectedDate);
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
        //tải lại lịch
        MaterialCalendar.refreshCalendar1(mMonthName,mCalendar,mMaterialCalendarAdapter);
        if (saveValue.getSaved_Add_Database_Integer() == 0)
        {
            //hiển thị nhật kí viết cuối cùng
            hienThiNhatKiCuoiCung();
        }
        else
        {
            hienThiNhatKiMoiThem();
        }

        //set lại text cho nút pass
        if (saveValue.getSaved_Pass_String() == ""
                || saveValue.getSaved_Pass_String() == null)
        {
            btnSetPass.setText(getResources().getString(R.string.set_pass));
            txtSetPass.setText(getResources().getString(R.string.set_pass));
        }
        else
        {
            btnSetPass.setText(getResources().getString(R.string.edit_pass));
            txtSetPass.setText(getResources().getString(R.string.edit_pass));

        }
    }


}


