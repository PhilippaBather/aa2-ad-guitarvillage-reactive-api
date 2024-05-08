package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("customer")
public class Customer {

    @Id
    private String id;
    @Field
    private String forename;
    @Field
    private String surname;
    @Field
    private String email;
    @Field
    private String tel;

    // TODO: orders
}
