package com.adedamola.medicalsoftware.controller;

import com.adedamola.medicalsoftware.model.Staff;
import com.adedamola.medicalsoftware.service.CSVExporterService;
import com.adedamola.medicalsoftware.service.PatientService;
import com.adedamola.medicalsoftware.service.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.UUID;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StaffController.class)
class StaffControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    StaffService staffService;

    @MockBean
    PatientService patientService;

    @MockBean
    CSVExporterService csvExporterService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveStaffSuccess() throws Exception {
        // given - precondition or setup
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        Staff staff = Staff.builder()
                .name("Adedamola Ajewole")
                .uuid(uuid)
                .registration_date(date).build();

        // when -  action or the behaviour that we are going test
        Mockito.when(staffService.saveStaff(staff)).thenReturn(staff);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/staff/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        // then - verify the output
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value(staff.getName()));
    }


    @Test
    public void givenUpdatedStaff_whenUpdateStaff_thenReturnUpdateStaffObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        Staff savedStaff = Staff.builder()
                .id(1)
                .name("Kin")
                .uuid(uuid)
                .registration_date(date).build();

        Staff updatedStaff = Staff.builder()
                .id(1)
                .name("Kin")
                .uuid(uuid)
                .registration_date(date).build();
        given(staffService.findByUUID(uuid)).willReturn(savedStaff);
        given(staffService.updateStaff(updatedStaff, uuid))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/staff/{uuid}/update", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStaff)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updatedStaff.getName()));
    }

    // JUnit test for update employee REST API - negative scenario
    @Test
    public void givenUpdatedStaff_whenUpdateStaff_thenReturn401() throws Exception {
        // given - precondition or setup
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        String invalidUUID = "5489b1b7-cb1c-4ae3-a3a0-66d1aa5ea67";
        Staff savedStaff = Staff.builder()
                .name("Kin")
                .uuid(uuid)
                .registration_date(date).build();

        Staff updatedStaff = Staff.builder()
                .name("Kins")
                .uuid(UUID.fromString(invalidUUID))
                .registration_date(date).build();
        given(staffService.findByUUID(updatedStaff.getUuid())).willReturn(null);
        given(staffService.updateStaff(updatedStaff, uuid))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/staff/{uuid}/update", UUID.fromString(invalidUUID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStaff)));

        // then - verify the output
        response.andExpect(status().isUnauthorized())
                .andDo(print());
    }

}