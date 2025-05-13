package br.com.stackmob.persistence.dao;

import br.com.stackmob.persistence.entity.BoardColumnEntity;
import br.com.stackmob.persistence.entity.BoardColumnKingEnum;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardColumnDao {

    private final Connection connection;

    public BoardColumnEntity insert(BoardColumnEntity entity){
        var sqlInsertBoardColumns = "INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlInsertBoardColumns)) {
            statement.setString(1,entity.getName());
            statement.setInt(2, entity.getOrder());
            statement.setString(3, entity.getKind().name());
            statement.setLong(4, entity.getBoard().getId());
            statement.executeUpdate();
            if(statement instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  entity;
    }

    public List<BoardColumnEntity> findByBoardId(Long id) {
        List<BoardColumnEntity> entities = new ArrayList();
        String sqlSelectBoardColumns = "SELECT id, name, `order`, kind FROM BOARDS_COLUMNS WHERE id = ? ORDER BY `order`";
        try (PreparedStatement statement = connection.prepareStatement(sqlSelectBoardColumns)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet rsSelectBoardColumns = statement.getResultSet();
            if(rsSelectBoardColumns.next()){
                BoardColumnEntity entity = new BoardColumnEntity();
                entity.setId(rsSelectBoardColumns.getLong("id"));
                entity.setName(rsSelectBoardColumns.getString("name"));
                entity.setOrder(rsSelectBoardColumns.getInt("order"));
                entity.setKind(BoardColumnKingEnum.findByName(rsSelectBoardColumns.getString("kind")));

                entities.add(entity);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    public List<BoardColumnEntity> findByBoard(Long id) {
        List<BoardColumnEntity> entities = new ArrayList();
        String sqlSelectBoardColumns = """
        SELECT 
            bc.id, 
            bc.name, 
            bc.`order`, 
            bc.kind,
            COUNT(SELECT c.id
                  FROM CARDS c
                  WHERE c.board_column_id = bc.id) as cards_amount
        FROM BOARDS_COLUMNS bc
        WHERE id = ? 
        ORDER BY `order`
        """;
        try (PreparedStatement statement = connection.prepareStatement(sqlSelectBoardColumns)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet rsSelectBoardColumns = statement.getResultSet();
            if(rsSelectBoardColumns.next()){
                BoardColumnEntity entity = new BoardColumnEntity();
                entity.setId(rsSelectBoardColumns.getLong("id"));
                entity.setName(rsSelectBoardColumns.getString("name"));
                entity.setOrder(rsSelectBoardColumns.getInt("order"));
                entity.setKind(BoardColumnKingEnum.findByName(rsSelectBoardColumns.getString("kind")));

                entities.add(entity);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
}
