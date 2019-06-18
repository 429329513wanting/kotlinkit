package com.sendinfo.kotlinkit.http


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

data class Response(var data:Any,
                    var errorCode:Int,
                    var errorMsg:String)