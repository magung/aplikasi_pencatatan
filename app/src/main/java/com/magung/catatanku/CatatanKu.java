package com.magung.catatanku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CatatanKu extends Fragment {

    @BindView(R.id.rv_container)
    RecyclerView rv;

    ArrayList<Catatan> catatanList = new ArrayList<>();

    @OnClick(R.id.tambah) void set(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateCatatan createCatatan = new CreateCatatan(rv, getActivity(), catatanList);
        createCatatan.show(fm, "Catatan");

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

//        return inflater.inflate(R.layout.fragment_catatan_ku, container, false);
        View v =  inflater.inflate(R.layout.fragment_catatan_ku, container, false);
        unbinder = ButterKnife.bind(this, v);
        showRecyclerView();
        getData();
        getActivity().setTitle("CatatanKu");
        return v;
    }

    public void showRecyclerView() {
        rv.setLayoutManager((new LinearLayoutManager(getContext())));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), catatanList, getActivity());
        rv.setAdapter(adapter);
    }

    private void getData() {
        //load data dari shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString("sp_list_catatan", "");
        Type type = new TypeToken<ArrayList<Catatan>>() {}.getType();


        if (json != "") {
            catatanList =  gson.fromJson(json, type);
            if ( catatanList.size() > 0 ) showRecyclerView();
        }
    }




}
