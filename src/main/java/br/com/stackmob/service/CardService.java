package br.com.stackmob.service;

import br.com.stackmob.dto.BoardColumnInfoDTO;
import br.com.stackmob.dto.CardDetailsDTO;
import br.com.stackmob.exception.CardBlockedException;
import br.com.stackmob.exception.CardFinishedException;
import br.com.stackmob.exception.EntityNotFoudException;
import br.com.stackmob.persistence.dao.BlockDAO;
import br.com.stackmob.persistence.dao.CardDAO;
import br.com.stackmob.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static br.com.stackmob.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.stackmob.persistence.entity.BoardColumnKindEnum.FINAL;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        try {
            CardDAO dao = new CardDAO(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException ex){
            connection.rollback();
            throw  ex;
        }
    }

    public void moveToNextColumn(final Long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try {

            CardDAO dao = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = dao.findById(cardId);
            CardDetailsDTO dto = optional.orElseThrow(() -> new EntityNotFoudException("O Card de id " + cardId + " não foi encontrado"));
            if(dto.blocked()){
                String message = "O Card de id " + cardId + " está bloqueado, não é possível mover para outra coluna";
                throw new CardBlockedException(message);
            }

           BoardColumnInfoDTO currentColumn = boardColumnsInfo.stream()
                    .filter(c -> c.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoudException("A coluna de id " + dto.columnId() + " não foi encontrada"));
            if(currentColumn.kind().equals(FINAL)){
                String message = "O Card de id " + cardId + " já está na coluna final, não é possível mover para outra coluna";
                throw new CardFinishedException(message);
            }

           BoardColumnInfoDTO nextColumn =  boardColumnsInfo.stream()
                    .filter(c -> c.order() == currentColumn.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoudException("Não existe uma coluna seguinte para o card de id " + cardId));

            dao.moveToColumn(cardId, nextColumn.id());
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void cancel(final Long cardId, final long cancelColumnId, final List<BoardColumnInfoDTO> boadColumnsInfo) throws SQLException {
        try {
            CardDAO dao = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = dao.findById(cardId);
            CardDetailsDTO dto = optional.orElseThrow(() -> new EntityNotFoudException("O Card de id " + cardId + " não foi encontrado"));
            if(dto.blocked()){
                String message = "O Card de id " + cardId + " está bloqueado, não é possível cancelar";
                throw new CardBlockedException(message);
            }

            BoardColumnInfoDTO currentColumn = boadColumnsInfo.stream()
                    .filter(c -> c.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoudException("A coluna de id " + dto.columnId() + " não foi encontrada"));
            if(currentColumn.kind().equals(FINAL)){
                String message = "O Card de id " + cardId + " já está na coluna final, não é possível cancelar";
                throw new CardFinishedException(message);
            }
            boadColumnsInfo.stream()
                    .filter(c -> c.order() == currentColumn.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoudException("A coluna de cancelamento de id " + cancelColumnId + " não foi encontrada"));
            dao.moveToColumn(cardId, cancelColumnId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void block(final Long cardId, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try {
            CardDAO dao = new CardDAO(connection);
            BlockDAO blockDAO = new BlockDAO(connection);
            Optional<CardDetailsDTO> optional = dao.findById(cardId);
            CardDetailsDTO dto = optional.orElseThrow(() -> new EntityNotFoudException("O Card de id " + cardId + " não foi encontrado"));
            if(dto.blocked()){
                String message = "O Card de id " + cardId + " já está bloqueado, não é possível bloquear novamente";
                throw new CardBlockedException(message);
            }
            BoardColumnInfoDTO currentColumn = boardColumnsInfo.stream()
                    .filter(c -> c.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoudException("A coluna de id " + dto.columnId() + " não foi encontrada"));
            if(currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCEL)){
                String message = "O Card de id " + cardId + " está na coluna final ou de cancelamento, não é possível bloquear";
                throw new CardFinishedException(message);
            }
            blockDAO.block(reason, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void unblock(final Long cardId, final String reason) throws SQLException {
        try {
            CardDAO dao = new CardDAO(connection);
            BlockDAO blockDAO = new BlockDAO(connection);
            Optional<CardDetailsDTO> optional = dao.findById(cardId);
            CardDetailsDTO dto = optional.orElseThrow(() -> new EntityNotFoudException("O Card de id " + cardId + " não foi encontrado"));
            if(!dto.blocked()){
                String message = "O Card de id " + cardId + " não está bloqueado, não é possível desbloquear";
                throw new CardBlockedException(message);
            }
            blockDAO.unblock(reason, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

}
