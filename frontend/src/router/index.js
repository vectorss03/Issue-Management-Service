import {createRouter, createWebHistory} from "vue-router";
import ProjectList from "@/views/ProjectList.vue";
import ProjectBoard from "@/views/Board/ProjectBoard.vue";
import ProjectHome from "@/views/Board/ProjectHome.vue";
import ProjectIssueList from "@/views/Board/ProjectIssueList.vue";
import ProjectAnalysis from "@/views/Board/ProjectAnalysis.vue";
import ProjectUserList from "@/views/Board/ProjectUserList.vue";
import IssueDeatil from "@/views/Board/Issue/IssueDeatil.vue";

const routes = [
    {
        path: '/',
        redirect: '/projects'
    },
    {
        path: '/projects',
        component: ProjectList
    },
    {
        path: '/projects/:project_id',
        name: 'project',
        component: ProjectBoard,
        redirect: { name: 'issues' },
        children: [
            {
                path: 'home',
                name: 'home',
                component: ProjectHome
            },
            {
                path: 'issues',
                name: 'issues',
                component: ProjectIssueList
            },
            {
                path: 'issues/:issue_id',
                name: 'issue-detail',
                component: IssueDeatil
            },
            {
                path: 'analysis',
                name: 'analysis',
                component: ProjectAnalysis
            },
            {
                path: 'users',
                name: 'users',
                component: ProjectUserList
            }
        ]
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: routes
})

export default router