package com.example.bloodon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyList extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        /*getSupportActionBar().setTitle("Hopitaux"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        // get the reference of RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        // set a LinearLayoutManager with default vertical orientation
        rv.setLayoutManager(new LinearLayoutManager(this));
        // call the constructor of MyAdapter to send the reference and data to Adapter
        rv.setAdapter(new MyAdapter(this,findHospital.getHospitals()));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void maps(View view) {
        Intent intent =new Intent(this,MapsActivity.class) ;
        this.startActivity(intent);
        Log.i("map","tttttttttt") ;
    }
}