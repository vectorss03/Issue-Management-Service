package com.se14.repository.fake;

public class FakeRepositoryInitializer {

    public static void main(String[] args) {
        // Create instances of the fake repositories
        CommentRepositoryFake commentRepository = new CommentRepositoryFake();
        UserRepositoryFake userRepository = new UserRepositoryFake();
        ProjectRepositoryFake projectRepository = new ProjectRepositoryFake(userRepository);
        IssueRepositoryFake issueRepository = new IssueRepositoryFake(projectRepository,commentRepository);

        // Debugging output (Optional)
        System.out.println("Repositories have been initialized:");
        //System.out.println("User Repository Size: " + userRepository.findAll().size());
        System.out.println("Project Repository Size: " + projectRepository.findAll().size());
        System.out.println("Issue Repository Size: " + issueRepository.findByProject(projectRepository.findById(1).get()).size());
        //System.out.println("Comments for First Issue in Project 1: " + commentRepository.findByIssue(issueRepository.findByProject(projectRepository.findById(1L).get()).get(0)).size());

        // The output will help confirm that the repositories are populated correctly
        // Additional debugging can be done by inspecting objects in a debugger
    }
}
