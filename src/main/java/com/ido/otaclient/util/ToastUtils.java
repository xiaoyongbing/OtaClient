package com.ido.otaclient.util;

import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.ido.otaclient.R;
import com.ido.otaclient.env.AppEnv;
import com.ido.otaclient.view.RotateTextView;


/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-12 17:20
 * @description
 */
public class ToastUtils {
    private static Toast sToast;
    private static Toast sToastImg;
    private static ImageView sImage;

    private ToastUtils() {
    }

    /**
     * 普通文字吐司
     *
     * @param message
     */
    public static void show(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
            Toast.makeText(AppEnv.instance().getContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            if (sToast == null) {
                sToast = Toast.makeText(AppEnv.instance().getContext(), message, Toast.LENGTH_SHORT);
            } else {
                sToast.setText(message);
            }
            sToast.show();
        }
    }


    /**
     * 带图片且显示在中间的吐司
     *
     * @param resId
     * @param message
     */
    public static void showWithImage(@DrawableRes int resId, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
            Toast toastImg = Toast.makeText(AppEnv.instance().getContext(), message, Toast.LENGTH_SHORT);
            toastImg.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toastImg.getView();
            sImage = new ImageView(AppEnv.instance().getContext());
            sImage.setPadding(0, ResourceUtil.getDimens(R.dimen.activity_vertical_margin), 0, 0);
            sImage.setImageResource(resId);
            toastView.addView(sImage, 0);
            toastImg.show();
        } else {
            if (sToastImg == null) {
                sToastImg = Toast.makeText(AppEnv.instance().getContext(), message, Toast.LENGTH_SHORT);
                sToastImg.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastView = (LinearLayout) sToastImg.getView();
                sImage = new ImageView(AppEnv.instance().getContext());
                sImage.setPadding(0, ResourceUtil.getDimens(R.dimen.activity_vertical_margin), 0, 0);
                sImage.setImageResource(resId);
                toastView.addView(sImage, 0);
            } else {
                sToastImg.setText(message);
                sImage.setImageResource(resId);
            }
            sToastImg.show();
        }
    }

    public static void show(@StringRes int resId, int rotation) {
        String message = ResourceUtil.getString(resId);
        show(message, rotation);
    }

    /**
     * 根据传入的当前方向,以一定的角度来显示Toast
     *
     * @param message  显示的文字
     * @param rotation 当前屏幕的方向,应当传入如下四个值之一
     *
     * @see android.view.Surface#ROTATION_0
     * @see android.view.Surface#ROTATION_90
     * @see android.view.Surface#ROTATION_180
     * @see android.view.Surface#ROTATION_270
     */
    public static void show(String message, int rotation, int leftDrawableId) {
        RotateTextView textView = new RotateTextView(AppEnv.instance().getContext());
        textView.setText(message);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.shape_rectangle_dark_gray_16_corner);
        textView.setDirection(rotation);
        if (leftDrawableId != 0)
            textView.setLeftCompoundDrawables(leftDrawableId);

        Toast toast = new Toast(AppEnv.instance().getContext());
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        if (rotation == 0) {
            toast.setGravity(Gravity.END, 300, -30);
        } else if (rotation == 1) {
            toast.setGravity(Gravity.TOP, 30, 100);
        } else if (rotation == 2) {
            toast.setGravity(Gravity.START, 300, -30);
        } else {
            toast.setGravity(Gravity.BOTTOM, 30, 100);
        }
        toast.show();
    }

    /**
     * 根据传入的当前方向,以一定的角度来显示Toast
     *
     * @param message  显示的文字
     * @param rotation 当前屏幕的方向,应当传入如下四个值之一
     * @see android.view.Surface#ROTATION_0
     * @see android.view.Surface#ROTATION_90
     * @see android.view.Surface#ROTATION_180
     * @see android.view.Surface#ROTATION_270
     */
    public static void show(String message, int rotation) {
        show(message, rotation, 0);
    }
}
