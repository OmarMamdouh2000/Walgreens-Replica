package com.example.Commands;

import com.example.Final.OrderRepo;
import com.example.Final.OrderTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ConfirmRefund implements Command{
    private JwtDecoderService jwtDecoderService;

    private OrderRepo orderRepo;

    @Autowired
    public ConfirmRefund(OrderRepo orderRepo,JwtDecoderService jwtDecoderService) {
        this.orderRepo=orderRepo;
        this.jwtDecoderService=jwtDecoderService;
    }

    @Override
    public Object execute(Map<String,Object> data) {

        String userId=(String) data.get("userId");
        String orderIdString = (String) data.get("orderId");

        UUID orderId = UUID.fromString(orderIdString);

        orderRepo.setStatus(orderId, "Refunded");

        return "Refund Confirmed";
    }

}
