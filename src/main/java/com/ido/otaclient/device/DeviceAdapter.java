package com.ido.otaclient.device;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ido.otaclient.R;
import com.ido.otaclient.module.Device;

import java.util.List;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-10 15:09
 * @description
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {
    //当前上下文对象
    Context context;
    //RecyclerView填充Item数据的List对象
    List<Device> datas;

    public DeviceAdapter(Context context, List<Device> datas){
        this.context = context;
        this.datas = datas;
    }

    //创建ViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化得到Item布局文件的View对象
        View v = View.inflate(context, R.layout.item_device,null);
        //返回MyViewHolder的对象
        return new MyViewHolder(v);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.textView.setText(datas.get(position));
        holder.textView.setText(datas.get(position).getMac());
    }

    //返回Item的数量
    @Override
    public int getItemCount() {
        return datas == null? 0 :datas.size();
    }

    //继承RecyclerView.ViewHolder抽象类的自定义ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_mac);
        }
    }
}
