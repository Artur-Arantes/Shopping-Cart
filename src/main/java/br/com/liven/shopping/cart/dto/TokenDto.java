package br.com.liven.shopping.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record TokenDto(String token) {
}

