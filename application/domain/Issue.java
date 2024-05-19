package application.domain;

import java.util.Date;
import java.util.List;

public class Issue {
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private Date reportedDate;
    private User reporter;
    private User fixer;
    private User assignee;
    private List<Comment> comments;

    public static void main(String[] args) {
        System.out.println(new Date());
    }
    // Default constructor
    public Issue() {
    }
    public Issue( User reporter, String title, String description, IssuePriority priority) { // 수정해도 좋을 것 같아요. report Issue 위해서 만들었습니다.
        this.reporter = reporter;
        this.title = title;
        this.description = description;
        this.priority= priority;
        this.status = IssueStatus.NEW;
        this.reportedDate = new Date();
        this.comments = null;
    }

    // Parameterized constructor
    public Issue(String title, String description, IssueStatus status, IssuePriority priority, Date reportedDate, User reporter, User fixer, User assignee, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.reportedDate = reportedDate;
        this.reporter = reporter;
        this.fixer = fixer;
        this.assignee = assignee;
        this.comments = comments;
    }

    // Getter and setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and setter for status
    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    // Getter and setter for priority
    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    // Getter and setter for reportedDate
    public Date getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(Date reportedDate) {
        this.reportedDate = reportedDate;
    }

    // Getter and setter for reporter
    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    // Getter and setter for fixer
    public User getFixer() {
        return fixer;
    }

    public void setFixer(User fixer) {
        this.fixer = fixer;
    }

    // Getter and setter for assignee
    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    // Getter and setter for comments
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
