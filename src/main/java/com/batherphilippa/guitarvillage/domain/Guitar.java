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
@Document("guitar")
public class Guitar {

    @Id
    private String id;
    @Field
    private String make;
    @Field
    private String model;
    @Field
    private String serialNumber;
    @Field
    private BigDecimal price;
    @Field
    private InstrumentType instrumentType;
    @Field
    private String description;


}
