package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import javax.management.InstanceAlreadyExistsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@SpringBootTest
@DisplayName("[CustomerService] - Unit Tests for create Customer")
public class CustomerServiceCreateTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CityService cityService;

    @MockBean
    private CustomerRepository repository;

    @Test
    public void create_shouldCallRepositoryToCreateANewCustomer() throws InstanceAlreadyExistsException {
        /* Given */
        final Customer newCustomer = JsonUtils.fromString(
                FileUtils.readFile("samples/customer_sample.json"),
                Customer.class
        );
        final Customer expectedCustomer = JsonUtils.fromString(
                FileUtils.readFile("./samples/existing_customer_sample.json"),
                Customer.class
        );
        given(this.repository.save(any(Customer.class))).willReturn(expectedCustomer);
        given(this.cityService.create(any(City.class))).willReturn(expectedCustomer.getCity());

        /* When */
        final Customer createdCustomer = this.service.create(newCustomer);

        /* Then */
        assertThat(createdCustomer).isEqualTo(expectedCustomer);
        verify(this.cityService).create(newCustomer.getCity());
        verify(this.repository).save(newCustomer);
    }

    @Test
    public void create_shouldThrowAnIllegalArgumentExceptionIdMustNotBeProvided() {
        final Customer newCustomer = JsonUtils.fromString(
                FileUtils.readFile("samples/existing_customer_sample.json"),
                Customer.class
        );
        assertThrows(IllegalArgumentException.class, () -> this.service.create(newCustomer));
        verifyNoInteractions(this.repository);
        verifyNoInteractions(this.cityService);
    }

}
