package com.hinesh.shopgenie.order.dto;

import com.hinesh.shopgenie.customer.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {


    private Long id;
    private CustomerDto customer;
    private String status;
}
