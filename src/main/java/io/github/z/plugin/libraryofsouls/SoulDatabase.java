package io.github.z.plugin.libraryofsouls;

import io.github.z.plugin.Plugin;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoulDatabase {

    private static final String DB_FILE = "library_of_souls.db";
    private static final String TABLE = "souls";

    private Connection mConnection;

    public SoulDatabase() {
        File dataFolder = Plugin.getPlugin().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        String url = "jdbc:sqlite:" + new File(dataFolder, DB_FILE).getAbsolutePath();
        try {
            mConnection = DriverManager.getConnection(url);
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to Library of Souls database", e);
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                "id TEXT PRIMARY KEY, " +
                "entity_type TEXT NOT NULL, " +
                "max_health REAL NOT NULL, " +
                "attack_damage REAL NOT NULL, " +
                "movement_speed REAL NOT NULL, " +
                "mob_spells TEXT NOT NULL" +
                ")";
        try (Statement stmt = mConnection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create souls table", e);
        }
    }

    public void insertSoul(Soul soul) {
        String sql = "INSERT INTO " + TABLE + " (id, entity_type, max_health, attack_damage, movement_speed, mob_spells) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = mConnection.prepareStatement(sql)) {
            stmt.setString(1, soul.getId());
            stmt.setString(2, soul.getType().name());
            stmt.setDouble(3, soul.getMaxHealth());
            stmt.setDouble(4, soul.getAttackDamage());
            stmt.setDouble(5, soul.getMovementSpeed());
            stmt.setString(6, serializeMobSpells(soul.getMobSpells()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert soul: " + soul.getId(), e);
        }
    }

    public Soul getSoul(String id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        try (PreparedStatement stmt = mConnection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rowToSoul(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get soul: " + id, e);
        }
        return null;
    }

    public List<Soul> getAllSouls() {
        String sql = "SELECT * FROM " + TABLE;
        List<Soul> souls = new ArrayList<>();
        try (Statement stmt = mConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                souls.add(rowToSoul(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all souls", e);
        }
        return souls;
    }

    public void updateSoul(Soul soul) {
        String sql = "UPDATE " + TABLE + " SET entity_type = ?, max_health = ?, attack_damage = ?, movement_speed = ?, mob_spells = ? WHERE id = ?";
        try (PreparedStatement stmt = mConnection.prepareStatement(sql)) {
            stmt.setString(1, soul.getType().name());
            stmt.setDouble(2, soul.getMaxHealth());
            stmt.setDouble(3, soul.getAttackDamage());
            stmt.setDouble(4, soul.getMovementSpeed());
            stmt.setString(5, serializeMobSpells(soul.getMobSpells()));
            stmt.setString(6, soul.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update soul: " + soul.getId(), e);
        }
    }

    public void deleteSoul(String id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        try (PreparedStatement stmt = mConnection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete soul: " + id, e);
        }
    }

    public List<String> getAllSoulIds() {
        String sql = "SELECT id FROM " + TABLE;
        List<String> ids = new ArrayList<>();
        try (Statement stmt = mConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all soul IDs", e);
        }
        return ids;
    }

    public boolean idExists(String id) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE id = ?";
        try (PreparedStatement stmt = mConnection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check soul ID: " + id, e);
        }
    }

    public void close() {
        try {
            if (mConnection != null && !mConnection.isClosed()) {
                mConnection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection", e);
        }
    }

    private Soul rowToSoul(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        EntityType type = EntityType.valueOf(rs.getString("entity_type"));
        double maxHealth = rs.getDouble("max_health");
        double attackDamage = rs.getDouble("attack_damage");
        double movementSpeed = rs.getDouble("movement_speed");
        Map<String, Integer> mobSpells = deserializeMobSpells(rs.getString("mob_spells"));
        return new Soul(id, type, maxHealth, attackDamage, movementSpeed, mobSpells);
    }

    // Serializes as: spellName1=level1;spellName2=level2;...
    // Empty map serializes as empty string.
    private String serializeMobSpells(Map<String, Integer> mobSpells) {
        if (mobSpells.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : mobSpells.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        return sb.toString();
    }

    private Map<String, Integer> deserializeMobSpells(String raw) {
        Map<String, Integer> map = new HashMap<>();
        if (raw == null || raw.isEmpty()) return map;
        for (String entry : raw.split(";")) {
            String[] parts = entry.split("=", 2);
            if (parts.length == 2) {
                map.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        return map;
    }
}
