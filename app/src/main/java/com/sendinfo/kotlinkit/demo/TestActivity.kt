package com.sendinfo.kotlinkit.demo

import android.content.Intent
import com.sendinfo.kotlinkit.R
import com.sendinfo.kotlinkit.R2.id.he_tv
import com.sendinfo.kotlinkit.base.BaseMvpActivity
import com.sendinfo.kotlinkit.base.BaseMvpFragment
import com.sendinfo.kotlinkit.http.HttpDto
import com.sendinfo.kotlinkit.http.Response
import com.sendinfo.kotlinkit.mvp.HttpPresenter
import com.sendinfo.kotlinkit.mvp.ICommonView
import com.sendinfo.kotlinkit.mvp.IPresenter
import com.sendinfo.kotlinkit.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*

class TestActivity : BaseMvpActivity<IPresenter>() {

    override fun initArgs(intent: Intent) {
    }

    override fun getLayoutId() = R.layout.activity_test
    override fun registerPresenter()= HttpPresenter::class.java


    override fun initView() {

        supportFragmentManager.beginTransaction().add(R.id.container, TestFragment()).commit()
    }

    override fun initData() {

    }
}
