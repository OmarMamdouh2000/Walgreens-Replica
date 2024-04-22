package com.example.Final;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

/*
INSERT INTO orders (
  id,
  user_id,
  transation_number,
  order_type,
  order_status,
  total_amount,
  date_issued,
  address,
  ispromoapplied,
  promocodeId,
  items,
  refundedItems
)
VALUES (
  uuid(),
  72678a7f-3922-4882-8a18-8dd0181213c2,
  uuid(),
  'shipping',
  'pending',
  100.50,
  '2024-04-01',
  '123 Main St',
  true,
  uuid(),
  [{ item_id: uuid(), item_count: 2, purchased_price: 10.50, deliveryType: 'standard', comment: 'No special instructions' }],
  []
);

*/
@Table("orders")
public class OrderTable {
	@Id
    private UUID id;

	@Column("user_id")
    private UUID user_id;

    @Column("transation_number")
    private UUID transation_number;

    @Column("order_type")
    private String order_type;

    @Column("order_status")
    private String order_status;

    @Column("total_amount")
    private double total_amount;

    @Column("date_issued")
    private java.sql.Date date_issued;

    private String address;
    private boolean ispromoapplied;
    private UUID promocodeId;

    private List<OrderItem> items;

    @Column("refundedItems")
    private List<OrderItem> refundedItems;


	public OrderTable(){

	}
	public OrderTable(UUID id,String address,java.sql.Date date_issued, boolean ispromoapplied,  List<OrderItem> items,
					  String order_status, String order_type,  UUID promoCodeId, List<OrderItem> refundedItems,
					  double total_amount, UUID transation_number,UUID user_id) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.transation_number = transation_number;
		this.order_type = order_type;
		this.order_status = order_status;
		this.total_amount = total_amount;
		this.ispromoapplied = ispromoapplied;
		this.promocodeId = promoCodeId;
		this.date_issued = date_issued;
		this.address = address;
		this.items = items;
		this.refundedItems = refundedItems;
		
	}
	
	
	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public UUID getUserId() {
		return user_id;
	}


	public void setUserId(UUID userId) {
		this.user_id = userId;
	}


	public UUID getTransactionNumber() {
		return transation_number;
	}


	public void setTransactionNumber(UUID transactionNumber) {
		this.transation_number = transactionNumber;
	}


	public String getOrderType() {
		return order_type;
	}


	public void setOrderType(String orderType) {
		this.order_type = orderType;
	}


	public String getOrderStatus() {
		return order_status;
	}


	public void setOrderStatus(String orderStatus) {
		this.order_status = orderStatus;
	}


	public double getTotalAmount() {
		return total_amount;
	}


	public void setTotalAmount(double totalAmount) {
		this.total_amount = totalAmount;
	}


	public java.sql.Date getDateIssued() {
		return date_issued;
	}


	public void setDateIssued(java.sql.Date dateIssued) {
		this.date_issued = dateIssued;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public boolean isPromoApplied() {
		return ispromoapplied;
	}


	public void setPromoApplied(boolean isPromoApplied) {
		this.ispromoapplied = isPromoApplied;
	}


	public UUID getPromoCodeId() {
		return promocodeId;
	}


	public void setPromoCodeId(UUID promoCodeId) {
		this.promocodeId = promoCodeId;
	}


	public List<OrderItem> getItems() {
		return items;
	}


	public void setItems(List<OrderItem> items) {
		this.items = items;
	}


	public List<OrderItem> getRefundedItems() {
		return refundedItems;
	}


	public void setRefundedItems(List<OrderItem> refundedItems) {
		this.refundedItems = refundedItems;
	}


	@Override
	public String toString() {
		return "Order [id=" + id + ", user_id=" + user_id + ", transation_number=" + transation_number + ", order_type="
				+ order_type + ", order_status=" + order_status + ", total_amount=" + total_amount + ", isPromoApplied="
				+ ispromoapplied + ", promoCodeId=" + promocodeId + ", date_issued=" + date_issued + ", address="
				+ address + "]";
	}
	
	
	

}
