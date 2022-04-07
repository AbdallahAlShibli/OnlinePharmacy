package com.pharmacy.onlinepharmacy;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddToCart extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();
    private String uid = FU.getUid();
    private String admin_id;

    private TextView medical_name, medical_description, medical_price;
    private Button addToCart;
    private ImageView imageView;

    ArrayList<ReviewDataModel> dataModelsList;
    ListView listView;
    private static ReviewDataAdapter reviewDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        medical_name = findViewById(R.id.medical_name);
        medical_description = findViewById(R.id.medical_description);
        medical_price = findViewById(R.id.medical_price);
        imageView = findViewById(R.id.medical_image);

        addToCart = findViewById(R.id.button_add_to_cart);
//        listView = findViewById(R.id.list_comment);


        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        String medicalID = prefs.getString("medical_id", "null");

        SharedPreferences prefs0 = getSharedPreferences("MedicalField", MODE_PRIVATE);
        String medical_field = prefs0.getString("medical_field", "null");


        DocumentReference docRef = Ff.collection(medical_field).document(medicalID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        medical_name.setText("Medical Name: "+document.get("medicalName").toString());
                        medical_description.setText("Medical Description: "+document.get("medicalDescription").toString());
                        medical_price.setText("Medical Price: "+document.get("medicalPrice").toString()+" OR");
                        imageView.setImageResource(R.mipmap.ic_launcher_round);
                        Picasso.get().load(prefs.getString("medical_cover_url", null)).into(imageView);

                        admin_id = document.get("adminID").toString();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddToCart.this);
                builder1.setMessage("Choose your payment method:");
                builder1.setCancelable(true);


                builder1.setPositiveButton(
                        "Cash",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                sendReq(medicalID, medical_field, "Cash");
                                Toast.makeText(getApplicationContext(), "Your Order has been set!", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder1.setNegativeButton(
                        "Credit card",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                sendReq(medicalID, medical_field, "Credit card");
                                startActivity(new Intent(getApplicationContext(), CheckOutActivity.class));
                                Toast.makeText(getApplicationContext(), "Your Order has been set!", Toast.LENGTH_SHORT).show();

                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });




    }


    private void sendReq(String medicalID, String medical_field, String payWay) {

        SharedPreferences sharedpreferencesProvider = getSharedPreferences("MedicalField", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferencesProvider.edit();
        editor.putString("medical_collection", medical_field);
        editor.apply();

        //for user
        DocumentReference docRef = Ff.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if (!(document.get("medical1") == null)) {


                            Object object = document.getData();
                            int count = 1;
                            if (object instanceof Map) {
                                Map map = (Map) object;

                                while (map.toString().contains("medical"+count)) {


                                    count++;
                                }



                            }



                            Map<String, Object> cartData = new HashMap<>();
                            Map<String, Object> medicalData = new HashMap<>();
                            medicalData.put("id", medicalID);
                            medicalData.put("collection", medical_field);
                            cartData.put("medical"+count, medicalData);
                            docRef.set(cartData,SetOptions.merge());

                        }else {

                            Map<String, Object> cartData = new HashMap<>();
                            Map<String, Object> medicalData = new HashMap<>();
                            medicalData.put("id", medicalID);
                            medicalData.put("collection", medical_field);
                            cartData.put("medical"+1, medicalData);
                            docRef.set(cartData,SetOptions.merge());

                        }

//                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}