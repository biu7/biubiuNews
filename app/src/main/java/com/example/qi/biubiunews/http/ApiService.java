package com.example.qi.biubiunews.http;

import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.Comment;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.models.Token;
import com.example.qi.biubiunews.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by qi on 17-4-25.
 */

public interface ApiService {
    /*
        用户类api_v1
     */

    //获取token
    @GET("token")
    Call<Token> get_token();

    //获取手机验证码
    @GET("auth/confirm-sms")
    Call<Void> get_regist_sms(@Query("phonenum") String phonenum);

    //验证手机验证码
    @FormUrlEncoded
    @POST("auth/verify-confirm-code")
    Call<Void> verify_regist_code(@Field("phonenum") String phonenum,
                                  @Field("verify_code") String code,
                                  @Field("password") String password,
                                  @Field("name") String name,
                                  @Field("city") String city,
                                  @Field("about_me") String about_me);

    //上传用户头像
    @Multipart
    @POST("user/icon")
    Call<User> upload_user_icon(@Part MultipartBody.Part file);



    //用户资料
    @GET("user/profile")
    Call<User> get_self_info();

    //修改用户资料
    @FormUrlEncoded
    @PUT("user/profile")
    Call<User> edit_self_profile(@Field("name") String name,
                                 @Field("location") String location,
                                 @Field("about_me") String about_me);

    //指定用户的资料
    @GET("user/{id}")
    Call<User> get_user_info(@Path("id") int id);

    //修改指定用户的资料
    @FormUrlEncoded
    @PUT("user/{id}/profile")
    Call<Void> edit_user_profile(@Path("id") int id,
                                 @Field("name") String name,
                                 @Field("location") String location,
                                 @Field("about_me") String about_me);

    //修改用户密码
    @FormUrlEncoded
    @PUT("user/change-password")
    Call<Void> change_self_password(@Field("password") String password,
                                    @Field("new_password") String new_password);

    //修改指定用户密码
    @FormUrlEncoded
    @PUT("user/{id}/change-password")
    Call<Void> change_user_password(@Path("id") int id,
                                    @Field("password") String password);

    //获取重置密码验证码
    @GET("auth/reset-sms")
    Call<Void> get_reset_sms(@Query("phonenum") String phonenum);
    
    //验证重置密码验证码
    @FormUrlEncoded
    @PUT("auth/verify-reset-code")
    Call<Void> verify_reset_code(@Field("phonenum") String phonenum,
                                 @Field("verify_code") String code,
                                 @Field("password") String password);

    //获取当前用户发表的文章
    @GET("user/news")
    Call<List<News>> get_self_news(@Query("page") int page);

    //获取指定用户发表的文章
    @GET("user/{id}/news")
    Call<List<News>> get_user_news(@Path("id") int id);

    //获取登录用户的时间线
    @GET("user/timeline")
    Call<List<News>> get_timeline(@Query("page") int page);

    //获取当前用户关注列表
    @GET("user/followed")
    Call<List<User>> get_self_followed();

    //获取指定用户的关注列表
    @GET("user/{id}/followed")
    Call<List<User>> get_user_followed(@Path("id") int id);

    //获取当前用户的粉丝列表
    @GET("user/follower")
    Call<List<User>> get_self_follower();

    //获取指定用户的粉丝列表
    @GET("user/{id}/follower")
    Call<List<User>> get_user_follower(@Path("id") int id);

    //关注用户
    @GET("user/follow/{id}")
    Call<Void> follow(@Path("id") int id);

    //取消关注
    @GET("user/unfollow/{id}")
    Call<Void> unfollow(@Path("id") int id);

    /*
        新闻类api_v1
    */
    //获取新闻源
    @GET("site-list")
    Call<List<Site>> get_site_list();

    //获取新闻分类
    @GET("category-list")
    Call<List<Category>> get_category_list();

    //获取新闻列表
    @GET("news-list")
    Call<List<News>> get_news_list(@Query("category") int category,
                                   @Query("page") int page);

    //获取单个新闻
    @GET("news/{id}")
    Call<News> get_news_info(@Path("id") int id);

    //新建新闻
    @POST("news")
    @FormUrlEncoded
    Call<News> new_news(@Field("title") String title,
                        @Field("content") String content);

    //编辑新闻
    @PUT("news/{id}")
    @FormUrlEncoded
    Call<News> edit_news(@Path("id") int id,
                         @Field("content") String content);

    //删除新闻
    @DELETE("news/{id}")
    Call<Void> delete_news(@Path("id") int id);

    /*
        评论类api_v1
     */
    //获取评论列表
    @GET("news/{news_id}/comment-list")
    Call<List<Comment>> get_comment_list(@Path("news_id") int id);

    //获取单个评论
    @GET("comment/{id}")
    Call<Comment> get_comment_info(@Path("id") int id);

    //新建评论
    @POST("comment")
    @FormUrlEncoded
    Call<Comment> new_comment(@Field("news_id") int news_id,
                              @Field("content") String content);

    //删除评论
    @DELETE("comment/{id}")
    Call<Void> delete_comment(@Path("id") int id);

}

