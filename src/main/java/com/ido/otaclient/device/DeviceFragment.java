package com.ido.otaclient.device;



import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ido.otaclient.R;
import com.ido.otaclient.base.BaseFragment;
import com.ido.otaclient.module.Device;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-11 17:13
 * @description
 */
public class DeviceFragment extends BaseFragment {
    /**
     * 设备数量
     */
    @BindView(R.id.tv_device_account)
    TextView mTvDeviceAccount;

    /**
     * 单板数量
     * @return
     */
    @BindView(R.id.tv_device_veneer_account)
    TextView mTvDeviceVeneer;
    /**
     * 单板数量
     */
    @BindView(R.id.rv_veneer)
    RecyclerView mRvVeneer;

    /**
     * 设备列表
     */
    private List<Device> mDevices = new ArrayList<>();

    private String PARAM_DEVICE = "device";

    public static DeviceFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_device;
    }

    @Override
    public void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);

    }

    @Override
    public void onViewCreatedTask(View view, Bundle savedInstanceState) {
        super.onViewCreatedTask(view, savedInstanceState);
        initData();
    }

    private void initData() {
        /*Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            mDevices = (List<Device>) bundle.getSerializable(PARAM_DEVICE);
        }*/
        List<Device> datas = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            Device device = new Device();
            device.setId(i);
            device.setMac("12345"+ i);
            datas.add(device);
        }
        //设置LayoutManager为LinearLayoutManager
        mRvVeneer.setLayoutManager(new GridLayoutManager(getActivity(),3));
        //设置Adapter
        mRvVeneer.setAdapter(new DeviceAdapter(getActivity(),datas));
    }


}
