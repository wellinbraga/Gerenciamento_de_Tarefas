package br.com.stackmob.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockEntity {
    private Long id;
    private OffsetDateTime blockeAt;
    private String blockReason;
    private OffsetDateTime unBlockeAt;
    private String unBlockReason;

}
