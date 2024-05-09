package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator;

public class TemplatePaths {
    public static final String basePath = "messageTemplates/userManagement/";
    public static final String adminPath = basePath + "admin/";
    public static final String userPath = basePath + "user/";
    public static final String commonPath = basePath + "common/";

    // Admin JSON paths
    public static final String addAdminPath = adminPath + "AddAdmin.json";
    public static final String addPharmacistPath = adminPath + "AddPharmacist.json";
    public static final String adminLoginPath = adminPath + "AdminLogin.json";
    public static final String banAccountPath = adminPath + "BanAccount.json";
    public static final String unbanAccountPath = adminPath + "UnbanAccount.json";
    public static final String viewUsersPath = adminPath + "ViewUsers.json";

    // Common JSON paths
    public static final String logoutPath = commonPath + "Logout.json";

    // User JSON paths
    public static final String changeEmailPath = userPath + "ChangeEmail.json";
    public static final String changePasswordPath = userPath + "ChangePassword.json";
    public static final String editDetailsPath = userPath + "EditDetails.json";
    public static final String registerPath = userPath + "Register.json";
    public static final String resetPasswordPath = userPath + "ResetPassword.json";
    public static final String resetPasswordCheckOtpPath = userPath + "ResetPasswordCheckOtp.json";
    public static final String twoFactorAuthLoginPath = userPath + "TwoFactorAuthLogin.json";
    public static final String update2FAStatusPath = userPath + "Update2FAStatus.json";
    public static final String userLoginPath = userPath + "UserLogin.json";
    public static final String verifyMailPath = userPath + "VerifyMail.json";
    public static final String verifyMailCheckOtpPath = userPath + "VerifyMailCheckOtp.json";


}
