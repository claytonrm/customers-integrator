package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.domain.CustomerCriteria;
import com.compasso.customersintegrator.domain.Gender;
import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;

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
        final Customer customer = getMockedExistingCustomer();
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

    @Test
    public void findById_shouldReturnNull() throws InstanceNotFoundException {
        assertThat(this.service.findById(null)).isNull();
    }

    @Test
    public void findByCriteria_shouldCallRepositoryToRetrieveCustomerByName() {
        /* Given */
        final Customer existingCustomer = getMockedExistingCustomer();
        final List<Customer> expectedCustomers = Arrays.asList(existingCustomer);
        given(this.repository.findByName(anyString())).willReturn(expectedCustomers);

        /* When */
        final List<Customer> customers = this.service.findByCriteria(CustomerCriteria.builder().name("Billy Jean").build());

        /* Then */
        assertThat(customers).isEqualTo(expectedCustomers);
    }

    @Test
    public void findByCriteria_shouldCallRepositoryToRetrieveAllCustomers() {
        /* Given */
        final Customer existingCustomer = getMockedExistingCustomer();
        final List<Customer> expectedCustomers = Arrays.asList(existingCustomer);
        given(this.repository.findAll()).willReturn(expectedCustomers);

        /* When */
        final List<Customer> customers = this.service.findByCriteria(null);

        /* Then */
        assertThat(customers).isEqualTo(expectedCustomers);
        verify(this.repository).findAll();
    }

    private Customer getMockedExistingCustomer() {
        return new Customer(
                1L,
                "Billy Jean",
                Gender.MALE,
                LocalDate.of(2000, 1, 22),
                20,
                new City(1L, "Florianópolis", "SC")
        );
    }

}
