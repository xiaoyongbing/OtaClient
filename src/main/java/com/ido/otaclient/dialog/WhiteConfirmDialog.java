package com.ido.otaclient.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.ido.otaclient.R;
import com.ido.otaclient.base.BaseDialogFragment;
import com.ido.otaclient.util.DipPixelUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-27 16:12
 * @description
 */
public class WhiteConfirmDialog extends BaseDialogFragment {

    private static final String MESSAGE = "message";
    private static final String TEXT_CONFIRM = "textConfirm";
    private static final String TEXT_CANCEL = "textCancel";
    private static final String SHOW_CANCEL = "showCancel";

    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.tv_message)
    TextView mTvMessage;

    private View.OnClickListener mConfirmListener;
    private View.OnClickListener mCancelListener;

    public static WhiteConfirmDialog newInstance(String message, String textConfirm, String textCancel,
                                                 boolean showCancel) {
        Bundle arguments = new Bundle();
        arguments.putString(MESSAGE, message);
        arguments.putString(TEXT_CONFIRM, textConfirm);
        arguments.putString(TEXT_CANCEL, textCancel);
        arguments.putBoolean(SHOW_CANCEL, showCancel);
        WhiteConfirmDialog dialog = new WhiteConfirmDialog();
        dialog.setArguments(arguments);
        dialog.setStyle(STYLE_NO_TITLE, R.style.AlertDialog_Dark);
        return dialog;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_white_confirm;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTvMessage.setText(bundle.getString(MESSAGE));
            mTvConfirm.setText(bundle.getString(TEXT_CONFIRM));
            mTvCancel.setText(bundle.getString(TEXT_CANCEL));
            mTvCancel.setVisibility(bundle.getBoolean(SHOW_CANCEL) ? View.VISIBLE : View.GONE);
        }
    }

    public WhiteConfirmDialog setOnConfirmListener(View.OnClickListener listener) {
        mConfirmListener = listener;
        return this;
    }

    public WhiteConfirmDialog setCancelListener(View.OnClickListener listener) {
        mCancelListener = listener;
        return this;
    }

    @OnClick(R.id.tv_cancel)
    public void doClickCancel(View v) {
        if (mCancelListener != null) {
            mCancelListener.onClick(v);
        }
    }

    @OnClick(R.id.tv_confirm)
    public void doClickConfirm(View v) {
        if (mConfirmListener != null) {
            mConfirmListener.onClick(v);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setWindowSize();
    }

    /**
     * 设置窗口大小
     */
    private void setWindowSize() {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(DipPixelUtil.dip2px(296),
                    DipPixelUtil.dip2px(210));
        }
    }
}
