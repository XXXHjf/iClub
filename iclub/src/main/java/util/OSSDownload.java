package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;


public class OSSDownload {
    private static String TAG = "OSS下载文件调试";
    public static void downloadOSS(Context context, String fileName, BitmapCallback callback) {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(OSS_config.TEP_ACCESS_KEY_ID, OSS_config.TRP_ACCESS_KEY_SECRET, OSS_config.TEP_ACCESS_STS_TOKEN);
        // 创建OSSClient实例。
        OSSClient oss = new OSSClient(context, OSS_config.OSS_ENDPOINT, credentialProvider);
        // 构造下载文件请求。
        GetObjectRequest get = new GetObjectRequest(OSS_config.BUCKET_NAME, fileName);

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功。
                Log.e(TAG, "DownloadSuccess");
                Log.e(TAG, "" + result.getContentLength());

                InputStream inputStream = result.getObjectContent();
                // 将 InputStream 转换为 Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Log.e(TAG, bitmap.toString());

                // 调用回调方法将 Bitmap 返回
                callback.onBitmapLoaded(bitmap);
            }

            @Override
            // GetObject请求成功，返回GetObjectResult。GetObjectResult包含一个输入流的实例。您需要自行处理返回的输入流。
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // 等待任务完成。
        task.waitUntilFinished();
    }

}
