package com.ido.otaclient.fontb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.ido.otaclient.R;
import com.ido.otaclient.base.BaseActivity;

import butterknife.OnClick;

public class FontBActivity extends BaseActivity {

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity, FontBActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);
        setContentView(R.layout.activity_font_b);
    }

    @OnClick(R.id.title_leftBtn)
    public void back(View view){
        ActivityCompat.finishAfterTransition(this);
    }
}
