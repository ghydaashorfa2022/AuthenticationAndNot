package com.example.assigmentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity2 extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    ImageView imageProfile;
    EditText Email, Username, Mobile;
    Button update;
    String id;
    Uri fileUserImage;
    Uri imageUriUser;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        FirebaseMessaging.getInstance().subscribeToTopic("ghydaa").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("ghydaa", "done");
                } else {
                    Log.e("ghydaa", "faild");
                }
            }
        });
        Email = findViewById(R.id.email);
        Mobile = findViewById(R.id.mobile);
        Username = findViewById(R.id.username);
        imageProfile = findViewById(R.id.imageProfile);
        update = findViewById(R.id.update);
       // update.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        getuserProfile();
        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setVisibility(View.VISIBLE);
            }
        });
        Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setVisibility(View.VISIBLE);
            }
        });

        update.setOnClickListener(v -> {
            storageReference = firebaseStorage.getReference();
            uploadImg();
        });
        imageProfile.setOnClickListener(v -> {
            update.setVisibility(View.VISIBLE);
            Intent intentImg = new Intent();
            intentImg.setType("image/*");
            intentImg.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentImg, 100);
        });

    }

    public void getuserProfile() {
        db = FirebaseFirestore.getInstance();

        db.collection("Users").whereEqualTo("idUserAuth", mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d("drn", "onSuccess: LIST EMPTY");
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        id = documentSnapshot.getId();
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("userName");
                            String mobile = documentSnapshot.getString("mobileuser");
                            String email = documentSnapshot.getString("email");
                            String userImage = documentSnapshot.getString("userImage");
                            Picasso.get().load(userImage).into(imageProfile);
                            Log.e("ghydaaaa", userImage);
                            Email.setText(email);
                            Username.setText(username);
                            Mobile.setText(mobile);

                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("testcons", "get failed with ");
            }
        });
    }

    public void UpateData() {
        db.collection("Users").document(id).update("userName", Username.getText().toString(), "mobileuser", Mobile.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("dareen", "DocumentSnapshot successfully updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("dareen", "Error updating document", e);
            }
        });
    }

    public void uploadImg() {
        StorageReference imgRefUser = storageReference.child("Users");
        Bitmap bitmapUser = ((BitmapDrawable) imageProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapUser.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] dataUser = baos.toByteArray();
        StorageReference childRefLogo = imgRefUser.child(System.currentTimeMillis() + "_UserImage");
        UploadTask uploadTask = childRefLogo.putBytes(dataUser);
        uploadTask.addOnFailureListener(exception -> Log.e("TAG", exception.getMessage())).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(ProfileActivity2.this, "Image Logo Upload Successfully", Toast.LENGTH_SHORT).show();
            childRefLogo.getDownloadUrl().addOnSuccessListener(uri -> {
                fileUserImage = uri;
                UpateData();
            });
        });
    }
}
