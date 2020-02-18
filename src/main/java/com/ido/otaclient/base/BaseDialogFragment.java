package com.ido.otaclient.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import com.ido.otaclient.R;
import com.ido.otaclient.env.LanguageManager;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-11 17:22
 * @description
 */
public class BaseDialogFragment extends DialogFragment {
    private static final String TAG = "BaseDialogFragment";
    private OnDismissionListener mOnDismissionListener;
    private OnShowingListener mOnShowingListener;
    private OrientationListener mOrientationListener;
    protected int mRotation = Surface.ROTATION_270;

    @Override
    public void onAttach(Context context) {
        super.onAttach(LanguageManager.setLanguage(context));
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   Bundle savedInstanceState) {

        if (getLayoutResId() == 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            return inflater.inflate(getLayoutResId(), container, false);
        }
    }

    protected @LayoutRes
    int getLayoutResId() {
        return 0;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // 定义Dialog动画
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.windowAnimations = getWindowAnimations();
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        initData();
        initUI(view);
        initListener(view);
    }

    protected void initData() {
    }

    protected void initUI(View view) {
    }

    protected void initListener(View view) {
    }

    /**
     * 当继承的子类使用系统自带的AlertDialog实现时,{@link #onViewCreated(View, Bundle)}方法会被忽略而不被执行.
     * 所以在子类中,就无法获取到{@link #mOrientationListener}的变化,所以需要将该listener的初始化放在此方法中.
     * 确保所有的子类都可以得到手机朝向改变的更新
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        initOrientationListener();
    }

    private void initOrientationListener() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        // 方向监听
        mOrientationListener = new OrientationListener(activity) {

            @Override
            protected void onRotationChanged(int rotation) {
                mRotation = rotation;
                setRotation(rotation);
            }
        };
    }

    /**
     * 旋转View,子类继承实现逻辑
     */
    protected void setRotation(int rotation) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissionListener != null) {
            mOnDismissionListener.onDismission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOrientationListener != null) {
            mOrientationListener.enable();
        }
        if (mOnShowingListener != null) {
            mOnShowingListener.onShowing();
        }
        //如果用户手动点击Home键或者电源键,再回来的时候,需要清除这个FLAG,否则用户点击屏幕外部Dialog不会消失
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOrientationListener != null) {
            mOrientationListener.disable();
        }
    }

    /**
     * 定义Dialog动画，默认为淡入淡出
     */
    protected @StyleRes
    int getWindowAnimations() {
        return R.style.DialogAnimationsFade;
    }


    public void show(FragmentManager fm) {
        try {
            if (fm.isDestroyed()) {
                return;
            }

            //避免重复显示Dialog
            String tag = getFragmentTag();
            if (tag != null && tag.equals(this.getClass().getSimpleName())) {
                // we do not show it twice
                if (fm.findFragmentByTag(tag) == null) {
                    super.show(fm, tag);
                }
            } else {
                super.show(fm, tag);
            }

            fm.executePendingTransactions();
            if (getDialog() != null && getDialog().getWindow() != null) {
                getDialog().getWindow().getDecorView().setSystemUiVisibility(
                        getActivity().getWindow().getDecorView().getSystemUiVisibility());

                // Make the dialogs window focusable again.
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(FragmentManager fm, int currentRotation) {
        mRotation = currentRotation;
        show(fm);
    }

    @Override
    public void dismiss() {
        if (getActivity() != null) {
            super.dismiss();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (getActivity() != null) {
            super.dismissAllowingStateLoss();
        }
    }

    protected String getFragmentTag() {
        return this.getClass().getSimpleName();
    }

    public void setOnDismissionListener(OnDismissionListener listener) {
        mOnDismissionListener = listener;
    }

    public void setOnShowingListner(OnShowingListener listner) {
        this.mOnShowingListener = listner;
    }

    public interface OnDismissionListener {
        public void onDismission();
    }

    public interface OnShowingListener {
        public void onShowing();
    }
}
