package br.com.stackmob.persistence.dao;

import br.com.stackmob.dto.CardDetailsDTO;
import br.com.stackmob.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardEntity insert(CardEntity entity) throws SQLException{
        String sql = "INSERTO INTO CARDS (title, description, board_column) VALUES (?,?,?);";
        try(PreparedStatement startement = connection.prepareStatement(sql)){
            startement.setString(1, entity.getTitle());
            startement.setString(2, entity.getDescription());
            startement.setLong(3, entity.getBoardColumn().getId());
        }

        return  entity;
    }

    public void moveToColumn(final Long cardId, final Long columnId) throws SQLException {
        String sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, columnId);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }


    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        String sql =
                """
                SELECT
                    c.id,
                    c.title,
                    b.description
                    b.blocked_at,
                    b.block_reason,
                    c.board_column_id,
                    bc.name,
                    (SELECT COUNT(sub_b.id) FROM BLOCK sub_b WHERE sub_b.id = c.id) block_amount
                FROM CARDS c
                LEFT JOIN BLOCKS b ON c.id = b.card_id AND b.unblocked IS NULL
                INNER JOIN BOARDS_COLUMNS bc ON bc.id = b.board_columns_id
                WHERE c.id = ?;
                """;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if(rs.next()){
                CardDetailsDTO dto = new CardDetailsDTO(
                        rs.getLong("c.id"),
                        rs.getString("c.title"),
                        rs.getString("c.description"),
                        nonNull(rs.getString("b.block_reason")),
                        OffsetDateTime.ofInstant(rs.getTimestamp("b.blocked_at").toInstant(), UTC),
                        rs.getString("b.block_reason"),
                        rs.getInt("block_amount"),
                        rs.getLong(" c.board_column_id"),
                        rs.getString("bc.name")
                );
            }
        }
        return  null;
    }
}
