package com.sendinfo.kotlinkit.demo

import android.content.Intent
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.ToastUtils
import com.ljb.mvp.kotlin.utils.JsonTool
import com.sendinfo.kotlinkit.R
import com.sendinfo.kotlinkit.base.BaseMvpActivity
import com.sendinfo.kotlinkit.http.EventResp
import com.sendinfo.kotlinkit.http.HttpDto
import com.sendinfo.kotlinkit.mvp.BaseResponse
import com.sendinfo.kotlinkit.mvp.HttpPresenter
import com.sendinfo.kotlinkit.mvp.ICommonView
import com.sendinfo.kotlinkit.mvp.IPresenter
import com.sendinfo.kotlinkit.utils.Constant
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

class TestActivity : BaseMvpActivity<IPresenter>(),ICommonView {

    var datas = arrayListOf<String>()
    val datas2 = arrayOf("1","2")
    var data3 = listOf<String>()
    var data4 = mutableListOf<String>()

    var map1 = mapOf<String,String>()
    var map2 = hashMapOf<String,String>()
    var map3 = mutableMapOf<String,String>()


    override fun initArgs(intent: Intent) {

    }
    override fun getLayoutId() = R.layout.activity_test
    override fun registerPresenter()= HttpPresenter::class.java

    override fun initView() {

        test_tv.setOnClickListener(object: View.OnClickListener{

            override fun onClick(v: View?) {

                var dto1 = HttpDto(Constant.LOGIN)
                dto1.params = mapOf("username" to "system","password" to EncryptUtils.encryptMD5ToString("1").toUpperCase(),"clientType" to "pc")
                getPresenter().getData(dto1)
            }
        })
    }
    @OnClick(R.id.test2_tv)
    fun viewClick(v: View){

        when(v.id){

            R.id.test2_tv -> {

                //up image
                var dto2 = HttpDto(Constant.UPLOAD)
                dto2.isUploadImage = true
                var file = File("/sdcard/icon_120@2x.png")
                val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestFile)
                dto2.partBody = body
                getPresenter().getData(dto2)
            }
        }
    }

    override fun initData() {

    }
    override fun onSuccess(result: BaseResponse, httpDto: HttpDto) {

        ToastUtils.showLong(JsonTool.toJson(result.data))
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventMsg(event: EventResp){

        ToastUtils.showLong(event.content)
    }
}
