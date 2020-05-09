package com.example.bloodon;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox hospitalAccount;
    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail = findViewById(R.id.register_email);
        UserPassword = findViewById(R.id.register_password);
        UserConfrmPassword = findViewById(R.id.register_confrm_password);
        CreateAccountButton = findViewById(R.id.register_create_account);
        hospitalAccount = findViewById(R.id.hospital_account);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orangeLight));
    }

    private void CreateNewAccount() {

        final String email = UserEmail.getText().toString();
        final String password = UserPassword.getText().toString();
        final String confrmpassword = UserConfrmPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "please write your email..", Toast.LENGTH_LONG).show();
        }
        else  if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "please write your password..", Toast.LENGTH_LONG).show();
        }
        else  if (TextUtils.isEmpty(confrmpassword)){
            Toast.makeText(this, "please confrm your password..", Toast.LENGTH_LONG).show();
        }
        else if (!password.equals(confrmpassword)){
            Toast.makeText(this, "Your Password do not match", Toast.LENGTH_LONG).show();
        }else if(hospitalAccount.isChecked()){
            sendEmailForHospitalVerification(email,password);
            Toast.makeText(this, "We have contacted you by your email to VERIFY you identity before activating your  hospital account! THANK YOU", Toast.LENGTH_LONG).show();
            SendUserToLoginActivity();
        }
        else{

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmail(email);
                                SendUserToSetupActivity(email, password);
                                Toast.makeText(RegisterActivity.this, "you are authenticated succefuly....", Toast.LENGTH_LONG).show();
                            }
                            else{
                                String message= task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occured" + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    private void SendUserToSetupActivity(String email, String password) {
        Intent setupIntent = new Intent(RegisterActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.w("email email am:",email);
        setupIntent.putExtra("email", email);
        setupIntent.putExtra("password", password);
        setupIntent.putExtra("activitySource", "Activity Register");
        startActivity(setupIntent);
        finish();
    }

    private void sendEmail(final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String body =  "Hello,\n you have registered with this email : " + email + " to our application Bloodon.\n Thank you.";
                    MailSender sender = new MailSender("bloodonapp@gmail.com", "bloodon 2020");
                    sender.sendMail("New user", body, "bloodonapp@gmail.com", email);

                    final String body1 =  "Hello,\n a new user has registered with this email : " + email + " to our application Bloodon.\n Thank you.";
                    sender.sendMail("New user", body1, "bloodonapp@gmail.com", "bloodonapp@gmail.com");
                }catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
    }


    private void sendEmailForHospitalVerification(final String email, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String body =  "Hello,\n you have registered with this email : " + email + " to our application Bloodon.\n To activate your account please send us a copy of your identity or equivalent to verify your responsability of an hospital.\n After verification your account will be activated.\nThank you.";
                    MailSender sender = new MailSender("bloodonapp@gmail.com", "bloodon 2020");
                    sender.sendMail("New user", body, "bloodonapp@gmail.com", email);

                    final String body1 =  "Hello,\n a new Hospital has registered with this email : " + email +" and password : "+password+ " to our application Bloodon and he need verification.\n Thank you to respond as fast as you can to our client.";
                    sender.sendMail("New user", body1, "bloodonapp@gmail.com", "bloodonapp@gmail.com");
                }catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
    }

    private void SendUserToLoginActivity() {
        Intent mainIntent= new Intent(RegisterActivity.this,LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}

