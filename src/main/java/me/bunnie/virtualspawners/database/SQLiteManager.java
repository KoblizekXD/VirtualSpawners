/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Object
 *  java.lang.String
 *  java.sql.Connection
 *  java.sql.DriverManager
 *  java.sql.SQLException
 *  java.sql.Statement
 */
package me.bunnie.virtualspawners.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.bunnie.virtualspawners.VirtualSpawners;

public class SQLiteManager {
    private final VirtualSpawners plugin;
    private Connection connection;
    private File file;

    public SQLiteManager(VirtualSpawners plugin) {
        this.plugin = plugin;
        this.createTables();
        this.connect();
    }

    private void connect() {
        try {
            this.connection = DriverManager.getConnection((String)("jdbc:sqlite:" + this.plugin.getDataFolder().getAbsolutePath() + "/data.db"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createTables() {
        this.file = new File(this.plugin.getDataFolder().getAbsolutePath(), "data.db");
        if (!this.file.exists()) {
            try {
                this.connection = DriverManager.getConnection((String)("jdbc:sqlite:" + this.plugin.getDataFolder().getAbsolutePath() + "/data.db"));
                Statement statement = this.connection.createStatement();
                statement.executeUpdate("CREATE TABLE profiles(UUID VARCHAR, SPAWNERS TEXT, SPAWNER_UPGRADES TEXT, BANK_UPGRADES TEXT, BANK_MEMBERS TEXT, PRIMARY KEY(UUID))");
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}