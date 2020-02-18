package com.ido.otaclient.two;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.ido.otaclient.R;
import com.ido.otaclient.base.BaseActivity;

import butterknife.OnClick;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-13 16:20
 * @description
 */
public class TwoToOneActivity extends BaseActivity {

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity,TwoToOneActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);
        setContentView(R.layout.activity_twoto_one);
    }


    @OnClick(R.id.title_leftBtn)
    public void back(View view){
        ActivityCompat.finishAfterTransition(this);
    }
}
