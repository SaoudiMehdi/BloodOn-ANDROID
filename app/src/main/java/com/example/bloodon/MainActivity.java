package com.example.bloodon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import stock.MainActivityStock;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName, PostsLocation;
    private ImageButton AddNewPostButton, allPosts, StockButton;
    private Spinner bloodSpinner;

    private boolean bloodAccess = true;

    private String cityPost;


    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef, LikesRaf, CommentsRef;
    String currentUserID;
    Boolean LikeChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBl));

        mAuth = FirebaseAuth.getInstance();

        LikesRaf = FirebaseDatabase.getInstance().getReference().child("Likes");

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        mToolbar = findViewById(R.id.main_page_toolbar);
        /*setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");*/
        //mToolbar.setTitle("Home");

        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setItemIconTintList(null);

        /*NavProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });*/

        postList = findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

//        RecyclerView recyclerView = findViewById(R.id.all_users_post_list);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
//        recyclerView.setLayoutManager(layoutManager);


        NavProfileImage = navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName = navView.findViewById(R.id.nav_user_full_name);
        PostsLocation = findViewById(R.id.post_location);

        AddNewPostButton = findViewById(R.id.add_new_post_button);
        allPosts = findViewById(R.id.all_posts);
        StockButton = findViewById(R.id.stock_button);

        bloodSpinner = findViewById(R.id.blood_spinner);

        ArrayList<String> bloodType = new ArrayList<String>();
        bloodType.add("sang");
        bloodType.add("A-"); bloodType.add("A+"); bloodType.add("B-"); bloodType.add("B+"); bloodType.add("O-"); bloodType.add("O+");
        bloodType.add("AB-"); bloodType.add("AB+");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(arrayAdapter);


        if (mAuth.getCurrentUser() != null) {
            currentUserID = mAuth.getCurrentUser().getUid();
            UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        if (dataSnapshot.hasChild("fullname")) {
                            String fullname = dataSnapshot.child("fullname").getValue().toString();
                            NavProfileUserName.setText(fullname);
                        }
                        if (dataSnapshot.hasChild("verified")){
                            if (dataSnapshot.child("verified").getValue()!=null) StockButton.setVisibility(View.VISIBLE);
                        }
                        if (dataSnapshot.hasChild("profileimage")) {
                            String image = dataSnapshot.child("profileimage").getValue().toString();
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                        } else {
                            Toast.makeText(MainActivity.this, "profile name do not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        cityPost = "unknown";

        allPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCityPostName();
            }
        });
        StockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToStockActivity();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });


        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPostActivity();
            }
        });

        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DisplayAllUsersPosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                bloodAccess = true;
                DisplayAllUsersPosts();
            }

        });

        DisplayAllUsersPosts();

    }

    private void setCityPostName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                if(PostsLocation.getText().toString().contains("local")) {
                    allPosts.setBackgroundResource(R.drawable.ic_worldwide);
                    cityPost = hereLocation(location.getLatitude(), location.getLongitude());
                    PostsLocation.setText("global");
                }else{
                    allPosts.setBackgroundResource(R.drawable.ic_local);
                    PostsLocation.setText("local");
                    cityPost = "unknown";
                }
                DisplayAllUsersPosts();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        cityPost = hereLocation(location.getLatitude(), location.getLongitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    cityPost = "unknown";
                    Toast.makeText(this, "Permission not granted...", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }
    }

    private void DisplayAllUsersPosts() {
        final String bloodType = bloodSpinner.getSelectedItem().toString();
        Query searchPostsByCity;

        Log.w("CITY",cityPost);
        Log.w("BLOODTYPE",bloodType);
        if (bloodType.compareTo("sang")==0){
            if(cityPost.compareTo("unknown")==0)  searchPostsByCity = PostsRef;
            else searchPostsByCity = PostsRef.orderByChild("city").startAt(cityPost.toLowerCase()).endAt(cityPost.toLowerCase()+"\uf8ff");
        }else {
            if(cityPost.compareTo("unknown")==0)  searchPostsByCity = PostsRef.orderByChild("blood and city").startAt(bloodType.toLowerCase()).endAt(bloodType.toLowerCase()+"\uf8ff");
            else searchPostsByCity = PostsRef.orderByChild("blood and city").startAt(bloodType.toLowerCase()+", "+cityPost.toLowerCase()).endAt(bloodType.toLowerCase()+", "+cityPost.toLowerCase()+"\uf8ff");
        }

        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>
                (Posts.class,
                        R.layout.activity_demandes,
                        PostsViewHolder.class,
                        searchPostsByCity

                ) {
            @Override
            protected void populateViewHolder(PostsViewHolder viewHolder, Posts model, int position) {
                    final String PostKey = getRef(position).getKey();

                    viewHolder.setFullname(model.getFullname());
                    viewHolder.setTime(model.getTime());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setDescription(model.getDescription());
                    viewHolder.setProfileimage(model.getProfileimage());
                    viewHolder.setPostimage(model.getPostimage());

                    viewHolder.setLikeButtonStatus(PostKey);
                    viewHolder.setCommentsStatus(PostKey);

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent clickPostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                            clickPostIntent.putExtra("PostKey", PostKey);
                            startActivity(clickPostIntent);
                        }
                    });

                    viewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent commentsIntent = new Intent(MainActivity.this, CommentsActivity.class);
                            commentsIntent.putExtra("PostKey", PostKey);
                            startActivity(commentsIntent);

                        }
                    });


                    viewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikeChecker = true;
                            LikesRaf.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (LikeChecker.equals(true)) {

                                        if (dataSnapshot.child(PostKey).hasChild(currentUserID)) {
                                            LikesRaf.child(PostKey).child(currentUserID).removeValue();
                                            LikeChecker = false;
                                        } else {
                                            LikesRaf.child(PostKey).child(currentUserID).setValue(true);
                                            LikeChecker = false;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }






    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ImageButton LikePostButton,CommentPostButton;
        TextView DisplayNoOfLikes, DisplayNoOfComments;
        int countLikes, countcomments;
        String currentUserId;
        DatabaseReference LikesRaf, CommentsRef;

        public PostsViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            LikePostButton = mView.findViewById(R.id.like_button);
            CommentPostButton = mView.findViewById(R.id.comment_button);
            DisplayNoOfLikes= mView.findViewById(R.id.display_no_of_likes);
            DisplayNoOfComments= mView.findViewById(R.id.display_no_of_comments);

            LikesRaf = FirebaseDatabase.getInstance().getReference().child("Likes");
            CommentsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }



        public void setCommentsStatus(final String PostKey) {
            CommentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(PostKey).hasChild("Comments")){
                        DataSnapshot dataSnapshot2 = dataSnapshot.child(PostKey);
                        countcomments= (int) dataSnapshot2.child("Comments").getChildrenCount();
                        DisplayNoOfComments.setText(Integer.toString(countcomments)+(" Comments"));
                    }
                    else{
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        public void setLikeButtonStatus(final String PostKey)
        {
            LikesRaf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId)){
                        countLikes= (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.ic_like_24dp);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Likes"));
                    }
                    else{
                        countLikes= (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.ic_deslike_24dp);
                        DisplayNoOfLikes.setText(  Integer.toString(countLikes)+(" Likes"));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




        public void setFullname(String fullname) {
            TextView username = mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }

        public void setProfileimage(String profileimage) {
            CircleImageView image = mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileimage).into(image);
        }

        public void setTime(String time) {
            TextView PostTime = mView.findViewById(R.id.post_time);
            PostTime.setText("" + time);
        }

        public void setDate(String date) {
            TextView PostDate = mView.findViewById(R.id.post_date);
            PostDate.setText("" + date);
        }

        public void setDescription(String description) {
            TextView PostDescription = mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setPostimage(String postimage) {
            ImageView PostImage = mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(PostImage);
        }
    }




    private void SendUserToPostActivity() {
        Intent addPostIntent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(addPostIntent);

    }


    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser==null){
            SendUserTologinActivity();
        }
        else {
            CheckUserExistence();
        }
    }

    private void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id)){
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToSetupActivity() {
        Bundle extras = getIntent().getExtras();
        String email;
        String password;
        String verified;
        if(extras == null) {
            email = mAuth.getCurrentUser().getEmail();
            password = null;
            verified = null;
        } else {
            email = extras.getString("email");
            password = extras.getString("password");
            verified = "true";
        }
        Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setupIntent.putExtra("activitySource","MainActivity");
        setupIntent.putExtra("email",email);
        setupIntent.putExtra("password",password);
        setupIntent.putExtra("verified",verified);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserTologinActivity() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_post:
                SendUserToPostActivity();
                break;

            case R.id.nav_profile:
                SendUserToProfileActivity();

                break;
            case R.id.nav_home:
                SendUserToMainActivity();
                break;
            case R.id.nav_hospitals:
                SendUserToFindHospital();
                break;
            case R.id.nav_find_donnators:
                SendUserToFindDonnatorsActivity();
                break;
            case R.id.nav_logout:

                mAuth.signOut();
                SendUserTologinActivity();
                break;

        }
    }

    private void SendUserToStockActivity() {
        Intent stockIntent = new Intent(MainActivity.this, MainActivityStock.class);
        startActivity(stockIntent);
    }

    private String hereLocation(double lat, double lon){
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(lat,lon,10);
            if (addresses.size()>0){
                for (Address adr: addresses){
                    if (adr.getLocality() != null && adr.getLocality().length()>0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void SendUserToFindDonnatorsActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, FindDonnatorsActivity.class);
        startActivity(loginIntent);

    }

    private void SendUserToProfileActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToFindHospital()
    {
        Intent loginIntent = new Intent(MainActivity.this, MyList.class);
        startActivity(loginIntent);
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

}

