package com.wdweblib.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by x-sir on 2020-02-25 :)
 * Function:使用 HttpURLConnection 下载文件的 Task
 */
public class DownloadTask extends AsyncTask<String, Void, Void> {

    private String url;
    private String destPath;
    private String postParams;
    private WeakReference<Activity> weakReference;
    private static final String TAG = "DownloadTask";

    public DownloadTask(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    public DownloadTask(Activity activity, String postParams) {
        this.weakReference = new WeakReference<>(activity);
        this.postParams = postParams;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "开始下载...");
        ToastUtils.showToast("开始下载...");
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.i(TAG, "doInBackground(),url:" + params[0] + ",dest:" + params[1]);
        url = params[0];
        destPath = params[1];

        if (TextUtils.isEmpty(postParams)) {
            getData();
        } else {
            postData();
        }

        return null;
    }

    private void getData() {
        boolean useHttps = url.startsWith("https");
        if (useHttps) {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        }

        OutputStream out = null;
        HttpURLConnection urlConnection = null;

        try {

            String fileName = UriUtils.getDownloadFileName(url);
            url = url.substring(0, url.lastIndexOf("/") + 1) + URLEncoder.encode(fileName, "utf-8");
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);

//            String headerField = urlConnection.getHeaderField("Content-disposition");
//            Log.i(TAG,"getHeaderField : "+headerField);
            Log.i(TAG, "getResponseCode : " + urlConnection.getResponseCode());
            InputStream in = urlConnection.getInputStream();
            out = new FileOutputStream(destPath);

            byte[] buffer = new byte[10 * 1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();

        } catch (IOException e) {
            Log.i(TAG, "error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void postData() {
        boolean useHttps = url.startsWith("https");
        if (useHttps) {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        }

        OutputStream out = null;
        DataOutputStream dos = null;
        HttpURLConnection connection = null;

        try {
            URL url1 = new URL(url);

            connection = (HttpURLConnection) url1.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();

            dos = new DataOutputStream(connection.getOutputStream());

            dos.writeBytes(postParams);
            dos.flush();
            dos.close();

            InputStream in = connection.getInputStream();
            out = new FileOutputStream(destPath);
            byte[] buffer = new byte[10 * 1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i(TAG, "下载完成~");
        ToastUtils.showToast("下载完成~");
        Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = getMIMEType(url);
        File file = new File(destPath);
        Uri uri = getUri(file);
        Log.i(TAG, "mimiType:" + mimeType + ",uri:" + uri);
        handlerIntent.setDataAndType(uri, mimeType);
        Activity activity = weakReference.get();
        if (activity != null) {
            try {
                activity.startActivity(handlerIntent);
            } catch (ActivityNotFoundException e) {
                ToastUtils.showToast("没有找到可打开的应用~");
            }
        }
    }

    private String getMIMEType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (TextUtils.isEmpty(extension)) {
            extension = "xls";
        }

        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        return type;
    }

    private Uri getUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 设置7.0以上共享文件，分享路径定义在xml/file_paths.xml
            Activity activity = weakReference.get();
            String authority = activity.getPackageName() + ".fileProvider";
            Log.i(TAG, "authority: " + authority + " ; file " + file.getAbsolutePath());
            uri = FileProvider.getUriForFile(activity, authority, file);
        } else {
            // 7.0以下,共享文件
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
