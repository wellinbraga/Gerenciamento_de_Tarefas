package br.com.stackmob.dto;

import br.com.stackmob.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(
        Long id,
        String name,
        int order,
        BoardColumnKindEnum kind,
        int cardAmout

) {
}
