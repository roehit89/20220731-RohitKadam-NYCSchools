package com.example.a20220731_rohitkadam_nycschools.network;

import static com.example.a20220731_rohitkadam_nycschools.Constants.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jakewharton.rxrelay3.BehaviorRelay;
import com.jakewharton.rxrelay3.Relay;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

public class NetworkLayer {

    private final String TAG = this.getClass().getSimpleName();
    private static NetworkLayer mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private Relay<String> apiChaseDataResponseRelay;

    private NetworkLayer(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        apiChaseDataResponseRelay = BehaviorRelay.create();
    }

    public Flowable<String> getApiChaseDataResponseRelayFlowable() {
        return apiChaseDataResponseRelay.toFlowable(BackpressureStrategy.LATEST);
    }

    public static synchronized NetworkLayer getInstance(Context context) {
        // If instance is not available, create it. If available, reuse and return the object.
        if (mInstance == null) {
            mInstance = new NetworkLayer(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key. It should not be activity context,
            // or else RequestQueue won’t last for the lifetime of your app
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }


    public void initiateNetworkRequest(String orgName) {
        String url = BASE_URL.value;
        makeRequest(mCtx, url, new NetworkLayerCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                Log.i(TAG + " error", result.toString());
                apiChaseDataResponseRelay.accept(result);
            }

            @Override
            public void onError(String result) throws Exception {
                Log.i(TAG + " error", result);
            }
        });
    }

    // Custom JSON Request Handler
    public void makeRequest(Context applicationContext, final String url, final NetworkLayerCallback callback) {

       StringRequest rq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        NetworkLayer.getInstance(mCtx).addToRequestQueue(rq);
    }


}