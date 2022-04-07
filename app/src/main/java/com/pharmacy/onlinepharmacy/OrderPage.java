package com.pharmacy.onlinepharmacy;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Map;

public class OrderPage extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();
    String uid = FU.getUid();
    private Button next;
    private TextView total;
    private ListView listView;
    private static DataAdapter DataAdapter;
    ArrayList<DataModel> dataModelsList;
    int p = 0;

//    SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
//    String medicalID = prefs.getString("medical_id", "null");
//
//    SharedPreferences prefs0 = getSharedPreferences("MedicalField", MODE_PRIVATE);
//    String medical_field = prefs0.getString("medical_field", "null");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        total = findViewById(R.id.total_price_id);
        next = findViewById(R.id.button_next);
        listView = findViewById(R.id.order_listView);


        getCartListData();








    }

    public void getMedicalTotalPrice(ArrayList<IDModel> idList) {

        int listSize = idList.size();

        SharedPreferences sharedpreferences = getSharedPreferences("MedicalData", Context.MODE_PRIVATE);




        for (int i = 0; i<listSize; i++) {


            String id_medical = idList.get(i).getId().trim(), collection_medical = idList.get(i).getCollection();

//            try {
//                Thread.sleep(1000);

            final int[] oo = {0};
                DocumentReference docRef = Ff.collection(collection_medical).document(id_medical);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {


//                                Log.d("MedicalDataPrice", document.get("medicalPrice").toString().trim());
                                 oo[0] =  Integer.parseInt(document.get("medicalPrice").toString().trim());

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt("medicalPrice", Integer.parseInt(document.get("medicalPrice").toString().trim()));
                                editor.apply();



                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }



            SharedPreferences prefs0 = getSharedPreferences("MedicalData", MODE_PRIVATE);
            int medicalP = prefs0.getInt("medicalPrice", 0);

            p += oo[0];
            Log.d("MedicalDataPrice", String.valueOf(medicalP));



        }

        total.setText("Price total: " + p + " OR");




    }

    private void getCartListData() {



        DocumentReference docRef = Ff.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        dataModelsList = new ArrayList<>();

                        if (!(document.get("medical1") == null)) {

                            ArrayList<IDModel> idList = new ArrayList<>();

                            Object object = document.getData();
                            int count = 1;

                            if (object instanceof Map) {
                                Map map = (Map) object;

                                String id = "", collection = "";

                                while (map.toString().contains("medical" + count)) {

                                    Map<String, Object> address = (Map<String, Object>) document.getData().get("medical"+count);

                                    for (Map.Entry<String, Object> entry : address.entrySet()) {

//                                        Log.d("MedicalData", document.getData().toString());
                                        if ((entry.getKey().equals("id"))) {

                                            id = entry.getValue().toString();




                                        }

                                        if ((entry.getKey().equals("collection"))) {

                                            collection = entry.getValue().toString();

                                        }




                                        DocumentReference docRef = Ff.collection(collection.trim()).document(id.trim());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {


                                                        dataModelsList.add(new DataModel(document.get("medicalName").toString(), document.get("medicalDescription").toString(), document.get("medicalPrice").toString(), document.get("medicalCover").toString(), document.getId().toString()));

                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });


                                    }

                                    idList.add(new IDModel(id, collection));

                                    count++;

                                }


                                DataAdapter = new DataAdapter(dataModelsList, getApplicationContext());
                                listView.setAdapter(DataAdapter);

                            }



                            getMedicalTotalPrice(idList);


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





}