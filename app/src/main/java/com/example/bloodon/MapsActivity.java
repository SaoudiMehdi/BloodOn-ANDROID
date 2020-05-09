package com.example.bloodon;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double longitude;
    double latitude;
    String nom;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.standard:
                Toast.makeText(this, "Standard", Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.Hybrid:
                Toast.makeText(this, "Hybrid", Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.Satellite:
                Toast.makeText(this, "Satellite", Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<findHospital> fh = findHospital.getHospitals();
        Bundle bundle=this.getIntent().getExtras();
       /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/      longitude=bundle.getDouble("Longitude");
        latitude=bundle.getDouble("Latitude");
        label=bundle.getString("Label");
        nom=bundle.getString("Name");
        //findHospital et = fh.get(i);
        LatLng position = new LatLng(latitude,longitude);
        mMap.addMarker(new
                MarkerOptions().position(position).title(label).snippet(nom));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));

    }


}
