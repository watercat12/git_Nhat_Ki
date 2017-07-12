package com.arm2.nhatki2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.arjsna.passcodeview.PassCodeView;

import static com.arm2.model.Const.BAT_DAU_SET_PASS;
import static com.arm2.model.Const.BUTTON_PRESS;
import static com.arm2.model.Const.arrHinhTheme;
import static com.arm2.model.Const.saveValue;


/**
 * Created by arjun on 23/1/17.
 */
//xử lý pass ở đây
public class NumLockFragment extends Fragment {
    private String PASSCODE = "";
    private PassCodeView passCodeView;
    TextView messageKhoa,txtTimeKhoa;
    ImageView imgKhoa;
    Intent intent;
    Integer kt;
    public static int dem;
    int soLanSai = 2;

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
        passCodeView.setKeyTextColor(R.color.black_shade);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        //messageKhoa.setTypeface(typeFace);
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
                            if (text.equals(PASSCODE))
                            {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                dem =0;
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

        //set Background
        int bg = saveValue.getSaved_Last_Theme_Integer();

        imgKhoa.setBackgroundResource(arrHinhTheme.getResourceId(bg,-1));

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
