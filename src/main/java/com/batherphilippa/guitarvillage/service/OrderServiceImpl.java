package com.batherphilippa.guitarvillage.service;

import com.batherphilippa.guitarvillage.domain.Order;
import com.batherphilippa.guitarvillage.domain.OrderDTOIn;
import com.batherphilippa.guitarvillage.respository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepo;

    public OrderServiceImpl(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public Flux<Order> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public Mono<Order> findById(String id) {
        return orderRepo.findById(id);
    }

    @Override
    public Mono<Order> save(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public Mono<Order> updateById(Mono<OrderDTOIn> order, String id) {
        return order.flatMap(o -> orderRepo.findById(id)
                .flatMap((o1) -> {
                    o1.setDate(o.getDate());
                    o1.setCustomerId(o.getCustomerId());
                    o1.setProductId(o.getProductId());
                    o1.setQuantity(o.getQuantity());
                    return orderRepo.save(o1);
                }));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return orderRepo.deleteById(id);
    }
}
