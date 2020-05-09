package com.example.bloodon;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
//    private ImageView googleSignInButton;

    private TextInputEditText UserEmail,UserPassword;

    private Button NeedNewAccountLink;

    private TextView ForgetPasswordLink;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, StocksRef;
    private ProgressDialog loadingBar;

    private static final  String TAG ="LoginActivity";

//    private static final int RC_SIGN_IN = 1;
//    private GoogleApiClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.orangeLight));

        NeedNewAccountLink= findViewById(R.id.register_account_link);
        UserEmail = findViewById(R.id.login_email);
        UserPassword = findViewById(R.id.login_password);
        LoginButton = findViewById(R.id.login_button);
        ForgetPasswordLink = findViewById(R.id.forget_password_link);

        mAuth = FirebaseAuth.getInstance();

        loadingBar= new ProgressDialog(this);
//        googleSignInButton = findViewById(R.id.google_signin_button);

        getLocationPermission();

    NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SendUserToRegisterActivity();
        }
    });
    LoginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //SendUserToMainActivity();
            AllowingUserToLogin();
        }
    });

    ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
         startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
        }
    });


    }


    private void getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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

                }else {
                    Toast.makeText(this, "Permission not granted...", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser!=null){
            SendUserToMainActivity();
        }
    }

    private void AllowingUserToLogin() {
        final String email=UserEmail.getText().toString();
        final String password = UserPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login ");
            loadingBar.setMessage("Please wait,While we are allowing you to into your account...");
            loadingBar.setCanceledOnTouchOutside(true);
         //   loadingBar.show();


            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                AddNewStock();
                                System.out.println("debug()enter task.issucceeful");

                                SendUserToMainActivity_2(email,password);
                                System.out.println("debug()main activity called");
                                Toast.makeText(LoginActivity.this, "you are Logged in successfully", Toast.LENGTH_SHORT).show();
                                System.out.println("debug()before dissimissing dialog");
           //                     loadingBar.dismiss();
                                System.out.println("debug()after dissimissing dialog");

                            }
                            else {
                                String message= task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error ocurred"+ message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }

    private void SendUserToMainActivity() {
        Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
       mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void AddNewStock() {
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StocksRef = FirebaseDatabase.getInstance().getReference().child("Stocks");

        final String current_user_id = mAuth.getCurrentUser().getUid();
        StocksRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            String a_plus="0";
            String b_plus="0";
            String a_moins="0";
            String b_moins="0";
            String ab_plus="0";
            String ab_moins="0";
            String o_plus="0";
            String o_moins="0";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
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
                }

                HashMap stocksMap = new HashMap();
                    stocksMap.put("uid",current_user_id);
                    stocksMap.put("a_plus",a_plus);
                    stocksMap.put("b_plus",b_plus);
                    stocksMap.put("a_moins",a_moins);
                    stocksMap.put("b_moins",b_moins);
                    stocksMap.put("ab_plus",ab_plus);
                    stocksMap.put("ab_moins",ab_moins);
                    stocksMap.put("o_plus",o_plus);
                    stocksMap.put("o_moins",o_moins);
                    StocksRef.child(current_user_id).updateChildren(stocksMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "GREAT!", Toast.LENGTH_LONG).show();

                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Error Ocurred While creating your stock please contact us!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity_2(String email, String password) {
        Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.putExtra("email",email);
        mainIntent.putExtra("password",password);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent mainIntent= new Intent(LoginActivity.this,LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }
}
