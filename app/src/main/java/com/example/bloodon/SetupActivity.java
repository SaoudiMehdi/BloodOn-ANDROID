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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SetupActivity extends AppCompatActivity {


    private EditText fullname,Prenom,Telephone,CIN,Adresse,Ville,Categorie_sang;

    private CircleImageView ProfileImage;
    private Button SaveInfomartionbutton;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private StorageReference UserProfileImageRef;

    private ProgressDialog loadingBar;

    private String email, password;

    String currentUsersID;
    final  static  int GalleryPick=1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            email= null;
            password = null;
        } else {
            email= extras.getString("email").toString();
            password= extras.getString("password").toString();
        }

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBl));

        loadingBar= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUsersID= mAuth.getCurrentUser().getUid();

        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUsersID);


        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Image");

        fullname = findViewById(R.id.setup_full_name);
        //Prenom = findViewById(R.id.prenom);
        Telephone = findViewById(R.id.setup_telephone);
        Adresse = findViewById(R.id.setup_adresse);
        Ville = findViewById(R.id.setup_ville);
        CIN = findViewById(R.id.setup_cin);
        Categorie_sang = findViewById(R.id.setup_categorie_sang);
        ProfileImage =findViewById(R.id.setup_profile_image);

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
                    }
                    else{
                        Toast.makeText(SetupActivity.this, "Please select profile image first..", Toast.LENGTH_SHORT).show();
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
        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
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
                            Toast.makeText(SetupActivity.this, "profile Image stored succesfully to firebase storage", Toast.LENGTH_SHORT).show();
                            StorageReference filePath = UserProfileImageRef.child(currentUsersID + ".jpg");
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl=uri.toString();
                                    //  Toast.makeText(SetupActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
                                    UserRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        Intent selfIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                                        startActivity(selfIntent);
                                                        Toast.makeText(SetupActivity.this, "profile image stored to Firebase succesfully", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                    else {
                                                        String message =task.getException().getMessage();
                                                        Toast.makeText(SetupActivity.this, "Error Ocurred" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Error ocurred Image can't be cropped try again", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void SaveAccountSetupInformation() {
        String nom= fullname.getText().toString();
        //String prenom= Prenom.getText().toString();
        String telephone= Telephone.getText().toString();
        String cin = CIN.getText().toString();
        String ville = Ville.getText().toString();
        String categorie_sang = Categorie_sang.getText().toString();
        String adresse = Adresse.getText().toString();


        if (TextUtils.isEmpty(nom)){
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        }
        /*if (TextUtils.isEmpty(prenom)){
            Toast.makeText(this, "Please write your fullname", Toast.LENGTH_SHORT).show();
        }*/
        if (TextUtils.isEmpty(telephone)){
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(cin)){
            Toast.makeText(this, "Please write your cin", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ville)){
            Toast.makeText(this, "Please write your ville", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(adresse)){
            Toast.makeText(this, "Please write your adresse", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(categorie_sang)){
            Toast.makeText(this, "Please write your categorie du sang", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait,While we are creating your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap=new HashMap();
            /*userMap.put("email",email);
            userMap.put("password",password);
            userMap.put("fullname",nom);
            //userMap.put("prenom",prenom);
            userMap.put("telephone",telephone);
            userMap.put("ville",ville);
            userMap.put("adresse",adresse);
            userMap.put("cin",cin);
            userMap.put("point",0);
            userMap.put("categorie du sang",categorie_sang);

            userMap.put("status","hey there, i am using Poster social Network");
            userMap.put("gender","none");
            userMap.put("dob","none");
            userMap.put("relationshipstatus","none");*/
            //Log.w("emaaaaaaaaaaaaail: ",email);
            AddChild("fullname",nom);
            AddChild("email",email);
            AddChild("password",password);
            AddChild("telephone",telephone);
            AddChild("ville",ville);
            AddChild("adresse",adresse);
            AddChild("cin",cin);
            AddChild("point",0);
            UserRef.child("categorie du sang").setValue(categorie_sang).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "your account is created succesfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else {
                        String message= task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error occured"+ message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SetupActivity.this, "Error occured"+ message, Toast.LENGTH_SHORT).show();
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
