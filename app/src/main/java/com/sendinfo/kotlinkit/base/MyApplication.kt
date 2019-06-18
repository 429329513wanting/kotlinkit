package com.sendinfo.kotlinkit.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils

import com.sendinfo.kotlinkit.utils.Constant
import net.ljb.kt.HttpConfig

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import java.util.LinkedList

/**
 * Created by ghwang on 2018/1/12.
 */

class MyApplication : Application() {


    override fun attachBaseContext(base: Context) {

        super.attachBaseContext(base)
        MultiDex.install(this)

    }

    override fun onCreate() {
        super.onCreate()

        activityLinkedList = LinkedList()
        instance = this
        context = applicationContext
        Utils.init(this)


        initHttp()

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                activityLinkedList!!.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {

            }


            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

                LogUtils.d("onActivityDestroyed: " + activity.localClassName)
                activityLinkedList!!.remove(activity)
                // 在Activity结束时（Destroyed（）） 写出Activity实例
            }
        })

        //使用网址https://www.jianshu.com/p/72494773aace
        Utils.init(this)
        //        if (ActivityCompat.checkSelfPermission(this,
        //                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        //            // TODO: Consider calling
        //            //    ActivityCompat#requestPermissions
        //            // here to request the missing permissions, and then overriding
        //            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //            //                                          int[] grantResults)
        //            // to handle the case where the user grants the permission. See the documentation
        //            // for ActivityCompat#requestPermissions for more details.
        //            return;
        //        }
        //        CrashUtils.init();

    }

    private inner class SafeHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

    fun exitApp() {

        // 先打印当前容器内的Activity列表
        for (activity in activityLinkedList!!) {
            LogUtils.d(activity.localClassName)
        }

        LogUtils.d("正逐步退出容器内所有Activity")

        // 逐个退出Activity
        for (activity in activityLinkedList!!) {
            activity.finish()
        }

        //  结束进程
        System.exit(0)
    }

    private fun initHttp() {

        HttpConfig.init(Constant.BASE_URL, mapOf(), mapOf(), false)
    }

    companion object {

        lateinit var instance: MyApplication
        var context: Context? = null
        private var activityLinkedList: LinkedList<Activity>? = null
    }
}

