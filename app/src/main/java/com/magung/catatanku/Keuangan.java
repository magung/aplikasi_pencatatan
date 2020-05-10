package com.magung.catatanku;

public class Keuangan {
    String catatan, jenis;
    int jumlah;

    public Keuangan(String catatan, int jumlah, String jenis) {
        this.catatan = catatan;
        this.jumlah = jumlah;
        this.jenis = jenis;
    }


    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
