package com.magung.catatanku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailKeuangan extends AppCompatActivity {

    @BindView(R.id.detail_catatan_keuangan)
    EditText detailCatatan;
    @BindView(R.id.detail_jumlah_keuangan)
    EditText detailJumlah;
    @BindView(R.id.detail_jenis_keuangan)
    Spinner detailJenis;
    @BindView(R.id.bt_update_keuangan)
    Button update;
    @BindView(R.id.bt_hapus_keuangan)
    Button hapus;


    private int posisi;

    ArrayList<Keuangan> keuanganArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_keuangan);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Detail Keuangan"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        //tangkap bundle
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        detailCatatan.setText(bundle.getString("b_catatan"));
        detailJumlah.setText(String.valueOf(bundle.getInt("b_jumlah")));
        posisi = bundle.getInt("posisi");

        //set data untuk spinner harus dikonversi dulu kedalam array
        String[] arrActive = getResources().getStringArray(R.array.jenis_keuangan);
        int idxActive = Arrays.asList(arrActive).indexOf(bundle.getString("b_jenis"));//find the index
        detailJenis.setSelection(idxActive);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catatan, jenis;
                int jumlah;
                catatan = String.valueOf(detailCatatan.getText());
                jumlah = Integer.parseInt(String.valueOf(detailJumlah.getText()));
                jenis = detailJenis.getSelectedItem().toString();

                keuanganArrayList.set(posisi, new Keuangan(catatan, jumlah, jenis));

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(keuanganArrayList);
                editor.putString("sp_list_keuangan", json);
                editor.apply();


                Intent intent = new Intent( v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }

        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keuanganArrayList.remove(posisi);
                //simpan kedalam shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(keuanganArrayList);
                editor.putString("sp_list_keuangan", json);
                editor.apply();
//
                Intent intent = new Intent( v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //load data dari shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString("sp_list_keuangan", "");
        Type type = new TypeToken<ArrayList<Keuangan>>() {}.getType();


        if (json != "") {
            keuanganArrayList =  gson.fromJson(json, type);
        }

    }
}
