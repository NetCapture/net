package cn.demo.download;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    private String name = "BD超清中字";
    private String videoName = "西柏坡2:英雄王二小";
    private String videoUrl = "http://jingdian.jiucai-zuida.com/20200916/32007_ccff6792/西柏坡2：英雄王二小-超清.mp4";
    
    @Override
    protected void onResume() {
        super.onResume();
        
        
    }
    
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDown1:
                FileDownloader.getImpl().create(videoUrl)
                        .setPath("/sdcard/Download")
                        .setListener(new FileDownloadListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                logi("pending: " + soFarBytes + "/" + totalBytes);
                            }
                            
                            @Override
                            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                                logi("connected ");
                            }
                            
                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                logi("progress : " + soFarBytes + "/" + totalBytes);
                            }
                            
                            @Override
                            protected void blockComplete(BaseDownloadTask task) {
                                logi("blockComplete ");
                            }
                            
                            @Override
                            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                                logi("retry ");
                                
                            }
                            
                            @Override
                            protected void completed(BaseDownloadTask task) {
                                logi("completed ");
                            }
                            
                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                logi("paused : " + soFarBytes + "/" + totalBytes);
                            }
                            
                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {
                                logi("error: " + Log.getStackTraceString(e
                                ));
                            }
                            
                            @Override
                            protected void warn(BaseDownloadTask task) {
                                logi("warn");
                            }
                        }).start();
                break;
            case R.id.btnDown2:
                break;
            case R.id.btnDown3:
                break;
            default:
                break;
        }
    }
    
    private void logi(String info) {
        Log.i("sanbo", info);
    }
}