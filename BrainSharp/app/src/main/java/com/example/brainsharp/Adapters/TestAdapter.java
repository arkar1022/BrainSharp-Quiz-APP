package com.example.brainsharp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brainsharp.Database.DBQuery;
import com.example.brainsharp.Models.TestModel;
import com.example.brainsharp.R;
import com.example.brainsharp.StartTestActivity;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<TestModel> testList;
    public TestAdapter(List<TestModel> testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        int progress = testList.get(position).getTopScore();
        holder.setData(position,progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView testNum;
        private TextView topScore;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            testNum = itemView.findViewById(R.id.testNum);
            topScore = itemView.findViewById(R.id.scoreText);
            progressBar = itemView.findViewById(R.id.testProgress);

        }
        private void setData(int pos, int progress){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBQuery.g_selected_test_index = pos;
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
            testNum.setText("Test  " + String.valueOf(pos + 1));
            topScore.setText(String.valueOf(progress) + " %");
            progressBar.setProgress(progress);
        }
    }
}
