package util;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class OSSUpload {
    public static void uploadOSS(Context context, String fileName, String filePath) {
        // 使用明文Access Key ID和Access Key Secret创建一个凭证提供者
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(OSS_config.OSS_ACCESS_KEY_ID, OSS_config.OSS_ACCESS_KEY_SECRET);
        // 创建OSSClient实例
        OSSClient oss = new OSSClient(context, OSS_config.OSS_ENDPOINT, credentialProvider);
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(OSS_config.BUCKET_NAME, fileName, filePath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
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
         task.waitUntilFinished(); // 等待上传完成。
    }

    public static String getUserAvatarPrefix() {
        return "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/userAvatar/";
    }

    public static String getClubAvatarPrefix() {
        return "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/clubAvatar/";
    }

    public static String getActCoverPrefix() {
        return "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/actCover/";
    }
}
