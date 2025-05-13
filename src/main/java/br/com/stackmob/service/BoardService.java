package br.com.stackmob.service;

import br.com.stackmob.persistence.dao.BoardColumnDao;
import br.com.stackmob.persistence.dao.BoardDAO;
import br.com.stackmob.persistence.entity.BoardColumnEntity;
import br.com.stackmob.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
public class BoardService {

    private Connection connection;
    BoardDAO dao = null;

    public BoardService (Connection connection){
        this.connection = connection;
        dao = new BoardDAO(connection);
    }

    public BoardEntity insert(BoardEntity entity) throws SQLException{
        BoardColumnDao boardColumnDAO = new BoardColumnDao(connection);
        try{
          dao.insert(entity);
            List<BoardColumnEntity> columns = entity.getBoardColumns().stream().map(c -> {
              c.setBoard(entity);
              return  c;
          }).toList();
          for(BoardColumnEntity column : columns ){
              boardColumnDAO.insert(column);
          }

          connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw  e;
        }

        return  entity;

    }

    public boolean delete(final Long id) throws SQLException{
        try{
            if (!dao.exists(id)){
                return false;
            }
            dao.delete(id);
            connection.commit();
            return  true;

        } catch (SQLException e) {
            connection.rollback();
            throw  e;
        }
        
    }
}
