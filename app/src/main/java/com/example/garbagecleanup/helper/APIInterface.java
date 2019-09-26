package com.example.garbagecleanup.helper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * User: Aman
 * Date: 24-09-2019
 * Time: 12:34 AM
 */
public interface APIInterface {
    @Multipart
    @POST("post/upload/")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image,
                                   @Part("title") RequestBody title,
                                   @Part("Description") RequestBody description,
                                   @Part("latitude") double latitude,
                                   @Part("longitude") double longitude,
                                   @Part("created_date") RequestBody created_date,
                                   @Part("published_date") RequestBody published_date,
                                   @Part("author") int author);
}
