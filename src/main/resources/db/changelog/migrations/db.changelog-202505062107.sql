--liquibase formatted sql
--changeset wellington:202505072253
--comment: set unblock_reason to not null

ALTER TABLE BLOCKS MODIFY unblock_reason VARCHAR(255) NULL;

-- rollback ALTER TABLE BLOCKS MODIFY unblock_reason VARCHAR(255) NOT NULL;