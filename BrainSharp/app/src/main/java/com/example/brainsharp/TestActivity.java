package com.example.brainsharp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brainsharp.Adapters.TestAdapter;
import com.example.brainsharp.Database.DBQuery;

public class TestActivity extends AppCompatActivity {
    private RecyclerView testView;
    private Toolbar toolbar;

    private Dialog progress_Dialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        int cat_index = getIntent().getIntExtra("CAT_INDEX", 0);
        int cat_index = DBQuery.g_selected_cat_index;
        System.out.println(cat_index);
        getSupportActionBar().setTitle(DBQuery.g_catList.get(cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        testView = findViewById(R.id.test_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        progress_Dialog = new Dialog(TestActivity.this);
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Fetching Data ...");
        progress_Dialog.show();
//        loadTestData();
        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                DBQuery.loadMyScores(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        TestAdapter adapter = new TestAdapter(DBQuery.g_testList);
                        testView.setAdapter(adapter);
                        progress_Dialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(TestActivity.this, "Cannot Load Scores", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure() {progress_Dialog.dismiss();
                Toast.makeText(TestActivity.this, "Cannot Load Categories", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void loadTestData() {
//        testList = new ArrayList<>();
//        testList.add(new TestModel("1",50,20));
//        testList.add(new TestModel("2",30,40));
//        testList.add(new TestModel("3",20,10));
//        testList.add(new TestModel("4",50,20));
//
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}