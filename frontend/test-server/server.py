from project import projects
from flask import Flask
from flask import request, jsonify
from http import HTTPStatus
import datetime

app = Flask(__name__)

@app.route('/api/projects')
def project_list():
    return projects

@app.route('/api/projects', methods=['POST'])
def create_project():
    params = request.get_json()
    new_id = (projects[-1]["project_id"] if projects else 0) + 1
    projects.append({
        "project_id": new_id,
        "title": params["title"],
        "description": params["description"],
        "issues": [],
        })
    return jsonify({"status": HTTPStatus.OK})

@app.route('/api/projects/<int:project_id>/issues')
def issue_list(project_id):
    return list(filter(lambda project: project["project_id"] == project_id, projects))[0]["issues"]

@app.route('/api/projects/<int:project_id>/issues', methods=['POST'])
def create_issue(project_id):
    params = request.get_json()
    project = list(filter(lambda project: project["project_id"] == project_id, projects))[0]
    new_id = (project["issues"][-1]["issue_id"] if project["issues"] else 0) + 1
    project["issues"].append({
        "issue_id": new_id,
        "title": params["title"],
        "description": params["description"],
        "status": "new",
        "priority": params["priority"],
        "assignee": None,
        "fixer": None,
        "reporter": "hysk",
        "reported_date": datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
        "comments": []
        })
    return jsonify({"status": HTTPStatus.OK})



@app.route('/api/projects/<int:project_id>/issues/<int:issue_id>')
def issue(project_id, issue_id):
    return list(filter(lambda issue: issue["issue_id"] == issue_id, list(filter(lambda project: project["project_id"] == project_id, projects))[0]["issues"]))[0]


@app.route('/api/projects/<int:project_id>/issues/<int:issue_id>/comments', methods=['POST'])
def add_comment(project_id, issue_id):
    params = request.get_json()
    issue = list(filter(lambda issue: issue["issue_id"] == issue_id, list(filter(lambda project: project["project_id"] == project_id, projects))[0]["issues"]))[0]
    new_id = (issue["comments"][-1]["comment_id"] if issue["comments"] else 0) + 1
    issue["comments"].append({
        "comment_id": new_id,
        "content": params["content"],
        "author": "hysk",
        "timestamp": datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    })
    return jsonify({"status": HTTPStatus.OK})



if __name__ == '__main__':
    app.run("localhost", 8080, debug=True)