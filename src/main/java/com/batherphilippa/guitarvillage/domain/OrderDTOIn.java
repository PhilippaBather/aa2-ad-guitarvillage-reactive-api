package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTOIn {

    @Field
    private String date;
    @Field
    private String customerId;
    @Field
    private String productId;
    @Field
    private int quantity;
    @Field
    private BigDecimal price;
}
