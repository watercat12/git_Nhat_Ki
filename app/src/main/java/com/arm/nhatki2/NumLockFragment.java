package com.arm.nhatki2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.arjsna.passcodeview.PassCodeView;

import static android.R.attr.id;
import static com.arm.nhatki2.KhoaActivity.saveValue;

import static com.arm.nhatki2.MainActivity.BAT_DAU_SET_PASS;
import static com.arm.nhatki2.MainActivity.BUTTON_PRESS;
import static com.arm.nhatki2.MainActivity.database;

/**
 * Created by arjun on 23/1/17.
 */
//xử lý pass ở đây
public class NumLockFragment extends Fragment {
    private String PASSCODE = "";
    private PassCodeView passCodeView;
    TextView messageKhoa,txtTimeKhoa,txtXoaPass;
    ImageView imgKhoa;
    Intent intent;
    Integer kt;
    public static int dem;
    int soLanSai = 2;
    long timeXoaPass;
    public static String HOM_NAY = "HOM_NAY";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getActivity().getIntent();
        kt = intent.getIntExtra(BAT_DAU_SET_PASS, 0);
        PASSCODE = saveValue.getSaved_Pass_String();
        Log.d("pass", kt + "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_khoa, container, false);
        passCodeView = (PassCodeView) mRootView.findViewById(R.id.pass_code_view);
        imgKhoa = (ImageView) mRootView.findViewById(R.id.imgKhoa);
        messageKhoa = (TextView) mRootView.findViewById(R.id.messageKhoa);
        txtTimeKhoa = (TextView) mRootView.findViewById(R.id.txtTimeKhoa);
        //Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HeraBig.ttf");
        // passCodeView.setTypeFace(typeFace);
        txtXoaPass = (TextView) mRootView.findViewById(R.id.txtXoaPass);
        passCodeView.setKeyTextColor(R.color.black_shade);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        //messageKhoa.setTypeface(typeFace);

        //timeXoaPass = System.currentTimeMillis();

//nếu đã bấm xóa pass sẽ hiện dialog cảnh báo pass sẽ bị xóa
        if (saveValue.getSaved_Time_Xoa_Pass_Long() != 0
                &&
                (System.currentTimeMillis() - saveValue.getSaved_Time_Xoa_Pass_Long())
                        < (10*1000))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.dialog_xoa_pass_al));
            builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        //nếu hết thời gian mà người ta chờ để xóa pass thì bỏ qua màn hình khóa luôn
        if ((System.currentTimeMillis() - (saveValue.getSaved_Time_Xoa_Pass_Long()) )
                >= (60*60*24*1000) && saveValue.getSaved_Time_Xoa_Pass_Long() != 0) {
            saveValue.setSaved_Pass_String("");

            saveValue.setSaved_Time_Xoa_Pass_Long(Long.valueOf(0));
            intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }


        //click xóa pass khi quên
        txtXoaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDemTimeXoaPass();

            }
        });

        //khi bấm nút set pass
        if (kt == BUTTON_PRESS) {

            if (PASSCODE == "" || PASSCODE == null) {
                setPASSCODE();
                Log.d("chua_co_pass", PASSCODE);
            } else {
                messageKhoa.setText(getResources().getString(R.string.message_enter_pass));
                setPASSCODE_Final();
                Log.d("da_co_pass", PASSCODE);
            }

        } else if (PASSCODE != "" && PASSCODE != null) {
            bindEvents();

        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        //khi còn khóa
        if ((System.currentTimeMillis() - (saveValue.getSaved_Time_Pass_Long()) )
                >= (10*1000)) {
            xuLyHetKhoa();
        }

        if (saveValue.getSaved_Is_Khoa_Boolean() == true)
        {
            xuLyConKhoa();
        }

        return mRootView;
    }
//------------------------đếm time để xóa pass----------------------------
    private void xuLyDemTimeXoaPass() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.dialog_xoa_pass));
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timeXoaPass = System.currentTimeMillis();
                saveValue.setSaved_Time_Xoa_Pass_Long(timeXoaPass);
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

//------------------------xóa pass khi quên----------------------------


