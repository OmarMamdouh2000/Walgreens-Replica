package com.example.Commands;

import com.example.Final.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreateOrder implements Command{
    private JwtDecoderService jwtDecoderService;

    private OrderRepo orderRepo;

    @Autowired
    public CreateOrder(OrderRepo orderRepo,JwtDecoderService jwtDecoderService) {
        this.orderRepo=orderRepo;
        this.jwtDecoderService=jwtDecoderService;
    }

    @Override
    public Object execute(Map<String, Object> data) {
        return null;
    }
}
