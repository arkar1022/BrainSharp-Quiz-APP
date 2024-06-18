package com.example.brainsharp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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


public class LoginActivity extends AppCompatActivity {

    private EditText email, pass;
    private Button loginBtn;
    private TextView forgotPwdBtn, signupBtn;

    private FirebaseAuth auth;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private final boolean showOneTapUI = true;

    private Dialog progress_Dialog;
    private TextView dialogText;
    private RelativeLayout googleBtn;


//    private GoogleSignInClient googleSignInClient;
//    private int RC_SIGN_IN = 104;
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

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginBtn = findViewById(R.id.LoginBtn);
        forgotPwdBtn = findViewById(R.id.forgot_pwd);
        signupBtn = findViewById(R.id.SignupBtn);
        auth = FirebaseAuth.getInstance();
//        googleBtn = findViewById(R.id.googleBtn);

        progress_Dialog = new Dialog(LoginActivity.this);
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Authenticating user...");

//        signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();

//        GoogleSignInOptions gos = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("58505159104-mt9e823603r2c76cj4bth4ar8k5csmka.apps.googleusercontent.com")
//                        .requestEmail().build();
//
//        googleSignInClient = GoogleSignIn.getClient(this, gos);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()) {
                    login();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                googleSignIn();
//            }
//        });
    }


    private boolean validateData() {

        if(email.getText().toString().isEmpty()) {
            email.setError("Enter Email");
            return false;
        }
        if(pass.getText().toString().isEmpty()) {
            pass.setError("Enter Password");
            return false;
        }
        return true;
    };

    private void login() {
        progress_Dialog.show();
        auth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    DBQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            progress_Dialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            progress_Dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Cannot Load Categories", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    progress_Dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });
    };


//private void googleSignIn() {
//    Intent signInIntent = googleSignInClient.getSignInIntent();
//    signInLauncher.launch(signInIntent);
//}
//private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
//        new ActivityResultContracts.StartActivityForResult(),
//        result -> {
//            int resultCode = result.getResultCode();
//            System.out.println(resultCode);
//            System.out.println(Activity.RESULT_OK);
//            Intent data = result.getData();
//            if (true) {
//                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
//                try {
//                    GoogleSignInAccount account = task.getResult(ApiException.class);
//                    firebaseAuthWithGoogle(account.getIdToken());
//                    // Handle successful sign-in
//                } catch (ApiException e) {
//                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                }
//            }else {
//                System.out.println("out login");
//            }
//        });
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        progress_Dialog.show();
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                            progress_Dialog.dismiss();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            LoginActivity.this.finish();
//                        } else {
//                            progress_Dialog.dismiss();
//                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}