//------------------------set pass khi đã có pass trước đó----------------------------
    private void setPASSCODE_Final() {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override
            public void onTextChanged(String text) {
                if (text.length() == 4) {
                    if (text.equals(PASSCODE)) {
                        passCodeView.reset();
                        dem =0;
                        setPASSCODE();

                    } else {
                        passCodeView.setError(true);
                        if (dem >= soLanSai)
                        {
                            xuLySaiPassNhieu();

                        }
                        passCodeView.setError(true);
                        dem += 1;
                    }
                }
            }
        });
    }

//----------------------kiểm tra pass để mở MainActivity-------------------------------
    private void bindEvents() {

            passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
                @Override
                public void onTextChanged(String text) {

                        if (text.length() == 4)
                        {
                            //đúng pass
                            if (text.equals(PASSCODE))
                            {
                                /*if (vietLuon == 1)
                                {
                                    Log.e("vietluon","vao`");
                                    Intent intent =
                                            new Intent(getActivity()
                                                    , VietNhatKiActivity.class);
                                    intent.setAction(HOM_NAY);
                                    startActivity(intent);
                                    getActivity().finish();
                                    //để không xóa pass nữa nếu nhập đúng pass
                                    saveValue.setSaved_Time_Xoa_Pass_Long((long) 0);
                                    dem =0;
                                }*/
                                /*else
                                {*/
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    //để không xóa pass nữa nếu nhập đúng pass
                                    saveValue.setSaved_Time_Xoa_Pass_Long((long) 0);
                                    dem =0;
                                //}


                            }
                            else
                            {
                                if (dem >= soLanSai)
                                {
                                    xuLySaiPassNhieu();

                                }
                                passCodeView.setError(true);
                                dem += 1;
                            }
                        }

                }
            });
    }
//-------------------------xử lý còn khóa------------------------------------
    private void xuLyConKhoa() {
        txtTimeKhoa.setVisibility(View.VISIBLE);
        passCodeView.setVisibility(View.GONE);
        messageKhoa.setVisibility(View.GONE);
        imgKhoa.setImageResource(R.drawable.meo_fuk);

    }

//------------------------hết khóa-----------------------------------
    private void xuLyHetKhoa() {
        txtTimeKhoa.setVisibility(View.GONE);
        passCodeView.setVisibility(View.VISIBLE);
        messageKhoa.setVisibility(View.VISIBLE);
        imgKhoa.setImageResource(R.drawable.iconkhoa);
        saveValue.setSaved_Is_Khoa_Boolean(false);
    }
//----------------------khi nhập sai pass nhiều thì khóa lâu-----------------------------
    private void xuLySaiPassNhieu() {

        txtTimeKhoa.setVisibility(View.VISIBLE);
        passCodeView.setVisibility(View.GONE);
        messageKhoa.setVisibility(View.GONE);
        imgKhoa.setImageResource(R.drawable.meo_fuk);
        long time = System.currentTimeMillis();
        saveValue.setSaved_Time_Pass_Long(time);
        saveValue.setSaved_Is_Khoa_Boolean(true);

    }
//-----------------------thiết lập pass mới------------------------------

    private void setPASSCODE() {
        messageKhoa.setText(getResources().getString(R.string.message_set_pass));
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override
            public void onTextChanged(String text) {
                if (text.length() == 4) {
                    //lấy pass lần 1
                    final String pass1 = text;
                    //reset text
                    passCodeView.reset();
                    messageKhoa.setText(getResources().getString(R.string.message_set_pass_again));
                    //xác nhận pass
                    setPASSCODE_1(pass1);


                }
            }
        });
    }

//----------------------xác nhận lại pass cho chắc ăn trước khi lưu--------------------
    private void setPASSCODE_1(final String pass1) {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override
            public void onTextChanged(String text) {
                if (text.length() == 4) {
                    //so pass lần 1 với pass lần 2
                    if (text.equals(pass1)) {
                        saveValue.setSaved_Pass_String(text);
                        getActivity().finish();
                    } else {
                        passCodeView.setError(true);
                    }
                }
            }
        });
    }


    //--------------------------------check pass---------------------------------


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
