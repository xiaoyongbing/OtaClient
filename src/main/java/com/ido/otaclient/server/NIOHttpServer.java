package com.ido.otaclient.server;

import android.text.TextUtils;
import android.util.Log;

import com.ido.otaclient.base.ApiConstant;
import com.ido.otaclient.base.Constant;
import com.ido.otaclient.base.IDLog;
import com.ido.otaclient.env.AppEnv;
import com.ido.otaclient.util.DevUtil;
import com.ido.otaclient.util.WifiUtils;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class NIOHttpServer implements HttpServerRequestCallback {
    private static final String TAG = "NIOHttpServer";

    private static NIOHttpServer mInstance;

    FileUploadHolder fileUploadHolder = new FileUploadHolder();

    AsyncHttpServer server = new AsyncHttpServer();

    public static NIOHttpServer getInstance() {
        if (mInstance == null) {
            // 增加类锁,保证只初始化一次
            synchronized (NIOHttpServer.class) {
                if (mInstance == null) {
                    mInstance = new NIOHttpServer();
                }
            }
        }
        return mInstance;
    }

    //仿照nanohttpd的写法
    public static enum Status {
        REQUEST_OK(200, "请求成功"),
        REQUEST_ERROR(500, "请求失败"),
        REQUEST_ERROR_API(501, "无效的请求接口"),
        REQUEST_ERROR_CMD(502, "无效命令"),
        REQUEST_ERROR_DEVICEID(503, "不匹配的设备ID"),
        REQUEST_ERROR_ENV(504, "不匹配的服务环境");

        private final int requestStatus;
        private final String description;

        Status(int requestStatus, String description) {
            this.requestStatus = requestStatus;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public int getRequestStatus() {
            return requestStatus;
        }
    }

    /**
     * 开启本地服务
     */
    public void startServer() {
        IDLog.d(TAG, "startServer: ");
        //如果有其他的请求方式，例如下面一行代码的写法
        server.addAction("OPTIONS", "[\\d\\D]*", this);
        server.get("[\\d\\D]*", this);
        server.post("[\\d\\D]*", this);
        server.listen(Constant.HTTP_PORT);
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        Log.d(TAG, "进来了，哈哈");
        //Toast.makeText(,"进来了，哈哈",Toast.LENGTH_LONG).show();
        String uri = request.getPath();
        IDLog.d(TAG, "onRequest: " + uri);
        //切割地址
        if(uri.contains("?")){
            String[] uris = uri.split("\\?");
            uri = uris[0];
        }
        IDLog.d(TAG, "onRequest: " + uri);
        //这个是获取header参数的地方，一定要谨记哦
        Multimap headers = request.getHeaders().getMultiMap();
        Log.d(TAG, uri+"uri");
        if (checkUri(uri)) {// 针对的是接口的处理
            //注意：这个地方是获取post请求的参数的地方，一定要谨记哦
            Multimap parms = ((AsyncHttpRequestBody<Multimap>)request.getBody()).get();
            if (headers != null) {
                //LogUtil.d(TAG, headers.toString());
                Log.d(TAG, "headers="+headers.toString());
            }
            if (parms != null) {
                Log.d(TAG, "parms = " + parms.toString());
            }

            if (TextUtils.isEmpty(uri)) {
                throw new RuntimeException("无法获取请求地址");
            }

            if ("OPTIONS".toLowerCase().equals(request.getMethod().toLowerCase())) {
                //LogUtil.d(TAG, "OPTIONS探测性请求");
                //addCORSHeaders(Status.REQUEST_OK, response);
                return;
            }
            //Log.d(TAG, "onRequest: " + parms.toString());
            switch (uri) {
                case "/test": {//接口2
                        Log.d(TAG, "onRequest: ");
                        getTestResponse(response);
                    }
                    break;
                case "/"+ ApiConstant.METHOD_VENEER_LIST:
                    //设备列表
                    getDeviceList(response);
                    //response.send("1" +"");
                    break;
                case "/" + ApiConstant.METHOD_TASK_START:
                    //todo 开始任务
                    startTask(response);
                    break;
                case "/" + ApiConstant.METHOD_TASK_STOP:
                    endTask(response);
                    break;
                case "/" + ApiConstant.METHOD_CONFIRM_FIREWARE:
                    Multimap parmsGet = request.getQuery();
                    String size = parmsGet.getString("size");
                    Log.d(TAG, "parmsGet = " + parmsGet.toString()+","+size);
                    confirmFireware(response);
                    break;
                case "/" + ApiConstant.METHOD_CHECK:
                    getStatus(response);
                    break;
                default: {
                    addHeaderResponse(Status.REQUEST_ERROR_API,response);
                }
            }
        } else {
            // 针对的是静态资源的处理
            Log.d(TAG, "onRequest: "+uri);
            String filePath = getFilePath(uri); // 根据url获取文件路径
            Log.d(TAG, "onRequest: "+uri +"," +filePath);
            saveFile(request,response);
            if (filePath == null) {
                //LogUtil.d(TAG, "sd卡没有找到");
                response.send("sd卡没有找到");
                return;
            }
            /*File file = new File(filePath);

            if (file != null && file.exists()) {
                Log.d(TAG, "file path = " + file.getAbsolutePath());

                response.sendFile(file);//和nanohttpd不一样的地方

            } else {
                Log.d(TAG, "file path = " + file.getAbsolutePath() + "的资源不存在");
            }*/
        }
    }

    private void getStatus(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认固件上传
     * @param response
     */
    private void confirmFireware(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束任务
     */
    private void endTask(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始任务并返回状态给客户端
     */
    private void startTask(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回列表
     * @param response
     */
    private void getDeviceList(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            jsonObject.put("ip", WifiUtils.getWifiIp(AppEnv.instance().getContext()));
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回一个测试信息
     * @param response
     */
    private void getTestResponse(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void saveFile(AsyncHttpServerRequest request,AsyncHttpServerResponse response){
        final MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
        Log.d(TAG, "saveFile: " + body.getContentType());
        //fileUploadHolder.setFileName("text.zip");
        body.setMultipartCallback((Part part) -> {
            if (part.isFile()) {
                body.setDataCallback((DataEmitter emitter, ByteBufferList bb) -> {
                    Log.d(TAG,bb.toString()+"++++buffer");
                    fileUploadHolder.write(bb.getAllByteArray());
                    bb.recycle();
                });
            } else {
                if (body.getDataCallback() == null) {
                    body.setDataCallback((DataEmitter emitter, ByteBufferList bb) -> {
                        try {
                            String fileName = URLDecoder.decode(new String(bb.getAllByteArray()),
                                    "UTF-8");
                            Log.d(TAG,fileName+"+++saveFile");
                            //fileUploadHolder.setFileName(fileName);
                            //fileUploadHolder.write(fileUploadHolder.getFileOutPutStream());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        bb.recycle();
                    });
                }
            }
        });

        request.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                fileUploadHolder.reset();
                //上传成功
                getFileUpload(response);
                //response.end();
            }
        });

    }

    /**
     * 返回
     * @param response
     */
    private void getFileUpload(AsyncHttpServerResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("errcode", Status.REQUEST_OK.requestStatus);
            jsonObject.put("errmsg", Status.REQUEST_OK.description);
            response.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void addHeaderResponse(Status requestErrorApi, AsyncHttpServerResponse response) {
        response.send(requestErrorApi.description);
    }



    /**
     *
     * @param uri
     * @return
     */
    private String getFilePath(String uri) {
        return "";
    }

    /**
     * 判断是否是个uri
     * @param uri
     * @return
     */
    private boolean checkUri(String uri) {
        if(!uri.contains("upload")){
            return true;
        }else {
            return false;
        }
    }

    public void stop() {
        Log.d(TAG, "Stopping http server...");
        mInstance.stop();
    }

    public class FileUploadHolder {
        private String fileName;
        private File recievedFile;
        private BufferedOutputStream fileOutPutStream;
        private long totalSize;
        File dst = new File(Constant.DIR, "test.zip");
        FileOutputStream fos;
        {
            try {
                fos = new FileOutputStream(dst);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        public BufferedOutputStream getFileOutPutStream() {
            return fileOutPutStream;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
            totalSize = 0;
            if (!Constant.DIR.exists()) {
                Constant.DIR.mkdirs();
            }
            this.recievedFile = new File(Constant.DIR, this.fileName);
            //imber.d(recievedFile.getAbsolutePath());
            try {
                fileOutPutStream = new BufferedOutputStream(new FileOutputStream(recievedFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void reset() {
            if (fileOutPutStream != null) {
                try {
                    fileOutPutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileOutPutStream = null;
        }

        public void write(byte[] data) {

            try {
                // = new FileOutputStream(dst);
                fos.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*try {
                this.fileName = "test.txt";
                this.recievedFile = new File(Constants.DIR, this.fileName);

                fileOutPutStream = new BufferedOutputStream(new FileOutputStream(recievedFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (fileOutPutStream != null) {
                *//*try {
                    fileOutPutStream.write(data);

                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
            }
            totalSize += data.length;
            Log.d(TAG, "write: " + data.length +","+ data.toString());
            try {
                fileOutPutStream.write(data,0, (int) totalSize);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            /*if (fileOutPutStream != null) {
                try {
                    fileOutPutStream.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            totalSize += data.length;
        }
    }
}
