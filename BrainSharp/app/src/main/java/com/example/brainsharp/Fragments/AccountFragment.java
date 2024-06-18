package com.example.brainsharp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brainsharp.Database.DBQuery;
import com.example.brainsharp.LoginActivity;
import com.example.brainsharp.MainActivity;
import com.example.brainsharp.MyCompleteListener;
import com.example.brainsharp.MyProfileActivity;
import com.example.brainsharp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private LinearLayout logoutBtn;
    private TextView profileText, name, score, rank;
    private LinearLayout leaderBtn, profileBtn;
    private Dialog progress_Dialog;
    private TextView dialogText;
    private ImageView profileImg;

    private BottomNavigationView bottomNavigationView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        initViews(view);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        String username = DBQuery.myProfile.getName();
        profileText.setText(username.toUpperCase().substring(0,1));
        name.setText(username);
        score.setText(String.valueOf(DBQuery.myPerformance.getScore()));

        progress_Dialog = new Dialog(getContext());
        progress_Dialog.setContentView(R.layout.dialog_layout);
        progress_Dialog.setCancelable(false);
        progress_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progress_Dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Fetching Data ...");

        if(DBQuery.g_userlist.size() == 0){
            progress_Dialog.show();
            DBQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if(DBQuery.myPerformance.getScore() != 0) {
                        if(!DBQuery.isCurrentUserOnTopList) {
                            calculateRank();
                        }
                        score.setText("Score : " + DBQuery.myPerformance.getScore());
                        rank.setText("Rank - " + DBQuery.myPerformance.getRank());

                    }
                    progress_Dialog.dismiss();
                }

                @Override
                public void onFailure() {
                    progress_Dialog.dismiss();
                    Toast.makeText(getContext(), "Some went Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            score.setText("Score : " + DBQuery.myPerformance.getScore());
            if (DBQuery.myPerformance.getRank() != 0)
                rank.setText("Rank - " + DBQuery.myPerformance.getRank());
        }
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        leaderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
            }
        });

        return view;
    }

    private void initViews(View view) {
        logoutBtn = view.findViewById(R.id.logoutBtn);
        profileText = view.findViewById(R.id.profile_txt);
        profileImg = view.findViewById(R.id.profile_img);
        name = view.findViewById(R.id.profile_name);
        rank = view.findViewById(R.id.profile_rank);
        score = view.findViewById(R.id.profile_score);
        leaderBtn = view.findViewById(R.id.profile_leaderboardBtn);
        profileBtn = view.findViewById(R.id.profileBtn);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);

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