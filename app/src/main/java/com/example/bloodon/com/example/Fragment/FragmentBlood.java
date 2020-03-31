package com.example.bloodon.com.example.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodon.MyDividerItemDecoration;
import com.example.bloodon.R;
import com.example.bloodon.Stock;

import java.util.ArrayList;
import java.util.List;

public class FragmentBlood extends Fragment {

    View view;
    private RecyclerView  rvb;
    List<Stock> lstBlood;


    public FragmentBlood() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blood_fragment, container, false);
        rvb = view.findViewById(R.id.blood_recyclerview);
        MyAdapterBlood rvAdapter = new MyAdapterBlood(getContext(),lstBlood);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rvb.setLayoutManager(layoutManager);
        rvb.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        rvb.setAdapter(rvAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstBlood = new ArrayList<>();
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type A+ / Litre","16",R.drawable.aplus));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type A- / Litre","26",R.drawable.amoins));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type B+ / Litre","36",R.drawable.bplus));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type B- / Litre","12",R.drawable.bmoins));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type AB+ / Litre","19",R.drawable.abplus));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type AB- / Litre","25",R.drawable.abmoins));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type O+ / Litre","31",R.drawable.omoins));
        lstBlood.add(new Stock("CHU Hassan II Fes","Le type O- / Litre","17",R.drawable.oplus));

    }
}
