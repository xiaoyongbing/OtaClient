package com.ido.otaclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ido.otaclient.base.IDLog;
import com.ido.otaclient.server.NIOHttpServer;

public class HttpService extends Service {
    private static final String TAG = "HttpService";
    public HttpService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IDLog.d(TAG, "onCreate: ");
        NIOHttpServer.getInstance().startServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NIOHttpServer.getInstance().stop();
    }
}
