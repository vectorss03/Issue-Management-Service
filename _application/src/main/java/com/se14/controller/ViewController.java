package com.se14.controller;

import com.se14.controller.modal.AssignDeveloperController;
import com.se14.controller.panel.AnalysisController;
import com.se14.view.modal.AddUserModal;
import com.se14.controller.modal.CreateProjectController;
import com.se14.controller.modal.ReportIssueController;
import com.se14.controller.panel.IssueController;
import com.se14.controller.panel.IssueDetailController;
import com.se14.controller.panel.ProjectController;
import com.se14.domain.MainViewPanel;
import com.se14.domain.UserSession;
import com.se14.controller.modal.AddUserController;
import com.se14.view.modal.AssignDeveloperModal;
import com.se14.view.modal.CreateProjectModal;
import com.se14.view.LoginView;
import com.se14.view.MainView;
import com.se14.view.SignInView;
import com.se14.view.modal.ReportIssueModal;
import com.se14.view.panel.AnalysisPanel;
import com.se14.view.panel.IssueDetailPanel;
import com.se14.view.panel.IssuePanel;
import com.se14.view.panel.ProjectPanel;
import lombok.Getter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

public class ViewController {
    private final MainController mainController;

    private final ProjectController projectController;
    private final IssueController issueController;
    private final IssueDetailController issueDetailController;
    private final AnalysisController analysisController;

    private final LoginController loginController;
    private final SignInController signInController;

    private final CreateProjectController createProjectController;
    private final ReportIssueController reportIssueController;
    private final AddUserController addUserController;
    private final AssignDeveloperController assignDeveloperController;

    private MainViewPanel currentPanel;
    public void setCurrentPanel(MainViewPanel currentPanel) {
        this.currentPanel = currentPanel;
        refreshMainView();
    }

    @Getter
    private final UserSession session;
    @Getter
    private final HttpClient client;

    public ViewController() {
        session = new UserSession();
        client = HttpClientBuilder.create().build();

        projectController = new ProjectController(this);
        ProjectPanel projectPanel = new ProjectPanel(projectController);
        projectController.setView(projectPanel);

        issueController = new IssueController(this);
        IssuePanel issuePanel = new IssuePanel(issueController);
        issueController.setView(issuePanel);

        issueDetailController = new IssueDetailController(this);
        IssueDetailPanel issueDetailPanel = new IssueDetailPanel(issueDetailController);
        issueDetailController.setView(issueDetailPanel);

        analysisController = new AnalysisController(this);
        AnalysisPanel analysisPanel = new AnalysisPanel(analysisController);
        analysisController.setView(analysisPanel);

        mainController = new MainController(this);
        mainController.setView(new MainView(mainController, projectPanel, issuePanel, issueDetailPanel, analysisPanel));

        currentPanel = MainViewPanel.PROJECT;

        loginController = new LoginController(this);
        loginController.setView(new LoginView(loginController));

        signInController = new SignInController(this);
        signInController.setView(new SignInView(signInController));

        createProjectController = new CreateProjectController(this);
        createProjectController.setView(new CreateProjectModal(createProjectController));

        reportIssueController = new ReportIssueController(this);
        reportIssueController.setView(new ReportIssueModal(reportIssueController));

        addUserController = new AddUserController(this);
        addUserController.setView(new AddUserModal(addUserController));

        assignDeveloperController = new AssignDeveloperController(this);
        assignDeveloperController.setView(new AssignDeveloperModal(assignDeveloperController));
    }

    public void run() {
        showMainView();
    }

    public void showMainView() {
        mainController.showView();
        refreshMainView();
    }

    public void refreshMainView() {
        switch (currentPanel) {
            case PROJECT:
                mainController.showProjectPanel();
                projectController.displayUserProjects();
                break;
            case ISSUE:
                mainController.showIssuePanel();
                issueController.displayProjectIssues();
                break;
            case ISSUE_DETAIL:
                mainController.showIssueDetailPanel();
                issueDetailController.displayDetailedIssue();
                break;
            case ANALYSIS:
                mainController.showAnalysisPanel();
                analysisController.displayCharts();
                break;
        }
    }

    public void showLoginView() {
        loginController.showView();
    }

    public void showSignInView() {
        signInController.showView();
    }

    public void showCreateProjectModal() {
        createProjectController.showView();
    }

    public void showReportIssueModal() {
        reportIssueController.showView();
    }

    public void showAddUserModal() {
        addUserController.showView();
    }

    public void showAssignDeveloperModal() {
        assignDeveloperController.showView();
    }
}
