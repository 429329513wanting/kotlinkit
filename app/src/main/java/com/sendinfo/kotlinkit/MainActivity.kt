package com.sendinfo.kotlinkit

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ActivityUtils
import com.sendinfo.kotlinkit.base.BaseMvpActivity
import com.sendinfo.kotlinkit.demo.TestActivity

import com.sendinfo.kotlinkit.mvp.HttpPresenter
import com.sendinfo.kotlinkit.mvp.IPresenter
import com.tbruyelle.rxpermissions2.Permission


class MainActivity : BaseMvpActivity<IPresenter>() {


    override fun getLayoutId() = R.layout.activity_main

    override fun registerPresenter()= HttpPresenter::class.java

    override fun initArgs(intent: Intent) {

    }

    override fun initView() {

        checkPermission(object : CheckPermissionListener {
            override fun onPermissionBack(permission: Permission) {

            }
        },android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

    }
    @OnClick(R.id.he_tv)
    public fun viewClick(view:View){

        ActivityUtils.startActivity(TestActivity::class.java)
    }

    override fun initData() {


    }
    

}
