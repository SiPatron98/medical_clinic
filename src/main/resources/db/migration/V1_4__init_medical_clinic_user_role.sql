CREATE TABLE medical_clinic_user_role
(
    user_id   INT      NOT NULL,
    role_id   INT      NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_car_medical_clinic_role_user
        FOREIGN KEY (user_id)
            REFERENCES medical_clinic_user (user_id),
    CONSTRAINT fk_medical_clinic_user_role_role
        FOREIGN KEY (role_id)
            REFERENCES medical_clinic_role (role_id)
);