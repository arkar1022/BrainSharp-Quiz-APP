package com.example.brainsharp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brainsharp.Models.QuestionModel;
import com.example.brainsharp.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<QuestionModel> quesList;

    public AnswerAdapter(List<QuestionModel> quesList) {
        this.quesList = quesList;
    }


    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout,parent,false);
        return new AnswerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.ViewHolder holder, int position) {
        String ques = quesList.get(position).getQuestion();
        String a = quesList.get(position).getOptionA();
        String b = quesList.get(position).getOptionB();
        String c = quesList.get(position).getOptionC();
        String d = quesList.get(position).getOptionD();
        int selected = quesList.get(position).getSlectedAns();
        int ans = quesList.get(position).getCorrectAns();

        holder.setData(position, ques, a, b, c, d, selected, ans);
    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView quesNo, question, optionA, optionB, optionC, optionD, result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);
        }
        private void setData(int pos, String ques, String a, String b, String c, String d, int selected, int correctAns){
            quesNo.setText("Question No. " + String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. " + a);
            optionB.setText("B. " + b);
            optionC.setText("C. " + c );
            optionD.setText("D. " + d);

            if(selected == -1){
                result.setText("UN-ANSWERED");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
            } else {
                if(selected == correctAns) {
                    result.setText("CORRECT");
                    result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                    setOptioColor(selected, R.color.green);
                }else {
                    result.setText("WRONG");
                    result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                    setOptioColor(selected, R.color.red);
                }
            }
        }

        private void setOptioColor(int selected, int color){
            switch (selected) {
                case 1 :
                    optionA.setTextColor(ContextCompat.getColor(itemView.getContext(),color));
                    break;
                case 2 :
                    optionB.setTextColor(ContextCompat.getColor(itemView.getContext(),color));
                    break;
                case 3 :
                    optionC.setTextColor(ContextCompat.getColor(itemView.getContext(),color));
                    break;
                case 4 :
                    optionD.setTextColor(ContextCompat.getColor(itemView.getContext(),color));
                    break;
                default:
            }
        }
    }


}
