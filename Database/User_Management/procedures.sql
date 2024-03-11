-- Procedure: register_user

/*
Description: This procedure is used to register a new user, including their basic personal details.

Parameters:
- p_first_name: First name of the user (VARCHAR).
- p_last_name: Last name of the user (VARCHAR).
- p_email: Email address of the user (VARCHAR).
- p_password: Password for the user's account (VARCHAR).

Returns: None.

Notes:
- Inserts user details into "User" table and creates a corresponding entry in the "Customer" table.
*/

CREATE OR REPLACE PROCEDURE register_user(
    p_first_name VARCHAR,
    p_last_name VARCHAR,
    p_email VARCHAR,
    p_password VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id VARCHAR;
BEGIN
    -- Inserting data into "User" table
    INSERT INTO "User" ("id", "email", "password",  "status", "role")
    VALUES (gen_random_uuid(), p_email, p_password, 'Active','Customer')
    RETURNING "id" INTO v_user_id;

    -- Inserting data into "Customer" table
    INSERT INTO "Customer" ("id", "first_name", "last_name")
    VALUES (v_user_id, p_first_name, p_last_name);
END;
$$;

-- Procedure: edit_personal_details

/*
Description: This procedure is used to edit the personal details of a user.

Parameters:
- p_user_id: User ID of the user whose details are being edited (VARCHAR).
- p_address: New address of the user (VARCHAR, default NULL).
- p_date_of_birth: New date of birth of the user (DATE, default NULL).
- p_gender: New gender of the user ("Gender" enum, default NULL).
- p_phone_number: New phone number of the user (INT, default NULL).
- p_extension: New extension for the phone number (VARCHAR, default NULL).

Returns: None.

Notes:
- Updates the "Customer" table with the provided details.
- Updates the phone number if provided and manages its association.
*/

CREATE OR REPLACE PROCEDURE edit_personal_details(
    p_user_id VARCHAR,
    p_address VARCHAR DEFAULT NULL,
    p_date_of_birth DATE DEFAULT NULL,
    p_gender "Gender" DEFAULT NULL,
    p_phone_number INT DEFAULT NULL,
    p_extension VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_phone_id VARCHAR;
BEGIN
    -- Check if phone number and extension are provided and exist
    IF p_phone_number IS NOT NULL AND p_extension IS NOT NULL THEN
        -- Get the existing phone_id associated with the user
        SELECT "phone_id" INTO v_phone_id
        FROM "Customer"
        WHERE "id" = p_user_id;

        -- If phone_id is not null, update Customer to nullify the reference
        IF v_phone_id IS NOT NULL THEN
            UPDATE "Customer"
            SET "phone_id" = NULL
            WHERE "id" = p_user_id;
        END IF;

        -- Delete the old phone number and extension if they exist
        DELETE FROM "Phone_Number"
        WHERE "id" = v_phone_id;

        -- Insert the new phone number and extension
        INSERT INTO "Phone_Number" ("id", "number", "verified", "extension")
        VALUES (gen_random_uuid(), p_phone_number, false, p_extension)
        RETURNING "id" INTO v_phone_id;

        -- Update "Customer" table with the new phone_id
        UPDATE "Customer"
        SET "phone_id" = v_phone_id
        WHERE "id" = p_user_id;
    END IF;

    -- Update "Customer" table with new details if not null
    UPDATE "Customer"
    SET "address" = COALESCE(p_address, "address"),
        "date_of_birth" = COALESCE(p_date_of_birth, "date_of_birth"),
        "gender" = COALESCE(p_gender, "gender")
    WHERE "id" = p_user_id;
END;
$$;

-- Procedure: ChangeEmailOrPassword

/*
Description: This procedure is used to change the email or password of a user.

Parameters:
- p_user_id: User ID of the user whose email or password is being changed (VARCHAR).
- p_new_email: New email address for the user (VARCHAR).
- p_new_password: New password for the user's account (VARCHAR).

Returns: None.

Notes:
- Checks the user's role and prohibits email/password changes for administrators.
- Updates the email/password if provided.
*/

CREATE OR REPLACE PROCEDURE ChangeEmailOrPassword(
    p_user_id VARCHAR,
    p_new_email VARCHAR,
    p_new_password VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_role "Role";
BEGIN
    SELECT role INTO v_user_role
    FROM "User"
    WHERE id = p_user_id;
    IF v_user_role = 'Administrator' THEN
        RAISE NOTICE 'Administrator email/password cannot be changed.';
    ELSE
        IF p_new_email IS NOT NULL THEN
            UPDATE "User"
            SET email = p_new_email
            WHERE id = p_user_id;
            RAISE NOTICE 'Email updated successfully.';
        END IF;

        IF p_new_password IS NOT NULL THEN
            UPDATE "User"
            SET password = p_new_password
            WHERE id = p_user_id;
            RAISE NOTICE 'Password updated successfully.';
        END IF;
    END IF;
END;
$$;

-- Function: Login

/*
Description: This function is used for user login authentication.

Parameters:
- email: Email address of the user attempting to log in (VARCHAR).
- password: Password provided by the user attempting to log in (VARCHAR).

Returns: A record containing the login status and role of the user.

Notes:
- Returns 'Wrong Email or Password' if the login credentials are incorrect.
*/
CREATE OR REPLACE FUNCTION Login(
    email VARCHAR,
    password VARCHAR,
    OUT status VARCHAR,
    OUT role VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
DECLARE
    v_2fa_enabled BOOLEAN;
BEGIN
    SELECT U.status, U.role, U."2FA_Enabled"
    INTO status, role, v_2fa_enabled
    FROM "User" U
    WHERE U.email = $1 AND U.password = $2;
    
    IF NOT FOUND THEN
        status := 'Wrong Email or Password';
    ELSE
        IF v_2fa_enabled THEN
            status := '2FA pending';
        END IF;
    END IF;
    
END;
$$;


-- Function: Login_Admin

/*
Description: This function is used for administrator login authentication.

Parameters:
- username: Username of the administrator attempting to log in (VARCHAR).
- password: Password provided by the administrator attempting to log in (VARCHAR).

Returns: The login status.

Notes:
- Returns 'Wrong Email or Password' if the login credentials are incorrect.
*/

CREATE OR REPLACE FUNCTION Login_Admin(
    username VARCHAR,
    password VARCHAR,
    OUT status VARCHAR
)
RETURNS character varying
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT *
    FROM "Adminstrator" A
    WHERE A.username = $1 AND A.password = $2;
    
    IF NOT FOUND THEN
        status := 'Wrong Email or Password';
    END IF;
END;
$$;

-- Procedure: update_2fa_enabled

/*
Description: This procedure is used to update the two-factor authentication (2FA) status of a user.

Parameters:
- _id: User ID of the user whose 2FA status is being updated (VARCHAR).
- _enabled: Boolean value indicating whether 2FA is enabled or disabled (BOOLEAN).

Returns: None.

Notes:
- Updates the "2FA_Enabled" field in the "User" table.
*/

CREATE OR REPLACE PROCEDURE update_2fa_enabled(_id varchar, _enabled boolean)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "User"
    SET "2FA_Enabled" = _enabled
    WHERE "id" = _id;
END;
$$;

-- View: ViewUsers

/*
Description: This view provides a comprehensive view of users, including their personal details and roles.

Columns:
- user_id: User ID (VARCHAR).
- email: Email address (VARCHAR).
- role: Role of the user (VARCHAR).
- status: Account status (VARCHAR).
- 2FA_Enabled: Two-factor authentication status (BOOLEAN).
- customer_first_name: First name of the customer (VARCHAR).
- customer_last_name: Last name of the customer (VARCHAR).
- customer_address: Address of the customer (VARCHAR).
- customer_date_of_birth: Date of birth of the customer (DATE).
- customer_gender: Gender of the customer ("Gender" enum).
- pharmacist_first_name: First name of the pharmacist (VARCHAR).
- pharmacist_last_name: Last name of the pharmacist (VARCHAR).

Notes:
- Combines data from "User", "Customer", and "Pharmacist" tables.
*/

CREATE OR REPLACE VIEW ViewUsers AS
SELECT
    u.id AS user_id,
    u.email,
    u.role,
    u.status,
    u."2FA_Enabled",
    c.first_name AS customer_first_name,
    c.last_name AS customer_last_name,
    c.address AS customer_address,
    c.date_of_birth AS customer_date_of_birth,
    c.gender AS customer_gender,
    p.first_name AS pharmacist_first_name,
    p.last_name AS pharmacist_last_name
FROM
    "User" u
LEFT JOIN
    "Customer" c ON u.id = c.id
LEFT JOIN
    "Pharmacist" p ON u.id = p.id;

-- Procedure: add_admin

/*
Description: This procedure is used to add a new administrator to the system.

Parameters:
- v_username: Username of the new administrator (VARCHAR).
- v_password: Password for the new administrator's account (VARCHAR).

Returns: None.

Notes:
- Inserts administrator details into the "Administrator" table.
*/

CREATE OR REPLACE PROCEDURE add_admin(
	v_username varchar,
	v_password varchar
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO "Adminstrator" ("id", "username", "password")
    VALUES (gen_random_uuid(), v_username, v_password);
END; 
$$;

-- Procedure: add_pharmacist

/*
Description: This procedure is used to add a new pharmacist to the system.

Parameters:
- pharma_first_name: First name of the new pharmacist (VARCHAR).
- pharma_last_name: Last name of the new pharmacist (VARCHAR).
- pharma_email: Email address of the new pharmacist (VARCHAR).
- pharma_password: Password for the new pharmacist's account (VARCHAR).

Returns: None.

Notes:
- Inserts pharmacist details into the "User" and "Pharmacist" tables.
*/

CREATE OR REPLACE PROCEDURE add_pharmacist(
    pharma_first_name VARCHAR,
    pharma_last_name VARCHAR,
    pharma_email VARCHAR,
    pharma_password VARCHAR)
LANGUAGE plpgsql
AS $$
DECLARE
   user_id VARCHAR;
BEGIN
    INSERT INTO "User" ("id", "email", "password", "status", "role")
    VALUES (gen_random_uuid(), pharma_email, pharma_password, 'Active','pharmacist')
    RETURNING "id" INTO user_id;
    INSERT INTO "Pharmacist" ("id", "first_name", "last_name")
    VALUES (user_id, pharma_first_name, pharma_last_name);
END; 
$$;

-- Procedure: ban_account

/*
Description: This procedure is used to ban an account by setting its status to 'banned'.

Parameters:
- user_id: User ID of the account to be banned (VARCHAR).

Returns: None.

Notes:
- Updates the status of the user's account in the "User" table.
*/

CREATE OR REPLACE PROCEDURE ban_account(
      user_id varchar)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "USER"
    SET "status" = 'banned'
    WHERE "id" = user_id;

END;
$$;