package com.magung.catatanku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailCatatan extends AppCompatActivity {

    @BindView(R.id.detail_judul)
    EditText etJudul;
    @BindView(R.id.detail_catatan)
    EditText etCatatan;
    @BindView(R.id.bt_update)
    Button btUpdate;
    @BindView(R.id.bt_hapus)
    Button btHapus;

    private int posisi;

    ArrayList<Catatan> catatanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_catatan);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Detail Catatan"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        //tangkap bundle
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();

        etJudul.setText(bundle.getString("b_judul"));
        etCatatan.setText(bundle.getString("b_catatan"));
        posisi = bundle.getInt("posisi");

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String judul, catatan;
                judul = etJudul.getText().toString();
                catatan = etCatatan.getText().toString();

                catatanArrayList.set(posisi, new Catatan(judul, catatan));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(catatanArrayList);
                editor.putString("sp_list_catatan", json);
                editor.apply();

                Intent intent = new Intent( v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catatanArrayList.remove(posisi);
                //simpan kedalam shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(catatanArrayList);
                editor.putString("sp_list_catatan", json);
                editor.apply();

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
        String json = prefs.getString("sp_list_catatan", "");
        Type type = new TypeToken<ArrayList<Catatan>>() {}.getType();


        if (json != "") {
            catatanArrayList =  gson.fromJson(json, type);
        }

    }

}
