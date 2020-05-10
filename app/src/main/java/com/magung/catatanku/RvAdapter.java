package com.magung.catatanku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.KeuanganViewHolder>{

    Context context;
    Activity activity;
    ArrayList<Keuangan> keuanganArrayList;

    public RvAdapter(Context context, Activity activity, ArrayList<Keuangan> keuanganArrayList) {
        this.context = context;
        this.activity = activity;
        this.keuanganArrayList = keuanganArrayList;
    }

    @NonNull
    @Override
    public RvAdapter.KeuanganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_keuangan, parent, false);
        return new KeuanganViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapter.KeuanganViewHolder holder, int position) {
        final String
                catatan = keuanganArrayList.get(position).getCatatan(),
                s_catatan,
                jenis = keuanganArrayList.get(position).getJenis();
        final int jumlah = keuanganArrayList.get(position).getJumlah();

        int lengCat = catatan.length();
        if(lengCat >= 30) {
            if(lengCat == 30) {
                s_catatan = catatan.substring(0, 30);
            } else {
                s_catatan = catatan.substring(0, 30) + "...";
            }
        } else {
            s_catatan = catatan;
        }

        holder.catatanKeuangan.setText(s_catatan);
        holder.jumlahKeuangan.setText(String.valueOf(jumlah));
        holder.jenisKeuangan.setText(jenis);
        Bundle b = new Bundle();
        b.putInt("b_jumlah", jumlah);
        b.putString("b_catatan", catatan);
        b.putString("b_jenis", jenis);
        b.putInt("posisi", position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(catatan)
                        .setCancelable(true)
                        .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                keuanganArrayList.remove(position);
                                notifyDataSetChanged();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(keuanganArrayList);
                                editor.putString("sp_list_keuangan", json);
                                editor.apply();
                            }
                        })
                        .setPositiveButton("Lihat", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent( v.getContext(), DetailKeuangan.class);
                                intent.putExtras(b);
                                v.getContext().startActivity(intent);
                            }
                        })
                        .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return keuanganArrayList.size();
    }

    public class KeuanganViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_catatan_keuangan)
        TextView catatanKeuangan;
        @BindView(R.id.tv_jumlah_keuangan)
        TextView jumlahKeuangan;
        @BindView(R.id.tv_jenis_keuangan)
        TextView jenisKeuangan;
        public KeuanganViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
