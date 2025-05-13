package br.com.stackmob.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {
    private Long id;
    private String name;
    private int order;
    private BoardColumnKingEnum kind;
    private BoardEntity board = new BoardEntity();
}
