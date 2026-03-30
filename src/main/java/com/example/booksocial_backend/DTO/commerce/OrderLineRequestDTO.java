package com.example.booksocial_backend.DTO.commerce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineRequestDTO {

  private Long productId;
  private Long orderId;
  private Integer quantity;

}