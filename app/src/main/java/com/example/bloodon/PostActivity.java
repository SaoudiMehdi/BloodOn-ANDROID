package com.example.bloodon;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;
    private Spinner bloodSpinner;

    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String Description;

    private String cityPost;

    private StorageReference PostImagesRefrence;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBl));

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostImagesRefrence = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        SelectPostImage = findViewById(R.id.select_post_image);
        UpdatePostButton = findViewById(R.id.update_post_button);
        PostDescription = findViewById(R.id.post_description);
        loadingBar = new ProgressDialog(this);

        mToolbar = findViewById(R.id.update_post_page_toolbar);
        /*setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");*/
        mToolbar.setTitle("Update Post");

        bloodSpinner = findViewById(R.id.sang_cherche);

        ArrayList<String> bloodType = new ArrayList<String>();
        bloodType.add("sang");
        bloodType.add("A-"); bloodType.add("A+"); bloodType.add("B-"); bloodType.add("B+"); bloodType.add("O-"); bloodType.add("O+");
        bloodType.add("AB-"); bloodType.add("AB+");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(arrayAdapter);

        setCityPostName();

        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

    }

        private void setCityPostName() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                try {
                    cityPost = hereLocation(location.getLatitude(), location.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();

                }

            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                            cityPost = hereLocation(location.getLatitude(),location.getLongitude());
                        }catch (Exception e){
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

    private void ValidatePostInfo() {
         Description = PostDescription.getText().toString();
        if (ImageUri == null){
            Toast.makeText(this, "Please Select post image...", Toast.LENGTH_SHORT).show();
        }
       else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please say something about your image...", Toast.LENGTH_SHORT).show();
        }else if(bloodSpinner.getSelectedItem().toString().compareTo("Catégorie sang cherche") == 0){
            Toast.makeText(this, "Please choose a blood type...", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait,While we updating your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();
        }

    }

    private void StoringImageToFirebaseStorage() {

        Calendar calFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currenTime.format(calFordate.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = PostImagesRefrence.child("Post Image").child(ImageUri.getLastPathSegment() + postRandomName +  ".jpg");
        StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
              if (task.isSuccessful()){

                  Toast.makeText(PostActivity.this, "Image uploaded succesfully to storage...", Toast.LENGTH_SHORT).show();
                  StorageReference filePath = PostImagesRefrence.child("Post Image").child(ImageUri.getLastPathSegment() +postRandomName + ".jpg");
                  filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          String downlooadPicUrl = uri.toString();

                          SavingPostInformationToDatabase(downlooadPicUrl);

                      }
                  });



              }
              else {
                  String message= task.getException().getMessage();
                  Toast.makeText(PostActivity.this, "Error ocurred: " + message, Toast.LENGTH_SHORT).show();
              }
            }
        });


    }

    private void SavingPostInformationToDatabase(final String download) {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()){

                  String userFullName = "";
                  if(dataSnapshot.child("fullname").getValue() != null) {
                      userFullName = dataSnapshot.child("fullname").getValue().toString();
                  }
                  String blood = bloodSpinner.getSelectedItem().toString();
                  String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                  HashMap postsMap = new HashMap();
                  postsMap.put("uid",current_user_id);
                  postsMap.put("date",saveCurrentDate);
                  postsMap.put("time",saveCurrentTime);
                  postsMap.put("description",Description);
                  postsMap.put("postimage",download);
                  postsMap.put("profileimage",userProfileImage);
                  postsMap.put("fullname",userFullName);
                  postsMap.put("city",cityPost.toLowerCase());
                  postsMap.put("blood and city",blood.toLowerCase()+", "+cityPost.toLowerCase());
                  PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                          .addOnCompleteListener(new OnCompleteListener() {
                              @Override
                              public void onComplete(@NonNull Task task) {
                                  if (task.isSuccessful()){
                                      SendUserToMainActivity();

                                      Toast.makeText(PostActivity.this, "New Post is updated successfully..", Toast.LENGTH_SHORT).show();
                                      loadingBar.dismiss();
                                  }
                                  else {
                                      Toast.makeText(PostActivity.this, "Error Ocurred While updating your profile", Toast.LENGTH_SHORT).show();
                                      loadingBar.dismiss();
                                  }
                              }
                          });
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(mainIntent);
    }
}
