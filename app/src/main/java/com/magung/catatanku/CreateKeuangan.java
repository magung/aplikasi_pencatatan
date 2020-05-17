package com.magung.catatanku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateKeuangan extends DialogFragment {
    @BindView(R.id.et_catatan_k)
    EditText etCatatan;
    @BindView(R.id.et_jumlah)
    EditText etJumlah;
    @BindView(R.id.sp_jenis)
    Spinner mspJenis;

    Unbinder unbinder;
    RecyclerView rvObject;
    Activity activity;
    ArrayList<Keuangan> keuanganArrayList = new ArrayList<>();

    public CreateKeuangan(RecyclerView recyclerView, Activity activity, ArrayList<Keuangan> keuanganArrayList){
        this.rvObject = recyclerView;
        this.activity = activity;
        if(keuanganArrayList != null) {
            this.keuanganArrayList = keuanganArrayList;
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(R.layout.form_input_keuangan)
                .setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            String catatan, jenis;
                            int jumlah;
                            if(etCatatan.getText().toString().equals("") || etJumlah.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Data Kurang Lengkap",Toast.LENGTH_SHORT).show();
                            } else {
                                catatan = String.valueOf(etCatatan.getText());
                                jumlah = Integer.parseInt(String.valueOf(etJumlah.getText()));
                                jenis = mspJenis.getSelectedItem().toString();



                                keuanganArrayList.add(
                                        new Keuangan(catatan, jumlah, jenis)
                                );

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(keuanganArrayList);
                                editor.putString("sp_list_keuangan", json);
                                editor.apply();


                                Toast.makeText(getActivity(),
                                        catatan + " berhasil disimpan",
                                        Toast.LENGTH_SHORT).show();
                            }



                    }
                })
                .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, getDialog()); //bind butter knife ( caranya begini kalo di dialog fragment )
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind(); //kalo udah di bind, harus di "UNBIND / DILEPAS" better knife-nya
    }

}
