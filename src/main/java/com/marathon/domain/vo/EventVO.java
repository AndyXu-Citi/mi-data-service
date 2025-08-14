package com.marathon.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EventVO {
    Long id;
    String title;
    Date date;
    String location;
    BigDecimal price;
    String image;
    String status;
}
