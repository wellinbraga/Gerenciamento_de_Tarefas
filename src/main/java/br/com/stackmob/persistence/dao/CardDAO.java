package br.com.stackmob.persistence.dao;

import br.com.stackmob.dto.CardDetailsDTO;
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

    private final Connection connection;

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
                WHERE id = ?;
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
