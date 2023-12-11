package com.example.icluub;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Beans.BeanUser;

public class login_config {


    static String dateString = "2023-12-07";
    static java.sql.Date date = java.sql.Date.valueOf(dateString);

    public static final BeanUser LOGIN_USER = new BeanUser("32100477", "胡纪福", "32100011",
            "https://bucket-hjf.oss-cn-hangzhou.aliyuncs.com/userAvatar/12345.jpg",
            "电讯業","采购部","pepper","男","182-6012-8820",
            date,0);

}
