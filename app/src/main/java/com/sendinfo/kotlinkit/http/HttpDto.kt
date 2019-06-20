package com.sendinfo.kotlinkit.http

import com.blankj.utilcode.util.LogUtils
import com.ljb.mvp.kotlin.utils.JsonParser
import com.sendinfo.kotlinkit.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

class HttpDto(url:String) {

    var method:String
    var url:String
    var params:Map<String,String>
    var multiParams:Map<String,String>? = null
    var partBody:MultipartBody.Part? = null
    var headers:Map<String,String>? = null
    var isUploadImage:Boolean = false

    var bodyString:String
    lateinit var fullUrl:String

    constructor(url: String,slience:Boolean):this(url){

        this.url = url
        this.slience = slience
    }

    var slience:Boolean = false

    init {

        this.url = url
        this.method = Constant.HTTP_METHOD.GET
        this.bodyString = ""
        this.params = mapOf()
        this.headers = mapOf()
    }

    fun print(){

        var surl:String? = null
        if (this.url.startsWith("http")){
            surl = this.url
        }else{

            surl = Constant.BASE_URL+this.url
        }
        fullUrl = surl
        LogUtils.d("请求URL:\n"+surl+
                "\n"+this.method +
                "\n请求参数:\n"+JsonParser.toJson(this.params)+
                "\n\n"+
                "\nheaders:"+this.headers
        )
    }
}