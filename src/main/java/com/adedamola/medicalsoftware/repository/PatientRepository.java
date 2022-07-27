package com.adedamola.medicalsoftware.repository;

import com.adedamola.medicalsoftware.model.Patient;
import com.adedamola.medicalsoftware.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, Long> {

     List<Patient> findByAgeLessThanEqual(long age);
    List<Patient> findByLastVisitDateBetween(Date startDate, Date endDate);

    @Transactional
    void deleteByLastVisitDateBetween(Date startDate, Date endDate);
//  List<Staff> findByPublished(boolean published);
//  List<Staff> findByTitleContaining(String title);
}