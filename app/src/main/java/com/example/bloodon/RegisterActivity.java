package com.example.bloodon;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText UserEmail,UserPassword,UserConfrmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail=findViewById(R.id.register_email);
        UserPassword=findViewById(R.id.register_password);
        UserConfrmPassword=findViewById(R.id.register_confrm_password);
        CreateAccountButton = findViewById(R.id.register_create_account);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBl));
    }

    private void CreateNewAccount() {

        String email= UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        String confrmpassword = UserConfrmPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "please write your email..", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "please write your password..", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(confrmpassword)){
            Toast.makeText(this, "please confrm your password..", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confrmpassword)){
            Toast.makeText(this, "Your Password do not match", Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendUserToSetupActivity();

                                Toast.makeText(RegisterActivity.this, "you are authenticated succefuly....", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String message= task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occured" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}

