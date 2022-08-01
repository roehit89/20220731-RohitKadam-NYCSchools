package com.example.a20220731_rohitkadam_nycschools.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChaseDataModelHelper {

    private final String TAG = this.getClass().getSimpleName();


    public List<ChaseDataModel> parseResponseJson(JSONArray jsonArray) {
        List<ChaseDataModel> GithubDataModelList = new ArrayList();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                Log.i(TAG + " json array = ", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    ChaseDataModel githubDataModel = new ChaseDataModel();
                    JSONObject eachBusiness = (JSONObject) jsonArray.get(i);
                    if (eachBusiness.has(JsonConstants.NAME.value)) {
                        githubDataModel.setName(eachBusiness.getString(JsonConstants.NAME.value));
                    }
                    if (eachBusiness.has(JsonConstants.HTML_URL.value)) {
                        githubDataModel.setHtmlUrl(eachBusiness.getString(JsonConstants.HTML_URL.value));
                    }
                    if (eachBusiness.has(JsonConstants.STARGAZERS_COUNT.value)) {
                        githubDataModel.setStargazersCount(eachBusiness.getInt(JsonConstants.STARGAZERS_COUNT.value));
                    }
                    GithubDataModelList.add(githubDataModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return GithubDataModelList;
    }

    public List<ChaseDataModel> sortByStargazers(List<ChaseDataModel> tempList, int maxListSize) {
        List<ChaseDataModel> result = new ArrayList<>();
        int max = 0;
        int count = 0;
        ChaseDataModel maxObject = new ChaseDataModel();

        while (count < maxListSize) {
            for (ChaseDataModel githubDataModel : tempList) {
                if (githubDataModel.getStargazersCount() > max) {
                    max = githubDataModel.getStargazersCount();
                    maxObject = githubDataModel;
                }
            }
            count++;
            result.add(maxObject);
            tempList.remove(maxObject);
            max = 0;
        }
        return result;
    }

    enum JsonConstants {
        NAME("name"),
        HTML_URL("html_url"),
        STARGAZERS_COUNT("stargazers_count");

        String value = "";

        JsonConstants(String input) {
            this.value = input;
        }
    }
}
