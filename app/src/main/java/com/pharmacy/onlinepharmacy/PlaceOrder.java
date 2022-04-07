package com.pharmacy.onlinepharmacy;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity {

    private EditText name, phone, address, city;
    private Button place;
    private TextView delivery_text, delivery_text_confirm;
    private LinearLayout layout;


    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();
    private String uid = FU.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        name = findViewById(R.id.shipment_name);
        phone = findViewById(R.id.shipment_phone_number);
        address = findViewById(R.id.shipment_address);
        city = findViewById(R.id.shipment_city);
        place = findViewById(R.id.place_order);
        delivery_text = findViewById(R.id.delivery);
        delivery_text_confirm = findViewById(R.id.delivery_confirm);
        layout = findViewById(R.id.place_layout);

        delivery_text.setVisibility(View.GONE);
        delivery_text_confirm.setVisibility(View.GONE);


        checkConfirmation();


        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                String Address = address.getText().toString();
                String City = city.getText().toString();

                if (checkBox(Name) && checkBox(Phone) && checkBox(Address) && checkBox(City)) {

                    //for admin
                    DocumentReference docRefuser = Ff.collection("orders").document("");
                    docRefuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {


                                    Map<String, Object> orderData = new HashMap<>();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", Name);
                                    map.put("phone", Phone);
                                    map.put("address", Address);
                                    map.put("city", City);
                                    orderData.put("confirm", "");
                                    orderData.put("order", map);
                                    docRefuser.set(orderData);


                                    Toast.makeText(getApplicationContext(), "Your order has been set!", Toast.LENGTH_SHORT).show();

                                    deleteCartListData();

                                    delivery_text.setVisibility(View.VISIBLE);
                                    layout.setVisibility(View.GONE);

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Please complete all gaps!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void checkConfirmation() {
        DocumentReference docRefuser = Ff.collection("orders").document("bIiejg0siWetXXV0M6w5RDdpEdw1");
        docRefuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        if (document.get("confirm").equals("true")){

                            delivery_text.setVisibility(View.GONE);
                            delivery_text_confirm.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.GONE);

                            Map<String, Object> orderData = new HashMap<>();
                            orderData.put("confirm", "");
                            docRefuser.set(orderData);

                            delivery_text_confirm.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                        }







                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private boolean checkBox(String character) {

        boolean check = false;

        if (!character.isEmpty()) return !TextUtils.isEmpty(character);


        return check;
    }


    private void deleteCartListData() {


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


                                while (map.toString().contains("medical" + count)) {

                                    Map<String, Object> orderData = new HashMap<>();
                                    orderData.put("medical" + count, null);
                                    docRef.set(orderData);


                                }


                            }


                        }


                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }


}