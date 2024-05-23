<template>
  <body class="min-h-screen">

  <section>
    <div class="flex">
      <div class="mr-10 w-2/3">
        <div>
          <h1 class="font-bold text-3xl m-3 mb-10">{{ issue.title }}</h1>
          <h2 class="font-bold text-m m-3">Description</h2>
          <div v-text="issue.description" style="white-space: pre-line; word-break: break-word"
               class="text-sm font-normal text-gray-700 mx-3 mb-10 min-h-40"/>
        </div>


        <div>
          <h2 class="m-3 font-semibold text-m">Comments</h2>
          <form v-on:submit.prevent="postComment" class="m-3 mb-5 border border-gray-200 rounded-lg bg-gray-50">
            <div class="px-4 py-2 bg-white rounded-t-lg">
              <label for="comment" class="sr-only">Your comment</label>
              <textarea id="comment" rows="4" v-model="commentForm.content"
                        class="w-full px-0 text-sm text-gray-900 bg-white border-0 focus:ring-0 resize-none"
                        placeholder="Write a comment..." required></textarea>
            </div>
            <div class="flex items-center justify-between px-3 py-2 border-t">
              <button type="Submit"
                      class="inline-flex items-center py-2.5 px-4 text-xs font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800">
                Post
              </button>
            </div>
          </form>

          <ol class="relative m-3">
            <li class="p-4 mb-1" v-for="(comment, index) in issue.comments" :key="index">
              <div class="items-center mb-3 sm:flex">
                <time class="mx-5 mb-1 text-xs font-normal text-gray-400 sm:order-last sm:mb-0">{{ comment.timestamp }}
                </time>
                <p class="font-semibold text-gray-900">{{ comment.author }}</p>
              </div>
              <div v-text="comment.content" style="white-space: pre-line; word-break: break-word"
                   class="text-sm font-normal text-gray-700"/>
            </li>
          </ol>
        </div>
      </div>


      <div class="w-1/3 h-fit">
        <h2 class="font-bold text-lg my-5 mt-20">Details</h2>
        <div class="flex border rounded">
          <ol class="mx-3 py-1 items-center my-3">
            <li class="font-semibold text-sm mb-3">Status</li>
            <li class="font-semibold text-sm mb-5">Priority</li>
            <li class="font-semibold text-sm mb-3">Assignee</li>
            <li class="font-semibold text-sm mb-5">Fixer</li>
            <li class="font-semibold text-sm mb-3">Reporter</li>
            <li class="font-semibold text-sm">Reported Date</li>
          </ol>

          <ol class="mx-16 items-center my-3">
            <li class="mb-1">
              <StatusBadge :status="issue.status.toUpperCase()"/>
              <button class="mx-2.5 text-blue-700 text-xs py-2 px-4 hover:underline">Change State</button>
            </li>
            <li class="mb-3">
              <PriorityBadge :priority="issue.priority.toUpperCase()"/>
            </li>
            <li class="mb-1">
              <span class="px-1.5 text-xs font-medium">{{ issue.assignee ? issue.assignee : "Unassigned" }}</span>
              <button v-if="!issue.assignee" class="mx-4 text-blue-700 text-xs py-2 px-4 hover:underline">Assign
                Developer
              </button>
            </li>
            <li class="mb-3.5">
              <span class="px-1.5 text-xs font-medium">{{ issue.fixer ? issue.fixer : "Unassigned" }}</span>
            </li>
            <li class="mb-2">
              <span class="px-1.5 text-xs font-medium">{{ issue.reporter }}</span>
            </li>
            <li class="">
              <span class="px-1.5 text-xs font-medium">{{ issue.reported_date }}</span>
            </li>
          </ol>
        </div>
      </div>
    </div>

  </section>
  </body>
</template>

<script setup>
import {inject, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import StatusBadge from "@/components/StatusBadge.vue";
import PriorityBadge from "@/components/PriorityBadge.vue";

const axios = inject('axios')
const route = useRoute()

const issue = ref({
  "title": "",
  "description": "",
  "status": "",
  "priority": "",
  "assignee": "",
  "fixer": "",
  "reporter": "",
  "reported_date": "",
})

const commentForm = {
  "content": ""
}

onMounted(() => {
  console.log("monuted")
  getIssue()
})

function getIssue() {
  console.log("getIssue")
  axios.get('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id)
    .then(response => {
      console.log(response.data)
      issue.value = response.data
    }).catch(error => {
      console.log(error)
      if (error.message.indexOf('Network Error') > -1) {
        alert('Network Error\nPlease Try again later')
      }
    })
}

function postComment() {
  axios.post('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id + '/comments', {
    "content": commentForm.content
  }).then((response) => {
    console.log(response.data)
    getIssue()
    commentForm.content = ""
  }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}
</script>
