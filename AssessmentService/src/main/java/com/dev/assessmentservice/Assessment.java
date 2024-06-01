package com.dev.assessmentservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("assessment")
public class Assessment {
    @Field(targetType = FieldType.STRING)
    private UUID id;

    private Date date;
    private Type type;

    public Assessment(UUID id) {
        this.id = id;
        this.date = new Date();
        this.type = Type.ASSESSING;
    }
}
