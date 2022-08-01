package com.example.a20220731_rohitkadam_nycschools.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.a20220731_rohitkadam_nycschools.model.ChaseDataModel;
import com.example.a20220731_rohitkadam_nycschools.model.ChaseDataModelHelper;
import com.example.a20220731_rohitkadam_nycschools.network.NetworkLayer;
import com.example.a20220731_rohitkadam_nycschools.viewmodel.recyclerview.RecyclerViewCallback;

import org.json.JSONArray;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChaseViewModel extends AndroidViewModel implements RecyclerViewCallback {

    ChaseDataModelHelper chaseDataModelHelper;
    private CompositeDisposable compositeDisposable;
    private NetworkLayer networkLayer;
    private MutableLiveData<List<ChaseDataModel>> githubDataModelListMutable;
    private MutableLiveData<String> mutableLiveUrl;
    String organisationName = "";
    final private String title = "Enter organisation name";
    final private String titleHint = "Google";
    final private String stargazersLabel = "Stargazers";

    private final String TAG = this.getClass().getSimpleName();

    public ChaseViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        chaseDataModelHelper = new ChaseDataModelHelper();
        githubDataModelListMutable = new MutableLiveData<>();
        mutableLiveUrl = new MutableLiveData<>();
        initializeVolley(application);
    }

    private void initializeVolley(Context applicationContext) {
        Log.i(TAG, "initiateNetworkRequest() called");

        networkLayer = NetworkLayer.getInstance(applicationContext);
        compositeDisposable.add(networkLayer.getApiChaseDataResponseRelayFlowable()
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::acceptRemoteResponse));
    }

    public MutableLiveData<List<ChaseDataModel>> getGithubDataModelListMutable(){
        return githubDataModelListMutable;
    }

    public MutableLiveData<String> getMutableLiveUrl() {
        return mutableLiveUrl;
    }


    public String getTitle() {
        return title;
    }

    public String getTitleHint() {
        return titleHint;
    }


    public String getStargazersLabel() {
        return stargazersLabel;
    }

    private void acceptRemoteResponse(String jsonArray) {
        Log.i(" zebra ",jsonArray);
        Log.i(" zebra ",convertStringToDocument(jsonArray).toString());
        //List<ChaseDataModel> tempList = chaseDataModelHelper.parseResponseJson(jsonArray);

//        List<ChaseDataModel> finalList = chaseDataModelHelper.sortByStargazers(tempList, 3);
//        githubDataModelListMutable.postValue(finalList);
    }

    private static InputStream convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String value) {
        this.organisationName = value;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setOrganisationName(s.toString());
    }

    public void onSearchClicked(View view) {
        Log.i(TAG, " searched with " + getOrganisationName());
        githubDataModelListMutable.postValue(new ArrayList<>());
        networkLayer.initiateNetworkRequest(getOrganisationName());
    }

    @Override
    public void onClick(ChaseDataModel chaseDataModel) {
        Log.i(TAG, chaseDataModel.getHtmlUrl());
        mutableLiveUrl.postValue(chaseDataModel.getHtmlUrl());
    }

}

