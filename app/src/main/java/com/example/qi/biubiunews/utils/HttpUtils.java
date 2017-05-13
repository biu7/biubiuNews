package com.example.qi.biubiunews.utils;

import android.content.Context;
import android.util.Log;

import com.example.qi.biubiunews.BiuApplication;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.http.ApiService;
import com.example.qi.biubiunews.http.ServiceGenerator;
import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.Comment;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.models.Token;
import com.example.qi.biubiunews.models.User;

import java.io.File;
import java.net.IDN;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

/**
 * Created by qi on 17-5-5.
 */

public class HttpUtils {
    private  String token;
    private ApiService apiService;
    public HttpUtils(Context context) {
        token = Utils.loadToken(context).getToken();
        Log.i("token",token);
        apiService = ServiceGenerator.createService(ApiService.class,token,"");
    }


    public void upload_icon(File file,final HttpCallback httpCallback){
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),imageBody);

        Call<User> call = apiService.upload_user_icon(body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });

    }

    public void get_token(final HttpCallback httpCallback){
        Call<Token> call = apiService.get_token();
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }


    public void get_self_profile(final HttpCallback httpCallback){
        Call<User> call = apiService.get_self_info();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });

    }

    public void edit_self_profile(User user, final HttpCallback httpCallback){
        Call<User> call = apiService.edit_self_profile(user.getName(),user.getLocation(),user.getAbout_me());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_user_info(int user_id, final HttpCallback httpCallback){
        Call<User> call = apiService.get_user_info(user_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void edit_user_profile(User user, final HttpCallback httpCallback){
        Call<Void> call = apiService.edit_user_profile(user.getId(),user.getName(),user.getLocation(),user.getAbout_me());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });

    }

    public void change_self_password(String password, String new_password, final HttpCallback httpCallback){
        Call<Void> call = apiService.change_self_password(password,password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void change_user_password(int id, String password, final HttpCallback httpCallback){
        Call<Void> call = apiService.change_user_password(id,password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_self_news(int page ,final HttpCallback httpCallback){
        Call<List<News>> call = apiService.get_self_news(page);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });

    }

    public void get_user_news(int id, final HttpCallback httpCallback){
        Call<List<News>> call = apiService.get_user_news(id);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_timeline(int page, final HttpCallback httpCallback){
        Call<List<News>> call = apiService.get_timeline(page);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                httpCallback.onResponse(response);

            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {

                httpCallback.onFailure(t);
            }
        });

    }

    public void get_self_followed(final HttpCallback httpCallback){
        Call<List<User>> call = apiService.get_self_followed();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_user_followed(int id, final HttpCallback httpCallback){
        Call<List<User>> call = apiService.get_user_followed(id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_self_follower(final HttpCallback httpCallback){
        Call<List<User>> call = apiService.get_self_follower();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_user_follower(int id, final HttpCallback httpCallback){
        Call<List<User>> call = apiService.get_user_follower(id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void follow(int id , final HttpCallback httpCallback){
        Call<Void> call = apiService.follow(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void unfollow(int id, final HttpCallback httpCallback){
        Call<Void> call = apiService.unfollow(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });

    }

    public void get_site_list(final HttpCallback httpCallback){
        Call<List<Site>> call = apiService.get_site_list();
        call.enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_category_list(final HttpCallback httpCallback){
        Call<List<Category>> call = apiService.get_category_list();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_news_list(int id, int page, final HttpCallback httpCallback){
        Call<List<News>> call = apiService.get_news_list(id,page);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_news_info(int id, final HttpCallback httpCallback){
        Call<News> call = apiService.get_news_info(id);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void new_news(News news, final HttpCallback httpCallback){
        Call<News> call = apiService.new_news(news.getTitle(),news.getContent());
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void edit_news(News news, final HttpCallback httpCallback){
        Call<News> call = apiService.edit_news(news.getId(),news.getContent());
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void delete_news(int id, final HttpCallback httpCallback){
        Call<Void> call = apiService.delete_news(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void get_comment_list(int id, final HttpCallback httpCallback){
        Call<List<Comment>> call = apiService.get_comment_list(id);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void new_comment(Comment comment, final HttpCallback httpCallback){
        Call<Comment> call = apiService.new_comment(comment.getNews_id(),comment.getContent());
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

    public void delete_comment(int id, final HttpCallback httpCallback){
        Call<Void> call = apiService.delete_comment(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpCallback.onResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                httpCallback.onFailure(t);
            }
        });
    }

}
