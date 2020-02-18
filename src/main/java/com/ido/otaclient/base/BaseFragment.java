package com.ido.otaclient.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ido.otaclient.env.LanguageManager;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-11 17:06
 * @description
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    private LifecycleHelper mLifecycleHelper = new LifecycleHelper(TAG);

    @Override
    public void onAttach(@NonNull Context context) {
        mLifecycleHelper.logLifecycle("onAttach",true);
        context = LanguageManager.setLanguage(context);
        super.onAttach(LanguageManager.setLanguage(context));
        onAttachTask(context);
        mLifecycleHelper.logLifecycle("onAttach",false);
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        mLifecycleHelper.logLifecycle("onCreate",true);
        super.onCreate(savedInstanceState);
        onCreateTask(savedInstanceState);
        mLifecycleHelper.logLifecycle("onCreate",false);

    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        mLifecycleHelper.logLifecycle("onCreateView",true);
        View view = onCreateViewTask(inflater,container,savedInstanceState);
        ButterKnife.bind(this,view);
        mLifecycleHelper.logLifecycle("onCreateView",false);
        return view;
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLifecycleHelper.logLifecycle("onViewCreated",true);
        super.onViewCreated(view, savedInstanceState);
        onViewCreatedTask(view,savedInstanceState);
        mLifecycleHelper.logLifecycle("onViewCreated",false);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mLifecycleHelper.logLifecycle("onActivityCreated",true);
        super.onActivityCreated(savedInstanceState);
        onActivityCreatedTask(savedInstanceState);
        mLifecycleHelper.logLifecycle("onActivityCreated",false);

    }

    @Override
    public final void onStart() {
        mLifecycleHelper.logLifecycle("onStart",true);
        super.onStart();
        onStartTask();
        mLifecycleHelper.logLifecycle("onStart",false);
    }

    @Override
    public final void onResume() {
        mLifecycleHelper.logLifecycle("onResume",true);
        super.onResume();
        onResumeTask();
        mLifecycleHelper.logLifecycle("onResume",true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(STATE_SAVE_IS_HIDDEN,isHidden());
    }

    @Override
    public final void onPause() {
        mLifecycleHelper.logLifecycle("onPause",true);
        super.onPause();
        onPauseTask();
        mLifecycleHelper.logLifecycle("onPause",false);
    }

    @Override
    public final void onStop() {
        mLifecycleHelper.logLifecycle("onStop",true);
        super.onStop();
        onStopTask();
        mLifecycleHelper.logLifecycle("onResume",false);
    }

    @Override
    public final void onDestroyView() {
        mLifecycleHelper.logLifecycle("onDestroyView",true);
        super.onDestroyView();
        onDestroyViewTask();
        mLifecycleHelper.logLifecycle("onDestroyView",false);
    }

    @Override
    public final void onDestroy() {
        mLifecycleHelper.logLifecycle("onDestroy",true);
        super.onDestroy();
        onDestroyTask();
        mLifecycleHelper.logLifecycle("onDestroy",false);
    }

    @Override
    public final void onDetach() {
        mLifecycleHelper.logLifecycle("onDetach",true);
        super.onDetach();
        onDetachTask();
        mLifecycleHelper.logLifecycle("onDetach",false);
    }

    /** {@link Fragment#onAttach(Context)} */
    public void onAttachTask(Context context) { }

    /** {@link Fragment#onCreate(Bundle)} */
    public void onCreateTask(Bundle savedInstanceState) { }

    /** {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}  */
    public View onCreateViewTask(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(),container,false);
    }

    /** {@link Fragment#onViewCreated(View, Bundle)}  */
    public void onViewCreatedTask(View view, Bundle savedInstanceState) { }

    /** {@link Fragment#onActivityCreated(Bundle)}  */
    public void onActivityCreatedTask(Bundle savedInstanceState) { }

    /** {@link Fragment#onStart()}  */
    public void onStartTask() { }

    /** {@link Fragment#onResume()}  */
    public void onResumeTask() { }

    /** {@link Fragment#onPause()}  */
    public void onPauseTask() { }

    /** {@link Fragment#onStop()}  */
    public void onStopTask() { }

    /** {@link Fragment#onDestroyView()}  */
    public void onDestroyViewTask() { }

    /** {@link Fragment#onDestroy()}  */
    public void onDestroyTask() { }

    /** {@link Fragment#onDetach()}  */
    public void onDetachTask() { }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 用户可见与不可见的处理
        // 通常用于数据的懒加载
        if(isVisibleToUser){
            onUserVisible();
        }else{
            onUserInvisible();
        }
    }

    /** 当Fragment对用户可见的时候回调 */
    protected void onUserVisible(){  }

    /** 当Fragment对用户不可见的时候回调 */
    protected void onUserInvisible(){  }

    /**
     * 告诉 {@link BaseFragment}  {@code layoutResId}
     * @return {@code layoutResId}
     */
    protected abstract  @LayoutRes
    int getLayoutResId();
}
