package com.example.assigmentapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SignupActivity2 extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText email;
    public String path;
    private Uri fileURI = null;
    public StorageReference storageReference = null;
    public ImageView imageView;
    private final int PICK_IMAGE_GALLERY_CODE = 78;
    private final int CAMERA_PERMISSION_REQUEST_CODE = 12345;
    private final int CAMERA_PICTURE_REQUEST_CODE = 56789;
    FirebaseAuth mAuth;
    EditText mobile;
    FirebaseFirestore db;
    TextView Logg;
    Button regsister;
    String Mobile;
    public DatabaseReference databaseReference = null;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        Logg = findViewById(R.id.Logg);
        regsister = findViewById(R.id.regsister);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = database.getReference().child("user_image");
        storageReference = firebaseStorage.getReference();

        imageView = findViewById(R.id.imagepro);

        regsister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regsisterbtn(v);
            }
        });

        Logg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity2.this, LogIn.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignupActivity2.this).toBundle());
            }
        });


    }

    public void regsisterbtn(View view) {

        String textUserName = username.getText().toString();
        String textPassword = password.getText().toString();
        String textEmail = email.getText().toString();
        String textMobile = mobile.getText().toString();
        if (TextUtils.isEmpty(textUserName)) {
            Log.e("testnama", " String.valueOf(fileURI)");

            Toast.makeText(this, "please enter your full name ", Toast.LENGTH_SHORT).show();
            username.setError(" user name is required");
            username.requestFocus();
        } else if (TextUtils.isEmpty(textPassword)) {
            Toast.makeText(this, "please enter your password   ", Toast.LENGTH_SHORT).show();
            password.setError(" password is required");
            password.requestFocus();
        } else if (password.length() < 6) {
            Toast.makeText(this, "please enter your password  more then 6  ", Toast.LENGTH_SHORT).show();
            password.setError(" password is required more the 6 ");
            password.requestFocus();
        } else if (TextUtils.isEmpty(textEmail)) {
            Toast.makeText(this, "please enter your email   ", Toast.LENGTH_SHORT).show();
            email.setError(" email is required  ");
            email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            Toast.makeText(this, "please enter your email true    ", Toast.LENGTH_SHORT).show();
            email.setError(" email is vaild true   ");
            email.requestFocus();
        } else {
            StorageReference ref = storageReference.child("image/" + UUID.randomUUID().toString());
            ref.putFile(fileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("testtnn", String.valueOf(fileURI));
                    Log.e("testtnn", " String.valueOf(fileURI)");

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.push().setValue(uri.toString());
                            Toast.makeText(SignupActivity2.this, "Image  up", Toast.LENGTH_SHORT).show();
                            path = String.valueOf(uri);
                            RegisterUSER(textUserName, textEmail, path, textMobile
                                    , textPassword);
                            Log.e("nada444"  , textUserName) ;
                            Log.e("nada444"  , "textUserName") ;


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity2.this, "Image  faild ", Toast.LENGTH_SHORT).show();

                                        }
                                    }
            );


        }


    }

    public void RegisterUSER(String username,String email, String path, String mobile,String password) {
        Log.e("nadaRRR" , email) ;
        Log.e("pass",password);
        Log.e("nada" , "username") ;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity2.this, "regisiter Successfully ", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                    firebaseUser.sendEmailVerification();

                    User users = new User("",firebaseUser.getUid(), username, email, path, mobile,password);
                    Log.e("pass",password);
                    if (users==null){
                        Log.e("user","null");
                    }else {
                        Log.e("us","not null");
                    }

                    db.collection("Users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("TAG", "Data added successfully to database");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAGNAada", "Data added falid to database");


                        }
                    });
                }else {
                    Log.e("taskNada" , "faslid") ;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("NDA" , "FALID")   ;
            }
        });
    }

    public void selectImageProfileUser(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("select image");
        builder.setMessage("Please select on option ");
        builder.setPositiveButton("camera", (dialogInterface, i) -> {
            checkPermisionImge();
            dialogInterface.dismiss();

        });
        builder.setNeutralButton("cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setNegativeButton("Gallery", (dialogInterface, i) -> {
            selectFormGallery();
            dialogInterface.dismiss();

        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void checkPermisionImge() {
        if (ContextCompat.checkSelfPermission(SignupActivity2.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SignupActivity2.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignupActivity2.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);

        } else {
            openImage();
        }

    }

    private void openImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_PICTURE_REQUEST_CODE);
        }

    }

    private void selectFormGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "selectImage"), PICK_IMAGE_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) return;
            try {
                fileURI = data.getData();
                Log.e("nada", String.valueOf(data.getData()));
                Log.e("naffda", String.valueOf(fileURI));

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileURI);
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {

            }
        }
    }
}