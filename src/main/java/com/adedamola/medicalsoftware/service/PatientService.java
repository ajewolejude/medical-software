package com.adedamola.medicalsoftware.service;

import com.adedamola.medicalsoftware.model.Patient;
import com.adedamola.medicalsoftware.repository.PatientRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PatientService {
    private final PatientRepository patientRepository;


    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> findAllPatientLessThanEqual(long age) {
        return patientRepository.findByAgeLessThanEqual(age);
    }

    public List<Patient> findByLastVisitDateBetween(Date startDate, Date endDate) {
        return patientRepository.findByLastVisitDateBetween(startDate, endDate);
    }

    @Transactional
    public void deleteByLastVisitDateBetween(Date startDate, Date endDate) {
        patientRepository.deleteByLastVisitDateBetween(startDate, endDate);
    }

    public Patient findById(long id) {
        Patient patient = patientRepository.findById(id).orElseGet(() -> null);
        return patient;
    }
}
