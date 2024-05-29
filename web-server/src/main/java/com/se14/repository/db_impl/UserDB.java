package com.se14.repository.db_impl;

import com.se14.domain.User;
import com.se14.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDB implements UserRepository {
    private Connection connection;

    // Constructor to initialize the database connection
    public UserDB(Connection connection) {
        this.connection = connection;
    }


    @Override
    public User save(User user) {
        // username 이미 있는지 확인
        User existingUser_ = findByUsername(user.getUsername());
        if (existingUser_ != null) {
            //username이 이미 있으면 exception.
            throw new IllegalArgumentException("Username already exists");
        }
        if (user.getUserId() == 0) {
            user.setUserId(generateUniqueUserId());
        }

        Optional<User> existingUser = findById(user.getUserId());
        if (existingUser.isPresent()) {
            return update(user);
        } else {
            //새로운 유저 DB에 insert.
            String sql = "INSERT INTO users (user_id, username, password, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, user.getUserId());
                statement.setString(2, user.getUsername());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getEmail());
                statement.executeUpdate();

                return user;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }


    private User update(User user) {
        String sql = "UPDATE users SET password = ?, email = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUsername());
            statement.executeUpdate();
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<User> findById(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }
    private int generateUniqueUserId() {
        String sql = "SELECT MAX(user_id) AS max_id FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(
                (int) resultSet.getLong("user_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email")
        );
    }
}
