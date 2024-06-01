package com.dev.itemstatusservice;

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
@Document("status")
public class QuotingStatus {
    @Field(targetType = FieldType.STRING)
    private UUID id;
    private String idUser;
    private Date date;
    private Status status;
    private Double priceBy;

    public QuotingStatus(QuotingStatusDTO info) {
        this.id = info.getId();
        this.idUser = info.getIdUser();
        this.priceBy = info.getPriceBy();
        this.date = new Date();
        this.status = Status.SHIPPING;
    }
}
