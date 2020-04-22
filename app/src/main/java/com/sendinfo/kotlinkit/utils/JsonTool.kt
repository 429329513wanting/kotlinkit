package com.ljb.mvp.kotlin.utils

import com.google.gson.Gson

/**
 * Created by L on 2017/7/17.
 */
object JsonTool {

    private val mGson by lazy { Gson() }
    private val googleJsonParser by lazy { com.google.gson.JsonParser() }

    fun <T> fromJsonToObj(json: String, clazz: Class<T>): T = mGson.fromJson(json, clazz)

    fun <T> fromJsonToArr(json: String, clazz: Class<T>): MutableList<T> {
        val result = ArrayList<T>()
        val jsonArray = googleJsonParser.parse(json).asJsonArray
        jsonArray.mapTo(result) { mGson.fromJson(it, clazz) }
        return result
    }
    fun toJson(obj: Any): String{

        return mGson.toJson(obj)
    }

}