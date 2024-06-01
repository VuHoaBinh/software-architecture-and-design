package com.dev.receivingservice;

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
@Document("receiving")
public class Receiving {
    @Field(targetType = FieldType.STRING)
    private UUID id;

    private String img;
    private Date date;

    public Receiving(ReceivingDTO info) {
        this.id = info.getId();
        this.img = info.getImg();
        this.date = new Date();
    }
}
