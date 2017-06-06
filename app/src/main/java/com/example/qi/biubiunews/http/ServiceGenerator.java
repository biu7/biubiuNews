package com.example.qi.biubiunews.http;

import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qi on 17-4-25.
 */

public class ServiceGenerator {
    public static final String API_BASE_URL = "http://118.89.231.249/api_v1/";
//    public static final String API_BASE_URL = "http://zzulinews.cn:5000/api_v1/";
//    public static final String API_BASE_URL = "http://192.168.123.48:5000/api_v1/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass){
        return createService(serviceClass,null);
    }
    public static <S> S createService(Class<S> serviceClass,String username,String password){

        if (!TextUtils.isEmpty(username)){
            String authToken = Credentials.basic(username,password);
            return createService(serviceClass,authToken);
        }
        return createService(serviceClass,null);
    }

    public static <S> S createService(Class<S> serviceCliss,final String authToken){
        if(!TextUtils.isEmpty(authToken)){
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);
            if (!httpClient.interceptors().contains(interceptor)){
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceCliss);
    }

}

