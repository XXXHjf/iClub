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
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class OSSUpload {
    /**
     * 上传图片到OSS里
     * @param context  上下文
     * @param fileName  文件名(例：userAvatar/1316546513.jpg、actCover/9984879872.png)，/前的数据决定存入OSS的文件夹
     * @param filePath  本地文件的实际路径
     */
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

    /**
     * 修改个人头像时获取完整URL路径的前缀
     * @return  路径前缀
     */
    public static String getUserAvatarPrefix() {
        return "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/userAvatar/";
    }

    /**
     * 上传社团Logo时获取完整URL路径的前缀
     * @return  路径前缀
     */
    public static String getClubAvatarPrefix() {
        return "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/clubAvatar/";
    }

    /**
     * 上传活动封面时获取完整URL路径的前缀
     * @return  路径前缀
     */
    public static String getActCoverPrefix() {
        return "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/actCover/";
    }
}
