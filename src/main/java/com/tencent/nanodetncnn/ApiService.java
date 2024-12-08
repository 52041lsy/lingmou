package com.tencent.nanodetncnn;
import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;


public class ApiService {
    public interface Register{
        @FormUrlEncoded
        @POST("user/add")
        Call<Boolean> toRegister(
                @Field("id") String userid,
                @Field("password") String password,
                @Field("phonenumber") String phonenumber,
                @Field("type") int type
        );

    }

    public interface Upload{
        @Multipart
        @POST("/")
        Call<Boolean> upload(
                @Part MultipartBody.Part image
        );
    }

    public interface Test{
        @GET("/hello")
        Call<Boolean> test(
        );
    }

    public interface Login{
        @GET("user/get")
        Call<User> toLogin(
                @Query("id") String userid
                //@Query("password") String password
        );
    }

    public interface Volunteer_cnt{
        @GET("user/Volnum")
        Call<Integer> Count();
    }

    public interface Patient_cnt{
        @GET("user/Patnum")
        Call<Integer> Count();
    }


    public interface Call_v{
        @GET("user/Phone")
        Call<String> GetPhone();
    }


    public interface GetArticle{
        @GET("article/get")
        Call<List<News>> getArticle(
        );
    }


}
