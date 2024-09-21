package com.semillero.ecosistemas.model;

import lombok.Getter;

@Getter
public enum Status {
    REVISION_INICIAL,
    ACEPTADO,
    DENEGADO,
    REQUIERE_CAMBIOS,
    CAMBIOS_REALIZADOS
}

