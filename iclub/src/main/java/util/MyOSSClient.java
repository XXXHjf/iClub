package util;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;

public class MyOSSClient {
    public static String accessKeyId = "LTAI5t8LpFuo42FoEUQ8bK5i";
    public static String accessKeySecret = "jbIFKjCWmErmQVfBmzMH2E2KHTwjbf";
    public static String endPoint = "https://oss-cn-hangzhou.aliyuncs.com";

    public static boolean initOSSClient(Context context) {
        boolean rs = false;

        // 从STS服务获取的安全令牌（SecurityToken）。
        String securityToken = "yourSecurityToken";

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(OSS_config.OSS_ACCESS_KEY_ID, OSS_config.OSS_ACCESS_KEY_SECRET);
        // 创建OSSClient实例。
        OSSClient oss = new OSSClient(context, OSS_config.OSS_ENDPOINT, credentialProvider);

        // 填写存储空间名称。
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(OSS_config.BUCKET_NAME);
        // 设置存储空间的访问权限为公共读，默认为私有读写。
        createBucketRequest.setBucketACL(CannedAccessControlList.PublicRead);
        // 指定存储空间所在的地域。
        createBucketRequest.setLocationConstraint("oss-cn-hangzhou");
        OSSAsyncTask createTask = oss.asyncCreateBucket(createBucketRequest, new OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>() {
            @Override
            public void onSuccess(CreateBucketRequest request, CreateBucketResult result) {
                Log.d("locationConstraint", request.getLocationConstraint());

            }
            @Override
            public void onFailure(CreateBucketRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                    clientException.printStackTrace();
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
        return rs;
    }
}
