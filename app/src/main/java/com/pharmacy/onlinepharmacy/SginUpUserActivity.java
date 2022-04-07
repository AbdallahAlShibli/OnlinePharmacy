package com.pharmacy.onlinepharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SginUpUserActivity extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();

    private Button registerButton;
    private EditText name, age, phone, email, password;
    private RelativeLayout progressBar;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgin_up_user);

        registerButton = findViewById(R.id.register);
        email = findViewById(R.id.userEmail);
        name = findViewById(R.id.userName);
        age = findViewById(R.id.userAge);
        phone = findViewById(R.id.userPhone);
        password = findViewById(R.id.userPassword);

        progressBar = findViewById(R.id.progressBarLayout);
        progressBar.setVisibility(View.GONE);

        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            }
        });


        register();

    }


    private void register() {


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                final String Email = email.getText().toString().trim();
                final String Name = name.getText().toString();
                final String Age = age.getText().toString();
                final String Phone = phone.getText().toString();
                if (checkBox(Phone) && checkBox(Age) && checkBox(Name) && checkEmail(Email) && checkPassword()) {
                    String Password = password.getText().toString();
                    FA.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SginUpUserActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Register is successfully!", Toast.LENGTH_SHORT).show();

                                CollectionReference userData = Ff.collection("users");
                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("name", Name);
                                data1.put("age", Age);
                                data1.put("phone", Phone);
                                data1.put("email", Email);
                                data1.put("medical1", "");
                                data1.put("location", "");


                                try {
                                    Thread.sleep(2000);

                                    userData.document(FA.getUid()).set(data1);

                                    progressBar.setVisibility(View.GONE);

                                    startActivity(new Intent(getApplication(), MainActivity.class));
                                    finish();

                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Error!, Check the Password or Email!", Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });

                }

            }
        });


    }

    private boolean checkBox(String character) {

        boolean check = false;

        if (!character.isEmpty()) return !TextUtils.isEmpty(character);


        return check;
    }

    private boolean checkPassword() {
        String FPassword = password.getText().toString();
        if (!FPassword.isEmpty()) {
            if (FPassword.length() >= 6 && FPassword.length() <= 16) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Please check your password!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return false;
            }
        } else {

            Toast.makeText(getApplicationContext(), "Please check your password!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;

        }
    }

    private boolean checkEmail(String character) {

        boolean check = false;

        if (!character.isEmpty()) {

            return !TextUtils.isEmpty(character) && Patterns.EMAIL_ADDRESS.matcher(character).matches();


        } else {
            Toast.makeText(getApplicationContext(), "Please check your Email!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return check;
        }
    }


}