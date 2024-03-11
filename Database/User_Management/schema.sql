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
  "id" varchar PRIMARY KEY,
  "email" varchar UNIQUE NOT NULL,
  "password" varchar NOT NULL,
  "role" "Role" NOT NULL,
  "status" "Status" NOT NULL,
  "2FA_Enabled" boolean DEFAULT false
);

CREATE TABLE "Phone_Number" (
  "id" varchar PRIMARY KEY,
  "number" int UNIQUE NOT NULL,
  "verified" boolean DEFAULT false,
  "extension" varchar NOT NULL
);

CREATE TABLE "Customer" (
  "id" varchar PRIMARY KEY,
  "first_name" varchar NOT NULL,
  "last_name" varchar NOT NULL,
  "address" varchar,
  "date_of_birth" date,
  "gender" "Gender",
  "phone_id" varchar,
  FOREIGN KEY ("id") REFERENCES "User" ("id"),
  FOREIGN KEY ("phone_id") REFERENCES "Phone_Number" ("id")
);

CREATE TABLE "Pharmacist" (
  "id" varchar PRIMARY KEY,
  "first_name" varchar,
  "last_name" varchar,
  FOREIGN KEY ("id") REFERENCES "User" ("id")
);

CREATE TABLE "Administrator" (
  "id" varchar PRIMARY KEY,
  "username" varchar NOT NULL,
  "password" varchar NOT NULL
);
