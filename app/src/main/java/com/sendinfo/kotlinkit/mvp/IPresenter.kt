package com.sendinfo.kotlinkit.mvp

import com.sendinfo.kotlinkit.http.HttpDto
import mvp.ljb.kt.contract.IPresenterContract


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

interface IPresenter:IPresenterContract {

    fun getData(httpDto: HttpDto)
    fun canCelRequest()

}