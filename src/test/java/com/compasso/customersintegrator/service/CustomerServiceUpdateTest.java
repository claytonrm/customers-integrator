package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.domain.Gender;
import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;
import com.github.fge.jsonpatch.JsonPatch;

@SpringBootTest
@DisplayName("[CustomerService] - Tests case for update Customer")
public class CustomerServiceUpdateTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    @Test
    public void update_shouldCallRepositoryToPerformAPartialUpdate() throws InstanceNotFoundException {
        /* Given */
        final Customer existingCustomer = getMockedExistingCustomer();
        given(this.repository.findById(anyLong())).willReturn(Optional.of(existingCustomer));
        given(this.repository.save(any(Customer.class))).willReturn(existingCustomer);
        final JsonPatch mockedPatch = new JsonPatch(Mockito.anyList());

        /* When */
        final Customer patchedCustomer = this.service.update(existingCustomer.getId(), mockedPatch);

        /* Then */
        assertThat(patchedCustomer).isEqualTo(existingCustomer);
        verify(this.repository).save(existingCustomer);
    }

    @Test
    public void update_shouldCallRepositoryToPerformEntireUpdate() throws InstanceNotFoundException {
        /* Given */
        final Customer existingCustomer = getMockedExistingCustomer();
        given(this.repository.findById(anyLong())).willReturn(Optional.of(existingCustomer));
        given(this.repository.save(any(Customer.class))).willReturn(existingCustomer);

        /* When */
        this.service.update(existingCustomer.getId(), existingCustomer);

        /* Then */
        verify(this.repository).findById(existingCustomer.getId());
        verify(this.repository).save(existingCustomer);
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
