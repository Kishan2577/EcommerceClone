package com.example.amazoncloneproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amazoncloneproject.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    EditText regName,regEmail, regPass, regConfirmPass;
    Button signUpButton;
    LinearLayout signInText;
    String imageUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        regName = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regConfirmPass = findViewById(R.id.regConfirmPass);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setBackgroundResource(R.drawable.intro_signin);

        signInText = findViewById(R.id.signInText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT");
        progressDialog.setCancelable(false);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = regName.getEditableText().toString();
                String email = regEmail.getEditableText().toString();
                String pass = regPass.getEditableText().toString();
                String cpass = regConfirmPass.getEditableText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(cpass))
                {
                    Toast.makeText(RegisterActivity.this, "PLEASE ENTER VALID DATA", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!email.matches(emailPattern))
                {
                    regEmail.setError("INVALID EMAIL");
                    Toast.makeText(RegisterActivity.this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else if(pass.length()<=6)
                {
                    regPass.setError("INVALID PASSWORD");
                    Toast.makeText(RegisterActivity.this, "PLEASE ENTER MORE THAN 6 CHARATAR", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!pass.equals(cpass))
                {
                    Toast.makeText(RegisterActivity.this, "PASSWORD DOES NOT MATCHES", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
                                // StorageReference storageReference = storage.getReference().child("upload").child(auth.getCurrentUser().getUid());

                                imageUri = "IMAGE URL HERE";


                                Users users = new Users(auth.getCurrentUser().getUid(),name,email,imageUri);

                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this, "ERROR IN CREATING NEW USER", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}