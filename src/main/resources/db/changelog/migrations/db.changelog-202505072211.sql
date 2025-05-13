--liquibase formatted sql
--changeset wellington:202505072253
--comment: boards table create

CREATE TABLE BOARDS(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- rollback DROP TABLE BOARDS