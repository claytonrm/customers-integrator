package com.compasso.customersintegrator.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.domain.Gender;
import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@SpringBootTest
@DisplayName("[CustomerService] - Tests case for delete Customer")
public class CustomerServiceDeleteTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    @Test
    public void remove_shouldCallRepositoryToRemoveCustomerById() throws InstanceNotFoundException {
        /* Given */
        final Customer existingCustomer = getMockedExistingCustomer();
        given(this.repository.findById(anyLong())).willReturn(Optional.of(existingCustomer));

        /* When */
        this.service.remove(existingCustomer.getId());

        /* Then */
        verify(this.repository).delete(existingCustomer);
    }

    @Test
    public void remove_shouldThrowInstanceNotFoundException() {
        given(this.repository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> this.service.remove(44L));
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
