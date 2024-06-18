package com.example.brainsharp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainsharp.Database.DBQuery;

public class StartTestActivity extends AppCompatActivity {
    private TextView catName, testNo, totalQ, bestScore, time;
    private Button startTestB;
    private ImageView backB;
    private Dialog progress_Dialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        progress_Dialog = new Dialog(StartTestActivity.this);
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Fetching Data ...");
        progress_Dialog.show();
        init();
        DBQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progress_Dialog.dismiss();
                setData();
            }

            @Override
            public void onFailure() {
                progress_Dialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Cannot Load Categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        catName = findViewById(R.id.st_CatName);
        testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);
        startTestB = findViewById(R.id.startBtn);
        backB = findViewById(R.id.backBtn);


        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTestActivity.this.finish();
            }
        });

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTestActivity.this, QuestionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData() {
        catName.setText(DBQuery.g_catList.get(DBQuery.g_selected_cat_index).getName());
        testNo.setText("Test No. " + String.valueOf(DBQuery.g_selected_test_index + 1));
        totalQ.setText(String.valueOf(DBQuery.g_quesList.size()));
        bestScore.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()));
    }
}