package com.se14.repository.db_impl;

import com.se14.repository.CommentRepository;
import com.se14.repository.IssueRepository;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import com.se14.repository.db_impl.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;


public class DBInitializer {

    public static DatabaseObjects initializeDatabase() {
        try {
            // Get connection from DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();

            // Initialize repository implementations with the connection

            CommentDB commentDB = new CommentDB(connection);
            UserDB userDB = new UserDB(connection);
            IssueDB issueDB = new IssueDB(connection,commentDB);
            ProjectDB projectDB = new ProjectDB(connection,userDB,commentDB);



            // Return the initialized database objects
            return new DatabaseObjects(userDB, projectDB, issueDB, commentDB);
        } catch (SQLException ex) {
            System.err.println("Failed to initialize database: " + ex.getMessage());
            return null;
        }
    }


    public static class DatabaseObjects {
        private final UserDB userDB;
        private final ProjectDB projectDB;
        private final IssueDB issueDB;
        private final CommentDB commentDB;

        public DatabaseObjects(UserDB userRepository, ProjectDB projectRepository,
                               IssueDB issueRepository, CommentDB commentRepository) {
            this.userDB = userRepository;
            this.projectDB = projectRepository;
            this.issueDB = issueRepository;
            this.commentDB = commentRepository;
        }

        public UserDB getUserDB() {
            return userDB;
        }

        public ProjectDB getProjectDB() {
            return projectDB;
        }

        public IssueDB getIssueDB() {
            return issueDB;
        }

        public CommentDB getCommentDB() {
            return commentDB;
        }
    }
}
