package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.domain.Gender;
import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;

@SpringBootTest
@DisplayName("[CustomerService] - Tests case for create Customer")
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
        final Customer newCustomer = getMockedCustomer();
        final Customer expectedCustomer = getMockedExistingCustomer();
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
        final Customer newCustomer = getMockedExistingCustomer();
        assertThrows(IllegalArgumentException.class, () -> this.service.create(newCustomer));
        verifyNoInteractions(this.repository);
        verifyNoInteractions(this.cityService);
    }

    private Customer getMockedCustomer() {
        return new Customer(
                null,
                "Billy Jean",
                Gender.MALE,
                LocalDate.of(2000, 1, 22),
                20,
                new City(null, "Florianópolis", "SC")
        );
    }

    private Customer getMockedExistingCustomer() {
        final Customer customer = getMockedCustomer();
        customer.setId(1L);
        customer.getCity().setId(1L);
        return customer;
    }

}
