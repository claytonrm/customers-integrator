package com.compasso.customersintegrator.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@WebMvcTest(CustomerController.class)
@DisplayName("[CustomerController] - Test cases for GETs on CustomerController")
public class CustomerControllerGetTest extends ControllerBase {

    @Test
    public void get_shouldFindCustomerByResourceIdAndReturnSuccess() throws Exception {
        final String expectedResponse = FileUtils.readFile("samples/existing_customer_sample.json");
        final Customer existingCustomer = JsonUtils.fromString(expectedResponse, Customer.class);
        given(super.customerService.findById(anyLong())).willReturn(existingCustomer);

        super.mockMvc.perform(get(CUSTOMER_RELATIVE_PATH + existingCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void get_shouldReturnNotFound() throws Exception {
        given(super.customerService.findById(anyLong())).willThrow(InstanceNotFoundException.class);

        super.mockMvc.perform(get(CUSTOMER_RELATIVE_PATH + "44").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


}
