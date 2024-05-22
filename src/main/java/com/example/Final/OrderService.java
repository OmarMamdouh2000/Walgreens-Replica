// package com.example.Final;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jws;
// import io.jsonwebtoken.Jwts;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Service;

// import com.example.Commands.JwtDecoderService;

// import java.sql.Date;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.UUID;


// @Service
// public class OrderService {
//     @Autowired
//     private JwtDecoderService jwtDecoderService;

//     @Autowired
//     private OrderRepo orderRepo;

//     public List<OrderTable> getOrders(String token){
//         String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa";
//         Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
//         if (claims != null) {
//             String userId=(String) claims.get("userId");

//             return orderRepo.getOrders(UUID.fromString(userId));


//         }else {
//             return new ArrayList<OrderTable>();
//         }
//     }

//     public List<OrderTable> getActiveOrders(String token){
//         String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa";
//         Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
//         if (claims != null) {
//             String userId=(String) claims.get("userId");

//             return orderRepo.getActiveOrders(UUID.fromString(userId));


//         }else {
//             return new ArrayList<OrderTable>();
//         }
//     }
//     public List<OrderTable> getUserOrders(String token){
//         String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa";
//         Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
//         try {
//             if (claims != null) {
//                 String userId = (String) claims.get("userId");
//                 return orderRepo.getUserOrders(UUID.fromString(userId));
//             } else {
//                 return new ArrayList<OrderTable>();
//             }
//         }catch (Exception e){
//             System.out.println(e.getMessage());
//             return null;
//         }
//     }
//     public List<OrderTable> filterOrders(String token,String dateString, String status) {
//         String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa";
//         Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
//         try{
//             if(claims != null){
//                 String user=(String) claims.get("userId");
//                 UUID userId = UUID.fromString(user);

//                 if(dateString != null && status != null){
//                     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                     LocalDate localDate = LocalDate.parse(dateString, formatter);

//                     return orderRepo.filterDateAndStatus(userId, localDate, status);
//                 }else if (dateString != null){
//                     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                     LocalDate localDate = LocalDate.parse(dateString, formatter);

//                     return orderRepo.filterByDate(userId, localDate);

//                 }else if (status != null){
//                     return orderRepo.filterByStatus(userId, status);
//                 }else{
//                     return orderRepo.getUserOrders(userId);
//                 }

//             }else{
//                 return null;
//             }
//         }catch(Exception e){
//             System.out.println(e.getMessage());
//             return null;
//         }

//     }
// }
