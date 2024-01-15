insert into medical_clinic_user (user_id, user_name, email, password, active) values (1, 'robert_sliwa', 'robert@sliwa.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (2, 'zygmunt_pacjent', 'zygmunt@pacjent.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (3, 'remigiusz_czornak', 'remigiusz@pzornak.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (4, 'maciej_maciejewski', 'maciej@maciejewski.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (5, 'robert_nowak', 'robert@nowak.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (6, 'adam_kowalski', 'adam@kowalski.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (7, 'ewelina_sliwa', 'ewelina@sliwa.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (8, 'alicja_pobozna', 'alicja@pobozna.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into medical_clinic_user (user_id, user_name, email, password, active) values (9, 'robert_lekarz', 'robert@lekarz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (10, 'adam_doktor', 'adam@doktor.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (11, 'martyna_lekarska', 'martyna@lekarska.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into medical_clinic_user (user_id, user_name, email, password, active) values (12, 'marcin_pobozny', 'marcin@pobozny.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into medical_clinic_user (user_id, user_name, email, password, active) values (13, 'patryk_luczak', 'patryk@luczak.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into medical_clinic_role (role_id, role) values (1, 'ADMIN'), (2, 'DOCTOR'), (3, 'PATIENT');

insert into medical_clinic_user_role (user_id, role_id) values (1, 3), (2, 3), (3, 3), (4, 3), (5, 3), (6, 3), (7, 3), (8, 3);
insert into medical_clinic_user_role (user_id, role_id) values (9, 2), (10, 2), (11, 2), (12, 2);
insert into medical_clinic_user_role (user_id, role_id) values (13, 1);

insert into PATIENT (name, surname, phone, pesel, email, address_id, user_id)
values
('Robert', 'Śliwa', '+48 983 233 321', '52070997836', 'robert@sliwa.pl', 1, 1),
('Zygmunt', 'Pacjent', '+48 723 233 021', '83011863727', 'zygmunt@pacjent.pl', 2, 2),
('Remigiusz', 'Czornak', '+48 342 233 321', '67111396321', 'remigiusz@pzornak.pl', 3, 3),
('Maciej', 'Maciejewski', '+48 564 233 321', '54073957136', 'maciej@maciejewski.pl', 4, 4),
('Robert', 'Nowak', '+48 232 233 321', '86073137836', 'robert@nowak.pl', 5, 5),
('Adam', 'Kowalski', '+48 434 233 321', '87230947536', 'adam@kowalski.pl', 6, 6),
('Ewelina', 'Śliwa', '+48 634 233 321', '75072393456', 'ewelina@sliwa.pl', 7, 7),
('Alicja', 'Pobożna', '+48 121 233 321', '97060995866', 'alicja@pobozna.pl', 8, 8);

insert into DISEASE (name, patient_id)
values
('Cukrzyca', 1),
('Choroba Hashimoto', 1),
('Nadciśnienie', 2),
('Cukrzyca', 4),
('Łuszczyca', 5),
('Padaczka', 8);

insert into DOCTOR (id_number, name, surname, specialization, user_id)
values
('1111', 'Robert', 'Lekarz', 'dermatologist', 9),
('2222', 'Adam', 'Doktor', 'eye doctor', 10),
('3333', 'Martyna', 'Lekarska', 'gynecologist', 11),
('4444', 'Marcin', 'Pobożny', 'pediatrician', 12);

insert into APPOINTMENT (id_number, date_time, note, doctor_id, patient_id)
values
('001', '2023-12-18 14:00:00 +01', 'Grypa - przepisano antybiotyk', 1, 1),
('002', '2024-03-21 10:00:00 +01', NULL, 1, 2),
('003', '2023-11-16 09:00:00 +01', 'covid - skierowano na testy', 2, 4),
('004', '2024-04-24 12:00:00 +01', NULL, 3, 5);


insert into CALENDAR (date_time, doctor_id)
values
('2023-12-18 14:00:00 +01:00', 1),
('2024-03-21 10:00:00 +01:00', 1),
('2023-11-16 09:00:00 +01:00', 2),
('2024-04-24 12:00:00 +01:00', 3);