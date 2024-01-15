CREATE TABLE medical_clinic_user
(
    user_id   SERIAL        NOT NULL,
    user_name VARCHAR(32)   NOT NULL,
    email     VARCHAR(32)   NOT NULL,
    password  VARCHAR(128)  NOT NULL,
    active    BOOLEAN       NOT NULL,
    PRIMARY KEY (user_id)
);