package com.arm2.model;

import java.io.Serializable;

/**
 * Created by ARM on 18-Mar-17.
 */

public class ChiTietNhatKi implements Serializable {
    private String dateNgayThangNam;
    private String tieuDe;
    private int hinhCamXuc;
    private String noiDungCamXuc;
    private String noiDung;
    private String [] hinhDinhKem;
    private String [] hinhDinhKemFull;
    private String time;
    private int camXuc,id;

    public ChiTietNhatKi() {

    }

    public ChiTietNhatKi(String tieuDe
                        , int hinhCamXuc, String noiDungCamXuc
                        , String noiDung, String time) {
        this.tieuDe = tieuDe;
        this.hinhCamXuc = hinhCamXuc;
        this.noiDungCamXuc = noiDungCamXuc;
        this.noiDung = noiDung;
        this.time = time;
    }

    public String getDateNgayThangNam() {
        return dateNgayThangNam;
    }

    public void setDateNgayThangNam(String dateNgayThangNam) {
        this.dateNgayThangNam = dateNgayThangNam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public int getHinhCamXuc() {
        return hinhCamXuc;
    }

    public void setHinhCamXuc(int hinhCamXuc) {
        this.hinhCamXuc = hinhCamXuc;
    }

    public String getNoiDungCamXuc() {
        return noiDungCamXuc;
    }

    public void setNoiDungCamXuc(String noiDungCamXuc) {
        this.noiDungCamXuc = noiDungCamXuc;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String[] getHinhDinhKem() {
        return hinhDinhKem;
    }

    public void setHinhDinhKem(String[] hinhDinhKem) {
        this.hinhDinhKem = hinhDinhKem;
    }

    /*public int getHinhDinhKem() {
        return hinhDinhKem;
    }

    public void setHinhDinhKem(int hinhDinhKem) {
        this.hinhDinhKem = hinhDinhKem;
    }*/

    public String[] getHinhDinhKemFull() {
        return hinhDinhKemFull;
    }

    public void setHinhDinhKemFull(String[] hinhDinhKemFull) {
        this.hinhDinhKemFull = hinhDinhKemFull;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCamXuc() {
        return camXuc;
    }

    public void setCamXuc(int camXuc) {
        this.camXuc = camXuc;
    }
}
