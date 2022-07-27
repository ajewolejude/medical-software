package com.adedamola.medicalsoftware.repository;

import com.adedamola.medicalsoftware.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByUuid(UUID uuid);

//  List<Staff> findByPublished(boolean published);
//  List<Staff> findByTitleContaining(String title);
}