package com.ido.otaclient.four;

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
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.ido.otaclient.R;
import com.ido.otaclient.api.OkhttpUtil;
import com.ido.otaclient.base.ApiConstant;
import com.ido.otaclient.base.BaseActivity;
import com.ido.otaclient.base.Constant;
import com.ido.otaclient.dialog.WhiteConfirmDialog;
import com.ido.otaclient.util.FileUtil;
import com.ido.otaclient.util.GZipUtil;
import com.ido.otaclient.util.ResourceUtil;
import com.ido.otaclient.util.ToastUtils;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-13 15:13
 * @description
 */
public class FourToOneActivity extends BaseActivity {
    /**
     * bootloader 文件选择
     */
    @BindView(R.id.iv_choose_bootloader)
    ImageButton mIvChooseBootloader;
    /**
     * bootloader 文件版本
     */
    @BindView(R.id.et_version_bootloader)
    EditText mEtVersionBootloader;
    /**
     * bootloader 文件路径
     */
    @BindView(R.id.tv_choose_file_bootloader)
    TextView mTvChooseFileBootloader;
    /**
     * 特殊固件
     */
    @BindView(R.id.iv_choose_special)
    ImageButton mIvChooseSpecial;
    /**
     * 特殊固件
     */
    @BindView(R.id.et_version_special)
    EditText mEtVersionSpecial;
    /**
     * 特殊文件名称
     */
    @BindView(R.id.tv_file_version_special)
    TextView mTvFileVersionSpecial;
    /**
     * 字库文件
     */
    @BindView(R.id.iv_choose_font)
    ImageButton mIvChooseFont;
    /**
     * 字库文件版本
      */
    @BindView(R.id.et_version_font)
    EditText mEtVersionFont;
    /**
     * 字库文件路径
     */
    @BindView(R.id.tv_file_version_font)
    TextView mTvFileVersionFont;
    /**
     * 最新固件
     */
    @BindView(R.id.iv_choose_new)
    ImageButton mIvChooseNew;
    /**
     * 最新固件版本
     */
    @BindView(R.id.et_version_new)
    TextView mEtVersionNew;
    /**
     * 最新固件路径
     */
    @BindView(R.id.tv_file_version_new)
    TextView mTvFileVersionNew;
    /**
     * bootloader 选择字库文件
     */
    private static final int REQUEST_CODE_B = 1000;
    /**
     * 选择特殊固件
     */
    private static final int REQUEST_CODE_SPECIAL = 1001;
    /**
     * 选择字库文件
     */
    private static final int REQUEST_CODE_FONT = 1002;
    /**
     * 选择最新固件
     */
    private static final int REQUEST_CODE_NEW = 1003;

    /**
     * 确认取消弹框
     */
    private WhiteConfirmDialog mWhiteConfirmDialog;

