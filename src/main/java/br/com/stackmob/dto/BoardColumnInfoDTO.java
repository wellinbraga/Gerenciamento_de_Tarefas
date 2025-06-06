package br.com.stackmob.dto;

import br.com.stackmob.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind ){

}
