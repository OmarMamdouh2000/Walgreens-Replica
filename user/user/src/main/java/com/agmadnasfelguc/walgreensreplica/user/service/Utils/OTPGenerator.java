package com.agmadnasfelguc.walgreensreplica.user.service.Utils;

public class OTPGenerator {
    //create a static function that generates a random 6 digit number
    public static String generateOTP() {
        int randomPin = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(randomPin);
    }
}
