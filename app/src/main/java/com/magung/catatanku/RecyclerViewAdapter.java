package com.magung.catatanku;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Catatan> catatanArrayList;
    Activity activity;

    public RecyclerViewAdapter(Context context, ArrayList<Catatan> catatanArrayList, Activity activity) {
        this.context = context;
        this.catatanArrayList = catatanArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.CategoryViewHolder holder, int position) {
        final String
                judul = catatanArrayList.get(position).getJudul(),
                catatan = catatanArrayList.get(position).getCatatan();

        int lengCat = catatan.length();
        String catatans;
        if (lengCat >= 20) {
            if(lengCat == 20) {
                catatans  = catatan.substring(0, 20);
            } else {
                catatans  = catatan.substring(0, 20) + "...";
            }
        } else {
            catatans = catatan;
        }
        holder.tvJudul.setText(judul);
        holder.tvCatatan.setText(catatans);
        Bundle b = new Bundle();
        b.putString("b_judul", judul);
        b.putString("b_catatan", catatan);
        b.putInt("posisi", position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(judul)
                        .setCancelable(true)
                        .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                catatanArrayList.remove(position);
                                notifyDataSetChanged();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(catatanArrayList);
                                editor.putString("sp_list_catatan", json);
                                editor.apply();
                            }
                        })
                        .setPositiveButton("Lihat", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent( v.getContext(), DetailCatatan.class);
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
        return catatanArrayList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_judul)
        TextView tvJudul;
        @BindView(R.id.tv_catatan)
        TextView tvCatatan;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
