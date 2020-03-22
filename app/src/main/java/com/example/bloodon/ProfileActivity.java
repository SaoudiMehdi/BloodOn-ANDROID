package com.example.bloodon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Spinner spinnerBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        spinnerBlood = (Spinner) this.findViewById(R.id.spinnerBlood);
        ArrayList bloodList = new ArrayList();
        String[] bloodCategories = {"A-", "A+", "B-", "B+", "AB-", "AB+", "O-", "O+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, bloodCategories);
       spinnerBlood.setAdapter(adapter);
    }
}
