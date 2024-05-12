package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator;

public class TemplatePaths {
    public static final String basePath = "messageTemplates/userManagement/";
    
    // Admin JSON paths
    public static final String addAdminPath = basePath + "AddAdmin.json";
    public static final String addPharmacistPath = basePath + "AddPharmacist.json";
    public static final String adminLoginPath = basePath + "AdminLogin.json";
    public static final String banAccountPath = basePath + "BanAccount.json";
    public static final String unbanAccountPath = basePath + "UnbanAccount.json";
    public static final String viewUsersPath = basePath + "ViewUsers.json";

    // Common JSON paths
    public static final String logoutPath = basePath + "Logout.json";

    // User JSON paths
    public static final String changeEmailPath = basePath + "ChangeEmail.json";
    public static final String changePasswordPath = basePath + "ChangePassword.json";
    public static final String editDetailsPath = basePath + "EditDetails.json";
    public static final String registerPath = basePath + "Register.json";
    public static final String resetPasswordPath = basePath + "ResetPassword.json";
    public static final String resetPasswordCheckOtpPath = basePath + "ResetPasswordCheckOtp.json";
    public static final String twoFactorAuthLoginPath = basePath + "TwoFactorAuthLogin.json";
    public static final String update2FAStatusPath = basePath + "Update2FAStatus.json";
    public static final String userLoginPath = basePath + "UserLogin.json";
    public static final String verifyMailPath = basePath + "VerifyMail.json";
    public static final String verifyMailCheckOtpPath = basePath + "VerifyMailCheckOtp.json";


}
