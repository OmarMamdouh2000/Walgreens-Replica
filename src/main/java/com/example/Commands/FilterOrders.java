package com.example.Commands;

import com.example.Final.OrderRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
public class FilterOrders implements Command{
    private JwtDecoderService jwtDecoderService;

    private OrderRepo orderRepo;

    @Autowired
    public FilterOrders(OrderRepo orderRepo,JwtDecoderService jwtDecoderService) {
        this.orderRepo=orderRepo;
        this.jwtDecoderService=jwtDecoderService;
    }


    @Override
    public Object execute(Map<String,Object> data) {
        

        String user = (String)data.get("userId");
        UUID userId = UUID.fromString(user);

        String dateString = (String) data.get("date");
        String status = (String) data.get("status");

        if(dateString != null && status != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return orderRepo.filterDateAndStatus(userId, localDate, status).toString();
        }else if (dateString != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return orderRepo.filterByDate(userId, localDate).toString();

        }else if (status != null){
            return orderRepo.filterByStatus(userId, status).toString();
        }else{
            return orderRepo.getUserOrders(userId).toString();
        }
    }
}
