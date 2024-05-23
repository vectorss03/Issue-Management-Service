<template>
  <body class="min-h-screen">

  <section>

    <!--    <div class="mx-3 py-2 flex items-center hover:bg-gray-100">-->
    <!--      <p class="font-semibold text-m">Status</p>-->
    <!--      <span class="mx-5 bg-red-100 text-red-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Assigned</span>-->
    <!--    </div>-->
    <!--    <div class="mx-3 py-2 flex items-center hover:bg-gray-100">-->
    <!--      <p class="font-bold text-m">Priority</p>-->
    <!--      <span class="mx-5 bg-red-100 text-red-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Critical</span>-->
    <!--    </div>-->

    <div class="flex">
      <div class="mr-10 w-2/3">
        <div>
          <h1 class="font-bold text-3xl m-3 mb-10">{{ issue.title }}</h1>
          <h1 class="font-bold text-m m-3">Description</h1>
          <div v-text="issue.description" style="white-space: pre-line; word-break: break-word"
               class="text-sm font-normal text-gray-700 mx-3 mb-10"/>
        </div>


        <div>
          <h2 class="m-3 font-semibold text-m">Comments</h2>
          <div class="m-3 mb-5 border border-gray-200 rounded-lg bg-gray-50">
            <div class="px-4 py-2 bg-white rounded-t-lg dark:bg-gray-800">
              <label for="comment" class="sr-only">Your comment</label>
              <textarea id="comment" rows="4" v-model="commentForm.content"
                        class="w-full px-0 text-sm text-gray-900 bg-white border-0 focus:ring-0 resize-none"
                        placeholder="Write a comment..." required></textarea>
            </div>
            <div class="flex items-center justify-between px-3 py-2 border-t dark:border-gray-600">
              <button type="Button" @click="createComment()"
                      class="inline-flex items-center py-2.5 px-4 text-xs font-medium text-center text-white bg-blue-700 rounded-lg focus:ring-4 focus:ring-blue-200 hover:bg-blue-800">
                Save
              </button>
            </div>
          </div>

          <ol class="relative m-3">
            <li class="p-4 mb-1" v-for="(comment, index) in issue.comments" :key="index">
              <div class="items-center mb-3 sm:flex">
                <time class="mx-5 mb-1 text-xs font-normal text-gray-400 sm:order-last sm:mb-0">{{
                    comment.timestamp
                  }}
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
              <span
                  class="bg-green-100 text-green-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">{{issue.status}}</span>
              <button class="mx-2.5 text-blue-700 text-xs py-2 px-4 hover:underline">Change State</button>
            </li>
            <li class="mb-3">
              <span class="bg-red-100 text-red-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">{{issue.priority}}</span>
            </li>
            <li class="mb-1">
              <span class="px-1.5 text-xs font-medium">{{ issue.assignee ? issue.assignee : "Unassigned" }}</span>
              <button v-if="!issue.assignee" class="mx-4 text-blue-700 text-xs py-2 px-4 hover:underline">Assign Developer</button>
            </li>
            <li class="mb-3.5">
              <span class="px-1.5 text-xs font-medium">{{ issue.fixer ? issue.fixer : "Unassigned" }}</span>
            </li>
            <li class="mb-2">
              <span class="px-1.5 text-xs font-medium">{{issue.reporter}}</span>
            </li>
            <li class="">
              <span class="px-1.5 text-xs font-medium">{{issue.reported_date}}</span>
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

const issue = ref({
  "issue_id": 1,
  "title": "First Issue",
  "description": "This is my first issue\nHello world!\n\n\n\n\n\n\n\nTest",
  "status": "new",
  "priority": "major",
  "assignee": null,
  "fixer": null,
  "reporter": "hysk",
  "reported_date": "2024-05-22 01:42:26"
})

// 시간 역순 정렬


const axios = inject('axios')
const route = useRoute()

function getIssue() {
  axios.get('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id)
      .then(response => {
        console.log(response.data)
        issue.value = response.data
      })
}

const commentForm = {
  "content": ""
}

function createComment() {
  axios.post('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id + '/comments', {
    "content": commentForm.content
  }).then((response) => {
    console.log(response.data)
    getIssue()
  })
  commentForm.content = ""
}
console.log('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id)
console.log(route.params)
onMounted(() => {
  getIssue()
})
</script>
