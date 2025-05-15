package br.com.stackmob.service;

import br.com.stackmob.dto.BoardColumnDTO;
import br.com.stackmob.dto.BoardDetailsDTO;
import br.com.stackmob.persistence.dao.BoardColumnDao;
import br.com.stackmob.persistence.dao.BoardDAO;
import br.com.stackmob.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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

    public Optional<BoardDetailsDTO> showBoardDetails (final Long id) throws SQLException {
        BoardDAO dao = new BoardDAO(connection);
        BoardColumnDao boardColumnDao = new BoardColumnDao(connection);
        Optional<BoardEntity> optional = dao.findById(id);
        if(optional.isPresent()){
            BoardEntity entity = optional.get();
            List<BoardColumnDTO> colunms = boardColumnDao.findByBoard(entity.getId());
            BoardDetailsDTO dto = new BoardDetailsDTO(entity.getId(), entity.getName(),colunms);
            return Optional.of(dto);
        }
        return Optional.empty();
    }




}
