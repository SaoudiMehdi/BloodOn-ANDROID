package com.example.bloodon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.bloodon.com.example.Fragment.FragmentBlood;
import com.example.bloodon.com.example.Fragment.FragmentEdit;
import com.example.bloodon.com.example.Fragment.FragmentStock;
import com.google.android.material.tabs.TabLayout;

public class List extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager =(ViewPager) findViewById(R.id.viewpager_id);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //add fragment
        adapter.addFragment(new FragmentStock(),"");
        adapter.addFragment(new FragmentBlood(),"");
        adapter.addFragment(new FragmentEdit(),"");



        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        tablayout.getTabAt(0).setIcon(R.drawable.ic_stock);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_blood);
        tablayout.getTabAt(2).setIcon(R.drawable.ic_edit);

        //remove shadow from the actionbar

       /*ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);*/



    }
}
