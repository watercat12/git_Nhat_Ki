package com.arm.model;

import android.graphics.Typeface;

/**
 * Created by ARM on 24-Mar-17.
 */

public class Font extends ChiTietNhatKi {
    private String tenFont;
    private Typeface kieuFont;

    public String getTenFont() {
        return tenFont;
    }

    public void setTenFont(String tenFont) {
        this.tenFont = tenFont;
    }

    public Typeface getKieuFont() {
        return kieuFont;
    }

    public void setKieuFont(Typeface kieuFont) {
        this.kieuFont = kieuFont;
    }
}
