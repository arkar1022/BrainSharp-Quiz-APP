package com.example.brainsharp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainsharp.Database.DBQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private EditText name, email, pass, confirmPass;
    private Button signUpBtn;
    private ImageView backBtn;
    private FirebaseAuth auth;
    private String emailStr, passStr, confirmPassStr, nameStr;

    private Dialog progress_Dialog;
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_password);
        signUpBtn = findViewById(R.id.SignupBtn);
        backBtn = findViewById(R.id.backBtn);
        auth = FirebaseAuth.getInstance();
        DBQuery.g_firestore = FirebaseFirestore.getInstance();




        progress_Dialog = new Dialog(SignUpActivity.this);
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering user...");



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    signupNewUser();
                }


            }
        });
    }

    private void signupNewUser() {
        progress_Dialog.show();
        auth.createUserWithEmailAndPassword(emailStr,passStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    DBQuery.createUserData(emailStr,nameStr, new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            DBQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progress_Dialog.dismiss();
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    SignUpActivity.this.finish();
                                }

                                @Override
                                public void onFailure() {
                                    progress_Dialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, "Cannot Load Categories", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onFailure() {
                            progress_Dialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    progress_Dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });
    }

    private boolean validate() {
        nameStr = name.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if(nameStr.isEmpty()) {
            name.setError("Enter name");
            return false;
        }
        if(emailStr.isEmpty()) {
            email.setError("Enter email");
            return false;
        }
        if(passStr.isEmpty()) {
            pass.setError("Enter password");
            return false;
        }
        if(confirmPassStr.isEmpty()) {
            confirmPass.setError("Enter password");
            return false;
        }
        if(passStr.compareTo(confirmPassStr) != 0) {
            Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}