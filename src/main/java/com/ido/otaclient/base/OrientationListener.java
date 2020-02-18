package com.ido.otaclient.base;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;

/**
 * 监听屏幕方向变化,并将角度转为 rotation
 * <p>  </p>
 *
 * @see Surface#ROTATION_0
 * @see Surface#ROTATION_90
 * @see Surface#ROTATION_180
 * @see Surface#ROTATION_270
 * Created by lize on 2017/8/14.
 */

public abstract class OrientationListener extends OrientationEventListener {

    private int mRotation;

    public OrientationListener(Context context) {
        super(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mRotation = wm.getDefaultDisplay().getRotation();
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN){ // 平放
            return;
        }

        int rotation = translate2Rotation(orientation, mRotation);

        if(mRotation != rotation){
            mRotation = rotation;
            onRotationChanged(mRotation);
        }
    }

    protected abstract void onRotationChanged(int rotation);

    public static int translate2Rotation(int orientation,int oriRotation) {
        int rotation;
        if (isRotation270(orientation)) {
            rotation = Surface.ROTATION_270;
        } else if (isRotation180(orientation)) {
            rotation = Surface.ROTATION_180;
        } else if (isRotation90(orientation)) {
            rotation = Surface.ROTATION_90;
        } else if (isRotation0(orientation)) {
            rotation = Surface.ROTATION_0;
        } else {
            //在临界值中间的角度时,
            rotation = oriRotation;
        }
        return rotation;
    }

    private static boolean isRotation0(int orientation) {
        return orientation > 340 || orientation < 20;
    }

    private static boolean isRotation90(int orientation) {
        return orientation > 70 && orientation < 110;
    }

    private static boolean isRotation180(int orientation) {
        return orientation > 160 && orientation < 200;
    }

    private static boolean isRotation270(int orientation) {
        return orientation > 250 && orientation < 290;
    }
}
