package com.sendinfo.kotlinkit.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import butterknife.ButterKnife
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.StringUtils
import com.othershe.nicedialog.NiceDialog
import com.sendinfo.kotlinkit.R
import mvp.ljb.kt.contract.IPresenterContract
import org.greenrobot.eventbus.EventBus
import mvp.ljb.kt.view.MvpFragment
import org.greenrobot.eventbus.Subscribe

/**
 * <pre>
 * author : ghwang
 * e-mail : 429329513@qq.com
 * time   : 2018/05/18
 * desc   :
</pre> *
 */

abstract class BaseMvpFragment<out P : IPresenterContract> : MvpFragment<P>() {


    // 将代理类通用行为抽出来
    private var inflater: LayoutInflater? = null
    private var mInputMethodManager: InputMethodManager? = null
    private var container: ViewGroup? = null
    lateinit var mView: View
    private var mSweetAlertDialog: SweetAlertDialog? = null
    private var loadingDialog: NiceDialog? = null

    // 当前时间
    // 两次点击的时间差
    val isFastClick: Boolean
        get() {
            val currentTime = System.currentTimeMillis()
            val time = currentTime - mLastClickTime
            if (0 < time && time < MIN_CLICK_DELAY_TIME) {
                return true
            }
            mLastClickTime = currentTime
            return false
        }

    protected abstract fun initArgs(bundle: Bundle)

    protected abstract fun initView(bundle: Bundle?)

    protected abstract fun initData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        this.inflater = inflater
        this.container = container
        mInputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        EventBus.getDefault().register(this)
        try {
            arguments?.let { initArgs(it) }
            initView(savedInstanceState)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        super.onViewCreated(view, savedInstanceState)
    }

    //子类需要调用
    protected fun setContentView(layout: Int) {
        mView = inflater!!.inflate(layout, container, false)
        ButterKnife.bind(this, mView)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        if (mSweetAlertDialog != null && mSweetAlertDialog!!.isShowing) {
            mSweetAlertDialog!!.dismiss()
            mSweetAlertDialog = null
        }

        EventBus.getDefault().unregister(this)
        super.onDestroyView()
        val parent = mView.parent as ViewGroup
        parent?.removeView(mView)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

    }



    fun showProgressDialog() {

        if (loadingDialog != null)return
        loadingDialog = NiceDialog.init()
            .setLayoutId(R.layout.loading_layout)
            .setWidth(135)
            .setHeight(135)
            .setOutCancel(true)
            .setDimAmount(0f)
            .show((activity as BaseMvpActivity<*>).getSupportFragmentManager()) as NiceDialog
    }

    @JvmOverloads
    fun showSweetDialog(
        type: Int,
        title: String,
        content: String,
        confirmText: String = "确定",
        cancelText: String? = null,
        confirmListener: SweetAlertDialog.OnSweetClickListener? = SweetAlertDialog.OnSweetClickListener { sweetAlertDialog -> sweetAlertDialog.dismiss() },
        cancelListener: SweetAlertDialog.OnSweetClickListener? = null
    ) {
        if (mSweetAlertDialog != null && mSweetAlertDialog!!.isShowing) {
            mSweetAlertDialog!!.changeAlertType(type)
        } else {
            mSweetAlertDialog = SweetAlertDialog(getActivity(), type)
            mSweetAlertDialog!!.setCancelable(false)
        }
        // Title
        if (!StringUtils.isEmpty(title)) {
            mSweetAlertDialog!!.titleText = title
        } else {
            mSweetAlertDialog!!.titleText = ""
        }
        // content
        if (!StringUtils.isEmpty(content)) {
            mSweetAlertDialog!!.contentText = content
        } else {
            mSweetAlertDialog!!.showContentText(false)
        }
        // confirmText
        if (!StringUtils.isEmpty(confirmText)) {
            mSweetAlertDialog!!.confirmText = confirmText
        }
        // cancelText
        if (!StringUtils.isEmpty(cancelText)) {
            mSweetAlertDialog!!.cancelText = cancelText
        } else {
            mSweetAlertDialog!!.showCancelButton(false)
        }
        // confirmListener
        if (confirmListener != null) {
            mSweetAlertDialog!!.setConfirmClickListener(confirmListener)
        } else {
            mSweetAlertDialog!!.setConfirmClickListener { sweetAlertDialog -> sweetAlertDialog.dismiss() }
        }
        // confirmListener
        if (confirmListener != null) {
            mSweetAlertDialog!!.setCancelClickListener(cancelListener)
        } else {
            mSweetAlertDialog!!.setCancelClickListener { sweetAlertDialog -> sweetAlertDialog.dismiss() }
        }
        mSweetAlertDialog!!.show()
    }


    fun dismissDialog() {


        if (mSweetAlertDialog != null) {

            mSweetAlertDialog!!.dismiss()
        }
    }

    fun dismissDialogForRequest() {

        if (loadingDialog != null) {

            loadingDialog!!.dismiss()
        }
    }

    fun showKeyBoard(editText: EditText) {

        mInputMethodManager!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

    }

    /**
     * 隐藏键盘
     */

    fun hideKeyBoard() {

        try {
            mInputMethodManager!!
                .hideSoftInputFromWindow(
                    activity!!.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        } catch (ignored: Exception) {
        }

    }

    companion object {

        private var mLastClickTime: Long = 0
        val MIN_CLICK_DELAY_TIME = 500
    }

    @Subscribe
    fun onEvent(event: String) {

    }
}
