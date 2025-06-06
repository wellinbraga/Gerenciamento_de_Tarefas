package br.com.stackmob.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(final String reason, final Long cardId) throws SQLException {
        Timestamp timestamp = nonNull(OffsetDateTime.now()) ? Timestamp.valueOf(String.valueOf(OffsetDateTime.now().atZoneSameInstant(UTC).toLocalTime())) : null;
        // Assuming there's a method to execute the SQL query
        String sql = "INSERT INTO blocks (reason, blocked_at, card_id) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reason);
            pstmt.setTimestamp(2, timestamp);
            pstmt.setLong(3, cardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        Timestamp timestamp = nonNull(OffsetDateTime.now()) ? Timestamp.valueOf(String.valueOf(OffsetDateTime.now().atZoneSameInstant(UTC).toLocalTime())) : null;
        String sql = "UPDATE BLOCKS SET unblocked_at =? , unblock_reason = ? WHERE card_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reason);
            pstmt.setTimestamp(2, timestamp);
            pstmt.setLong(3, cardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
}



