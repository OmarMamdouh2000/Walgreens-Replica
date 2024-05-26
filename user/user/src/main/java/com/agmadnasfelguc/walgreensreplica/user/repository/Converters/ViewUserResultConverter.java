package com.agmadnasfelguc.walgreensreplica.user.repository.Converters;

import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.ViewUserResult;
import jakarta.persistence.Tuple;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ViewUserResultConverter {
    public static ViewUserResult convertTupleToDTO(Map<String, Object> tuple) {
        if(tuple == null){
            return null;
        }
        ViewUserResult userDTO = new ViewUserResult();
        userDTO.setUserId(String.valueOf(tuple.get("user_id")));
        userDTO.setEmail(String.valueOf(tuple.get("email")));
        userDTO.setRole(String.valueOf(tuple.get("role")));
        userDTO.setStatus(String.valueOf(tuple.get("status")));
        userDTO.setEmailVerified(Boolean.parseBoolean(String.valueOf(tuple.get("email_verified"))));
        userDTO.setFirstName(String.valueOf(tuple.get("first_name")));
        userDTO.setLastName(String.valueOf(tuple.get("last_name")));
        if (!tuple.get("address").equals("")) {
            userDTO.setAddress(String.valueOf(tuple.get("address")));
        }
        java.sql.Date sqlDateOfBirth = (Date) tuple.get("date_of_birth");
        if (sqlDateOfBirth != null) {
            java.util.Date utilDateOfBirth = new java.util.Date(sqlDateOfBirth.getTime());
            String isoDateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(utilDateOfBirth);
            userDTO.setDateOfBirth(isoDateOfBirth);
        }
        if (tuple.get("image_id") != null) {
             userDTO.setImageUrl(String.valueOf(tuple.get("image_url")));
        }
        if (tuple.get("gender") != null) {
            userDTO.setGender(String.valueOf(tuple.get("gender")));
        }
        if (!tuple.get("phone_number").equals("")) {
            userDTO.setPhoneNumber(String.valueOf(tuple.get("phone_number")));
        }
        if (!tuple.get("extension").equals("")) {
            userDTO.setExtension(String.valueOf(tuple.get("extension")));
        }
        return userDTO;
    }
}
