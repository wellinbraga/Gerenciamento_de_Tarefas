package br.com.stackmob.persistence.config;

import lombok.NoArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        //String url = "jdbc:mysql://localhost/board";
        String url = "jdbc:mysql://127.0.0.1:3306/board?allowPublicKeyRetrieval=true&useSSL=false";
        Connection connection = DriverManager.getConnection(url, "board","board");
        connection.setAutoCommit(false);
        return connection;
    }
}
