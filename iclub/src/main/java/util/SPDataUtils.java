package util;

import android.content.Context;
import android.content.SharedPreferences;

import Beans.BeanUser;
import tools.TransitionTool;

/**
 * 使用SharedPreferences实现数据的存取工具类
 */
public class SPDataUtils {
    // 文件名称
    private static final String myFileName = "myData";

    /**
     * 保存登陆的当前用户的个人信息
     * @param context 上下文
     * @param beanUser 用户信息的实体类
     */
    public static void saveUserLoginInfo(Context context, BeanUser beanUser) {
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userID", beanUser.getUserID());
        editor.putString("userName", beanUser.getUserName());
        editor.putString("password", beanUser.getPassword());
        editor.putString("profileURL", beanUser.getProfileURL());
        editor.putString("college", beanUser.getCollege());
        editor.putString("majorClass", beanUser.getMajorClass());
        editor.putString("nickName", beanUser.getNickName());
        editor.putString("sex", beanUser.getSex());
        editor.putString("phoneNum", beanUser.getPhoneNum());
        String StrBirthDate = TransitionTool.sqlDateToString(beanUser.getBirthDate());
        editor.putString("birthDate", StrBirthDate);
        editor.putInt("isPresident", beanUser.getIsPresident());
        editor.apply();
    }

    /**
     * 获取登陆的用户的详细信息
     * @param context 上下文信息
     * @return BeanUser 用户的实体类
     */
    public static BeanUser getUserInfo(Context context) {
        BeanUser resultBean = new BeanUser();
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        // 第一个参数：sp文件里的键名，第二个参数：没有获取到键值对的默认值
        String userID = sp.getString("userID", null);
        String userName = sp.getString("userName", null);
        String password = sp.getString("password", null);
        String profileURL = sp.getString("profileURL", null);
        String college = sp.getString("college", null);
        String majorClass = sp.getString("majorClass", null);
        String nickName = sp.getString("nickName", null);
        String sex = sp.getString("sex", null);
        String phoneNum = sp.getString("phoneNum", null);
        String str_birthDate = sp.getString("birthDate", null);
        java.sql.Date sqlDate_birthDate= TransitionTool.StringToSqlDate(str_birthDate);
        int isPresident = sp.getInt("isPresident", 0);

        resultBean.setUserID(userID);
        resultBean.setUserName(userName);
        resultBean.setPassword(password);
        resultBean.setProfileURL(profileURL);
        resultBean.setCollege(college);
        resultBean.setMajorClass(majorClass);
        resultBean.setNickName(nickName);
        resultBean.setSex(sex);
        resultBean.setPhoneNum(phoneNum);
        resultBean.setBirthDate(sqlDate_birthDate);
        resultBean.setIsPresident(isPresident);
        return resultBean;
    }

    /**
     * 修改登陆用户的头像链接
     * @param context 上下文
     * @param AvatarURL 存储头像的连接
     */
    public static void updateAvatar(Context context, String AvatarURL) {
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profileURL", AvatarURL);
        editor.apply();
    }

    /**
     * 修改登录用户的个人信息
     * @param context 上下文
     * @param college 学院
     * @param majorClass 专业班级
     * @param nickName 昵称
     * @param sex 性别
     * @param phoneNum 电话号码
     * @param birthDate 生日
     */
    public static void updatePersonalInfo(Context context, String college, String majorClass, String nickName, String sex, String phoneNum, String birthDate) {
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("college", college);
        editor.putString("majorClass", majorClass);
        editor.putString("nickName", nickName);
        editor.putString("sex", sex);
        editor.putString("phoneNum", phoneNum);
        editor.putString("birthDate", birthDate);
        editor.apply();
    }

    /**
     * 修改登陆时是否保存密码、是否自动登录的配置
     * @param context 上下文
     * @param rmbPwdFlag 是否保存密码的Flag
     * @param autoLoginFlag 是否自动登录的Flag
     */
    public static void updateRmb_Auto(Context context, Boolean rmbPwdFlag, Boolean autoLoginFlag) {
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("rmbPwdFlag", rmbPwdFlag);
        editor.putBoolean("autoLoginFlag", autoLoginFlag);
        editor.apply();
    }
}
