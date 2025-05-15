package br.com.stackmob.service;

import br.com.stackmob.persistence.dao.BoardColumnDao;
import br.com.stackmob.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {

        BoardColumnDao dao = new BoardColumnDao(connection);
        return dao.findById(id);
    }
}
