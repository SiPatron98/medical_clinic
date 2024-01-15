CREATE TABLE appointment
(
    appointment_id  SERIAL      NOT NULL,
    id_number  VARCHAR(32) NOT NULL,
    date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    note TEXT,
    doctor_id    INT NOT NULL,
    patient_id    INT NOT NULL,
    UNIQUE (id_number),
    PRIMARY KEY (appointment_id),
       CONSTRAINT fk_appointment_doctor
            FOREIGN KEY (doctor_id)
                REFERENCES doctor (doctor_id)
);