package com.ido.otaclient.api;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-09 15:45
 * @description
 */
public class OkhttpUtil {
    private static final String TAG = "OkhttpUtil";
    private static volatile OkhttpUtil instance;
    public static Handler handler = new Handler();
    private OkhttpUtil callback;
    /**
     * 双重检测锁-单例模式
     *
     * @return
     */

    public static OkhttpUtil getInstance() {
        if (instance == null) {
            synchronized (OkhttpUtil.class) {
                if (instance == null) {
                    instance = new OkhttpUtil();
                }
            }
        }
        return instance;
    }


    /**
     * okhttp的同步请求
     * @param url
     */
    public void getSyn(final String url, OkhttpCallback okhttpCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建OkHttpClient对象
                    OkHttpClient client = new OkHttpClient();
                    //创建Request
                    Request request = new Request.Builder()
                            .url(url)//访问连接
                            .get()
                            .build();
                    //创建Call对象
                    Call call = client.newCall(request);
                    //通过execute()方法获得请求响应的Response对象
                     Response response = call.execute();
                    if (response.isSuccessful()) {
                        //处理网络请求的响应，处理UI需要在UI线程中处理
                        //...
                        if(okhttpCallback!=null){
                            okhttpCallback.onSuccess(response.body().string());
                        }
                        //String str = response.body().string();
                        //Log.d(TAG,"成功" + str);
                    }else {
                        if(okhttpCallback!=null){
                            okhttpCallback.onFailed("失败");
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG,e.toString() + " ++++");
                    okhttpCallback.onFailed("失败");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * okttp的异步求情
     */
    public void getDataAsync(String url, OkhttpCallback okhttpCallback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(okhttpCallback != null){
                    okhttpCallback.onFailed("失败");
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    /*Log.d("kwwl","获取数据成功了");
                    Log.d("kwwl","response.code()=="+response.code());
                    Log.d("kwwl","response.body().string()=="+response.body().string());*/
                    String str = response.body().toString();
                    okhttpCallback.onSuccess(str);
                }
            }
        });
    }


    /**
     * post的方式球球
     */
    public void postDataWithParame() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("username","zhangsan");//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://www.baidu.com")
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
            //。。。
        });//回调方法的使用与get异步请求相同，此时略。
    }



    public interface OkhttpCallback {

        /**
         * 访问成功
         */
        void onSuccess(String success);

        /**
         * 访问失败
         */
        void onFailed(String message);
    }



}
