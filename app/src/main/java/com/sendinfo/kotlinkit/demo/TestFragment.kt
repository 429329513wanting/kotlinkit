package com.sendinfo.kotlinkit.demo

import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.kotlinkit.R
import com.sendinfo.kotlinkit.base.BaseMvpFragment
import com.sendinfo.kotlinkit.http.HttpDto
import com.sendinfo.kotlinkit.mvp.BaseResponse
import com.sendinfo.kotlinkit.mvp.HttpPresenter
import com.sendinfo.kotlinkit.mvp.ICommonView
import com.sendinfo.kotlinkit.mvp.IPresenter
import com.sendinfo.kotlinkit.utils.Constant
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/14
 *     desc   :
 * </pre>
 */

class TestFragment: BaseMvpFragment<IPresenter>(),ICommonView {


    override fun registerPresenter() = HttpPresenter::class.java

    override fun initView(bundle: Bundle?) {

        setContentView(R.layout.fragment_test_layout)
    }

    override fun initData() {

        showSweetDialog(SweetAlertDialog.SUCCESS_TYPE,"提示",
            "测试",
            "确定","取消",
            SweetAlertDialog.OnSweetClickListener {

                var dto1 = HttpDto(Constant.LOGIN)
                dto1.params = mapOf("username" to "system","password" to EncryptUtils.encryptMD5ToString("1").toUpperCase(),"clientType" to "pc")
                getPresenter().getData(dto1)


            },
            SweetAlertDialog.OnSweetClickListener {

                it.dismiss()

            })
    }

    override fun initArgs(bundle: Bundle) {

    }

    override fun onSuccess(result: BaseResponse, httpDto: HttpDto) {

        LogUtils.d(result.data,httpDto.url)
    }
}