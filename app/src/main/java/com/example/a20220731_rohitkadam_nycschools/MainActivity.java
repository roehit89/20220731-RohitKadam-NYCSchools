package com.example.a20220731_rohitkadam_nycschools;

import static com.example.a20220731_rohitkadam_nycschools.Constants.WEBVIEW_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.a20220731_rohitkadam_nycschools.databinding.ActivityMainBinding;
import com.example.a20220731_rohitkadam_nycschools.model.ChaseDataModel;
import com.example.a20220731_rohitkadam_nycschools.viewmodel.ChaseViewModel;
import com.example.a20220731_rohitkadam_nycschools.viewmodel.recyclerview.RecyclerViewAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    ChaseViewModel chaseViewModel;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chaseViewModel = new ViewModelProvider(this).get(ChaseViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(chaseViewModel);
        activityMainBinding.setLifecycleOwner(this);
        recyclerView = activityMainBinding.recViewId;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewAdapter = new RecyclerViewAdapter(chaseViewModel);

        recyclerView.setAdapter(recyclerViewAdapter);

        chaseViewModel.getGithubDataModelListMutable().observe(this, new Observer<List<ChaseDataModel>>() {
            @Override
            public void onChanged(List<ChaseDataModel> list) {
                recyclerViewAdapter.setChaseList(list);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        chaseViewModel.getMutableLiveUrl().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                openWebview(s);
            }
        });
    }

    private void openWebview(String s) {
//        Bundle bundle = new Bundle();
//        bundle.putString(WEBVIEW_URL.value, s);
//        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }
}