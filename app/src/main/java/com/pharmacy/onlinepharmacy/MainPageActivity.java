package com.pharmacy.onlinepharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPageActivity extends AppCompatActivity {

    private ImageButton user, provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        user = findViewById(R.id.loginUser);
        provider = findViewById(R.id.loginAdmin);


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LogInActivity.class));
            }
        });


        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LogInAdminActivity.class));
            }
        });


    }



    @Override
    protected void onPostResume() {
        FirebaseAuth FA = FirebaseAuth.getInstance();
        FirebaseUser FU = FA.getCurrentUser();
        FU = FA.getCurrentUser();
        if (FU != null) {

            FA.signOut();

        }
        super.onPostResume();
    }
}