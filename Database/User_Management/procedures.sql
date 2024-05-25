-- Function: register_user
/*
Description: Registers a new user with their personal details into the system.
Parameters:
- p_first_name: First name of the user (VARCHAR).
- p_last_name: Last name of the user (VARCHAR).
- p_email: Email address of the user (VARCHAR).
- p_password: Password for the user's account (VARCHAR).
Returns: Status message indicating successful registration (VARCHAR).
*/
CREATE OR REPLACE FUNCTION register_user(
    p_first_name VARCHAR,
    p_last_name VARCHAR,
    p_email VARCHAR,
    p_password text
)
RETURNS text
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id UUID;
BEGIN
    INSERT INTO "User" ("id", "email", "password", "status", "role")
    VALUES (gen_random_uuid(), p_email, p_password, 'Active', 'Customer')
    RETURNING "id" INTO v_user_id;

    INSERT INTO "Customer" ("id", "first_name", "last_name")
    VALUES (v_user_id, p_first_name, p_last_name);

    RETURN 'User Registered Successfully'; -- Return the hashed password
END;
$$;



-- Function: edit_personal_details
/*
Description: Edits personal details of an existing user based on provided information.
Parameters:
- p_user_id: User ID of the user whose details are being edited (VARCHAR).
- p_address, p_date_of_birth, p_gender: New address, date of birth, and gender of the user (VARCHAR, DATE, "Gender" ENUM).
- p_phone_number, p_extension: New phone number and its extension if provided (INT, VARCHAR).
Returns: Status message indicating successful update (VARCHAR).
*/
CREATE OR REPLACE FUNCTION edit_personal_details(
    p_user_id UUID,
    p_address VARCHAR DEFAULT NULL,
    p_date_of_birth DATE DEFAULT NULL,
    p_gender "Gender" DEFAULT NULL,
    p_phone_number VARCHAR DEFAULT NULL,
    p_extension VARCHAR DEFAULT NULL,
    OUT status VARCHAR ,
    OUT message VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
DECLARE
    v_phone_id UUID;
    v_old_phone_number VARCHAR;
    v_old_phone_extension VARCHAR;
    v_message VARCHAR := 'Details updated successfully for: ';
    v_first BOOLEAN := TRUE;
BEGIN
    IF p_address IS NULL AND p_date_of_birth IS NULL AND p_gender IS NULL AND p_phone_number IS NULL AND p_extension IS NULL THEN
        status := 'Failure';
        message := 'No details provided for update';
    ELSE
       IF (p_phone_number IS NULL AND p_extension IS NOT NULL) OR (p_phone_number IS NOT NULL AND p_extension IS NULL) THEN
            status := 'Failure';
            message := 'Phone number and extension must be provided together or not at all.';
            RETURN;
       ELSE
            IF p_phone_number IS NOT NULL AND p_extension IS NOT NULL THEN
                SELECT "id", "number", "extension"
                INTO v_phone_id, v_old_phone_number, v_old_phone_extension
                FROM "Phone_Number"
                WHERE "id" = (SELECT "phone_id" FROM "Customer" WHERE "id" = p_user_id);

                IF v_phone_id IS NOT NULL THEN
                    UPDATE "Customer"
                    SET "phone_id" = NULL WHERE "id" = p_user_id;
                    DELETE FROM "Phone_Number" WHERE "id" = v_phone_id;
                END IF;

                INSERT INTO "Phone_Number" ("id", "number", "extension")
                VALUES (gen_random_uuid(), p_phone_number, p_extension)
                RETURNING "id" INTO v_phone_id;

                UPDATE "Customer"
                SET "phone_id" = v_phone_id
                WHERE "id" = p_user_id;
            END IF;

            IF p_phone_number IS NOT NULL THEN
                IF v_first THEN
                    v_message := v_message || 'phone number';
                    v_first := FALSE;
                ELSE
                    v_message := v_message || ', phone number';
                END IF;
            END IF;

            IF p_extension IS NOT NULL THEN
              IF v_first THEN
                  v_message := v_message || 'extension';
                  v_first := FALSE;
              ELSE
                  v_message := v_message || ', extension';
              END IF;
            END IF;

            IF p_address IS NOT NULL THEN
                IF v_first THEN
                    v_message := v_message || 'address';
                    v_first := FALSE;
                ELSE
                    v_message := v_message || ', address';
                END IF;
            END IF;

            IF p_date_of_birth IS NOT NULL THEN
                IF v_first THEN
                    v_message := v_message || 'date of birth';
                    v_first := FALSE;
                ELSE
                    v_message := v_message || ', date of birth';
                END IF;
            END IF;

            IF p_gender IS NOT NULL THEN
                IF v_first THEN
                    v_message := v_message || 'gender';
                    v_first := FALSE;
                ELSE
                    v_message := v_message || ', gender';
                END IF;
            END IF;
       END IF;
    END IF;

    IF NOT v_first THEN
        UPDATE "Customer"
        SET "address" = COALESCE(p_address, "address"),
            "date_of_birth" = COALESCE(p_date_of_birth, "date_of_birth"),
            "gender" = COALESCE(p_gender, "gender")
        WHERE "id" = p_user_id;
        status := 'Success';
        message := v_message;
    END IF;
END;
$$;


-- Function: ChangePassword
/*
Description: Changes the password of an existing user after validating the old password.
Parameters:
- p_user_id: User ID of the user whose password is being changed (VARCHAR).
- p_old_password: Current password to validate (VARCHAR).
- p_new_password: New password to set (VARCHAR).
Returns: Status message indicating the result of the operation (VARCHAR).
*/
CREATE OR REPLACE FUNCTION change_password(
    p_user_id UUID,
    p_old_password text,
    p_new_password text,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_password VARCHAR;
BEGIN
    SELECT password INTO v_current_password
    FROM "User"
    WHERE id = p_user_id;
    IF v_current_password = p_old_password THEN
        IF p_new_password IS NOT NULL THEN
            UPDATE "User"
            SET password = p_new_password
            WHERE id = p_user_id;
            status := 'Success';
            message := 'Password updated successfully.';
        ELSE
            status := 'Failure';
            message := 'New password cannot be empty.';
        END IF;
    ELSE
        status := 'Failure';
        message := 'Old Password does not match.';
    END IF;
END;
$$;

-- Function: update_password
/*
Description: Updates the password of a user based on their email address.
Parameters:
- p_email: Email address of the user whose password is being updated (VARCHAR).
- p_new_password: New password to set (VARCHAR).
Returns: Status message indicating the result of the operation (VARCHAR).
*/
CREATE OR REPLACE FUNCTION update_password(
    p_email VARCHAR,
    p_new_password text,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS RECORD
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "User"
    SET password = p_new_password
    WHERE email = p_email;
    IF FOUND THEN
        status := 'Success';
        message := 'Password Updated successfully';
    ELSE
        status := 'Failure';
        message := 'User not found';
    END IF;
END;
$$;

-- Function: verify_email
/*
Description: Verifies the email address of a user by setting the 'email_verified' flag to TRUE.
Parameters:
- p_user_id: User ID of the user whose email is being verified (VARCHAR).
Returns: Status message indicating the result of the operation (VARCHAR).
*/
CREATE OR REPLACE FUNCTION verify_email(
    p_user_id UUID,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "User"
    SET "email_verified" = TRUE
    WHERE "id" = p_user_id;
    IF FOUND THEN
        status := 'Success';
        message := 'Email verified successfully';
    ELSE
        status := 'Failure';
        message := 'User not found';
    END IF;
END;
$$;

-- Function: ChangeEmail
/*
Description: Changes the email of an existing user after validating the current password.
Parameters:
- p_user_id: User ID of the user whose email is being changed (VARCHAR).
- p_password: Current password to validate (VARCHAR).
- p_new_email: New email to set (VARCHAR).
Returns: Status message indicating the result of the operation (VARCHAR).
*/
CREATE OR REPLACE FUNCTION change_email(
    p_user_id UUID,
    p_password text,
    p_new_email VARCHAR,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_password text;
BEGIN
    SELECT password INTO v_current_password
    FROM "User"
    WHERE id = p_user_id;
    IF v_current_password = p_password THEN
        IF p_new_email IS NOT NULL THEN
            UPDATE "User"
            SET email = p_new_email, "email_verified" = FALSE, "TwoFactorAuth_Enabled" = FALSE
            WHERE id = p_user_id;
            status := 'Success';
            message := 'Email updated successfully.';
        ELSE
            status := 'Failure';
            message := 'New Email cannot be empty.';
        END IF;
    ELSE
        status := 'Failure';
        message := 'Password does not match.';
    END IF;
END;
$$;



-- Function: update_TwoFactorAuth_Enabled
/*
Description: Updates the two-factor authentication status for a user.
Parameters:
- _id: User ID for whom the 2FA status is being updated (VARCHAR).
- _enabled: Boolean indicating whether 2FA is enabled or not (BOOLEAN).
Returns: Status message indicating the result of the operation (VARCHAR).
*/
CREATE OR REPLACE FUNCTION update_2fa_status(
    p_id UUID,
    p_enabled BOOLEAN,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS RECORD
LANGUAGE plpgsql
AS $$
DECLARE
    v_email_verified BOOLEAN;
BEGIN
    SELECT "email_verified" INTO v_email_verified
    FROM "User"
    WHERE "id" = p_id;

    IF NOT FOUND THEN
        status := 'Failure';
        message := 'User not found';
        RETURN;
    END IF;

    IF NOT v_email_verified THEN
        status := 'Failure';
        message := 'Email not verified';
        RETURN;
    END IF;

    UPDATE "User"
    SET "TwoFactorAuth_Enabled" = p_enabled
    WHERE "id" = p_id;

    IF FOUND THEN
        status := 'Success';
        message := '2FA status updated successfully';
    ELSE
        status := 'Failure';
        message := 'Update failed unexpectedly';
    END IF;
END;
$$;



-- Function: add_admin
/*
Description: Adds a new administrator to the system with a unique username and password.
Parameters:
- v_username: Username for the new administrator (VARCHAR).
- v_password: Password for the new administrator's account (VARCHAR).
Returns: Status message indicating successful addition of the administrator (VARCHAR).
*/
CREATE OR REPLACE FUNCTION add_admin(
    v_username VARCHAR,
    v_password text
)
RETURNS VARCHAR
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO "Administrator" ("id", "username", "password")
    VALUES (gen_random_uuid(), v_username, v_password);
    RETURN 'Administrator added successfully';
END;
$$;

-- Function: add_pharmacist
/*
Description: Adds a new pharmacist to the system, registering them as both a user and a pharmacist.
Parameters:
- p_first_name: First name of the pharmacist (VARCHAR).
- p_last_name: Last name of the pharmacist (VARCHAR).
- p_email: Email address of the pharmacist (VARCHAR).
- p_password: Password for the pharmacist's account (VARCHAR).
Returns: Status message indicating successful addition of the pharmacist (VARCHAR).
*/
CREATE OR REPLACE FUNCTION add_pharmacist(
    p_first_name VARCHAR,
    p_last_name VARCHAR,
    p_email VARCHAR,
    p_password text
)
RETURNS VARCHAR
LANGUAGE plpgsql
AS $$
DECLARE
    user_id UUID;
BEGIN
    INSERT INTO "User" ("id", "email", "password", "status", "role")
    VALUES (gen_random_uuid(), p_email, p_password, 'Active', 'Pharmacist')
    RETURNING "id" INTO user_id;
    INSERT INTO "Pharmacist" ("id", "first_name", "last_name")
    VALUES (user_id, p_first_name, p_last_name);
    RETURN 'Pharmacist added successfully';
END;
$$;

-- Function: ban_account
/*
Description: Bans a user's account by setting its status to 'Banned'.
Parameters:
- user_id: User ID of the account to be banned (VARCHAR).
Returns: Status message indicating successful account ban (VARCHAR).
*/
CREATE OR REPLACE FUNCTION ban_account(
    p_user_id UUID,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS RECORD
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "User"
    SET "status" = 'Banned'
    WHERE "id" = p_user_id;
    if found then
        status := 'Success';
        message := 'Account banned successfully';
    else
        status := 'Failure';
        message := 'Account not found';
    end if;
END;
$$;

-- Function: unban_account
/*
Description: Unbans a user's account by setting its status to 'Active'.
Parameters:
- user_id: User ID of the account to be unbanned (VARCHAR).
Returns: Status message indicating successful account unban (VARCHAR).
*/
CREATE OR REPLACE FUNCTION unban_account(
    p_user_id UUID,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS RECORD
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "User"
    SET "status" = 'Active'
    WHERE "id" = p_user_id;
    if found then
        status := 'Success';
        message := 'Account unbanned successfully';
    else
        status := 'Failure';
        message := 'Account not found';
    end if;
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
CREATE OR REPLACE FUNCTION login(
    p_email VARCHAR,
    p_password TEXT,
    OUT user_id UUID,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
DECLARE
    v_HashedPassword TEXT;
    v_TwoFactorAuth_Enabled BOOLEAN;
BEGIN
    -- Retrieve hashed password and 2FA status
    SELECT U.id, U.password, U."TwoFactorAuth_Enabled"
    INTO user_id, v_HashedPassword, v_TwoFactorAuth_Enabled
    FROM "User" U
    WHERE U.email = p_email;

    -- Check if user was found and verify password
    IF user_id IS NULL THEN
        status := 'Failure';
        message := 'User not found';
    ELSIF p_password = v_HashedPassword THEN
        IF v_TwoFactorAuth_Enabled THEN
            status := 'Pending';
            message := '2FA pending. Please complete the authentication.';
        ELSE
            status := 'Success';
            message := 'Logged in successfully';
        END IF;
    ELSE
        status := 'Failure';
        message := 'Wrong Password';
    END IF;
END;
$$;


-- Function: get_all_users
/*
Description: Authenticates an administrator login attempt by verifying username and password.
Parameters:
- username: Username of the administrator attempting to log in (VARCHAR).
- password: Password provided by the administrator attempting to log in (VARCHAR).
Returns: Status message indicating the outcome of the login attempt (VARCHAR).
*/
CREATE OR REPLACE FUNCTION get_users_by_ids(user_ids UUID[])
RETURNS TABLE(
    user_id UUID,
    email VARCHAR,
    "role" "Role",
    "status" "Status",
    email_verified BOOLEAN,
    first_name VARCHAR,
    last_name VARCHAR,
    address VARCHAR,
    date_of_birth DATE,
    image_id UUID,
    "gender" "Gender",
    phone_number VARCHAR,
    "extension" VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.id AS user_id,
        u.email,
        u.role,
        u.status,
        u.email_verified,
        COALESCE(c.first_name, p.first_name, '') AS first_name,
        COALESCE(c.last_name, p.last_name, '') AS last_name,
        COALESCE(c.address, '') AS address,
        c.date_of_birth,
        u.image_id,
        c.gender,
        COALESCE(pn.number, '') AS phone_number,
        COALESCE(pn.extension, '') AS extension
    FROM "User" u
    LEFT JOIN "Customer" c ON u.id = c.id
    LEFT JOIN "Pharmacist" p ON u.id = p.id
    LEFT JOIN "Phone_Number" pn ON c.phone_id = pn.id
    WHERE u.id = ANY(user_ids);
END;
$$;




-- Function: get_user
/*
Description: Retrieves the personal details of a user based on their user ID.
Parameters:
- p_id: User ID of the user whose details are being retrieved (VARCHAR).
Returns: A record containing the user's personal details.
*/
CREATE OR REPLACE FUNCTION get_user(p_id UUID)
RETURNS TABLE(
    user_id UUID,
    email VARCHAR,
    "role" "Role",
    "status" "Status",
    email_verified BOOLEAN,
    first_name VARCHAR,
    last_name VARCHAR,
    address VARCHAR,
    date_of_birth DATE,
    image_id UUID,
    "gender" "Gender",
    phone_number VARCHAR,
    "extension" VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.id AS user_id,
        u.email,
        u.role,
        u.status,
        u.email_verified,
        COALESCE(c.first_name, p.first_name, '') AS first_name,
        COALESCE(c.last_name, p.last_name, '') AS last_name,
        COALESCE(c.address, '') AS address,
        c.date_of_birth,
        u.image_id,
        c.gender,
        COALESCE(pn.number, '') AS phone_number,
        COALESCE(pn.extension, '') AS extension
    FROM "User" u
    LEFT JOIN "Customer" c ON u.id = c.id
    LEFT JOIN "Pharmacist" p ON u.id = p.id
    LEFT JOIN "Phone_Number" pn ON c.phone_id = pn.id
    WHERE u.id = p_id;
END;
$$;

-- Function: Login_Admin
/*
Description: Authenticates an administrator login attempt by verifying username and password.
Parameters:
- username: Username of the administrator attempting to log in (VARCHAR).
- password: Password provided by the administrator attempting to log in (VARCHAR).
Returns: Status message indicating the outcome of the login attempt (VARCHAR).
*/
CREATE OR REPLACE FUNCTION login_admin(
    p_username VARCHAR,
    p_password text,
    OUT admin_id UUID,
    OUT status VARCHAR,
    OUT message VARCHAR
)
RETURNS record
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_password text;  -- Declaration of the variable
BEGIN
    SELECT A.id, A.password
    INTO admin_id, v_current_password
    FROM "Administrator" A
    WHERE A.username = p_username;

    IF NOT FOUND THEN
        status := 'Failure';
        message := 'User not found';
    ELSIF p_password = v_current_password THEN
        status := 'Success';
        message := 'Logged in successfully';
    ELSE
        status := 'Failure';
        message := 'Wrong password';
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_user_ids()
RETURNS TABLE(user_id UUID)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT id AS user_id FROM "User";
END;
$$;








