package com.example.brainsharp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.brainsharp.Adapters.QuestionAdapter;
import com.example.brainsharp.Database.DBQuery;

import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {


    private RecyclerView questionsView;
    private long timeleft;
    private TextView tvQuesID, timerTV, catNameTV;
    private Button sumbitB, markB, clearSlB;
    private ImageButton prevQuesB, nextQuesB;
    private ImageView quesListB;

    private int quesID;

    private QuestionAdapter questionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        questionAdapter = new QuestionAdapter(DBQuery.g_quesList);
        questionsView.setAdapter(questionAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        setSnapHelper();
        setClickListener();
        startTimer();
    }

    private void init() {
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_CatName);
        sumbitB = findViewById(R.id.submitBtn);
        markB = findViewById(R.id.markB);
        clearSlB = findViewById(R.id.clear_selB);
        prevQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
//        quesListB = findViewById(R.id.ques_list_Btn);
        quesID = 0;

        tvQuesID.setText("1/" + String.valueOf(DBQuery.g_quesList.size()));
        catNameTV.setText(DBQuery.g_catList.get(DBQuery.g_selected_cat_index).getName());
    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);
                tvQuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(DBQuery.g_quesList.size()));

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    private void setClickListener() {
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quesID > 0) {
                    questionsView.smoothScrollToPosition(quesID - 1);
                }
            }
        });
        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quesID < DBQuery.g_quesList.size() - 1) {
                    questionsView.smoothScrollToPosition(quesID + 1);
                }
            }
        });

        clearSlB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBQuery.g_quesList.get(quesID).setSlectedAns(-1);
                questionAdapter.notifyDataSetChanged();
            }
        });

        sumbitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long totalTime = DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()*60*1000;

                Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                intent.putExtra("TIME_TAKEN",totalTime - timeleft);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        });
    }

    private void startTimer() {
        long totalTime = DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()*60*1000;
        CountDownTimer timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                timeleft = remainingTime;
                String time = String.format("%02d : %02d min",TimeUnit.MILLISECONDS.toMinutes(remainingTime), TimeUnit.MILLISECONDS.toSeconds(remainingTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));
                timerTV.setText(time);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                intent.putExtra("TIME_TAKEN",totalTime - timeleft);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        };
        timer.start();
    }
}