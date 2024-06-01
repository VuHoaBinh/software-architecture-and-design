package com.dev.quotingservice.dto;

import com.dev.quotingservice.entity.Category;
import com.dev.quotingservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuotingDTO {
    private String idUser;
    private Category category;
    private Long priceOrigin;
    private Date dateBy;
    private Status status;
}
