package com.sendinfo.kotlinkit.utils


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

object Constant {

    const val BASE_URL:String = "https://www.wanandroid.com"
    const val BASE1_URL:String = "http://220.191.224.192:8085"


    const val BANNER:String = "/banner/json"
    const val LOGIN:String =  "/user/login"
    const val UPLOAD:String = BASE1_URL+"/api/upload/fileUpload.htm"


    object HTTP_METHOD{

        const val GET:String = "GET"
        const val POST:String = "POST"
    }
}