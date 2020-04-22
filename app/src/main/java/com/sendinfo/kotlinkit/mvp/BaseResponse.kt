package com.sendinfo.kotlinkit.mvp


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

data class BaseResponse(var data:Any,
                        var errorCode:Int,
                        var errorMsg:String,
                        var code:Int,
                        var success: Boolean,
                        var message: String)