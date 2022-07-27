package com.adedamola.medicalsoftware.service;

import com.adedamola.medicalsoftware.model.Patient;
import com.adedamola.medicalsoftware.model.Staff;
import com.adedamola.medicalsoftware.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    private PatientService patientService;


    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }


    public Staff saveStaff(Staff newStaff) {
        UUID uuid = UUID.randomUUID();
        Staff staff = staffRepository.save(new Staff(newStaff.getName(), uuid, new Date()));
        return staff;
    }
    private Map<String, Object> serializeStaff(Staff staff) {
        return staff.serialize();
    }

    public Staff findByUUID(UUID uuid) {
        Staff staff = staffRepository.findByUuid(uuid);
        return staff;
    }


    public boolean isUserPermitted(UUID uuid) {
        Staff staff = findByUUID(uuid);
        if (staff != null) {
            return true;
        } else {
            return false;
        }
    }

    public Staff updateStaff(Staff staffBody, UUID uuid) {
        Staff staff = findByUUID(uuid);
        if (staff != null) {
            staff.setName(staffBody.getName());
            Staff updatedStaff =  staffRepository.save(staff);
            return updatedStaff;
        } else {
            return null;
        }
    }

}
