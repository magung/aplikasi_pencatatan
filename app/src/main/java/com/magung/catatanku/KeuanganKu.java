package com.magung.catatanku;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class KeuanganKu extends Fragment {

    @BindView(R.id.rv_container_keuangan)
    RecyclerView rv;
    @BindView(R.id.tv_total_pemasukan)
    TextView totalPemasukan;
    @BindView(R.id.tv_total_pengeluaran)
    TextView totalPengeluaran;
    @BindView(R.id.tv_total_saldo)
    TextView totalSaldo;

    ArrayList<Keuangan> keuanganArrayList = new ArrayList<>();

    private int pemasukan = 0, pengeluaran = 0, saldo = 0;

    @OnClick(R.id.tambah_keuangan) void set() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateKeuangan createKeuangan = new CreateKeuangan(rv, getActivity(), keuanganArrayList);
        createKeuangan.show(fm, "Keuangan");
    }

    private Unbinder unbinder;

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        return inflater.inflate(R.layout.fragment_keuangan_ku, container, false);

        View v =  inflater.inflate(R.layout.fragment_keuangan_ku, container, false);
        unbinder = ButterKnife.bind(this, v);
        showRv();
        getData();
        getActivity().setTitle("KeuanganKu");
//        if ( keuanganArrayList.size() > 0 ) {
//            showRv();
//            int pem = 0, peng = 0;
//            for (int i = 0; i < keuanganArrayList.size(); i++) {
//                if(keuanganArrayList.get(i).getJenis().equals("Pemasukan")) {
//                    pem += keuanganArrayList.get(i).getJumlah();
//                } else if(keuanganArrayList.get(i).getJenis().equals("Pengeluaran")) {
//                    peng += keuanganArrayList.get(i).getJumlah();
//                }
//            }
//            this.pemasukan = pem;
//            this.pengeluaran = peng;
//
//        }
//        totalPemasukan.setText(String.valueOf(pemasukan));
//        totalPengeluaran.setText(String.valueOf(pengeluaran));
//        totalSaldo.setText(String.valueOf(saldo));
        return v;
    }

    public void showRv() {
        rv.setLayoutManager((new LinearLayoutManager(getContext())));
        RvAdapter adapter = new RvAdapter(getContext(), getActivity(), keuanganArrayList);
        rv.setAdapter(adapter);
    }

    public void getData() {
        //load data dari shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString("sp_list_keuangan", "");
        Type type = new TypeToken<ArrayList<Keuangan>>() {}.getType();

        if (json != "") {
            keuanganArrayList =  gson.fromJson(json, type);
            if (keuanganArrayList.size() > 0 ) {

                int pem = 0, peng = 0;

                for (int i = 0; i < keuanganArrayList.size(); i++) {
                    if(keuanganArrayList.get(i).getJenis() != null) {
                        if(keuanganArrayList.get(i).getJenis().equals("Pemasukan")) {
                            pem += keuanganArrayList.get(i).getJumlah();
                        } else if(keuanganArrayList.get(i).getJenis().equals("Pengeluaran")) {
                            peng += keuanganArrayList.get(i).getJumlah();
                        }
                    }

                }
                this.pemasukan = pem;
                this.pengeluaran = peng;
                this.saldo = pem - peng;
            }

            showRv();
        }

        totalPemasukan.setText(String.valueOf(pemasukan));
        totalPengeluaran.setText(String.valueOf(pengeluaran));
        totalSaldo.setText(String.valueOf(saldo));
    }
}
