package com.compasso.customersintegrator.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@WebMvcTest(CustomerController.class)
@DisplayName("[CustomerController] - Test cases for PUTs on CustomerController")
public class CustomerControllerPutTest extends ControllerBase {

    @Test
    public void update_shouldCallServiceToUpdateEntireCustomer() throws Exception {
        final String requestBody = FileUtils.readFile("./samples/customer_sample.json");
        final Customer customer = JsonUtils.fromString(requestBody, Customer.class);

        super.mockMvc.perform(put(CUSTOMER_RELATIVE_PATH + 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody))
                .andExpect(status().isNoContent());

        verify(super.customerService).update(1L, customer);
    }

}
