package com.example.brainsharp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainsharp.Database.DBQuery;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTV, timeTV, totalQTV, correctQTV, wrongQTV, unattemptQTV;
    private Button homeBtn, reattemptBtn, viewAnsBtn;
    private BottomNavigationView bottomNavigationView;

    private Dialog progress_Dialog;

    private FrameLayout main_frame;
    private TextView dialogText;

    private int finalScore;

    private long timeTaken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Toolbar toolbar = findViewById(R.id.r_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress_Dialog = new Dialog(ScoreActivity.this);
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Saving...");





        init();
        loadData();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        viewAnsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, AnswerActivity.class);
                startActivity(intent);
            }
        });

        reattemptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt ();
            }
        });

        progress_Dialog.show();
        saveResult();
    }

    private void init() {
        scoreTV = findViewById(R.id.tv_score);
        timeTV = findViewById(R.id.time);
        totalQTV = findViewById(R.id.totalQ);
        correctQTV = findViewById(R.id.correctQ);
        wrongQTV = findViewById(R.id.wrongQ);
        unattemptQTV = findViewById(R.id.unattemptQ);
        reattemptBtn = findViewById(R.id.reattmeptBtn);
        viewAnsBtn = findViewById(R.id.viewAnsBtn);
        homeBtn = findViewById(R.id.backHomBtn);
    }

    private void loadData() {
        int correctQ = 0, wrongQ = 0, unattemptQ=0;
        for (int i = 0; i < DBQuery.g_quesList.size(); i++) {
            if (DBQuery.g_quesList.get(i).getSlectedAns() == -1) {
                unattemptQ++;
            }else {
                if(DBQuery.g_quesList.get(i).getSlectedAns() == DBQuery.g_quesList.get(i).getCorrectAns()){
                    correctQ++;
                } else {
                    wrongQ++;
                }
            }

        }

        finalScore = (correctQ*100)/DBQuery.g_quesList.size();
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptQTV.setText(String.valueOf(unattemptQ));
        scoreTV.setText(String.valueOf(finalScore));
        totalQTV.setText(String.valueOf(DBQuery.g_testList.size() - 1));
        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);
        String time = String.format("%02d : %02d min", TimeUnit.MILLISECONDS.toMinutes(timeTaken), TimeUnit.MILLISECONDS.toSeconds(timeTaken) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));
        timeTV.setText(time);
    }

    private void saveResult(){
        DBQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progress_Dialog.dismiss();
            }

            @Override
            public void onFailure() {
                progress_Dialog.dismiss();
                Toast.makeText(ScoreActivity.this, "Cannot Save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reAttempt(){
        for(int i=0; i<DBQuery.g_quesList.size(); i++){
            DBQuery.g_quesList.get(i).setSlectedAns(-1);

        }
        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}