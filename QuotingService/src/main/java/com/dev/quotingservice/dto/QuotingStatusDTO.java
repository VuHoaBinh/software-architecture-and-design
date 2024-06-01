package com.dev.quotingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuotingStatusDTO {
    @Field(targetType = FieldType.STRING)
    private UUID id;
    private String idUser;
    private Double priceBy;
}
