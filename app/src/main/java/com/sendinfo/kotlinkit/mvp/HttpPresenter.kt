package com.sendinfo.kotlinkit.mvp

import android.text.TextUtils
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blankj.utilcode.util.LogUtils
import com.google.gson.internal.LinkedTreeMap
import com.ljb.mvp.kotlin.utils.JsonParser
import com.ljb.mvp.kotlin.utils.RxUtils
import com.sendinfo.kotlinkit.base.BaseMvpActivity
import com.sendinfo.kotlinkit.base.BaseMvpFragment
import com.sendinfo.kotlinkit.http.HttpAPI
import com.sendinfo.kotlinkit.http.HttpDto
import com.sendinfo.kotlinkit.http.Response
import com.sendinfo.kotlinkit.utils.Constant
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.presenter.BaseMvpPresenter
import net.ljb.kt.client.HttpFactory


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2019/06/13
 *     desc   :
 * </pre>
 */

class HttpPresenter: BaseMvpPresenter<ICommonView>(),IPresenter {

    private var disposable:Disposable? = null

    /**
     * 发送网络请求
     */
    override fun getData(httpDto: HttpDto) {

        httpDto.print()
        getHttpApi(httpDto).doOnSubscribe(Consumer {

            disposable = it
            if (!httpDto.slience){

                //显示网络加载
                showProgressDialog()
            }

        })
            .compose(RxUtils.bindToLifecycle(getMvpView()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<Any> {

                //请求成功
                dismissDialog()
                LogUtils.json(JsonParser.toJson(it))
                getMvpView().onSuccess(it,httpDto)

            },
                Consumer<Throwable>{

                    //请求异常
                    LogUtils.e(it.message)
                    dismissDialog()
                    showDialog(it.localizedMessage)

                })


    }

    /**
     * 获取GET或POST请求
     */
    private fun getHttpApi(httpDto: HttpDto): Observable<Any>{

        val httpAPI = HttpFactory.getProtocol(HttpAPI::class.java)

        if (httpDto.method.equals(Constant.HTTP_METHOD.GET)){

            if (httpDto.url.startsWith("http")){

                return httpAPI.sendFullGetRequest(httpDto.url,httpDto.headers!!,httpDto.params)
            }
            return httpAPI.sendGetRequest(httpDto.url,httpDto.headers!!,httpDto.params)

        }else if (httpDto.method.equals(Constant.HTTP_METHOD.POST)){

            //上传图片接口
            if (httpDto.isUplaod == true){

                return httpAPI.upImage(httpDto.url,httpDto.headers!!,httpDto.partBody!!)
            }

            if (httpDto.url.startsWith("http")){

                //body提交方式
                if (!TextUtils.isEmpty(httpDto.bodyString)){

                    return httpAPI.sendFullPostBodyRequest(httpDto.url,httpDto.headers!!,httpDto.bodyString)

                }
                return httpAPI.sendFullPostRequest(httpDto.url,httpDto.headers!!,httpDto.params)

            }else{

                //body提交方式
                if (!TextUtils.isEmpty(httpDto.bodyString)){

                    return httpAPI.sendPostBodyRequest(httpDto.url,httpDto.headers!!,httpDto.bodyString)

                }
                return httpAPI.sendPostRequest(httpDto.url,httpDto.headers!!,httpDto.params)

            }
        }

        return Observable.never()
    }

    /**
     * 处理网络提示
     */
    private fun showProgressDialog(){

        if (getMvpView() as? BaseMvpActivity<*> != null){

            (getMvpView() as BaseMvpActivity<*>).showProgressDialog()

        }else{

            (getMvpView() as BaseMvpFragment<*>).showProgressDialog()

        }
    }
    private fun showDialog(msg:String){

        if (getMvpView() as? BaseMvpActivity<*> != null){

            (getMvpView() as BaseMvpActivity<*>).showSweetDialog(SweetAlertDialog.ERROR_TYPE,"提示",
                msg)
        }else{

            (getMvpView() as BaseMvpFragment<*>).showSweetDialog(SweetAlertDialog.ERROR_TYPE,"提示",
                msg)
        }
    }

    private fun dismissDialog(){

        if (getMvpView() as? BaseMvpActivity<*> != null){

            (getMvpView() as BaseMvpActivity<*>).dismissDialogForRequest()


        }else{

            (getMvpView() as BaseMvpFragment<*>).dismissDialogForRequest()

        }
    }

    /**
     * 取消请求
     */
    override fun canCelRequest() {

        if (disposable != null && !disposable!!.isDisposed){
            disposable!!.dispose()
        }
        LogUtils.d("请求取消")
    }
}