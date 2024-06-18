package com.example.brainsharp.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brainsharp.Adapters.RankAdapter;
import com.example.brainsharp.Database.DBQuery;
import com.example.brainsharp.MainActivity;
import com.example.brainsharp.MyCompleteListener;
import com.example.brainsharp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView totalUsersTV, myImgTxt, myScoreTV, myRankTV, myName;
    private Dialog progress_Dialog;
    private TextView dialogText;

    private RecyclerView usersView;

    private RankAdapter adapter;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_leaderboard, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Leaderboard");

        initViews(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DBQuery.g_userlist);
        usersView.setAdapter(adapter);

        progress_Dialog = new Dialog(getContext());
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Fetching Data ...");
        progress_Dialog.show();

        DBQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                    if(!DBQuery.isCurrentUserOnTopList) {
                        calculateRank();
                    }
                    myScoreTV.setText("Score : " + DBQuery.myPerformance.getScore());
                    myRankTV.setText("Rank - " + DBQuery.myPerformance.getRank());
                    myName.setText(DBQuery.myPerformance.getName());



                progress_Dialog.dismiss();
            }

            @Override
            public void onFailure() {
                progress_Dialog.dismiss();
                Toast.makeText(getContext(), "Some went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        totalUsersTV.setText("Total Users: " +  DBQuery.g_usersCount );
        myImgTxt.setText(DBQuery.myPerformance.getName().toUpperCase().substring(0,1));
        return view;
    }

    private void initViews(View view) {
        totalUsersTV = view.findViewById(R.id.total_users);
        myScoreTV = view.findViewById(R.id.total_score);
        myRankTV = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.userView);
        myName = view.findViewById(R.id.name);
        myImgTxt =view.findViewById(R.id.imgTxt);

    }

    private void calculateRank() {
        int lowTopScore = DBQuery.g_userlist.get(DBQuery.g_userlist.size() - 1).getScore();
        int remaining_slots = DBQuery.g_usersCount - 20;
        int myslot = (DBQuery.myPerformance.getScore() * remaining_slots) / lowTopScore ;
        int rank;
        if(lowTopScore != DBQuery.myPerformance.getScore()) {
            rank =DBQuery.g_usersCount - myslot;
        }else  {
            rank = 21;
        }
        DBQuery.myPerformance.setRank(rank);
    }
}