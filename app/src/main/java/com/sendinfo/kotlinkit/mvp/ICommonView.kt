package com.sendinfo.kotlinkit.mvp

import com.sendinfo.kotlinkit.http.HttpDto
import com.sendinfo.kotlinkit.http.Response
import mvp.ljb.kt.contract.IViewContract


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

interface ICommonView:IViewContract {

    fun onSuccess(result:Any,httpDto: HttpDto)
}