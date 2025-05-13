package br.com.stackmob.service;

import br.com.stackmob.persistence.dao.BoardColumnDao;
import br.com.stackmob.persistence.dao.BoardDAO;
import br.com.stackmob.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService extends  BoardEntity{

    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        BoardDAO dao = new BoardDAO(connection);
        BoardColumnDao boardColumnDao = new BoardColumnDao(connection);
        Optional<BoardEntity> optional = dao.findById(id);
        if(optional.isPresent()){
            BoardEntity entity = optional.get();
            entity.setBoardColumns(boardColumnDao.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return  Optional.empty();

    }

    public void showBoardDetails (final Long id) throws SQLException {
        BoardDAO dao = new BoardDAO(connection);
        BoardColumnDao boardColumnDao = new BoardColumnDao(connection);
        Optional<BoardEntity> optional = dao.findById(id);
        if(optional.isPresent()){
            BoardEntity entity = optional.get();
            entity.setBoardColumns(boardColumnDao.findByBoard(entity.getId()));
        //    return Optional.of(entity);
        }
   //    return  Optional.empty();

    }




}
