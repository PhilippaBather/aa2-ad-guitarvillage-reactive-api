package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTOIn implements Serializable {

    @Field
    private LocalDateTime creationDate;
    @Field
    private String customerId;
    @Field
    private String productId;
    @Field
    private int quantity;
    @Field
    private BigDecimal price;      // price as sold will not always reflect that bound to the product in the db
}
