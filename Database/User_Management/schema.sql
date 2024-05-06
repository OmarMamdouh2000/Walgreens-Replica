create type "Role" as enum (
  'Adminstrator',
  'Customer',
  'Pharmacist'
);

create type "Gender" as enum(
'Male',
'Female'
);

create type "Status" as enum(
'Active',
'Banned'
);

CREATE TABLE "User" (
  "id" UUID PRIMARY KEY,
  "email" varchar UNIQUE NOT NULL,
  "password" varchar NOT NULL,
  "role" "Role" NOT NULL,
  "status" "Status" NOT NULL,
  "email_verified" boolean DEFAULT false,
  "TwoFactorAuth_Enabled" boolean DEFAULT false
);

CREATE TABLE "Phone_Number" (
  "id" UUID PRIMARY KEY,
  "number" varchar UNIQUE NOT NULL,
  "extension" varchar NOT NULL
);

CREATE TABLE "Customer" (
  "id" UUID PRIMARY KEY,
  "first_name" varchar NOT NULL,
  "last_name" varchar NOT NULL,
  "address" varchar,
  "date_of_birth" date,
  "gender" "Gender",
  "phone_id" UUID,
  FOREIGN KEY ("id") REFERENCES "User" ("id"),
  FOREIGN KEY ("phone_id") REFERENCES "Phone_Number" ("id")
);

CREATE TABLE "Pharmacist" (
  "id" UUID PRIMARY KEY,
  "first_name" varchar,
  "last_name" varchar,
  FOREIGN KEY ("id") REFERENCES "User" ("id")
);

CREATE TABLE "Administrator" (
  "id" UUID PRIMARY KEY,
  "username" varchar UNIQUE NOT NULL,
  "password" varchar NOT NULL
);
