package com.sendinfo.kotlinkit.http

import com.blankj.utilcode.util.LogUtils
import com.ljb.mvp.kotlin.utils.JsonTool
import com.sendinfo.kotlinkit.utils.Constant
import okhttp3.MultipartBody


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
    var fullUrl: String

    var params:Map<String,String>
    var multiParams:Map<String,String>? = null
    var bodyString:String
    //上传图片
    var isUploadImage:Boolean = false
    var partBody:MultipartBody.Part? = null

    var headers:Map<String,String>? = null
    var slience:Boolean = false

    constructor(url: String,slience:Boolean):this(url){

        this.url = url
        this.slience = slience
    }


    init {

        this.url = url
        this.fullUrl = Constant.BASE1_URL+this.url
        this.method = Constant.HTTP_METHOD.POST
        this.bodyString = ""
        this.params = mapOf()
        this.headers = mapOf()
    }

    fun print(){

        LogUtils.d("请求URL:\n"+fullUrl+
                "\n"+this.method +
                "\n请求参数:\n"+JsonTool.toJson(this.params)+
                "\n\n"+
                "\nheaders:"+this.headers
        )
    }
}