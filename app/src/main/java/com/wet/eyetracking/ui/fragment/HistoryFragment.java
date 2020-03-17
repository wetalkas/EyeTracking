package com.wet.eyetracking.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wet.eyetracking.model.ExperimentItem;
import com.wet.eyetracking.adapter.HistoryAdapter;
import com.wet.eyetracking.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private HistoryAdapter mAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Realm realm = Realm.getDefaultInstance();
        RealmResults<ExperimentItem> experiments = realm.where(ExperimentItem.class)
                .findAllSorted(ExperimentItem.DATE, Sort.DESCENDING);

        List<ExperimentItem> list = new ArrayList<>();
        list.addAll(experiments);

        mAdapter = new HistoryAdapter(list);

        mRecyclerView.setAdapter(mAdapter);
    }
}
