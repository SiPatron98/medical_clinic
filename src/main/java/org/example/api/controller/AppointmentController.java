package org.example.api.controller;

import lombok.AllArgsConstructor;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/new_appointment")
@AllArgsConstructor
public class AppointmentController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping
    public String appointmentPanelPage(
            @RequestParam(value = "doctorId", required = false) String doctorId,
            @RequestParam(value = "pesel", required = false) String pesel,
            Model model
    ) {
        var allPesels = getAvailablePatients().stream()
                .map(PatientDTO::getPesel)
                .toList();
        var doctorIds = getAvailableDoctors().stream()
                .map(DoctorDTO::getIdNumber)
                .toList();
        var allDoctorAppointments = appointmentService.findAvailableByDoctorsId(doctorId).stream()
                .map(appointmentMapper::map)
                .collect(Collectors.toSet());
        var futureDoctorAppointments = allDoctorAppointments.stream()
                .filter(appointmentDTO -> appointmentDTO.getDateTime().isAfter(OffsetDateTime.now()))
                .collect(Collectors.toSet());
        var allPatientAppointments = appointmentService.findAvailableByPatientsPesel(pesel).stream()
                .map(appointmentMapper::map)
                .collect(Collectors.toSet());
        var futurePatientAppointments = allPatientAppointments.stream()
                .filter(appointmentDTO -> appointmentDTO.getDateTime().isAfter(OffsetDateTime.now()))
                .collect(Collectors.toSet());

        var futureAppointments = new HashSet<>(futureDoctorAppointments);
        futureAppointments.addAll(futurePatientAppointments);
        List<Integer> years = List.of(2024, 2025, 2026);


        LinkedHashMap<String, Integer> mapOfDaysAndMonths = getMapOfDaysAndMonths();
        LinkedHashMap<String, Integer> mapOfDaysAndMonthsLeapYear = getMapOfDaysAndMonthsLeapYear();


        List<Integer> days = new ArrayList<>();

        for (int i = 0; i < 31; i++) {
            days.add(i + 1);
        }
        List<Integer> hours = List.of(8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);

        model.addAttribute("allPesels", allPesels);
        model.addAttribute("doctorIds", doctorIds);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("pesel", pesel);
        model.addAttribute("futureDoctorAppointments", futureDoctorAppointments);
        model.addAttribute("futurePatientAppointments", futurePatientAppointments);
        model.addAttribute("futureAppointments", futureAppointments);
        model.addAttribute("years", years);
        model.addAttribute("days", days);
        model.addAttribute("hours", hours);
        model.addAttribute("mapOfDaysAndMonths", mapOfDaysAndMonths);
        model.addAttribute("mapOfDaysAndMonthsLeapYear", mapOfDaysAndMonthsLeapYear);
        return "new_appointment";
    }

    @PostMapping("/new")
    public String makeAnAppointment(
            @RequestParam(value = "doctorId") String doctorId,
            @RequestParam(value = "pesel") String pesel,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") String month,
            @RequestParam(value = "day") Integer day,
            @RequestParam(value = "hour") Integer hour,
            ModelMap model
    ) {

        LinkedHashMap<String, Integer> mapOfMonthsPositions = getMapOfMonthsPositions();
        LinkedHashMap<String, Integer> mapOfDaysAndMonths = getMapOfDaysAndMonths();
        LinkedHashMap<String, Integer> mapOfDaysAndMonthsLeapYear = getMapOfDaysAndMonthsLeapYear();
        Doctor doctor = doctorService.findDoctor(doctorId);
        Patient patient = patientService.findPatient(pesel);
        Integer monthInt = mapOfMonthsPositions.get(month);

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

        model.addAttribute("patientName", patient.getName());
        model.addAttribute("patientSurname", patient.getSurname());
        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("doctorSurname", doctor.getSurname());
        model.addAttribute("appointmentDate", appointment.getDateTime().toLocalDateTime());

        return "success_appointment";
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


    private List<PatientDTO> getAvailablePatients() {
        return patientService.findAvailable().stream()
                .map(patientMapper::map)
                .toList();
    }

    private List<DoctorDTO> getAvailableDoctors() {
        return doctorService.findAvailable().stream()
                .map(doctorMapper::map)
                .toList();

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
