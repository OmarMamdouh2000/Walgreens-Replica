package com.example.Commands;

import com.example.Final.CartTable;
import com.example.Final.OrderItem;
import com.example.Final.OrderRepo;
import com.example.Final.OrderTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        String user = (String)data.get("userId");
        String transcaction = (String)data.get("transactionNumber");

        UUID userId = UUID.fromString(user);
        UUID transactionId = UUID.fromString(transcaction);

        OrderTable newOrder = new OrderTable();

        CartTable userCart = (CartTable)data.get("cart");

        List<OrderItem> items = new ArrayList<>();

        for (int i = 0; i < userCart.getItems().size(); i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(userCart.getItems().get(i).getItemId());
            orderItem.setItemCount(userCart.getItems().get(i).getItemCount());
            orderItem.setPurchasedPrice(userCart.getItems().get(i).getPurchasedPrice());
            orderItem.setDeliveryType(userCart.getItems().get(i).getDeliveryType());
            orderItem.setComment(userCart.getItems().get(i).getComment());
            items.add(orderItem);
        }

        newOrder.setId(UUID.randomUUID());
        newOrder.setUserId(userId);
        newOrder.setTransactionNumber(transactionId);
        newOrder.setOrderType("online");
        newOrder.setTotalAmount(userCart.getTotalAmount());
        newOrder.setPromoApplied(userCart.getAppliedPromoCodeId().isEmpty());
        newOrder.setPromoCodeId(userCart.getAppliedPromoCodeId());
        newOrder.setPromoAmount(userCart.getPromoCodeAmount());
        newOrder.setDateIssued(LocalDate.now());
        newOrder.setOrderStatus("confirmed");
        newOrder.setItems(items);
        newOrder.setRefundedItems(new ArrayList<>());
        newOrder.setAddress("5th settlement, new cairo, egypt");

        orderRepo.save(newOrder);

        return "Order Created Successfully";
    }
}