    /**
     * 任务名称
     */
    private String taskName = "fourToOne";

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity,FourToOneActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);
        setContentView(R.layout.activity_four_to_one);
    }



    @OnClick(R.id.title_leftBtn)
    public void back(View view){
        ActivityCompat.finishAfterTransition(this);
    }


    @OnClick(R.id.iv_choose_bootloader)
    public void toChooseBootloader(View view){
        chooseFile(REQUEST_CODE_B);
    }

    @OnClick(R.id.iv_choose_special)
    public void toChooseSpecial(View view){
        chooseFile(REQUEST_CODE_SPECIAL);
    }

    @OnClick(R.id.iv_choose_font)
    public void toChooseFont(View view){
        chooseFile(REQUEST_CODE_FONT);
    }

    @OnClick(R.id.iv_choose_new)
    public void toChooseNew(View view){
        chooseFile(REQUEST_CODE_NEW);
    }

    @OnClick(R.id.btn_confirm)
    public void toConfirmConfig(View view){
        uploadAndConfirmFile();
    }


    /**
     * 上传文件
     * @param url
     * @param filePath
     */
    private void uploadFile(String url, String filePath,String fileBName,String fileVersionB,
                            String fileSpecialName,String fileVersionSpecial,String fileFontName,
                            String fileFontVersion, String fileNewName,String fileVersionNew) {
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
                confirmUploadFile(url,sizeStr,fileBName,fileVersionB,
                        fileSpecialName,fileVersionSpecial,fileFontName,fileFontVersion,
                        fileNewName,fileVersionNew);
            }
        });
    }


    /**
     * 文件确认
     * @param url
     * @param sizeStr
     */
    private void confirmUploadFile(String url, String sizeStr,String fileBName,String fileVersionB,
                                   String fileSpecialName,String fileVersionSpecial,String fileFontName,
                                   String fileFontVersion, String fileNewName,String fileVersionNew) {
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
        sb.append("fileBName=");
        sb.append(fileBName);
        sb.append("&");
        sb.append("fileVersionB=");
        sb.append(fileVersionB);
        sb.append("&");
        sb.append("fileSpecialName=");
        sb.append(fileSpecialName);
        sb.append("&");
        sb.append("fileVersionSpecial=");
        sb.append(fileVersionSpecial);
        sb.append("&");
        sb.append("fileFontName=");
        sb.append(fileFontName);
        sb.append("&");
        sb.append("fileFontVersion=");
        sb.append(fileFontVersion);
        sb.append("&");
        sb.append("fileNewName=");
        sb.append(fileNewName);
        sb.append("&");
        sb.append("fileVersionNew=");
        sb.append(fileVersionNew);
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
     * todo升级类型
     */
    private void saveUploadType() {
        uploadAndConfirmFile();
    }


    private void showConfirmCancel() {
        mWhiteConfirmDialog = WhiteConfirmDialog.newInstance(
                ResourceUtil.getString(R.string.upload_file_fail),
                ResourceUtil.getString(R.string.confirm),
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

    /**
     * 上传固件
     */
    private void uploadAndConfirmFile() {
        String filePathB = mTvChooseFileBootloader.getText().toString();
        String fileVersionB = mEtVersionBootloader.getText().toString();
        String filePathSpecial = mTvFileVersionSpecial.getText().toString();
        String fileVersionSpecial = mEtVersionSpecial.getText().toString();
        String filePathFont = mTvFileVersionFont.getText().toString();
        String fileFontVersion = mEtVersionFont.getText().toString();
        String filePathNew = mTvFileVersionNew.getText().toString();
        String fileVersionNew = mEtVersionNew.getText().toString();

        if(TextUtils.isEmpty(filePathB)){
            ToastUtils.show("请选择bootloader文件");
            return;
        }
        if(TextUtils.isEmpty(fileVersionB)){
            ToastUtils.show("请输入bootloader文件版本");
            return;
        }
        if(TextUtils.isEmpty(filePathSpecial)){
            ToastUtils.show("请选择特殊固件");
            return;
        }
        if(TextUtils.isEmpty(fileVersionSpecial)){
            ToastUtils.show("请输入特殊固件版本");
            return;
        }
        if(TextUtils.isEmpty(filePathFont)){
            ToastUtils.show("请选择字库固件");
            return;
        }
        if(TextUtils.isEmpty(fileFontVersion)){
            ToastUtils.show("请输入字库文件版本");
            return;
        }
        if(TextUtils.isEmpty(filePathNew)){
            ToastUtils.show("请选择最新固件版本");
            return;
        }
        if(TextUtils.isEmpty(fileVersionNew)){
            ToastUtils.show("请输入最新固件版本");
            return;
        }
        //1、将所有的zip文件移动到一个文件夹中2、压缩文件3、上传给服务器器
        String pathTo = Constant.DIR_IN_SDCARD_UPLOAD;
        File fileFolder = new File(pathTo);
        if(fileFolder.exists()){
            fileFolder.delete();
            fileFolder.mkdirs();
        }else {
            fileFolder.mkdirs();
        }
        try {
            FileUtil.copyFile(filePathB,pathTo,false);
            FileUtil.copyFile(filePathSpecial,pathTo,false);
            FileUtil.copyFile(filePathFont,pathTo,false);
            FileUtil.copyFile(filePathNew,pathTo,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String pathFolderZip = Constant.DIR_IN_SDCARD_UPLOAD_zip;
        try {
            //压缩文件
            GZipUtil.ZipFolder(pathTo,pathFolderZip);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String fileBName = FileUtil.getFileNameFromPath(filePathB);
        String fileSpecialName = FileUtil.getFileNameFromPath(filePathSpecial);
        String fileFontName = FileUtil.getFileNameFromPath(filePathFont);
        String fileNewName = FileUtil.getFileNameFromPath(filePathNew);
        // todo 遍历上传文件
        uploadFile("",pathFolderZip,fileBName,fileVersionB,
                fileSpecialName,fileVersionSpecial,fileFontName,fileFontVersion,
                fileNewName,fileVersionNew);
    }

    /**
     * 选择文件
     * @param reuestCode
     */
    private void chooseFile(int reuestCode){
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
        this.startActivityForResult(intent, reuestCode);
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
        switch (requestCode){
            case REQUEST_CODE_B:
                mIvChooseBootloader.setImageDrawable(ResourceUtil.getDrawable(R.drawable.ic_collection));
                mTvChooseFileBootloader.setVisibility(View.VISIBLE);
                mTvChooseFileBootloader.setText(path +"");
                break;
            case REQUEST_CODE_SPECIAL:
                mIvChooseSpecial.setImageDrawable(ResourceUtil.getDrawable(R.drawable.ic_collection));
                mTvFileVersionSpecial.setVisibility(View.VISIBLE);
                mTvFileVersionSpecial.setText(path +"");
                break;
            case REQUEST_CODE_FONT:
                mIvChooseFont.setImageDrawable(ResourceUtil.getDrawable(R.drawable.ic_collection));
                mTvFileVersionFont.setVisibility(View.VISIBLE);
                mTvFileVersionFont.setText(path +"");
                break;
            case REQUEST_CODE_NEW:
                mIvChooseNew.setImageDrawable(ResourceUtil.getDrawable(R.drawable.ic_collection));
                mTvFileVersionNew.setVisibility(View.VISIBLE);
                mTvFileVersionNew.setText(path +"");
                break;
            default:
                break;
        }

    }


}
