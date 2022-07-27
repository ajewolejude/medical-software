package com.adedamola.medicalsoftware.controller;

import com.adedamola.medicalsoftware.model.Patient;
import com.adedamola.medicalsoftware.model.Staff;
import com.adedamola.medicalsoftware.service.CSVExporterService;
import com.adedamola.medicalsoftware.service.PatientService;
import com.adedamola.medicalsoftware.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private CSVExporterService csvExporterService;

    @PostMapping("/create")
    public ResponseEntity<Staff> saveStaff(@RequestBody Staff staff) {
        try {
            Staff _staff = staffService
                    .saveStaff(staff);
            return new ResponseEntity<>(_staff, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{uuid}/update")
    public ResponseEntity<?> updateStaff(@PathVariable("uuid") UUID uuid, @RequestBody Staff staffBody) {

        Staff updatedStaff = staffService.updateStaff(staffBody, uuid);
        if (updatedStaff != null) {
            return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }


    }


    // bonus method
    @GetMapping("/{uuid}/last-visit")
    public ResponseEntity<?> findByLastVisitDateBetween(@PathVariable("uuid") UUID uuid,
                                                        @RequestParam(value = "startDate")
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                        @RequestParam(value = "endDate")
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        if (staffService.isUserPermitted(uuid)) {
            List<Patient> patientList = patientService.findByLastVisitDateBetween(startDate, endDate);
            patientList.forEach(patient -> {
                patient.setLast_visit_date(patient.getLastVisitDate());
            });
            if (patientList.size() > 0) {
                return new ResponseEntity<>(patientList, HttpStatus.OK);
            } else {
                return ResponseEntity.ok()
                        .body("No patient profiles");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }
    }

    @DeleteMapping("/{uuid}/last-visit/delete")
    public ResponseEntity<?> deleteByLastVisitDateBetween(@PathVariable("uuid") UUID uuid, @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                          @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        if (staffService.isUserPermitted(uuid)) {
            patientService.deleteByLastVisitDateBetween(startDate, endDate);
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }
    }


    @GetMapping("/{uuid}/patient/age")
    public ResponseEntity<?> findAllPatientLessThanOrEqualToTwo(@PathVariable("uuid") UUID uuid) {

        if (staffService.isUserPermitted(uuid)) {
            //since age limit is 2
            List<Patient> patientList = patientService.findAllPatientLessThanEqual(2);
            if (patientList.size() > 0) {
                return new ResponseEntity<>(patientList, HttpStatus.OK);
            } else {
                return ResponseEntity.ok()
                        .body("No patient profiles");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }


    }

    //bonus method
    @GetMapping("/{uuid}/patient/{age}/age")
    public ResponseEntity<?> findAllPatientLessThanOrEqualThan(@PathVariable("uuid") UUID uuid, @PathVariable("age") long age) {

        if (staffService.isUserPermitted(uuid)) {
            //since age limit is 2
            List<Patient> patientList = patientService.findAllPatientLessThanEqual(age);
            if (patientList.size() > 0) {
                return new ResponseEntity<>(patientList, HttpStatus.OK);
            } else {
                return ResponseEntity.ok()
                        .body("No patient profiles");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }


    }

    //bonus method
    @GetMapping("/{uuid}/{id}/patient")
    public ResponseEntity<?> findOnePatient(@PathVariable("uuid") UUID uuid, @PathVariable("id") long id) {

        if (staffService.isUserPermitted(uuid)) {
            Patient patient = patientService.findById(id);

            if (patient != null) {
                return new ResponseEntity<>(patient, HttpStatus.OK);
            } else {
                return ResponseEntity.ok()
                        .body("No patient profiles");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }
    }

    @GetMapping(value = "/{uuid}/{id}/patient/exportCSV", produces = "text/csv")
    public ResponseEntity<?> findOnePatientAndExport(@PathVariable("uuid") UUID uuid, @PathVariable("id") long id) {

        if (staffService.isUserPermitted(uuid)) {
            Patient patient = patientService.findById(id);

            if (patient != null) {
                InputStreamResource fileInputStream = csvExporterService.getFileInputStream(patient);

                String csvFileName = "people.csv";

                // setting HTTP headers
                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
                // defining the custom Content-Type
                headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

                return new ResponseEntity<>(
                        fileInputStream,
                        headers,
                        HttpStatus.OK
                );

            } else {
                return ResponseEntity.ok()
                        .body("No patient profiles");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to make updates.");
        }
    }

}