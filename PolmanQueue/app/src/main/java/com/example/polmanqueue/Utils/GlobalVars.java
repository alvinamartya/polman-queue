package com.example.polmanqueue.Utils;

public class GlobalVars {
    public static String BASE_URL = "http://48ede1ce.ngrok.io/app_antrianPolman/index.php/";
    public static String URL_LOGIN = BASE_URL + "Login";
    public static String URL_CUSTOMER = BASE_URL + "Customer";
    public static String URL_SERVICE = BASE_URL + "Service";
    public static String URL_CHANGEPASSWORD = URL_LOGIN + "/ChangePassword";
    public static String URL_CHANGEPROFILE = URL_CUSTOMER + "/changeProfile";
    public static String URL_CHECKUSERNAME = URL_CUSTOMER + "/getUsername";
    public static String URL_GETLASTIDCUSTOMER = URL_CUSTOMER + "/getLastID";
    public static String URL_GETSERVICETODAY = URL_SERVICE + "/getServiceToday";
    public static String URL_GETSERVICECUSTOMER = URL_SERVICE + "/getServiceCustomer";
    public static String URL_GETLASTIDSERVICE = URL_SERVICE + "/getLastID";
    public static String URL_GETSEEQUEUE = URL_SERVICE + "/getSeeQueue";
}
