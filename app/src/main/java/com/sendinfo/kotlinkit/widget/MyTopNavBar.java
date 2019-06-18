package com.sendinfo.kotlinkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sendinfo.kotlinkit.R;


/**
 * <pre>
 *     author : ghwang
 *     e-mail : 429329513@qq.com
 *     time   : 2018/05/24
 *     desc   :
 * </pre>
 */

public class MyTopNavBar extends RelativeLayout {

    private Button backBtn;
    private boolean isShowBack;
    private int backgroundColor;
    private TextView navTitleTV;
    private ImageView rightImgV;
    private String navTitle;
    private int rigtIcon;

    public MyTopNavBar(Context context) {
        super(context);
        initView(context);
    }

    public MyTopNavBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.MyNavBar);

        isShowBack = types.getBoolean(R.styleable.MyNavBar_is_show_back,true);
        backgroundColor = types.getColor(R.styleable.MyNavBar_backgroundColor,
                Color.parseColor("#2469df"));
        navTitle = types.getString(R.styleable.MyNavBar_nav_title);
        rigtIcon = types.getResourceId(R.styleable.MyNavBar_right_icon,0);

        types.recycle();
        initView(context);
    }

    public MyTopNavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }


    private void initView(Context context){


        View.inflate(context,R.layout.top_nav_bar,MyTopNavBar.this);

        backBtn = findViewById(R.id.nav_back_btn);
        navTitleTV = findViewById(R.id.nav_title_tv);
        navTitleTV.setText(navTitle);
        rightImgV = findViewById(R.id.right_icon);
        rightImgV.setImageResource(rigtIcon);

        setBackgroundColor(backgroundColor);

        if (isShowBack){

            backBtn.setVisibility(View.VISIBLE);

        }else {

            backBtn.setVisibility(View.GONE);

        }

    }

    public void setBackClickListener(OnClickListener listener){

        backBtn.setOnClickListener(listener);
    }
    public void setRightClickListener(OnClickListener listener){

        rightImgV.setOnClickListener(listener);
    }
    public void setTitle(String title){

        navTitleTV.setText(title);
    }
    public void setNavBgColor(int color){

        setBackgroundColor(color);

    }

    public void setRightIcon(int icon){


        rightImgV.setImageResource(icon);

    }
}
