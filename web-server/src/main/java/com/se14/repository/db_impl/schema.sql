CREATE DATABASE se14;

USE se14;

CREATE TABLE users (
                       user_id INT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL
);

CREATE TABLE projects (
                          project_id VARCHAR(255) PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT NOT NULL
);

CREATE TABLE project_members (
                                 project_id VARCHAR(255),
                                 user_id INT,
                                 PRIMARY KEY (project_id, user_id),
                                 FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
                                 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE project_member_roles (
                                      project_id VARCHAR(255),
                                      user_id INT,
                                      user_role VARCHAR(255),
                                      PRIMARY KEY (project_id, user_id, user_role),
                                      FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
                                      FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE issues (
                        issue_id INT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        status ENUM('ASSIGNED', 'FIXED', 'RESOLVE', 'CLOSED', 'REOPENED') NOT NULL,
                        priority ENUM('BLOCKER', 'CRITICAL', 'MAJOR', 'MINOR', 'TRIVIAL') NOT NULL,
                        date DATE NOT NULL,
                        project_id VARCHAR(255),
                        reporter_id INT,
                        fixer_id INT,
                        assignee_id INT,
                        FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
                        FOREIGN KEY (reporter_id) REFERENCES users(user_id),
                        FOREIGN KEY (fixer_id) REFERENCES users(user_id),
                        FOREIGN KEY (assignee_id) REFERENCES users(user_id)
);

CREATE TABLE comments (
                          comment_id INT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          text TEXT NOT NULL,
                          timestamp DATETIME NOT NULL,
                          issue_id INT,
                          author_id INT,
                          FOREIGN KEY (issue_id) REFERENCES issues(issue_id) ON DELETE CASCADE,
                          FOREIGN KEY (author_id) REFERENCES users(user_id)
);
