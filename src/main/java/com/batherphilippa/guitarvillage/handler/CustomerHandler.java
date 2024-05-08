package com.batherphilippa.guitarvillage.handler;

import com.batherphilippa.guitarvillage.domain.Customer;
import com.batherphilippa.guitarvillage.domain.CustomerDTOIn;
import com.batherphilippa.guitarvillage.exception.ErrorResponse;
import com.batherphilippa.guitarvillage.service.CustomerService;
import com.batherphilippa.guitarvillage.validation.CustomerValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.batherphilippa.guitarvillage.exception.ErrorType.NOT_FOUND;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class CustomerHandler {

    private final CustomerService customerService;
    private final Validator validator;

    public CustomerHandler(CustomerService customerService) {
        this.customerService = customerService;
        this.validator = new CustomerValidator();
    }

    public Mono<ServerResponse> getAllCustomers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.findAll(), Customer.class);
    }

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {
        Mono<Customer> newCustomer = serverRequest.bodyToMono(Customer.class)
                .doOnNext(this::validate);

        return newCustomer.flatMap(customer -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.save(customer), Customer.class));
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");
        return customerService.findById(customerId)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(g), Customer.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Customer with id %s not found", customerId))), ErrorResponse.class));
    }

    public Mono<ServerResponse> updateCustomerById(ServerRequest serverRequest) throws IllegalArgumentException {
        String customerId = serverRequest.pathVariable("customerId");
        return customerService.updateById(serverRequest.bodyToMono(CustomerDTOIn.class), customerId)
                .doOnNext(this::validate)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(g))) // from Object deprecated
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Customer with id %s not found", customerId))), ErrorResponse.class));
    }

    public Mono<ServerResponse> deleteCustomerById(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");
        return customerService.findById(customerId)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(customerService.deleteById(g.getId()), ServerResponse.noContent().build().getClass()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Customer with id %s not found", customerId))), ErrorResponse.class));
    }

    private void validate(Customer customer) {
        Errors errors = new BeanPropertyBindingResult(customer, "customer");
        validator.validate(customer, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getAllErrors().toString());
        }
    }

}
