package com.ido.otaclient.nordic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.ido.otaclient.R;
import com.ido.otaclient.UploadConfigPerfence;
import com.ido.otaclient.api.OkhttpUtil;
import com.ido.otaclient.base.ApiConstant;
import com.ido.otaclient.base.BaseActivity;
import com.ido.otaclient.base.Constant;
import com.ido.otaclient.base.IDLog;
import com.ido.otaclient.dialog.WhiteConfirmDialog;
import com.ido.otaclient.module.UploadConfig;
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

public class NordicActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView mTitleText;
    /**
     * 升级类型
     */
    @BindView(R.id.nordic_tab)
    RadioGroup mNodicTab;

    /**
     * 选择文件
     */
    @BindView(R.id.iv_choose)
    ImageButton mIvChoose;
    /**
     * 选择文件
     */
    @BindView(R.id.tv_choose_file)
    TextView mTvChooseFile;
    /**
     * 文件版本
     */
    @BindView(R.id.et_version)
    EditText mEtVersion;
    /**
     * 升级类型
     */
    private String mUpdateType = ApiConstant.UPDATE_TYPE_APPLICATION;

    private int REQUEST_CODE = 1000;

    /**
     * 确认取消弹框
     */
    private WhiteConfirmDialog mWhiteConfirmDialog;

    /**
     * 已连接的单板
     */
    private List<Veneer> mVeneerList = new ArrayList<>();

    private static final String EXTRA_VENEER_LIST = "veneer_list";
    /**
     * 任务名称
     */
    private String taskName = "nordic";


    public static void startActivity(Activity activity,List<Veneer> veneerList){
        Intent intent = new Intent(activity,NordicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_VENEER_LIST, (Serializable) veneerList);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);
        setContentView(R.layout.activity_nordic);

        initTitle();
        initData();
        initListener();
    }

    private void initListener() {
        mNodicTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tab_application:
                            mUpdateType = ApiConstant.UPDATE_TYPE_APPLICATION;
                        break;
                    case R.id.tab_bootloader:
                            mUpdateType = ApiConstant.UPDATE_TYPE_BOOTLOADER;
                        break;
                }
            }
        });
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mVeneerList = (List<Veneer>) bundle.getSerializable(EXTRA_VENEER_LIST);
            for(Veneer veneer : mVeneerList){
                IDLog.d(TAG, "initData: " + veneer.getIp());
            }
        }
    }

    private void initTitle() {
        mTitleText.setText("Nordic升级");
    }

    @OnClick(R.id.title_leftBtn)
    public void back(View view){
        ActivityCompat.finishAfterTransition(this);
    }

    @OnClick(R.id.btn_confirm)
    public void confirmUpdate(View view){
        //showConfirmCancel();
        uploadAndConfirmFile();

        //确认配置 1、上传文件 2、确认配置


    }

    /**
     * 文件上传与确定
     */
    private void uploadAndConfirmFile() {
        String filePath = mTvChooseFile.getText().toString();
        String fileVersion = mEtVersion.getText().toString();
        if(TextUtils.isEmpty(filePath)){
            ToastUtils.show(ResourceUtil.getString(R.string.upload_choose_file));
            return;
        }
        if(TextUtils.isEmpty(fileVersion)){
            ToastUtils.show(ResourceUtil.getString(R.string.upload_input_version));
            return;
        }
        if(mVeneerList == null || mVeneerList.size() == 0){
            ToastUtils.show("获取单板为空，请重新扫描");
            return;
        }
        for(Veneer veneer : mVeneerList){
            if(!TextUtils.isEmpty(veneer.getIp())){
                String url = ApiConstant.URL_HEAD + veneer.getIp() +":"+Constant.HTTP_PORT;
                uploadFile(url,filePath,fileVersion);
            }
        }

    }

    /**
     * 上传文件
     * @param url
     * @param filePath
     */
    private void uploadFile(String url, String filePath,String fileVersion) {
        long size = FileUtil.getFileSize(filePath);
        String sizeStr = String.valueOf(size);
        
        AsyncHttpPost post = new AsyncHttpPost(url + "/upload");
            MultipartFormDataBody body = new MultipartFormDataBody();
            body.addFilePart("text.zip", new File(filePath));
            // 文件名传递
            body.addStringPart("foo", "upload.zip");
            post.setBody(body);
            AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
                @Override
                public void onCompleted(Exception ex, AsyncHttpResponse source, String result) {
                    if (ex != null) {
                        showConfirmCancel();
                        ex.printStackTrace();
                        return;
                    }
                    System.out.println("Server says: " + result);
                    //确认成功
                    confirmUploadFile(url,sizeStr,fileVersion);
                }
            });
    }

    /**
     * 文件确认
     * @param url
     * @param sizeStr
     */
    private void confirmUploadFile(String url, String sizeStr,String fileVersion) {
        String name = FileUtil.getFileNameFromPath(url);
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        sb.append("/");
        sb.append(ApiConstant.METHOD_CONFIRM_FIREWARE);
        sb.append("?");
        sb.append("size=");
        sb.append(sizeStr);
        sb.append("&");
        sb.append("taskType=");
        sb.append(taskName);
        sb.append("&");
        sb.append("fileName=");
        sb.append(name);
        sb.append("&");
        sb.append("uploadType=");
        sb.append(mUpdateType);
        sb.append("&");
        sb.append("fileVersion=");
        sb.append(fileVersion);
        url = sb.toString();
        /*url = url + "/" + ApiConstant.METHOD_CONFIRM_FIREWARE+ "?"+"size=" +size+"&"+
                "taskType=" + "&"+"fileName=" + "fileName" ;*/
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
                //保存升级类型到
                saveUploadType();
            }

            @Override
            public void onFailed(String message) {
                //mTvtest.setText(message);
                Log.d(TAG,message );
                //todo
                showConfirmCancel();
            }
        });
    }

    /**
     * 保存
     */
    private void saveUploadType() {
        UploadConfigPerfence uploadConfigPerfence = UploadConfigPerfence.getInstance();
        String filePath = mTvChooseFile.getText().toString();
        String fileName = FileUtil.getFileNameFromPath(filePath);
        String fileVersion = mEtVersion.getText().toString();
        UploadConfig uploadConfig = new UploadConfig();
        uploadConfig.setTaskName(taskName);
        uploadConfig.setUploadName(mUpdateType);
        uploadConfig.setFirewareNormalName(fileName);
        uploadConfig.setFirewareNormalVersion(fileVersion);
        uploadConfigPerfence.saveUploadConfig(uploadConfig);

    }


    private void showConfirmCancel() {
        mWhiteConfirmDialog = WhiteConfirmDialog.newInstance(
                ResourceUtil.getString(R.string.upload_file_fail),ResourceUtil.getString(R.string.confirm),
                ResourceUtil.getString(R.string.cancel),true);
        mWhiteConfirmDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新下发固件
                uploadAndConfirmFile();
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
        mWhiteConfirmDialog.show(getSupportFragmentManager());
    }

    @OnClick(R.id.iv_choose)
    public void ChooseFile(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String fileFolder = Constant.DIR_OUT_SDCRA;
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
    }


}
