package com.treeaxes.suscripcionservice.DTO;

import lombok.Data;

@Data
public class    SuscripcionRequest {

    private Long userId;
    private Long planId;
    private String nombreTarjeta;
    private String numeroTarjeta;

}
