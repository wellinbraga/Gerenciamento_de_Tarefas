package br.com.stackmob.persistence.entity;

import java.util.stream.Stream;

public enum BoardColumnKingEnum {
    INITIAL, FINAL, CANCEL, PENDING;

    public static BoardColumnKingEnum findByName(final String name){
        return Stream.of(BoardColumnKingEnum.values())
                .filter(b -> b.name().equals(name))
                .findFirst().orElseThrow();
    }
}
