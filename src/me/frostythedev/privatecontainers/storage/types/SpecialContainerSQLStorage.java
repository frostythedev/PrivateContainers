package me.frostythedev.privatecontainers.storage.types;

import me.frostythedev.privatecontainers.PCPlugin;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.storage.DataStorage;
import me.frostythedev.privatecontainers.storage.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SpecialContainerSQLStorage implements DataStorage<SpecialContainer> {

    private PCPlugin plugin;
    private MySQL mySQL;

    public SpecialContainerSQLStorage(PCPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isConnected() {
        return this.mySQL.hasConnection();
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    @Override
    public void setup() {
        this.mySQL = new MySQL(plugin);
        try {
            if (!this.mySQL.openConnection()) {
                plugin.setSpecialContainerStorage(new SpecialContainerFileStorage(plugin));
                plugin.getLogger().info("An error occurred while trying to connect to MySQL. " +
                        "Initialized using default data storage type: YML");
            } else {
                this.mySQL.syncUpdate("CREATE TABLE IF NOT EXISTS pc_special_table(id INT PRIMARY KEY AUTO_INCREMENT, randomId VARCHAR(36), data VARCHAR(9999));");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SpecialContainer load(String name) {
        if (isConnected()) {
            ResultSet rs = this.mySQL.syncQuery("SELECT * FROM `pc_special_table` WHERE randomId='" + name + "'");
            if (rs != null) {
                try {
                    if (rs.next()) {
                        String data = rs.getString("data");
                        return plugin.getGson().fromJson(data, SpecialContainer.class);
                    }
                    return null;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void save(SpecialContainer container) {
        if (isConnected()) {
            if (load(container.getRandomId().toString()) != null) {
                String query = "UPDATE `pc_special_table` SET data=? WHERE randomId=?";
                PreparedStatement stmt = null;
                try {
                    stmt = this.mySQL.getConnection().prepareStatement(query);
                    stmt.setString(1, plugin.getGson().toJson(container, SpecialContainer.class));
                    stmt.setString(2, container.getRandomId().toString());
                    stmt.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                String query = "INSERT INTO pc_special_table(randomId,data) VALUES (?,?);";
                PreparedStatement stmt = null;
                try {
                    stmt = this.mySQL.getConnection().prepareStatement(query);
                    stmt.setString(1, container.getRandomId().toString());
                    stmt.setString(2, plugin.getGson().toJson(container, SpecialContainer.class));
                    stmt.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean remove(SpecialContainer container) {
        if (isConnected()) {
            String query = "DELETE FROM `pc_special_table` WHERE randomId='" + container.getRandomId().toString() + "';";
            if(this.mySQL.syncUpdate(query)){
                return true;
            }
        }
        return false;
    }
}
