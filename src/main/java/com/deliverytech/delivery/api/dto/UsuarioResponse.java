package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Usuario;

public record UsuarioResponse(
        long id,
        String nome,
        String email
        ) {

    public static UsuarioResponse of(Usuario u) {
        if (u == null) {
            return null;
        }
        return new UsuarioResponse(
                u.getId(),
                u.getNome(),
                u.getEmail());
    }
}
