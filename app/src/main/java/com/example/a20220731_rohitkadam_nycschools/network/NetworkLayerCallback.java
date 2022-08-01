package com.example.a20220731_rohitkadam_nycschools.network;

import org.json.JSONArray;
import org.json.JSONException;

public interface NetworkLayerCallback {

    void onSuccess(String result) throws JSONException;

    void onError(String result) throws Exception;
}

