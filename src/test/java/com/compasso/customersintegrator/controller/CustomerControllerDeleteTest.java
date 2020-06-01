package com.compasso.customersintegrator.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.compasso.customersintegrator.domain.dto.CustomerDTO;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@WebMvcTest(CustomerController.class)
@DisplayName("[CustomerController] - Test cases for DELETEs on CustomerController")
public class CustomerControllerDeleteTest extends ControllerBase {

    @Test
    public void delete_shouldCallServiceToRemoveCustomerAndReturnSuccess() throws Exception {
        final CustomerDTO existingCustomer = JsonUtils.fromString(FileUtils.readFile("samples/existing_customer_sample.json"), CustomerDTO.class);
        given(super.customerService.findById(anyLong())).willReturn(super.toEntity(existingCustomer));

        super.mockMvc.perform(MockMvcRequestBuilders.delete(CUSTOMER_RELATIVE_PATH + existingCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        verify(super.customerService).remove(existingCustomer.getId());
    }

    @Test
    public void delete_shouldReturnNotFound() throws Exception {
        doThrow(InstanceNotFoundException.class).when(super.customerService).remove(anyLong());

        super.mockMvc.perform(MockMvcRequestBuilders.delete(CUSTOMER_RELATIVE_PATH + "4").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
