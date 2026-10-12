CREATE TABLE contacts (
    contact_id VARCHAR(10) NOT NULL,
    first_name VARCHAR(10) NOT NULL,
    last_name VARCHAR(10) NOT NULL,
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(30) NOT NULL,

    PRIMARY KEY (contact_id),

    CONSTRAINT chk_contact_id_not_empty
        CHECK (CHAR_LENGTH(TRIM(contact_id)) > 0),

    CONSTRAINT chk_first_name_not_empty
        CHECK (CHAR_LENGTH(TRIM(first_name)) > 0),

    CONSTRAINT chk_last_name_not_empty
        CHECK (CHAR_LENGTH(TRIM(last_name)) > 0),

    CONSTRAINT chk_address_not_empty
        CHECK (CHAR_LENGTH(TRIM(address)) > 0),

    CONSTRAINT chk_phone_number
        CHECK (
            CHAR_LENGTH(phone_number) = 10
            AND NOT REGEXP_LIKE(phone_number, '[^0-9]')
        )
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_bin;