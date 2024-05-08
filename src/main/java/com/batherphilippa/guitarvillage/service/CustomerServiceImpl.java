package com.batherphilippa.guitarvillage.service;

import com.batherphilippa.guitarvillage.domain.Customer;
import com.batherphilippa.guitarvillage.domain.CustomerDTOIn;
import com.batherphilippa.guitarvillage.respository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Flux<Customer> findAll() {
        return customerRepo.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return customerRepo.findById(id);
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Mono<Customer> updateById(Mono<CustomerDTOIn> customer, String id) {
        return customer.flatMap(c -> customerRepo.findById(id)
                .flatMap((c1) -> {
                    c1.setForename(c.getForename());
                    c1.setSurname(c.getSurname());
                    c1.setEmail(c.getEmail());
                    c1.setTel(c.getTel());
                    return customerRepo.save(c1);
                }));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return customerRepo.deleteById(id);
    }
}
