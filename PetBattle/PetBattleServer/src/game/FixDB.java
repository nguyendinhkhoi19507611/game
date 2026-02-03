package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FixDB {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to database pet_battle
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pet_battle?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf-8",
                    "root", "");
            Statement stmt = conn.createStatement();

            // Alter nickname column length to 64
            String sql = "ALTER TABLE user_info MODIFY nickname VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ''";
            System.out.println("Executing: " + sql);
            stmt.executeUpdate(sql);

            System.out.println("Successfully altered table user_info nickname column to VARCHAR(64)");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
