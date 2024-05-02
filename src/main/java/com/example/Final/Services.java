package com.example.Final;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Commands.JwtDecoderService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


@Service
public class Services {
	@Autowired
    private JwtDecoderService jwtDecoderService;
	@Autowired
	private OrderRepo orderRepo;
	
	
	public OrderTable getOrderByDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        
        // Step 2: Convert the LocalDate to java.sql.Date
        
        System.out.println(localDate+"++++++++++++++++++++++");
        return orderRepo.getOrderByDate(localDate);
	}

}
