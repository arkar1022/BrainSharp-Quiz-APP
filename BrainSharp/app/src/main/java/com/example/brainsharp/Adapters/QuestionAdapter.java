package com.example.brainsharp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brainsharp.Database.DBQuery;
import com.example.brainsharp.Models.QuestionModel;
import com.example.brainsharp.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {



    private List<QuestionModel> questionsList;

    public QuestionAdapter(List<QuestionModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ques;
        private Button optionA, optionB, optionC, optionD, prevSelectedB;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.tv_ques);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null;
        }

        private  void setData(final int pos) {
            ques.setText(questionsList.get(pos).getQuestion());
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            optionC.setText(questionsList.get(pos).getOptionC());
            optionD.setText(questionsList.get(pos).getOptionD());

            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);


            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionA, 1, pos);
                }
            });
            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionB, 2, pos);
                }
            });
            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionC, 3, pos);
                }
            });
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionD, 4, pos);
                }
            });
        }

        private void selectOption(Button btn, int option_num, int quesID) {

            if(prevSelectedB == null) {
                btn.setBackgroundResource(R.drawable.selected_btn);
                DBQuery.g_quesList.get(quesID).setSlectedAns(option_num);
                prevSelectedB = btn;
            }
            else {
                if(prevSelectedB.getId() == btn.getId()) {
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DBQuery.g_quesList.get(quesID).setSlectedAns(-1);
                    prevSelectedB = null;
                } else {
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);
                    DBQuery.g_quesList.get(quesID).setSlectedAns(option_num);
                    prevSelectedB = btn;
                }
            }
        }
        private void setOption(Button btn, int optiion_num, int queID) {
            if(DBQuery.g_quesList.get(queID).getSlectedAns() == optiion_num) {
                btn.setBackgroundResource(R.drawable.selected_btn);
            }else {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }

    }
}
