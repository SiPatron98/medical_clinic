CREATE TABLE doctor
(
    doctor_id  SERIAL      NOT NULL,
    id_number  VARCHAR(32) NOT NULL,
    name     VARCHAR(32) NOT NULL,
    surname        VARCHAR(32) NOT NULL,
    specialization        VARCHAR(32) NOT NULL,
    user_id    INT,
    UNIQUE (id_number),
    PRIMARY KEY (doctor_id),
       CONSTRAINT fk_doctor_user
            FOREIGN KEY (user_id)
                REFERENCES medical_clinic_user (user_id)
);