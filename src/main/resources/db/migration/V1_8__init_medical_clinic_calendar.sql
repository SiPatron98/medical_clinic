CREATE TABLE calendar
(
    calendar_id  SERIAL      NOT NULL,
    date_time     TIMESTAMP WITH TIME ZONE NOT NULL,
    doctor_id    INT NOT NULL,
    PRIMARY KEY (calendar_id),
       CONSTRAINT fk_calendar_doctor
            FOREIGN KEY (doctor_id)
                REFERENCES doctor (doctor_id)
);