package com.batherphilippa.guitarvillage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTOIn {

    @Field
    private String forename;
    @Field
    private String surname;
    @Field
    private String email;
    @Field
    private String tel;

}
