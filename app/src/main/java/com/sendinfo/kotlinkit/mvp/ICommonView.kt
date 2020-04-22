package com.sendinfo.kotlinkit.mvp

import com.sendinfo.kotlinkit.http.HttpDto
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

    fun onSuccess(result:BaseResponse,httpDto: HttpDto)
}