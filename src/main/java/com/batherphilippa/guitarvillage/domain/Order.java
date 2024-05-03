package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("order")
public class Order {

    @Id
    private String id;
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
