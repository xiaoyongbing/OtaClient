package com.ido.otaclient.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.ido.otaclient.DeviceManagerModel;
import com.ido.otaclient.R;
import com.ido.otaclient.api.OkhttpUtil;
import com.ido.otaclient.base.ApiConstant;
import com.ido.otaclient.base.BaseFragment;
import com.ido.otaclient.base.Constant;
import com.ido.otaclient.base.IDLog;
import com.ido.otaclient.dialog.WhiteConfirmDialog;
import com.ido.otaclient.module.Device;
import com.ido.otaclient.module.Veneer;
import com.ido.otaclient.util.FileUtil;
import com.ido.otaclient.util.GZipUtil;
import com.ido.otaclient.util.WifiUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-11 17:13
 * @description
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    /**
     * 升级配置
     */
    @BindView(R.id.btn_update_config)
    Button mbtnUpdateConfig;
    /**
     * 开始任务
     */
    @BindView(R.id.btn_start)
    Button mBtnStart;
    /**
     * 升级名称
     */
    @BindView(R.id.tv_update_name)
    TextView mTvUpdateName;

    /**
     * 升级中的数量
     */
    @BindView(R.id.tv_upgrade_count)
    TextView mUpgradeCount;
    /**
     * 升级成功的数量
     */
    @BindView(R.id.tv_success_count)
    TextView mSuccessCount;
    /**
     * 失败的数量
     */
    @BindView(R.id.tv_fail_count)
    TextView mFailCount;
    /**
     * 配置监听
     */
    private OnItemListener mOnItemListener;
    /**
     * 除端口号后面一位的ip
     */
    private String mBaseUrl = "";
    /**
     * 遍历ip的次数
     */
    private int mCount = 0;
    /**
     * 设备列表
     */
    private List<Device> mDeviceList = new ArrayList<>();
    /**
     * 单板数量
     */
    private List<Veneer> mVeneerList = new ArrayList<>();

    /**
     * 确认取消弹框
     */
    private WhiteConfirmDialog mWhiteConfirmDialog;

    private static final int VENEER_WHAT = 1000;
    /**
     * 主界面之间的数据传递
     */
    private DeviceManagerModel mDeviceManagerModel;






    //新建Handler对象。
    Handler mHandler = new Handler(){

        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VENEER_WHAT:
                    //todo 发起扫描指令
                    //handleDevlist(msg.obj);
                    handleVeneer(msg.obj);
                    break;
            }
        }
    };

    /**
     * 得到ip的集合
     * @param obj
     */
    private void handleVeneer(Object obj) {
        String veneerStr = obj.toString();
        Log.d(TAG, veneerStr+"设备");
        Gson gson = new Gson();
        Veneer veneer = gson.fromJson(veneerStr,Veneer.class);
        mVeneerList.add(veneer);
    }

    public void setOnItemListener(OnItemListener onItemListener){
        this.mOnItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onConfig();
    }

    public static HomeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreatedTask(View view, Bundle savedInstanceState) {
        super.onViewCreatedTask(view, savedInstanceState);

        initData();
        initListener();
    }

    private void initData() {
        mDeviceManagerModel = ViewModelProviders.of(getActivity()).get(DeviceManagerModel.class);

        mDeviceManagerModel.getPlayTime().observe(this,time-> {
            Log.d(TAG,time+"");
            getVeenStatus();
        });
        //获取当前ip
        getCurrentIp();
        //扫描ip
        getVeneer();
    }

    private void getVeenStatus(){
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        for(int i = 0;i < mVeneerList.size();i++){
            String ip = mVeneerList.get(i).getIp();
            if(TextUtils.isEmpty(ip)){
                return;
            }
            ip = ApiConstant.URL_HEAD + ip + ":" + Constant.HTTP_PORT +"/"+ApiConstant.METHOD_CHECK;
            Log.d(TAG,ip + "");
            okhttpUtil.getSyn(ip, new OkhttpUtil.OkhttpCallback() {
                @Override
                public void onSuccess(String success) {
                    // mTvtest.setText(success + ","+count);
                    //todo 显示状态
                    Log.d(TAG, "onSuccess: " + success);
                }

                @Override
                public void onFailed(String message) {
                }
            });
        }
    }

    private void getCurrentIp() {
        String url = WifiUtils.getWifiIp(getActivity());
        Log.d(TAG, url +"++++++");
        if(!TextUtils.isEmpty(url)){
            if(url.contains(".")){
                String[] ips = url.split("\\.");
                Log.d(TAG,ips.length+"----");
                String last = ips[ips.length - 1];
                mBaseUrl = ApiConstant.URL_HEAD+ url.substring(0,(url.length() - last.length()));
            }
        }
        IDLog.d(TAG, "getCurrentIp: " + mBaseUrl);
    }

    private void initListener() {

    }

    @OnClick(R.id.btn_update_config)
    public void toConfig(View view){
        if(mOnItemListener != null){
            mOnItemListener.onConfig();
        }
    }

    @OnClick(R.id.btn_start)
    public void toStart(View view){
        //遍历ip
        Log.d(TAG, "toStart: ");
        //getVeneer();
        //showConfirmCancel();
        //startTask();
        mDeviceManagerModel.startTimer();
    }

    /**
     * 获取单板列表
     */
    private void getVeneer() {
        //获取列表
        mCount = 0;
        mDeviceList = new ArrayList<>();
        mVeneerList = new ArrayList<>();
        for(int i= 0;i<= 255;i++){
            String ip = mBaseUrl + i+":" + Constant.HTTP_PORT
                    +"/"+ ApiConstant.METHOD_VENEER_LIST;
            IDLog.d(TAG, "getVeneer: " + ip);
            getVeneerByIp(ip);
        }
        //String ip = "http://192.168.1.5:5000/veneer_list";
        //getVeneerByIp(ip);
    }

    /**
     * 访问网络
     * @param s
     */
    private void getVeneerByIp(String s) {
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        okhttpUtil.getSyn(s, new OkhttpUtil.OkhttpCallback() {
            @Override
            public void onSuccess(String success) {
                mCount++;
                // mTvtest.setText(success + ","+count);
                //todo 到达255次后跳转到设备列表界面
                Log.d(TAG, "onSuccess: " + mCount);
                Message message = new Message();
                message.what = VENEER_WHAT;
                message.obj = success;
                mHandler.sendMessage(message);
                scanComplete();
            }

            @Override
            public void onFailed(String message) {
                //mTvtest.setText(message);
                mCount++;
                Log.d(TAG,message + mCount);
                //todo
                scanComplete();
            }
        });
    }

    /**
     * 扫描结束
     */
    private void scanComplete() {
        // 或超过三十秒
        if(mCount >= 250){
            mDeviceManagerModel.getVeneerModelLiveData().postValue(mVeneerList);
            //开始任务
            //startTask();
            //mBtnStart.setText("结束任务");
        }
    }

    /**
     * 开启任务
     */
    private void startTask() {
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        for(int i = 0;i < mVeneerList.size();i++){
            String ip = mVeneerList.get(i).getIp();
            if(TextUtils.isEmpty(ip)){
                return;
            }
            ip = ApiConstant.URL_HEAD + ip + ":" + Constant.HTTP_PORT +"/"+ApiConstant.METHOD_TASK_START;
            Log.d(TAG,ip + "");
            okhttpUtil.getSyn(ip, new OkhttpUtil.OkhttpCallback() {
                @Override
                public void onSuccess(String success) {
                    mCount++;
                    // mTvtest.setText(success + ","+count);
                    //todo 到达255次后跳转到设备列表界面
                    Log.d(TAG, "onSuccess: " + mCount);
                    Message message = new Message();
                    message.what = VENEER_WHAT;
                    message.obj = success;
                    mHandler.sendMessage(message);
                    scanComplete();
                }

                @Override
                public void onFailed(String message) {
                    //mTvtest.setText(message);
                    mCount++;
                    Log.d(TAG,message + mCount);
                    //todo
                    scanComplete();
                }
            });
        }


    }

    private void showConfirmCancel() {
        mWhiteConfirmDialog = WhiteConfirmDialog.newInstance(
                "是否确定取消当前升级？进度将被请零","确定",
                "取消",true);
        mWhiteConfirmDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 确定取消升级
            }
        });
        mWhiteConfirmDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消弹框
                mWhiteConfirmDialog.dismissAllowingStateLoss();
            }
        });

        mWhiteConfirmDialog.setCancelable(false);
        mWhiteConfirmDialog.show(getActivity().getSupportFragmentManager());
    }









}
