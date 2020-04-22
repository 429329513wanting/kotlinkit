package com.sendinfo.kotlinkit.http

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

interface HttpTool {

    /**
     * GET请求
     */
    @GET
    fun sendGetRequest(@Url url:String,
                            @HeaderMap headers:Map<String,String>,
                            @QueryMap params: Map<String, String>):Observable<Any>

    /**
     * POST请求
     */
    @FormUrlEncoded
    @POST
    fun sendPostRequest(@Url url:String,
                        @HeaderMap headers:Map<String,String>,
                        @FieldMap params: Map<String, String>):Observable<Any>



    /**
     * POST请求
     * 请求参数直接放到body里
     */
    @FormUrlEncoded
    @POST
    fun sendPostBodyRequest(@Url url:String,
                            @HeaderMap headers:Map<String,String>,
                            @Body body: String):Observable<Any>


    /**
     * 上传图片
     */
    @Multipart
    @POST
    fun upImage(@Url url:String,
                @HeaderMap headers: Map<String,String>,
                @Part params:MultipartBody.Part): Observable<Any>



    /**
     * Multi上传
     */
    @Multipart
    @POST
    fun upMultiPart(@Url url:String,
                    @HeaderMap headers: Map<String,String>,
                    @PartMap params: Map<String, RequestBody>?
    ): Observable<Any>

}