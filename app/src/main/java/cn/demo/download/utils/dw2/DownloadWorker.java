package cn.demo.download.utils.dw2;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 下载逻辑的同步执行器
 */
public class DownloadWorker {
    private final static DownloadWorker DOWNLOAD_WORKER = new DownloadWorker();
    
    private DownloadWorker() {
    }
    
    public static DownloadWorker getInstance() {
        return DOWNLOAD_WORKER;
    }
    
    public void download(final String dir, final String fileName, final String url) {
        try {
            download(isExistDir(dir), fileName, url);
        } catch (Exception e) {
            loge(e);
        }
    }
    
    
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        return downloadFile.getAbsolutePath();
    }
    
    public void download(final File dir, final String fileName, final String url) {
        //删除以前下载过的补丁文件和缓存占位文件
        File file = new File(dir, fileName);
        if (file.exists()) {
            boolean del = file.delete();
            logd(String.format("del old file %s", del));
        }
        
        File cacheFile = new File(dir, String.format("%scache", fileName));
        if (cacheFile.exists()) {
            boolean del = cacheFile.delete();
            logd(String.format("del cache file %s", del));
        }
        
        httpConnectionDownload(url, dir, fileName);
    }
    
    private void httpConnectionDownload(String urlStr, File filePath, String fileName) {
        HttpsURLConnection con = null;
        InputStream in = null;
        try {
            URL url = new URL(urlStr);
            con = (HttpsURLConnection) url.openConnection();
            con.setHostnameVerifier(NOT_VERYFY);
            con.setSSLSocketFactory(getSSLFactory());
            con.setConnectTimeout(30 * 1000);
            con.setReadTimeout(10 * 60 * 1000);
            con.setUseCaches(true);
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // 文件大小
                long fileLength = con.getContentLength();
                logd("start download file length : " + fileLength);
                // 文件名
                File result = new File(filePath, fileName);
                File dir = result.getParentFile();
                if (!dir.exists() && !dir.mkdirs()) {
                    logd("mk dir error : " + dir);
                    return;
                }
                //先往临时的缓存文件路径中写入
                File cache = new File(filePath, fileName.concat("cache"));
                if (cache.exists() && !cache.delete()) {
                    logd("delete cache error : " + cache.getName());
                    return;
                }
                in = con.getInputStream();
                writeToFile(in, cache);
                //写入完成后 重命名
                if (!cache.renameTo(result)) {
                    logd("rename cache error");
                    return;
                }
                if (fileLength == -1 || result.length() == fileLength) {
                    logd("finish download file : " + result.getName());
                } else {
                    boolean del = result.delete();
                    logd(String.format("file length error delete %s", del));
                }
            } else {
                logd(String.format(Locale.getDefault(), "code:%d DOWNLOAD_WORKER info:%s", responseCode, con.getHeaderFields().toString()));
            }
        } catch (Throwable e) {
            loge(e);
        } finally {
            close(in);
            close(con);
        }
    }
    
    @SuppressLint("BadHostnameVerifier")
    public static final HostnameVerifier NOT_VERYFY = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };
    
    public static SSLSocketFactory getSSLFactory() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new ClX509TrustManager()}, new java.security.SecureRandom());
            return sslcontext.getSocketFactory();
        } catch (Throwable ignored) {
        }
        return null;
    }
    
    @SuppressLint("TrustAllX509TrustManager")
    public static class ClX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            
        }
        
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    
    public static void close(HttpURLConnection c) {
        try {
            if (c != null) {
                c.disconnect();
            }
        } catch (Throwable ignored) {
        }
    }
    
    public static void close(Closeable c) {
        try {
            c.close();
            if (c != null) {
                c.close();
            }
        } catch (Throwable ignored) {
        }
    }
    
    
    public static void copy(InputStream in, OutputStream out) throws IOException {
        int size;
        byte[] buf = new byte[4096];
        while ((size = in.read(buf)) != -1) {
            out.write(buf, 0, size);
        }
        out.flush();
    }
    
    public static void writeToFile(InputStream in, File file) {
        FileOutputStream out = null;
        try {
            copy(in, out = new FileOutputStream(file));
        } catch (Throwable e) {
            file.delete();
        } finally {
            close(out);
        }
    }
    
    public static void writeToFile(byte[] bytes, File file) {
        FileOutputStream out = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            out.write(bytes);
            logd("write bytes success");
        } catch (Throwable e) {
            logd("write bytes error", e);
        } finally {
            close(out);
        }
    }
    
    private static void logd(String info) {
        Log.e("sanbo", info);
    }
    
    private static void logd(String info, Throwable e) {
        Log.e("sanbo", info + "----" + Log.getStackTraceString(e));
    }
    
    private static void loge(Throwable e) {
        Log.e("sanbo", Log.getStackTraceString(e));
    }
}
