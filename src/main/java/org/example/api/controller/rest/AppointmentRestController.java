package org.example.api.controller.rest;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.AppointmentService;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Appointment;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.example.domain.exception.ProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping(AppointmentRestController.API_APPOINTMENT)
public class AppointmentRestController {
    public static final String API_APPOINTMENT = "/api/appointment";
    public static final String NEW_APPOINTMENT = "/api/appointment/new";

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @PostMapping(value = NEW_APPOINTMENT)
    public AppointmentDTO makeAnAppointment(
            @RequestParam(value = "doctorId") String doctorId,
            @RequestParam(value = "pesel") String pesel,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") String month,
            @RequestParam(value = "day") Integer day,
            @RequestParam(value = "hour") Integer hour
    ) {
        LinkedHashMap<String, Integer> mapOfMonthsPositions = getMapOfMonthsPositions();
        LinkedHashMap<String, Integer> mapOfDaysAndMonths = getMapOfDaysAndMonths();
        LinkedHashMap<String, Integer> mapOfDaysAndMonthsLeapYear = getMapOfDaysAndMonthsLeapYear();
        Integer monthInt = mapOfMonthsPositions.get(month);
        Doctor doctor = doctorService.findDoctor(doctorId);
        Patient patient = patientService.findPatient(pesel);


        if (isALeapYear(year)) {
            if (day > mapOfDaysAndMonthsLeapYear.get(month)) {
                throw new ProcessingException("There is no so many days in this month!");
            }
        } else {
            if (day > mapOfDaysAndMonths.get(month)) {
                throw new ProcessingException("There is no so many days in this month!");
            }
        }

        OffsetDateTime dateOfAppointment = OffsetDateTime.of(year, monthInt, day, hour, 0, 0, 0, ZoneOffset.UTC);

        if (dateOfAppointment.isBefore(OffsetDateTime.now())) {
            throw new ProcessingException("You can't make an appointment in the past");
        }

        Appointment appointment = Appointment.builder()
                .dateTime(dateOfAppointment)
                .doctor(doctor)
                .patient(patient)
                .build();
        appointmentService.makeAnAppointment(appointment);
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
        return appointmentDTO;
    }

    private static LinkedHashMap<String, Integer> getMapOfDaysAndMonths() {
        LinkedHashMap<String, Integer> mapOfDaysAndMonths = new LinkedHashMap<>();
        mapOfDaysAndMonths.put("January", 31);
        mapOfDaysAndMonths.put("February", 28);
        mapOfDaysAndMonths.put("March", 31);
        mapOfDaysAndMonths.put("April", 30);
        mapOfDaysAndMonths.put("May", 31);
        mapOfDaysAndMonths.put("June", 30);
        mapOfDaysAndMonths.put("July", 31);
        mapOfDaysAndMonths.put("August", 31);
        mapOfDaysAndMonths.put("September", 30);
        mapOfDaysAndMonths.put("October", 31);
        mapOfDaysAndMonths.put("November", 30);
        mapOfDaysAndMonths.put("December", 31);
        return mapOfDaysAndMonths;
    }

    private static LinkedHashMap<String, Integer> getMapOfDaysAndMonthsLeapYear() {
        LinkedHashMap<String, Integer> mapOfDaysAndMonthsLeapYear = new LinkedHashMap<>();
        mapOfDaysAndMonthsLeapYear.put("January", 31);
        mapOfDaysAndMonthsLeapYear.put("February", 29);
        mapOfDaysAndMonthsLeapYear.put("March", 31);
        mapOfDaysAndMonthsLeapYear.put("April", 30);
        mapOfDaysAndMonthsLeapYear.put("May", 31);
        mapOfDaysAndMonthsLeapYear.put("June", 30);
        mapOfDaysAndMonthsLeapYear.put("July", 31);
        mapOfDaysAndMonthsLeapYear.put("August", 31);
        mapOfDaysAndMonthsLeapYear.put("September", 30);
        mapOfDaysAndMonthsLeapYear.put("October", 31);
        mapOfDaysAndMonthsLeapYear.put("November", 30);
        mapOfDaysAndMonthsLeapYear.put("December", 31);
        return mapOfDaysAndMonthsLeapYear;
    }

    private static LinkedHashMap<String, Integer> getMapOfMonthsPositions() {
        LinkedHashMap<String, Integer> mapOfMonthsPositions = new LinkedHashMap<>();
        mapOfMonthsPositions.put("January", 1);
        mapOfMonthsPositions.put("February", 2);
        mapOfMonthsPositions.put("March", 3);
        mapOfMonthsPositions.put("April", 4);
        mapOfMonthsPositions.put("May", 5);
        mapOfMonthsPositions.put("June", 6);
        mapOfMonthsPositions.put("July", 7);
        mapOfMonthsPositions.put("August", 8);
        mapOfMonthsPositions.put("September", 9);
        mapOfMonthsPositions.put("October", 10);
        mapOfMonthsPositions.put("November", 11);
        mapOfMonthsPositions.put("December", 12);
        return mapOfMonthsPositions;
    }

    private boolean isALeapYear(Integer year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
