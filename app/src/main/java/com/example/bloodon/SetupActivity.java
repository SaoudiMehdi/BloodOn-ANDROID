package com.example.bloodon;

import android.app.ProgressDialog;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SetupActivity extends AppCompatActivity {


    private EditText Nom,Prenom,Telephone,CIN,Adresse,Ville,Categorie_sang;
    private Button SaveInfomartionbutton;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    private ProgressDialog loadingBar;

    String currentUsersID;
    final  static  int GalleryPick=1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBl));

        loadingBar= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUsersID= mAuth.getCurrentUser().getUid();

        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUsersID);

        Nom = findViewById(R.id.nom);
        Prenom = findViewById(R.id.prenom);
        Telephone = findViewById(R.id.telephone);
        Adresse = findViewById(R.id.adresse);
        Ville = findViewById(R.id.ville);
        CIN = findViewById(R.id.cin);
        Categorie_sang = findViewById(R.id.categoriesang);

        SaveInfomartionbutton = findViewById(R.id.register_create_account);

        SaveInfomartionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });


    }

    private void SaveAccountSetupInformation() {
        String nom= Nom.getText().toString();
        String prenom= Prenom.getText().toString();
        String telephone= Telephone.getText().toString();
        String cin = CIN.getText().toString();
        String ville = Ville.getText().toString();
        String categorie_sang = Categorie_sang.getText().toString();
        String adresse = Adresse.getText().toString();


        if (TextUtils.isEmpty(nom)){
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(prenom)){
            Toast.makeText(this, "Please write your fullname", Toast.LENGTH_SHORT).show();
        }
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
            userMap.put("nom",nom);
            userMap.put("prenom",prenom);
            userMap.put("telephone",telephone);
            userMap.put("ville",ville);
            userMap.put("adresse",adresse);
            userMap.put("cin",cin);
            userMap.put("categorie du sang",categorie_sang);

            userMap.put("status","hey there, i am using Poster social Network");
            userMap.put("gender","none");
            userMap.put("dob","none");
            userMap.put("relationshipstatus","none");
            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
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



}
