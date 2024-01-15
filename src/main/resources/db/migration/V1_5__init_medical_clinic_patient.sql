CREATE TABLE patient
(
    patient_id  SERIAL      NOT NULL,
    name     VARCHAR(32) NOT NULL,
    surname        VARCHAR(32) NOT NULL,
    phone        VARCHAR(32) NOT NULL,
    pesel VARCHAR(32) NOT NULL,
    email    VARCHAR(32) NOT NULL,
    address_id    INT,
    user_id   INT,
    PRIMARY KEY (patient_id),
    UNIQUE (pesel),
    UNIQUE (email),
        CONSTRAINT fk_patient_address
            FOREIGN KEY (address_id)
                REFERENCES address (address_id),
        CONSTRAINT fk_patient_user
                    FOREIGN KEY (user_id)
                        REFERENCES medical_clinic_user (user_id)
);