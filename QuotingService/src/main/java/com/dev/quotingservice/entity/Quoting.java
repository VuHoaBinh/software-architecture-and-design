package com.dev.quotingservice.entity;

import com.dev.quotingservice.dto.QuotingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Quoting {
    @Field(targetType = FieldType.STRING)
    private UUID id;
    private Date dateRecycling;

    private String idUser;

    private Category category;
    private Long priceOrigin;
    private Date dateBy;
    private Status status;

    private Double priceBy;

    public Quoting(QuotingDTO info) {
        this.id = UUID.randomUUID();
        this.dateRecycling = new Date();

        this.idUser = info.getIdUser();
        this.category = info.getCategory();
        this.priceOrigin = info.getPriceOrigin();
        this.dateBy = info.getDateBy();
        this.status = info.getStatus();
    }
}
