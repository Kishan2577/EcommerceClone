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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email,pass;
    Button signInButton;
    LinearLayout signUpButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT");
        progressDialog.setCancelable(false);


        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginPassword);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setBackgroundResource(R.drawable.intro_signin);
        signUpButton = findViewById(R.id.signUpText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                String emailstr = email.getEditableText().toString();
                String passstr = pass.getEditableText().toString();

                if(TextUtils.isEmpty(emailstr) || TextUtils.isEmpty(passstr))
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "EMAIL AND PASSWORD IS INCORRECT", Toast.LENGTH_SHORT).show();
                }
                else if(!emailstr.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    email.setError("INVALID EMAIL");
                    Toast.makeText(LoginActivity.this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                }
                else if(passstr.length() <= 6)
                {
                    progressDialog.dismiss();
                    pass.setError("INVALID PASSWORD");
                    Toast.makeText(LoginActivity.this, "INVALID PASSWORD", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(emailstr,passstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                            else 
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}