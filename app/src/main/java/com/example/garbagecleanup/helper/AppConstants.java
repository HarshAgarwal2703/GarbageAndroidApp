package com.example.garbagecleanup.helper;

public class AppConstants {
    public static final String SHARED_PREFERENCES_NAME = "Garbage";
    public static final String SP_GET_USER = "user";
    public static final String SP_LOGGED_IN = "logged_in";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public static final int CAMERA_REQUEST_CODE = 1002;
    public static final int RequestPermissionCode = 1003;

    public static final String ServerURL = "http://bhavya17ahir.pythonanywhere.com/";
    public static final String POST_IMAGES_URL = ServerURL + "post/upload/";
    public static final String REGISTER_USER = ServerURL + "user/register/";
    public static final String LOGIN_USER = ServerURL + "user/login/";
    public static final String GET_POSTS = ServerURL + "postAPI/";
    public static final String MY_ISSUES = ServerURL + "getuserspost/";
    public static final String UPVOTE_POST = ServerURL + "upvote/";
    public static final String DELETE_POST = ServerURL + "post/delete/";
    public static final String DOWNVOTE_POST = ServerURL + "downvote/";
    //    public static final String UPVOTE_LIST = ServerURL + "userslikedpost?uid=%1$s";
    public static final String UPVOTE_LIST = ServerURL + "userslikedpost/";
    public static final String FILTER_POST = ServerURL + "filterpost/";

}
