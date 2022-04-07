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
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInAdminActivity extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();

    private Button loginButton;
    private EditText email, password;
    private RelativeLayout progressBar;
    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);


        loginButton = findViewById(R.id.login);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);

        progressBar = findViewById(R.id.progressBarLayout);
        progressBar.setVisibility(View.GONE);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SginUpAdminActivity.class));
            }
        });

        Login();

    }

    private void Login() {


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                final String Email = email.getText().toString();
                if (checkEmail(Email) && checkPassword()) {
                    String Password = password.getText().toString();
                    FA.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LogInAdminActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login is successfully!", Toast.LENGTH_SHORT).show();

                                try {
                                    Thread.sleep(2000);


                                    progressBar.setVisibility(View.GONE);

                                    startActivity(new Intent(getApplication(), AdminMainPage.class));
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
        }else {

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