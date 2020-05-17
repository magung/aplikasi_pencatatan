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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateCatatan extends DialogFragment {
    @BindView(R.id.et_judul)
    EditText met_judul;
    @BindView(R.id.et_catatan)
    EditText met_catatan;

    Unbinder unbinder;
    RecyclerView rvObject;
    Activity activity;
    private ArrayList<Catatan> catatanArrayList = new ArrayList<>();

    public CreateCatatan(RecyclerView rvObject, Activity activity, ArrayList<Catatan> catatanArrayList) {
        this.rvObject = rvObject;
        this.activity = activity;
        if(catatanArrayList != null){
            this.catatanArrayList = catatanArrayList;
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(R.layout.form_input_catatan)
                .setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String judul, catatan;
                        judul = String.valueOf(met_judul.getText());
                        catatan = String.valueOf(met_catatan.getText());

                        if(judul.equals("") || catatan.equals("")){
                            Toast.makeText(getActivity(), "Data Kurang Lengkap",Toast.LENGTH_SHORT).show();
                        }else{
                            catatanArrayList.add(
                                    new Catatan(judul, catatan)
                            );

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(catatanArrayList);
                            editor.putString("sp_list_catatan", json);
                            editor.apply();

                            Toast.makeText(getActivity(),
                                    judul + " berhasil disimpan",
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
