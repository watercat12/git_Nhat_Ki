package com.arm2.model;

import android.content.res.TypedArray;

import java.util.ArrayList;

/**
 * Created by ARM on 21-Apr-17.
 */

public class Const {

    public static final int CAMERA_REQUEST_CODE = 8;
    public static final String HIEN_DS_NHAT_KI_TIME_LINE = "HIEN_DS_NHAT_KI_TIME_LINE";
    public static final String THEM_NK_NGAY_DANG_CHON = "THEM_NK_NGAY_DANG_CHON";
    public static final String THEM_NK_NGAY_HIEN_TAI = "THEM_NK_NGAY_HIEN_TAI";

    public static ArrayList<String> arrDataTenCamXuc;
    public static ArrayList<Integer> arrDataHinhCamXuc;
    public static ArrayList<String> arrDataTenCamXucGoc;
    public static ArrayList<Integer> arrDaTaHinhCamXucGoc;
    public static String arrTenSetting[];
    public static TypedArray arrHinhCamXuc;
    public static TypedArray arrHinhChonBackground;
    public static TypedArray arrHinhTheme;
    public static TypedArray arrHinhBackground_1;
    public static TypedArray arrHinhBackground_2;
    public static TypedArray arrHinhBackground_3;

    public static TypedArray arrHinhSetting;
    public static final String EDIT_NHAT_KI = "EDIT_NHAT_KI";
    public static final String CAM_XUC_EDIT = "CAM_XUC_SELECT";
    public static final String INTEN_EDIT_CAM_XUC = "INTEN_EDIT_CAM_XUC";
    public static final String NHAT_KI_DOI_TUONG_BUNDLE = "NHAT_KI_DOI_TUONG_BUNDLE";
    public static final String BAT_DAU_SET_PASS = "BAT_DAU_SET_PASS";
    public static final Integer BUTTON_PRESS = 1;
    public static Integer PICK_IMAGE_REQUEST = 5;

    public static SaveValue saveValue;
    public static String arrFont[];
}
