package com.example.brainsharp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainsharp.Database.DBQuery;

public class MyProfileActivity extends AppCompatActivity {

    private EditText name, email, phone;
    private LinearLayout editBtn, buttonLayout;
    private Button cancelBtn, saveBtn;
    private Toolbar toolbar;
    private TextView profileText;
    private Dialog progress_Dialog;
    private TextView dialogText;

    private String nameStr, phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress_Dialog = new Dialog(MyProfileActivity.this);
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Updating ...");


        name = findViewById(R.id.edit_name);
        email = findViewById(R.id.edit_email);
        phone = findViewById(R.id.edit_ph);
        profileText = findViewById(R.id.profile_name);
        editBtn = findViewById(R.id.editBtn);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        buttonLayout = findViewById(R.id.buttonLayout);

        disableEditing();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditing();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()) {
                    saveData();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void disableEditing() {
        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        buttonLayout.setVisibility(View.GONE);

        name.setText(DBQuery.myProfile.getName());
        email.setText(DBQuery.myProfile.getEmail());

        if(DBQuery.myProfile.getPhone() != null) {
            phone.setText(DBQuery.myProfile.getPhone());
        }
        String profileName = DBQuery.myProfile.getName();
        profileText.setText(profileName.toUpperCase().substring(0,1));
    }
    private void enableEditing() {
        name.setEnabled(true);
        phone.setEnabled(true);
//        email.setEnabled(true);
        buttonLayout.setVisibility(View.VISIBLE);
    }

    private boolean validate() {
        nameStr = name.getText().toString();
        phoneStr = phone.getText().toString();

        if(nameStr.isEmpty()) {
            name.setError("Name cannot be empty");
            return false;
        }
        if(!phoneStr.isEmpty()) {
            if(phoneStr.length() < 6 || phoneStr.length() > 13){
                phone.setError("Enter Valid Number");
                return false;
            }
        }
        return true;
    }

    private void saveData() {
        progress_Dialog.show();
        if(phoneStr.isEmpty()){
            phoneStr = null;
        }
        DBQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progress_Dialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                disableEditing();
            }

            @Override
            public void onFailure() {
                progress_Dialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
}