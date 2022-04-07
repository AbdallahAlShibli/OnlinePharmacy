package com.pharmacy.onlinepharmacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMedical extends AppCompatActivity {

    private FirebaseFirestore Ff = FirebaseFirestore.getInstance();
    private FirebaseAuth FA = FirebaseAuth.getInstance();
    private FirebaseUser FU = FA.getCurrentUser();
    String uid = FU.getUid();
    private EditText medicalName, medicalDescription, medicalPrice;
    private Button uploadMedical;
    private ImageView inputMedicalImage;
    private String productRandomKey, downLoadImageUrl;
    private StorageReference medicaltImagesRef;

    private static final int GalleryPick = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical);

        medicalName = findViewById(R.id.medical_name);
        medicalDescription = findViewById(R.id.medical_description);
        medicalPrice = findViewById(R.id.medical_price);
        inputMedicalImage = (ImageView) findViewById(R.id.medical_image);
        uploadMedical = findViewById(R.id.button_add_medical_into_library);

        medicaltImagesRef = FirebaseStorage.getInstance().getReference().child("MedicalCover");

        inputMedicalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMedicalImage();
            }
        });


    }

    public void addMedicalDetails() {


        SharedPreferences prefs = getSharedPreferences("MedicalField", MODE_PRIVATE);
        String medical_field = prefs.getString("medical_field", "null");

        String MedicalName = medicalName.getText().toString();
        String MedicalDescription = medicalDescription.getText().toString();
        String MedicalPrice = medicalPrice.getText().toString().trim();


        CollectionReference userData = Ff.collection(medical_field);
        Map<String, Object> data1 = new HashMap<>();
        data1.put("medicalName", MedicalName);
        data1.put("medicalDescription", MedicalDescription);
        data1.put("medicalPrice", MedicalPrice);
        data1.put("adminID", uid);
        data1.put("medicalCover", downLoadImageUrl);


        try {
            Thread.sleep(2000);

            userData.document().set(data1);

            Toast.makeText(getApplicationContext(), "Medical Details Uploaded.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), AdminMainPage.class));
            finish();

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }


    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            inputMedicalImage.setImageURI(imageUri);
        }
    }


    private void SaveMedicalImage() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calender = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = medicaltImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Medical Image uploaded Successfully....", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downLoadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            downLoadImageUrl = task.getResult().toString();
                            Toast.makeText(getApplicationContext(), "got the Medical cover Url Successfully...", Toast.LENGTH_SHORT).show();

                            addMedicalDetails();
                        }

                    }
                });
            }
        });


    }
}