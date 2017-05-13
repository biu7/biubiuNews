package com.example.qi.biubiunews.callback;

import retrofit2.Response;

/**
 * Created by qi on 17-5-5.
 */

public interface HttpCallback {
    void onResponse(Response response);
    void onFailure(Throwable t);
}
