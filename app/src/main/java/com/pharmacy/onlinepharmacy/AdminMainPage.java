package com.pharmacy.onlinepharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminMainPage extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();

    private ImageButton chronic, bones, skin, eyes;
    private Button newOrder;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        chronic = findViewById(R.id.button_chronic);
        bones = findViewById(R.id.button_bones);
        skin = findViewById(R.id.button_skin);
        eyes = findViewById(R.id.button_eyes);
        newOrder = findViewById(R.id.button_check_new_order);

        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewOrders.class));
            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences("MedicalField", Context.MODE_PRIVATE);


        chronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SginUpAdminActivity.class));

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("medical_field", "chronic");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), AddMedical.class));



            }
        });

        bones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SginUpAdminActivity.class));

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("medical_field", "bones");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), AddMedical.class));

            }
        });

        skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SginUpAdminActivity.class));

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("medical_field", "skin");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), AddMedical.class));

            }
        });

        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SginUpAdminActivity.class));

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("medical_field", "eyes");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), AddMedical.class));

            }
        });



    }


}