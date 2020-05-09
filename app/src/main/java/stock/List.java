package stock;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.bloodon.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class List extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private FirebaseAuth mAuth;
    private String current_user_id;
    private DatabaseReference UsersRef, StocksRef, StocksChildRef, bloodNeeded;

    public  String a_plus;
    public String b_plus;
    public String a_moins;
    public String b_moins;
    public String ab_plus;
    public String ab_moins;
    public String o_plus;
    public String o_moins;
    public String fullname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager =(ViewPager) findViewById(R.id.viewpager_id);

        mAuth = FirebaseAuth.getInstance();


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StocksRef = FirebaseDatabase.getInstance().getReference().child("Stocks");

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            //add fragment
            //adapter.addFragment(new FragmentStock(), "");
            adapter.addFragment(new FragmentBlood(bundle.getString("a_plus"),bundle.getString("b_plus"),bundle.getString("a_moins"),bundle.getString("b_moins"),bundle.getString("ab_plus"),bundle.getString("ab_moins"),bundle.getString("o_plus"),bundle.getString("o_moins"),"test"), "");
            //adapter.addFragment(new FragmentEdit(), "");

            viewPager.setAdapter(adapter);
            tablayout.setupWithViewPager(viewPager);

            //tablayout.getTabAt(0).setIcon(R.drawable.ic_stock);
            tablayout.getTabAt(0).setIcon(R.drawable.ic_blood);
            //tablayout.getTabAt(1).setIcon(R.drawable.ic_edit);
        }else {


            //remove shadow from the actionbar

       /*ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);*/


            if (mAuth.getCurrentUser() != null) {
                current_user_id = mAuth.getCurrentUser().getUid();
                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.hasChild("fullname")) {
                                fullname = dataSnapshot.child("fullname").getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                StocksRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.hasChild("a_plus")) {
                                a_plus = dataSnapshot.child("a_plus").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("b_plus")) {
                                b_plus = dataSnapshot.child("b_plus").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("a_moins")) {
                                a_moins = dataSnapshot.child("a_moins").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("b_moins")) {
                                b_moins = dataSnapshot.child("b_moins").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("ab_plus")) {
                                ab_plus = dataSnapshot.child("ab_plus").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("ab_moins")) {
                                ab_moins = dataSnapshot.child("ab_moins").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("o_plus")) {
                                o_plus = dataSnapshot.child("o_plus").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("o_moins")) {
                                o_moins = dataSnapshot.child("o_moins").getValue().toString();
                            }
                            Intent selfIntent;
                            selfIntent = new Intent(List.this, List.class);
                            selfIntent.putExtra("a_plus", a_plus);
                            selfIntent.putExtra("b_plus", b_plus);
                            selfIntent.putExtra("a_moins", a_moins);
                            selfIntent.putExtra("b_moins", b_moins);
                            selfIntent.putExtra("ab_plus", ab_plus);
                            selfIntent.putExtra("ab_moins", ab_moins);
                            selfIntent.putExtra("o_plus", o_plus);
                            selfIntent.putExtra("o_moins", o_moins);
                            startActivity(selfIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }



    }
}
