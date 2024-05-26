CREATE TYPE productSize (
  id uuid,
  name text,
  price decimal
);

CREATE TABLE IF NOT EXISTS products (
  id uuid PRIMARY KEY,
  name text,
  description text,
  price decimal,
  discount decimal,
  sale boolean,
  image text,
  parent_categories list<uuid>,
  product_type text,
  brand_id uuid,
  size list<frozen<productSize>>,
  custom_message text,
  inStore boolean,
  sameDayDelivery boolean,
  shipping boolean,
  inventory int,
  pricePerUnit decimal,
  ingredients text,
  warnings text,
  frequentlyBoughtWith text,
  refundable boolean
);

CREATE TABLE IF NOT EXISTS categories (
  id uuid PRIMARY KEY,
  name text,
  image text,
  sub_categories list<uuid>,
  products list<uuid>
);

CREATE TABLE IF NOT EXISTS brands (
  id uuid PRIMARY KEY,
  name text,
  categories list<uuid>
);

CREATE TABLE IF NOT EXISTS product_types (
  id uuid PRIMARY KEY,
  name text,
  category_id uuid
);

CREATE TABLE IF NOT EXISTS prescription (
  id uuid PRIMARY KEY,
  patientName text,
  prescriptionNumber decimal,
  dateFilled date,
  medicationId uuid,
  medicationName text,
  medicationStrength text,
  dosageInstructions text,
  dispensedQuantity text,
  refills text,
  autoRefills boolean,
  prescriberInformation text,
  expirationDate date,
  pharmacyInformation text,
  warningLabels text,
  barcodes text,
  insuranceInformation text,
  substitutionInformation text,
  sameDayDelivery boolean,
  inStorePickup boolean,
  driveThrough boolean,
  status text,
  price decimal
);
