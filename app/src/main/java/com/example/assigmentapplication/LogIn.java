package com.example.assigmentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText textEditEmail;
    EditText textEditPassword;
    Button LoginBtn;
    TextView Signup;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        textEditEmail = findViewById(R.id.email);
        textEditPassword = findViewById(R.id.password);
        LoginBtn = findViewById(R.id.log);
        Signup = findViewById(R.id.Signup);
        mAuth = FirebaseAuth.getInstance();

        LoginBtn.setOnClickListener(view -> {
            String textEmail = textEditEmail.getText().toString();
            String textPassword = textEditPassword.getText().toString();
            Log.e("email", textEmail);
            if (TextUtils.isEmpty(textEmail)) {
                Toast.makeText(LogIn.this, "please enter your email   ", Toast.LENGTH_SHORT).show();
                textEditEmail.setError(" email is required  ");
                textEditEmail.requestFocus();
            } else if (TextUtils.isEmpty(textPassword)) {
                Toast.makeText(LogIn.this, "please enter your password   ", Toast.LENGTH_SHORT).show();
                textEditPassword.setError(" password is required");
                textEditPassword.requestFocus();
            } else {
                loginFirebaseDB(textEmail, textPassword);
            }

        });
        Signup.setOnClickListener(view -> {
                    Intent intent = new Intent(LogIn.this, SignupActivity2.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LogIn.this).toBundle());
                }
        );


    }

    private void loginFirebaseDB(String textEmail, String textPassword) {
        mAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

//                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                   Intent intent = new Intent(LogIn.this  , ProfileActivity2.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LogIn.this).toBundle());


//                    SharedPreferences sharedPref = getSharedPreferences("loginAndLogoutOP", Context.MODE_PRIVATE);
//                    sharedPref.edit().putBoolean(String.valueOf(R.string.LoginActive), true).apply();

                } else {
                    Toast.makeText(LogIn.this, "something is wrong !", Toast.LENGTH_SHORT).show();
                }

            }


        });
    }
}