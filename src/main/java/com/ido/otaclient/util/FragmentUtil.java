package com.ido.otaclient.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-17 18:17
 * @description
 */
public class FragmentUtil {

    /**
     * 将 {@code fragment} 添加到 {@code frameId} 的容器中.
     */
    public static void addFragment(@NonNull FragmentManager fragmentManager,
                                   @NonNull Fragment fragment, int frameId, String fragmentTag) {
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, fragmentTag);
        transaction.commit();
    }

    /**
     * 将 {@code fragment} 添加到 {@code frameId} 的容器中.
     */
    public static void addFragment(@NonNull FragmentManager fragmentManager,
                                   @NonNull Fragment fragment, int frameId) {
        addFragment(fragmentManager,fragment,frameId,false);
    }

    /**
     * 将 {@code fragment} 添加到 {@code frameId} 的容器中.
     */
    public static void addFragment(@NonNull FragmentManager fragmentManager,
                                   @NonNull Fragment fragment, int frameId, boolean addBack) {
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(addBack == true) { transaction.addToBackStack(null); }
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * 用 {@code fragment} 替换 {@code frameId} 容器中的{@link Fragment}
     */
    public static void replaceFragment(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment,int frameId){
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId,fragment);
        transaction.commit();
    }

    /** 移除 {@code fragment} */
    public static void removeFragment(@NonNull FragmentManager fragmentManager,
                                      @NonNull Fragment fragment){
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }


    public static void showFragment(FragmentManager fragmentManager,Fragment fragment) {
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment).commit();
    }

    /**
     * 得到fragment的tag
     * 根据{@link FragmentPagerAdapter#makeFragmentName(int,long)} 而来
     * 只有使用了FragmentPagerAdapter的可用
     * @param viewpagerId
     * @param position
     * @return
     */
    public static String makeFragmentName(int viewpagerId, long position) {
        return "android:switcher:" + viewpagerId + ":" + position;
    }

    /**
     * 隐藏fragment
     * @param fragmentManager
     * @param fragment
     */
    public static void hideFragment(FragmentManager fragmentManager, Fragment fragment) {
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment).commit();
    }
}
