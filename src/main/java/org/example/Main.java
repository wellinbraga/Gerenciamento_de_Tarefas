package org.example;

import br.com.stackmob.persistence.migration.MigrationStrategy;
import br.com.stackmob.ui.MainMenu;

import java.sql.SQLException;

import static br.com.stackmob.persistence.config.ConnectionConfig.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {

        try(var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        }

      new MainMenu().execute();
    }
}