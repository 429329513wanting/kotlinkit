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

interface HttpAPI {

    /**
     * GET请求
     * encode=true 防止url转义
     */
    @GET("{path}")
    fun sendGetRequest(@Path("path",encoded = true) path:String,
                       @HeaderMap headers:Map<String,String>,
                       @QueryMap params:Map<String,String>):Observable<Any>


    /**
     * POST请求
     */
    @FormUrlEncoded
    @POST("{path}")
    fun sendPostRequest(@Path("path",encoded = true) path:String,
                        @HeaderMap headers:Map<String,String>,
                        @FieldMap params: Map<String, String>):Observable<Any>



    /**
     * POST请求
     * 参数直接放到请求body里
     */
    @FormUrlEncoded
    @POST("{path}")
    fun sendPostBodyRequest(@Path("path",encoded = true) path:String,
                        @HeaderMap headers:Map<String,String>,
                        @Body body:String):Observable<Any>


    /**
     * GET请求
     * @Url如果地址含有完整的请求路径,则会替换掉最初的BaseURL
     */
    @GET
    fun sendFullGetRequest(@Url url:String,
                            @HeaderMap headers:Map<String,String>,
                            @QueryMap params: Map<String, String>):Observable<Any>

    /**
     * POST请求
     * @Url如果地址含有完整的请求路径,则会替换掉最初的BaseURL
     */
    @FormUrlEncoded
    @POST
    fun sendFullPostRequest(@Url url:String,
                        @HeaderMap headers:Map<String,String>,
                        @FieldMap params: Map<String, String>):Observable<Any>



    /**
     * POST请求
     * @Url如果地址含有完整的请求路径,则会替换掉最初的BaseURL
     * 请求参数直接放到body里
     */
    @FormUrlEncoded
    @POST
    fun sendFullPostBodyRequest(@Url url:String,
                            @HeaderMap headers:Map<String,String>,
                            @Body body: String):Observable<Any>


    /**
     * 上传图片
     *
     */
    @Multipart
    @POST("{path}")
    fun upImage(@Path("path",encoded = true) path:String,
                @HeaderMap headers: Map<String,String>,
                @Part params:MultipartBody.Part): Observable<Any>



}