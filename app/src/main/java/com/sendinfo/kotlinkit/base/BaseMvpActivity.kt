package com.sendinfo.kotlinkit.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import butterknife.ButterKnife
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.gyf.barlibrary.ImmersionBar
import com.othershe.nicedialog.NiceDialog

import com.sendinfo.kotlinkit.R
import com.sendinfo.kotlinkit.base.BaseMvpFragment.Companion.MIN_CLICK_DELAY_TIME
import com.sendinfo.kotlinkit.widget.MyTopNavBar
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.view.MvpFragmentActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseMvpActivity<out P : IPresenterContract> : MvpFragmentActivity<P>() {

    protected lateinit var myTopNavBar: MyTopNavBar
    protected lateinit var rxPermissions: RxPermissions
    protected var myApplication: MyApplication? = null
    private var parentLinearLayout: LinearLayout? = null
    private var mSweetAlertDialog: SweetAlertDialog? = null
    private var inputMethodManager: InputMethodManager? = null
    private var loadingDialog: NiceDialog? = null

    protected abstract fun initArgs(intent: Intent)

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun getLayoutId(): Int



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

    protected val appApplication: MyApplication
        get() {
            if (null == myApplication) {
                myApplication = getApplication() as MyApplication
            }
            return myApplication!!
        }

    val displayMetrics: DisplayMetrics
        get() {
            val mDisplayMetrics = DisplayMetrics()
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics)
            return mDisplayMetrics
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        myApplication = appApplication
        //initContentView(R.layout.activity_b)
        setContentView(getLayoutId())
        //configTopBar()
        rxPermissions = RxPermissions(this)
        ButterKnife.bind(this)

        ImmersionBar.with(this).init()

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {
            initArgs(intent)
            initView()
            initData()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        EventBus.getDefault().register(this)

    }

     override fun onDestroy() {


        if (mSweetAlertDialog != null) {

            mSweetAlertDialog!!.dismiss()
        }
        if (loadingDialog != null) {

            loadingDialog!!.dismiss()
        }

        EventBus.getDefault().unregister(this)

        ImmersionBar.with(this).destroy()

        super.onDestroy()

    }

    private fun configTopBar() {

        myTopNavBar = findViewById(R.id.my_top_bar)
        myTopNavBar.setBackClickListener({ v -> finish() })
    }

    //IView 方法

    fun showProgressDialog() {

        if (loadingDialog != null)return

        loadingDialog = NiceDialog.init()
            .setLayoutId(R.layout.loading_layout)
            .setWidth(135)
            .setHeight(135)
            .setOutCancel(false)
            .setDimAmount(0f)
            .show(getSupportFragmentManager()) as NiceDialog

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
            mSweetAlertDialog = SweetAlertDialog(this, type)
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

        inputMethodManager!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

    }

    fun hideKeyBoard() {

        try {
            inputMethodManager!!.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (ignored: Exception) {
        }

    }

    //如果不是新建Activity回传值

     override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    /**
     * 重新初始化根布局
     *
     * @param layoutResID
     */
    private fun initContentView(@LayoutRes layoutResID: Int) {

        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        viewGroup.removeAllViews()
        parentLinearLayout = LinearLayout(this)
        parentLinearLayout!!.orientation = LinearLayout.VERTICAL
        viewGroup.addView(parentLinearLayout)
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true)

    }

    /**
     * 点击软键盘之外的空白处，隐藏软件盘
     *
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = getCurrentFocus()
            if (isShouldHideInput(v, ev)) {

                if (inputMethodManager != null) {
                    inputMethodManager!!.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (getWindow().superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    protected fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {

        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return if (event.x > left && event.x < right
                && event.y > top && event.y < bottom
            ) {
                // 点击的是输入框区域，保留点击EditText的事件
                false
            } else {
                true
            }
        }
        return false
    }

    @Subscribe
    fun onEvent(event: String) {

    }



    protected fun checkPermission(checkPermissionListener: CheckPermissionListener?, vararg permissions: String) {
        rxPermissions.requestEach(*permissions)
            .subscribe { permission ->
                checkPermissionListener?.onPermissionBack(permission)
                if (permission.granted) {
                    // 用户已经同意该权限
                    LogUtils.d(permission.name + " is granted.")

                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，
                    // 还会提示请求权限的对话框
                    LogUtils.d(permission.name + " is denied. More info should be provided.")

                } else {
                    // 用户拒绝了该权限，并且选中『不再询问』
                    LogUtils.d(permission.name + " is denied.")
                }
            }
    }

    interface CheckPermissionListener {
        fun onPermissionBack(permission: Permission)
    }

    companion object {


        private var mLastClickTime: Long = 0
        val MIN_CLICK_DELAY_TIME = 500
    }
}
