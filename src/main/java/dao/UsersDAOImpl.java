package dao;

import helper.JDBC;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * class to implement the required methods for users
 */
public class UsersDAOImpl implements UsersDAO {

    /**
     * connects to the database to get a user with a matching user id
     *
     * @param id the user id to search for in the database
     * @return the user with the matching user id in the database
     * @throws SQLException
     */
    @Override
    public Users get(int id) throws SQLException {
        Users user = null;
        String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int userID = rs.getInt(1);
            String userName = rs.getString(2);
            String password = rs.getString(3);

            user = new Users(userID, userName, password);
        }
        return user;
    }

    /**
     * connects to the database to get all users
     *
     * @return all users found in the database
     * @throws SQLException
     */
    @Override
    public List<Users> getALL() throws SQLException {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");

            Users user = new Users(userID, userName, password);

            users.add(user);
        }
        return users;
    }

    /**
     * connects to the database to insert a new user
     *
     * @param user the user to be inserted into the database
     * @return a certain int depending on if a user was inserted into the database or not
     * @throws SQLException
     */
    @Override
    public int insert(Users user) throws SQLException {
        String sql = "INSERT INTO users(User_ID, User_Name, Password, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, user.getUserID());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getPassword());
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(5, "script");
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, "script");

        int result = ps.executeUpdate();

        return result;
    }

    /**
     * connects to the database to update an existing user
     *
     * @param user the user to update in the database
     * @return a certain int depending on if a user was updated or not
     * @throws SQLException
     */
    @Override
    public int update(Users user) throws SQLException {
        String sql = "UPDATE users SET User_ID = ?, User_Name = ?, Password = ?, Create_Date = ?, Created_By = ?," +
                "Last_Update = ?, Last_Updated_By = ? WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, user.getUserID());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getPassword());
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(5, "script");
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, "script");
        ps.setInt(8, user.getUserID());

        int result = ps.executeUpdate();
        return result;
    }

    /**
     * connects to the database to delete an existing user
     *
     * @param user the user to delete in the database
     * @return a certain int depending on if a user was deleted or not
     * @throws SQLException
     */
    @Override
    public int delete(Users user) throws SQLException {
        String sql = "DELETE FROM users WHERE User_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, user.getUserID());

        int result = ps.executeUpdate();

        return result;
    }
}
