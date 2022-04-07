package com.pharmacy.onlinepharmacy;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

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

public class NewOrders extends AppCompatActivity {

    private TextView name, phone, location, confirmText;
    private Button confirmButton;
    private FrameLayout confirmDetails;

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();
    private String uid = FU.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_orers);

        name = findViewById(R.id.user_name0);
        phone = findViewById(R.id.user_phone);
        location = findViewById(R.id.user_location);
        confirmText = findViewById(R.id.confirmed);
        confirmDetails = findViewById(R.id.confirmPage);
        confirmButton = findViewById(R.id.send_approve);

        confirmText.setVisibility(View.GONE);

        checkConfirmation();

    }

    private void checkConfirmation() {
        DocumentReference docRefuser = Ff.collection("orders").document("bIiejg0siWetXXV0M6w5RDdpEdw1");
        docRefuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (!(document.get("order") == null)) {


                            Object object = document.getData();

                            if (object instanceof Map) {
                                Map map = (Map) object;


                                if (map.toString().contains("order")) {


                                    Map<String, Object> address = (Map<String, Object>) document.getData().get("order");

                                    for (Map.Entry<String, Object> entry : address.entrySet()) {

                                        if ((entry.getKey().equals("name"))) {
                                            name.setText("Name: "+entry.getValue().toString());
                                        }
                                        if ((entry.getKey().equals("phone"))) {
                                            phone.setText("Phone: "+entry.getValue().toString());
                                        }
                                        String lo = "", ci = "";
                                        if ((entry.getKey().equals("address"))) {
                                            lo = entry.getValue().toString();

                                            if ((entry.getKey().equals("city"))) {
                                                ci = entry.getValue().toString();

                                                location.setText("Location: "+lo+" ,"+ci);

                                            }

                                        }





                                    }





                                }


                            }


                        }


                        if (document.get("confirm").equals("true")){

                            confirmText.setVisibility(View.VISIBLE);
                            confirmDetails.setVisibility(View.GONE);

                            Map<String, Object> orderData = new HashMap<>();
                            orderData.put("confirm", "");
                            docRefuser.set(orderData);

                        }



                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Map<String, Object> orderData = new HashMap<>();
                                orderData.put("confirm", "true");
                                docRefuser.set(orderData);

                            }
                        });



                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}