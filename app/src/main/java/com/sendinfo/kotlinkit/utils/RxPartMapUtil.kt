package com.sendinfo.kotlinkit.utils

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/20
 *     desc   :
 * </pre>
 */

object  RxPartMapUtil {

    private fun toRequestBodyOfText(value: String) = RequestBody.create(MediaType.parse("text/plain"),value)

    private fun toRequestBodyOfImage(file: File) = RequestBody.create(MediaType.parse("image/*"),file)

    fun changeToBodyMap(params: Map<String,String>): Map<String,RequestBody>{


        var result = mutableMapOf<String, RequestBody>()

        params.keys.forEach { it

            if (it.contains("file")){

                params[it]?.let { it1 -> toRequestBodyOfImage(File(it1))}?.let { it2 ->

                    result.put("file\"; filename=\""+"xx.png"+"", it2)
                }

            }else{

                params[it]?.let { it1 -> toRequestBodyOfText(it1) }?.let { it2 -> result.put(it, it2) }

            }
        }

        return result
    }

}