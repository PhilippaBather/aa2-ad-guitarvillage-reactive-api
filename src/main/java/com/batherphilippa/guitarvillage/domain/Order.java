package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("order")
public class Order implements Serializable {

    @Id
    private String id;
    @Field
    private LocalDateTime creationDate;
    @Field
    private String customerId;
    @Field
    private String productId;
    @Field
    private BigDecimal price;   // price as sold will not always reflect that bound to the product in the db
    @Field
    private int quantity;

}
