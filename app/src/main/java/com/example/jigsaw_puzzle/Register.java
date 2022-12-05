package com.example.jigsaw_puzzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    EditText mUsername, mEmail, mPassword, mPhone, mRetypePassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRetypePassword = findViewById(R.id.retypepassword);
        mRegisterBtn = findViewById(R.id.register);
        mLoginBtn = findViewById(R.id.login);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //check if current user already exists
//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), AppActivity.class));
//            return;
//        }
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String retypepass = mRetypePassword.getText().toString().trim();
                String username = mUsername.getText().toString().trim();
                //check certain conditions for registration
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long");
                    return;
                }

                if (!password.equals(retypepass)) {
                    mRetypePassword.setError("Passwords must match");
                    return;
                }

                //register the user via firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String UID = task.getResult().getUser().getUid();
                            Map<String, Object> userDocument = new HashMap<>();
                            userDocument.put("id", UID);
                            userDocument.put("username", username);


                            db.collection("users").document(UID).set(userDocument);



                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AppActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}