package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuitarDTOIn implements Serializable {
    
    @Field
    private ProductType product;
    @Field
    private String make;
    @Field
    private String model;
    @Field
    private String colour;
    @Field
    private String serialNumber;
    @Field
    private BigDecimal price;
    @Field
    private InstrumentType type;
    @Field
    private String description;

}
