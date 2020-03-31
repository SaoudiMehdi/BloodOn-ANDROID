package com.example.mybloodon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userProfName, userProfCin, userBonusValue;
    private EditText userProfEmail, userProfAdresse, userProfTelephone;
    private CircleImageView userProfileImage;

    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBl));

        mAuth = FirebaseAuth.getInstance();
        currentUserId =mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);


        userProfName= findViewById(R.id.profileName);
        userProfEmail= findViewById(R.id.profileEmail);
        userProfTelephone = findViewById(R.id.profilePhone);
        userProfCin= findViewById(R.id.profileCIN);
        userProfAdresse= findViewById(R.id.profileAdresse);
        userBonusValue= findViewById(R.id.bonusValue);
        userProfileImage = findViewById(R.id.profileImage);
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    //String myProfileEmail = dataSnapshot.child("email").getValue().toString();
                    String myProfileTelephone = dataSnapshot.child("telephone").getValue().toString();
                    String myProfileAdresse = dataSnapshot.child("adresse").getValue().toString();
                    String bonusValue = dataSnapshot.child("point").getValue().toString();
                    String cin = dataSnapshot.child("cin").getValue().toString();

                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);

                    userProfName.setText(myProfileName);
                    userProfTelephone.setText(myProfileTelephone);
                    userProfAdresse.setText(myProfileAdresse);
                    userProfCin.setText(cin);
                    userBonusValue.setText(bonusValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

