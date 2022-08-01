package com.example.a20220731_rohitkadam_nycschools.viewmodel.recyclerview;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a20220731_rohitkadam_nycschools.BR;
import com.example.a20220731_rohitkadam_nycschools.R;
import com.example.a20220731_rohitkadam_nycschools.model.ChaseDataModel;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<ChaseDataModel> githubRepositoryList;
    String TAG = RecyclerViewAdapter.class.getSimpleName();
    RecyclerViewCallback recyclerViewCallback;

    public RecyclerViewAdapter(RecyclerViewCallback callback) {
        githubRepositoryList = new ArrayList<>();
        recyclerViewCallback = callback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, " onCreateViewHolder called ");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding view = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_repository, parent, false);
        view.setVariable(BR.viewModel,recyclerViewCallback);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i(TAG, " onBindViewHolder called " + position);
        ChaseDataModel githubDataModel = githubRepositoryList.get(position);
        holder.set(githubDataModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewCallback.onClick(githubDataModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount =" + githubRepositoryList.size());
        return githubRepositoryList.size();
    }

    public void setChaseList(List<ChaseDataModel> list) {
        if (list != null) {
            Log.i(TAG, "original list cleared");
            this.githubRepositoryList.clear();
            this.githubRepositoryList.addAll(list);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView stargazersTextView;

        public MyViewHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());
            Log.i(TAG, " MyViewHolder called ");
            nameTextView = itemView.getRoot().findViewById(R.id.tv_repository_name);
            stargazersTextView = itemView.getRoot().findViewById(R.id.tv_stargazers_count);
        }

        public void set(ChaseDataModel githubDataModel) {
            Log.i(TAG, " MyViewHolder called " + githubDataModel.getName());
            nameTextView.setText(githubDataModel.getName());
            stargazersTextView.setText(String.valueOf(githubDataModel.getStargazersCount()));
        }
    }
}