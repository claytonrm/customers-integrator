package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@SpringBootTest
@DisplayName("[CustomerService] - Tests cases for find methods on Customer")
public class CustomerServiceFindTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    @Test
    public void findById_shouldFindCustomerById() throws InstanceNotFoundException {
        /* Given */
        final Customer customer = JsonUtils.fromString(
                FileUtils.readFile("./samples/existing_customer_sample.json"),
                Customer.class
        );
        given(this.repository.findById(customer.getId())).willReturn(Optional.of(customer));

        /* When */
        final Customer existingCustomer = this.service.findById(customer.getId());

        /* Then */
        assertThat(existingCustomer).isEqualTo(customer);
    }

    @Test
    public void findById_shouldThrowInstanceNotFoundException() {
        given(this.repository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> this.service.findById(99L));
    }

}
