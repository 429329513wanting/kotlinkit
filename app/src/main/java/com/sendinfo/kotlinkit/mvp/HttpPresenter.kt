package com.sendinfo.kotlinkit.mvp

import android.text.TextUtils
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.ljb.mvp.kotlin.utils.JsonTool
import com.ljb.mvp.kotlin.utils.RxUtils
import com.sendinfo.kotlinkit.base.BaseMvpActivity
import com.sendinfo.kotlinkit.base.BaseMvpFragment
import com.sendinfo.kotlinkit.http.EventResp
import com.sendinfo.kotlinkit.http.HttpTool
import com.sendinfo.kotlinkit.http.HttpDto
import com.sendinfo.kotlinkit.utils.Constant
import com.sendinfo.kotlinkit.utils.RxPartMapUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.presenter.BaseMvpPresenter
import net.ljb.kt.client.HttpFactory
import org.greenrobot.eventbus.EventBus
import java.lang.Exception


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
                LogUtils.json(JsonTool.toJson(it))
                //非正常json格式走通知
                if (!it.toString().startsWith("{")){

                    EventBus.getDefault().post(EventResp(it.toString()))

                }else{

                    var result = JsonTool.fromJsonToObj(JsonTool.toJson(it),BaseResponse::class.java)
                    getMvpView().onSuccess(result,httpDto)
                }

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

        val httpAPI = HttpFactory.getProtocol(HttpTool::class.java)

        //GET
        if (httpDto.method.equals(Constant.HTTP_METHOD.GET)){

            return httpAPI.sendGetRequest(httpDto.fullUrl,httpDto.headers!!,httpDto.params)

        }
        //POST
        if (httpDto.method.equals(Constant.HTTP_METHOD.POST)){

            //上传图片接口
            if (httpDto.isUploadImage == true){

                return httpAPI.upImage(httpDto.fullUrl,httpDto.headers!!,httpDto.partBody!!)
            }
            //body提交方式
            if (!TextUtils.isEmpty(httpDto.bodyString)){

                return httpAPI.sendPostBodyRequest(httpDto.url,httpDto.headers!!,httpDto.bodyString)

            }
            //mulipart文件提交
            if(httpDto.multiParams!=null){

                return  httpAPI.upMultiPart(httpDto.fullUrl,
                    httpDto.headers!!,
                    RxPartMapUtil.changeToBodyMap(httpDto.multiParams!!))
            }
            //表单提交
            if (httpDto.params != null){

                return httpAPI.sendPostRequest(httpDto.fullUrl,httpDto.headers!!,httpDto.params)
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