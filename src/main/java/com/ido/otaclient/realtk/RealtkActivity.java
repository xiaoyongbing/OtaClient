package com.ido.otaclient.realtk;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.ido.otaclient.R;
import com.ido.otaclient.api.OkhttpUtil;
import com.ido.otaclient.base.ApiConstant;
import com.ido.otaclient.base.BaseActivity;
import com.ido.otaclient.base.Constant;
import com.ido.otaclient.base.IDLog;
import com.ido.otaclient.module.Veneer;
import com.ido.otaclient.util.FileUtil;
import com.ido.otaclient.util.ResourceUtil;
import com.ido.otaclient.util.ToastUtils;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RealtkActivity extends BaseActivity {
    private static final String TAG = "RealtkActivity";

    @BindView(R.id.title_text)
    TextView mTitleText;
    /**
     * 选择文件
     */
    @BindView(R.id.iv_choose)
    ImageButton mIvChoose;
    /**
     * 升级文件的版本
     */
    @BindView(R.id.et_version)
    EditText mEtVersion;
    /**
     * 选择文件的目录
     */
    @BindView(R.id.tv_choose_file)
    TextView mTvChooseFile;

    private String fileFolder = Environment.getExternalStorageDirectory() +
            File.separator + "DCIM";

    private int REQUEST_CODE = 1000;

    private static final String EXTRA_VENEER_LIST = "veneer_list";

    /**
     * 设备集合
     */
    private List<Veneer> mVeneerList = new ArrayList<>();

    public static void startActivity(Activity activity,List<Veneer> veneerList){
        Intent intent = new Intent(activity,RealtkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_VENEER_LIST, (Serializable) veneerList);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);
        setContentView(R.layout.activity_realtk);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            mVeneerList = (List<Veneer>) bundle.getSerializable(EXTRA_VENEER_LIST);

            for(Veneer veneer : mVeneerList){
                IDLog.d(TAG, "initData: " + veneer.getIp());
            }
        }
    }

    private void initView() {
        mTitleText.setText("Realtk升级");

    }

    @OnClick(R.id.title_leftBtn)
    public void back(View view){
        ActivityCompat.finishAfterTransition(this);
    }

    @OnClick(R.id.btn_confirm)
    public void toConfirm(View view){
        updateFile();
    }

    private void updateFile() {

        //getSyn();
        String url = "http://192.168.1.5:" + Constant.HTTP_PORT;
        //String BASE_URL_TEST = "http://192.168.1.6:5000";
        //getSyn(BASE_URL_TEST + "/test");
        /*try {
            ResponseCacheMiddleware.addCache(AsyncHttpClient.getDefaultInstance(),
                    getFileStreamPath("asynccache"),
                    1024 * 1024 * 10);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String filePath = Environment.getExternalStorageDirectory() + "/ota/" + "test.zip";
        long size = FileUtil.getFileSize(filePath);

        String sizeStr = String.valueOf(size);
        String filename = mTvChooseFile.getText().toString();
        if(!TextUtils.isEmpty(filePath)){
            AsyncHttpPost post = new AsyncHttpPost(url + "/upload");
            MultipartFormDataBody body = new MultipartFormDataBody();
            body.addFilePart("text.zip", new File(filePath));
            // 文件名传递
            body.addStringPart("foo", "bar");
            post.setBody(body);
            AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
                @Override
                public void onCompleted(Exception ex, AsyncHttpResponse source, String result) {
                    if (ex != null) {
                        ex.printStackTrace();
                        return;
                    }
                    System.out.println("Server says: " + result);
                    //确认成功
                    confirmUploadFile(url,sizeStr);
                }
            });
        }else {
            ToastUtils.show("文件不能为空");
        }
    }

    /**
     * 确认上传成功
     */
    private void confirmUploadFile(String url,String size) {
        StringBuffer sb = new StringBuffer();
        url = url + "/" + ApiConstant.METHOD_CONFIRM_FIREWARE+ "?"+"size=" +size+"&"+
                "taskType=" + "realtk&"+"fileName=" + "fileName" ;
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        okhttpUtil.getSyn(url, new OkhttpUtil.OkhttpCallback() {
            @Override
            public void onSuccess(String success) {
                // mTvtest.setText(success + ","+count);
                //todo 到达255次后跳转到设备列表界面
                Log.d(TAG, "onSuccess: " + success);
                Message message = new Message();
                message.what = 0;
                message.obj = success;
            }

            @Override
            public void onFailed(String message) {
                //mTvtest.setText(message);
                Log.d(TAG,message );
                //todo
            }
        });
    }



    @OnClick(R.id.iv_choose)
    public void toChooseFile(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        File file = new File(fileFolder);
        if (file.exists()) {
            //从指定目录下选择文件 这个现在不知道能不能实现 现在还是查询所有目录下的
            Uri uri = FileUtil.getUriFromFile(this, fileFolder);
            //Log.d(TAG, fileFolder + "," + uri.getPath());
            //intent.setDataAndType(uri, "*/*");
            intent.setType("*/*");
        } else {
            intent.setType("*/*");
        }
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        String path = "";
        Uri uri = data.getData(); // 获取用户选择文件的URI
        if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
            path = uri.getPath();
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = FileUtil.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                //path = FileUtil.getRealPathFromURI(this, uri);
            }
        }
        //todo 设置不同的文件
        //mIvChoose.setVisibility(View.GONE);
        mIvChoose.setImageDrawable(ResourceUtil.getDrawable(R.drawable.ic_collection));
        mTvChooseFile.setVisibility(View.VISIBLE);
        mTvChooseFile.setText(path +"");
        /*ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // 未查询到，说明为普通文件，可直接通过URI获取文件路径
            String path = uri.getPath();
            return;
        }
        if (cursor.moveToFirst()) {
            // 多媒体文件，从数据库中获取文件的真实路径

            mTvChooseFile.setVisibility(View.VISIBLE);
            String path = cursor.getString(cursor.getColumnIndex("_data"));
            mTvChooseFile.setText(path);

            Log.d(TAG, path + "文件路径");
        }
        cursor.close();*/
    }



}
