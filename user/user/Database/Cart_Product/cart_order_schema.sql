
create TYPE IF NOT EXISTS orderItem(
        item_id uuid, 
        item_count int,
        purchased_price decimal,
        comment text
);

create TABLE IF NOT EXISTS "order"(
  id uuid primary key,
  user_id uuid, 
  transaction_number uuid, 
  order_type text , 
  order_status text ,
  total_amount decimal,
  date_issued date,
  address text,
  items list<FROZEN<orderItem>>,
  refundedItems list<FROZEN<orderItem>>
);

create Table IF NOT EXISTS "cart"(
      id uuid primary key,
      user_id uuid, 
      items list<FROZEN<orderItem>>,
      total_amount decimal,
      appliedPromoCodeId uuid,
);
create Table IF NOT EXISTS "PromoCodes"(
  id uuid primary key,
  code text,
  discountValue decimal,
  valid boolean,
  expiryDate date,
);

create Table IF NOT EXISTS "UserUsedPromo"(
  userId uuid, 
  promoCodeId uuid,
  Primary key(userId,promoCodeId)
);



