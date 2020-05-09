package com.example.bloodon;

import android.app.ProgressDialog;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SetupActivity extends AppCompatActivity {


    private EditText fullname, Telephone, CIN, Adresse, Ville, Categorie_sang, emailEditText;

    private CircleImageView ProfileImage;
    private Button SaveInfomartionbutton;
    private Spinner bloodSpinner;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private StorageReference UserProfileImageRef;

    private DatabaseReference profileUserRef;

    private String currentUserId;

    private ProgressDialog loadingBar;

    private String email, password, verifiedOrganisation,activitySource;

    String currentUsersID;
    final  static  int GalleryPick=1;

    boolean imageExist = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.orangeLight));

        loadingBar= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUsersID= mAuth.getCurrentUser().getUid();

        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUsersID);


        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Image");

        fullname = findViewById(R.id.setup_full_name);
        Telephone = findViewById(R.id.setup_telephone);
        Adresse = findViewById(R.id.setup_adresse);
        Ville = findViewById(R.id.setup_ville);
        CIN = findViewById(R.id.setup_cin);
        ProfileImage =findViewById(R.id.setup_profile_image);
        emailEditText = findViewById(R.id.setup_email);
        bloodSpinner = findViewById(R.id.setup_sang);

        ArrayList<String> bloodType = new ArrayList<String>();
        bloodType.add("Catégorie sang");
        bloodType.add("A-"); bloodType.add("A+"); bloodType.add("B-"); bloodType.add("B+"); bloodType.add("O-"); bloodType.add("O+");
        bloodType.add("AB-"); bloodType.add("AB+");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(arrayAdapter);

        activitySource = "";

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            email = null;
            password = null;
            verifiedOrganisation = null;
        } else {
            activitySource = extras.getString("activitySource");
            if(activitySource.compareToIgnoreCase("Activity Register") == 0 || activitySource.compareToIgnoreCase("MainActivity") == 0 ) {
                email = extras.getString("email");
                password = extras.getString("password");
                verifiedOrganisation = extras.getString("verified");
                emailEditText.setEnabled(false);
            }
            else if(activitySource.compareToIgnoreCase("Activity Profile") == 0) {
                mAuth = FirebaseAuth.getInstance();
                currentUserId =mAuth.getCurrentUser().getUid();
                profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null) {
                    emailEditText.setText(user.getEmail());
                }

                profileUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                            String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                            String myProfileTelephone = dataSnapshot.child("telephone").getValue().toString();
                            String myProfileAdresse = dataSnapshot.child("adresse").getValue().toString();
                            String cin = dataSnapshot.child("cin").getValue().toString();
                            String ville = dataSnapshot.child("ville").getValue().toString();
                            String categorieSang = dataSnapshot.child("categorie du sang").getValue().toString();
                            if(dataSnapshot.child("verified").getValue() != null){
                                findViewById(R.id.cin_container).setVisibility(View.GONE);
                                bloodSpinner.setVisibility(View.GONE);
                                verifiedOrganisation = "true";
                            }

                            Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(ProfileImage);

                            fullname.setText(myProfileName);
                            Telephone.setText(myProfileTelephone);
                            CIN.setText(cin);
                            Ville.setText(ville);
                            if(categorieSang != null) {
                                int spinnerPosition = arrayAdapter.getPosition(categorieSang);
                                bloodSpinner.setSelection(spinnerPosition);
                            }
                            Adresse.setText(myProfileAdresse);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                emailEditText.setEnabled(true);

            }
        }

        if (verifiedOrganisation!=null){
            findViewById(R.id.cin_container).setVisibility(View.GONE);
            bloodSpinner.setVisibility(View.GONE);
        }



        if(activitySource.compareToIgnoreCase("Activity Register") == 0 || activitySource.compareToIgnoreCase("MainActivity")==0){
            emailEditText.setText(email);
        }
        
        SaveInfomartionbutton = findViewById(R.id.setup_information_button);

        SaveInfomartionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);

            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile)
                                .into(ProfileImage);
                        imageExist = true;
                    }
                    else{
                        Toast.makeText(SetupActivity.this, "Please select profile image first..", Toast.LENGTH_LONG).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null){
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){

                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait,While we updating your profile image...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                Uri resultUri=result.getUri();
                StorageReference filePath = UserProfileImageRef.child(currentUsersID + ".jpg");
                StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SetupActivity.this, "profile Image stored succesfully to firebase storage", Toast.LENGTH_LONG).show();
                            StorageReference filePath = UserProfileImageRef.child(currentUsersID + ".jpg");
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl=uri.toString();
                                    //  Toast.makeText(SetupActivity.this, downloadUrl, Toast.LENGTH_LONG).show();
                                    UserRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        /*Intent selfIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                                        startActivity(selfIntent);*/
                                                        Toast.makeText(SetupActivity.this, "profile image stored to Firebase succesfully", Toast.LENGTH_LONG).show();

                                                    }
                                                    else {
                                                        String message =task.getException().getMessage();
                                                        Toast.makeText(SetupActivity.this, "Error Ocurred" + message, Toast.LENGTH_LONG).show();
                                                        loadingBar.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    }
                });

            }
            else {
                Toast.makeText(this, "Error ocurred Image can't be cropped try again", Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        }
    }

    private void SaveAccountSetupInformation() {
        String nom = fullname.getText().toString();
        String telephone = Telephone.getText().toString();
        String cin = CIN.getText().toString();
        String ville = Ville.getText().toString();
        String adresse = Adresse.getText().toString();
        String _email = emailEditText.getText().toString();
        String blood = "Catégorie sang";
        if (bloodSpinner.getVisibility()!=View.GONE) blood = bloodSpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(nom)){
            Toast.makeText(this, "Please write your username", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(telephone)){
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(cin) && verifiedOrganisation==null){
            Toast.makeText(this, "Please write your cin", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(ville)){
            Toast.makeText(this, "Please write your ville", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(adresse)){
            Toast.makeText(this, "Please write your adresse", Toast.LENGTH_LONG).show();
        }
        else if(blood.compareTo("Catégorie sang") == 0 && verifiedOrganisation==null){
            Toast.makeText(this, "Please choose your blood type", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(_email)){
            Toast.makeText(this, "Please write your  email", Toast.LENGTH_LONG).show();
        }
        else if(imageExist == false) {
            Toast.makeText(SetupActivity.this, "Please select profile image first..", Toast.LENGTH_LONG).show();
        }
        else{
            /*loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait,While we are creating your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);*/
            String point = "0";
            if (verifiedOrganisation!=null){
                cin = "-";
                blood = "-";
                point = "-";
            }

            AddChild("fullname",nom.toLowerCase());
            AddChild("telephone",telephone);
            AddChild("ville",ville);
            AddChild("adresse",adresse);
            AddChild("cin",cin);
            AddChild("point",point);
            AddChild("verified",verifiedOrganisation);
            UserRef.child("categorie du sang").setValue(blood).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "your account is created succesfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else {
                        String message= task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error occured"+ message, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
            });

        }


    }
    private void AddChild(String key, Object value){
        UserRef.child(key).setValue(value).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                }
                else {
                    String message= task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error occured"+ message, Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent= new Intent(SetupActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


}
