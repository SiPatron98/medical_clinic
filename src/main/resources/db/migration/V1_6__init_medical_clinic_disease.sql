CREATE TABLE disease
(
    disease_id  SERIAL      NOT NULL,
    name     VARCHAR(32) NOT NULL,
    patient_id    INT NOT NULL,
    PRIMARY KEY (disease_id),
       CONSTRAINT fk_disease_patient
            FOREIGN KEY (patient_id)
                REFERENCES patient (patient_id)
);