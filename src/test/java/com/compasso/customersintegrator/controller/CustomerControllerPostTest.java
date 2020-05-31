package com.compasso.customersintegrator.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashSet;

import javax.management.InstanceAlreadyExistsException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.model.Gender;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@WebMvcTest(CustomerController.class)
@DisplayName("[CustomerController] - Test Cases for POSTs on CustomerController")
public class CustomerControllerPostTest extends ControllerBase {

    @Test
    public void create_shouldCallServiceToCreateANewCustomerAndReturnCreated() throws Exception {
        final String requestBody = FileUtils.readFile("samples/customer_sample.json");
        final Customer expectedCustomer = new Customer(1L,
                "Billy Jean",
                Gender.MALE,
                LocalDate.of(2000, 1, 22),
                20,
                new City(1L, "Florianópolis", "SC")
        );
        given(super.customerService.create(any(Customer.class))).willReturn(expectedCustomer);

        super.mockMvc.perform(post(CUSTOMER_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.uri", is("http://localhost/v1/customers/1")));

        verify(super.customerService).create(JsonUtils.fromString(requestBody, Customer.class));
    }

    @Test
    public void create_shouldCallServiceToCreateANewCustomerWithNoCityAndReturnCreated() throws Exception {
        final String requestBody = FileUtils.readFile("samples/customer_without_city_sample.json");
        final Customer expectedCustomer = new Customer(1L,
                "Billy Jean",
                Gender.MALE,
                LocalDate.of(2000, 1, 22),
                20,
                null
        );
        given(super.customerService.create(any(Customer.class))).willReturn(expectedCustomer);

        super.mockMvc.perform(post(CUSTOMER_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.uri", is("http://localhost/v1/customers/1")));

        verify(super.customerService).create(JsonUtils.fromString(requestBody, Customer.class));
    }

    @Test
    public void create_shouldCallServiceAndThrowBadRequestCityDoesNotExist() throws Exception {
        final String requestBody = FileUtils.readFile("samples/customer_city_id_sample.json");
        given(super.customerService.create(any(Customer.class))).willThrow(new ConstraintViolationException(
                "City ID does not exist", new HashSet<>()
        ));

        super.mockMvc.perform(post(CUSTOMER_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verify(super.customerService).create(JsonUtils.fromString(requestBody, Customer.class));
    }

    @Test
    public void create_shouldReturnConflictCustomerAlreadyExists() throws Exception {
        final String requestBody = FileUtils.readFile("samples/existing_customer_sample.json");
        final InstanceAlreadyExistsException e = new InstanceAlreadyExistsException("Customer 1 already exists.");
        given(super.customerService.create(any(Customer.class))).willThrow(e);

        super.mockMvc.perform(post(CUSTOMER_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]", is(e.getMessage())));
    }


}
