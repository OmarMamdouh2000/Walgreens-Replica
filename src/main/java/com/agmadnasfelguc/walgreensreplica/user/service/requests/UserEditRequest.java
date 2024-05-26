package com.agmadnasfelguc.walgreensreplica.user.service.requests;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
public class UserEditRequest {
    private String sessionId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String address;
    private String extension;
    private String gender;

    public java.sql.Date formatDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date date = formatter.parse(dateOfBirth);
            return new java.sql.Date(date.getTime());
        } catch (Exception e) {
            System.out.println("Error parsing the date: " + e.getMessage());
        }
        return null;
    }
}
