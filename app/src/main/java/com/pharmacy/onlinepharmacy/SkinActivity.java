package com.pharmacy.onlinepharmacy;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SkinActivity extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();
//    String uid = FU.getUid();

    ArrayList<DataModel> dataModelsList;
    ListView listView;
    private static DataAdapter DataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        listView = findViewById(R.id.list_data);

        getMedicalData();

    }


    public void getMedicalData() {

        SharedPreferences prefs = getSharedPreferences("MedicalField", MODE_PRIVATE);
        String medical_field = prefs.getString("medical_field", "null");

        Ff.collection(medical_field).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    dataModelsList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());

                        Map<String, Object> map = document.getData();
                        String name = "", description = "", price = "", coverURL = "", medicalID = "";
                        for(Map.Entry<String, Object> entry : map.entrySet()) {
//                                    String key = entry.getKey();
//                                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                            //Iterate again and add the data

                            medicalID = document.getId();

                            if (entry.getKey().equals("medicalName")) name = entry.getValue().toString();
                            if (entry.getKey().equals("medicalDescription")) description = entry.getValue().toString();
                            if (entry.getKey().equals("medicalPrice")) price = entry.getValue().toString();
                            if (entry.getKey().equals("medicalCover")) coverURL = entry.getValue().toString();




                            Log.d("ProvidersNames", name);
                        }
                        dataModelsList.add(new DataModel(name, description, price, coverURL, medicalID));


                    }



                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                DataAdapter = new DataAdapter(dataModelsList, getApplicationContext());
                listView.setAdapter(DataAdapter);

                SharedPreferences sharedpreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DataModel dataModel = dataModelsList.get(i);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("medical_id", dataModel.getMedicalID());
                        editor.apply();

                        startActivity(new Intent(getApplicationContext(), AddToCart.class));

                    }
                });

            }
        });


    }
}