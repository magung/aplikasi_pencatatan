package com.magung.catatanku;

public class Catatan {
    String judul, catatan;

    public Catatan(String judul, String catatan) {
        this.judul = judul;
        this.catatan = catatan;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
