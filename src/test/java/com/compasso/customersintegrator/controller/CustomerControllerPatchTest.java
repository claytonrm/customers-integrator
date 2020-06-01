package com.compasso.customersintegrator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.compasso.customersintegrator.domain.dto.CustomerDTO;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;
import com.github.fge.jsonpatch.JsonPatch;


@WebMvcTest(CustomerController.class)
@DisplayName("[CustomerController] - Test cases for PATCHes on CustomerController")
public class CustomerControllerPatchTest extends ControllerBase {

    @Test
    public void update_shouldReturnSuccessContainingUpdatedResourceByCallingService() throws Exception {
        final String requestBody = FileUtils.readFile("./samples/customer_patch_name_sample.json");
        final String expectedResponseBody = FileUtils.readFile("./samples/customer_sample.json");
        final CustomerDTO expectedCustomer = JsonUtils.fromString(expectedResponseBody, CustomerDTO.class);
        given(super.customerService.update(anyLong(), any(JsonPatch.class))).willReturn(super.toEntity(expectedCustomer));

        super.mockMvc.perform(patch(CUSTOMER_RELATIVE_PATH + 1)
                .contentType("application/json-patch+json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    public void update_shouldReturnNotFoundResourceDoesNotExist() throws Exception {
        final String requestBody = FileUtils.readFile("./samples/customer_patch_name_sample.json");
        given(super.customerService.update(anyLong(), any(JsonPatch.class))).willThrow(InstanceNotFoundException.class);

        super.mockMvc.perform(patch(CUSTOMER_RELATIVE_PATH + 1)
                .contentType("application/json-patch+json")
                .content(requestBody))
                .andExpect(status().isNotFound());
    }
}
