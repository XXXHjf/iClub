package util;

public class OSS_config {
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    public static final String OSS_ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
    public static final String BUCKET_NAME = "bucket-hjf";
    // RAM用户的ACCESS信息
    public static final String OSS_ACCESS_KEY_ID = "LTAI5tJgrjDHXonS6Rpg5bfZ";
    public static final String OSS_ACCESS_KEY_SECRET = "DfFbTNNTvTqapENn2CLt4Mt2pSKmxe";
    // 接口调用的STS令牌产生的临时ACCESS信息
    public static final String TEP_ACCESS_KEY_ID = "STS.NUW2NjdQg3hic4eE2VUmJ6nEh";
    public static final String TRP_ACCESS_KEY_SECRET = "B44ZJPhDxZhrVoMtmVotxapJYV3LLsmNEWh65yjgVaX3";
    public static final String TEP_ACCESS_STS_TOKEN = "CAIS+QF1q6Ft5B2yfSjIr5biefTeiY5GhKqCYRLUoTIDWeJm2avuijz2IHhMfnBhAuwXsv0xnGtZ6vgYlqR8RoNifnf/UfBM449LqYV0z3ht4p7b16cNrbH4M0rxYkeJ8a2/SuH9S8ynCZXJQlvYlyh17KLnfDG5JTKMOoGIjpgVBbZ+HHPPD1x8CcxROxFppeIDKHLVLozNCBPxhXfKB0ca0WgVy0EHsPnlmZLNtUCD1QyrkLdF+L6ceMb0M5NeW75kSMqw0eBMca7M7TVd8RAi9t0t1PQeoGac4oDBWgcJuETabbDOl9JiMAhifbMmALJJt+TsakG4XS8HJe4agAEU8DDLYmO5rJwGnyvvDTNO8zue3wjAdRjSfUbzaX0IZJ9mMYidZUNKP8wSRBxZRvQ8ckVAjCwUJOH2R04neM4A13JRC191XRfuAsLNl4bkZ+37UR5V0nS29uOVtV0N+vkpr0fyvi1pZ2IkMheUStUkZnea61nPCkssLU7QbPPW3yAA";
    public static final int DOWNLOAD_SUC = 1;
    public static final int DOWNLOAD_Fail = 2;
    public static final int UPLOAD_SUC = 3;
    public static final int UPLOAD_Fail = 4;
    public static final int UPLOAD_PROGRESS = 5;
    public static final int LIST_SUC = 6;
    public static final int HEAD_SUC = 7;
    public static final int RESUMABLE_SUC = 8;
    public static final int SIGN_SUC = 9;
    public static final int BUCKET_SUC = 10;
    public static final int GET_STS_SUC = 11;
    public static final int MULTIPART_SUC = 12;
    public static final int STS_TOKEN_SUC = 13;
    public static final int FAIL = 9999;
    public static final int REQUESTCODE_AUTH = 10111;
    public static final int REQUESTCODE_LOCALPHOTOS = 10112;
}
