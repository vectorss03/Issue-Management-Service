<template>
  <body class="min-h-screen">

  <section>
    <div class="flex">
      <div class="mr-5 grow">
        <div>
          <h1 class="font-bold text-3xl m-3 mb-10">{{ issue.title }}</h1>
          <h2 class="font-semibold text-m m-3">Description</h2>
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
            <li class="p-4 mb-1" v-for="(comment, index) in comments" :key="index">
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


      <div class="min-w-[30rem] h-fit">
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
          <ol class="mx-16 items-center my-3 py-1">
            <li class="text-sm mb-3">
              <StatusBadge :status="issue.status.toUpperCase()"/>
              <button data-modal-target="change-state-modal" data-modal-toggle="change-state-modal"
                  class="mx-2.5 text-blue-700 text-xs px-4 hover:underline" :class="canChangeState ? '' : 'hidden'">Change State</button>
            </li>
            <li class="text-sm mb-5">
              <PriorityBadge :priority="issue.priority.toUpperCase()"/>
            </li>
            <li class="text-sm mb-3">
              <span class="px-1.5 text-xs font-medium">{{ issue.assignee ? issue.assignee : "Unassigned" }}</span>
              <span class="mx-2.5 px-4">
                <button data-modal-target="assign-developer-modal" data-modal-toggle="assign-developer-modal"
                        class="text-blue-700 text-xs hover:underline" v-if="isProjectLead" :class="issue.status === `NEW` || issue.status === 'REOPENED' ? '' : 'hidden'">
                  Assign Developer
                </button>
              </span>
            </li>
            <li class="text-sm mb-5">
              <span class="px-1.5 text-xs font-medium">{{ issue.fixer ? issue.fixer : "Unassigned" }}</span>
            </li>
            <li class="text-sm mb-3">
              <span class="px-1.5 text-xs font-medium">{{ issue.reporter }}</span>
            </li>
            <li class="text-sm">
              <span class="px-1.5 text-xs font-medium">{{ issue.reported_date }}</span>
            </li>
          </ol>
        </div>
      </div>
    </div>

    <div id="assign-developer-modal" data-modal-backdrop="dynamic" tabindex="-1" aria-hidden="true"
         class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
      <div class="relative p-4 w-full max-w-md max-h-full">
        <!-- Modal content -->
        <div class="relative bg-white rounded-lg shadow">
          <!-- Modal header -->
          <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t">
            <h3 class="text-lg font-semibold text-gray-900">Assign Developer</h3>
            <button type="button" data-modal-hide="assign-developer-modal"
                    class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
              <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                   viewBox="0 0 14 14">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
              </svg>
              <span class="sr-only">Close modal</span>
            </button>
          </div>
          <!-- Modal body -->
          <form class="p-4 md:p-5" v-on:submit.prevent="assignDeveloper">
            <div class="grid gap-4 mb-4 grid-cols-2">
              <div class="col-span-2">
                <h3 class="font-bold text-m ps-0.5">Recommended</h3>
                <ul class="max-h-48 overflow-y-auto py-3 px-0.5 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                  <li v-for="(user, index) in recommendedDeveloperList" :key="index">
                    <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                      <input :id="`assignee-${user.username}`" type="radio" :value="user.username" name="default-radio" v-model="assignDeveloperForm.developer"
                             class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300">
                      <label :for="`assignee-${user.username}`"
                             class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                    </div>
                  </li>
                </ul>
              </div>

              <div class="col-span-2">
                <div class="relative">
                  <div class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none">
                    <svg class="w-4 h-4 text-gray-500" aria-hidden="true"
                         xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                    </svg>
                  </div>
                  <input type="text" id="input-group-search" @input="searchUsers($event)"
                         class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded bg-gray-50 focus:border-blue-500"
                         placeholder="Search user">
                </div>
                <ul class="max-h-48 overflow-y-auto py-3 px-0.5 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                  <li v-for="(user, index) in searchedUserList" :key="index">
                    <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                      <input :id="`assignee-${user.username}`" type="radio" :value="user.username" name="default-radio" v-model="assignDeveloperForm.developer"
                             class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300">
                      <label :for="`assignee-${user.username}`"
                             class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                    </div>
                  </li>
                </ul>

              </div>
            </div>
            <button type="submit" data-popover-target="popover-assign" data-popover-placement="right"
                    class="text-white inline-flex items-center bg-blue-600 hover:bg-blue-700 focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                    data-modal-hide="assign-developer-modal">
              Assign
            </button>
          </form>
        </div>
      </div>
    </div>

    <div data-popover id="popover-assign" role="tooltip" class="absolute z-50 invisible inline-block w-64 text-sm text-gray-500 transition-opacity duration-300 bg-white border border-gray-200 rounded-lg shadow-sm opacity-0">
      <div class="px-3 py-2 bg-gray-100 border-b border-gray-200 rounded-t-lg">
        <h3 class="font-semibold text-gray-900">Status Change</h3>
      </div>
      <div class="px-3 py-2">
        <span>Status of this issue will be changed into 'Assigned'</span>
      </div>
    </div>


    <div id="change-state-modal" data-modal-backdrop="dynamic" tabindex="-1" aria-hidden="true"
         class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
      <div class="relative p-4 w-full max-w-md max-h-full">
        <!-- Modal content -->
        <div class="relative bg-white rounded-lg shadow">
          <!-- Modal header -->
          <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t">
            <h3 class="text-lg font-semibold text-gray-900">Change State</h3>
            <button type="button" data-modal-hide="change-state-modal"
                    class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
              <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                   viewBox="0 0 14 14">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
              </svg>
              <span class="sr-only">Close modal</span>
            </button>
          </div>
          <!-- Modal body -->
          <form class="p-4 md:p-5" v-on:submit.prevent="changeState">
            <div class="grid gap-4 mb-4 grid-cols-2">
              <div class="col-span-2 flex">
                <select id="status"
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block p-2.5 w-2/5"
                        disabled>
                  <option selected>{{ issue.status }}</option>
                </select>

                <svg class="w-1/5 h-6 text-gray-800 my-2.5 mx-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 12H5m14 0-4 4m4-4-4-4"/>
                </svg>

<!--                <select id="priority" v-model="changeStateForm.nextState.value" @click="console.log(changeStateForm.nextState.value)"-->
<!--                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block p-2.5 w-2/5" disabled>-->
<!--                  <option value="NEW">New</option>-->
<!--                  <option value="ASSIGNED">Assigned</option>-->
<!--                  <option value="FIXED">Fixed</option>-->
<!--                  <option value="RESOLVED">Resolved</option>-->
<!--                  <option value="CLOSED">Closed</option>-->
<!--                  <option value="REOPENED">Reopened</option>-->
<!--                </select>-->

                  <select id="status" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block p-2.5 w-2/5" disabled>
                    <option selected>{{ changeStateForm.nextState.value }}</option>
                  </select>
              </div>
            </div>
            <button type="submit"
                    class="text-white inline-flex items-center bg-blue-600 hover:bg-blue-700 focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                    data-modal-hide="change-state-modal">
              Change
            </button>
          </form>
        </div>
      </div>
    </div>

  </section>
  </body>
</template>

<script setup>
import {computed, inject, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import StatusBadge from "@/components/StatusBadge.vue";
import PriorityBadge from "@/components/PriorityBadge.vue";
import {initFlowbite} from "flowbite";
import {HttpStatusCode} from "axios";
import store from "@/vuex/store";

const axios = inject('axios')
const route = useRoute()
const router = useRouter()

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

const comments = ref([])

const commentForm = {
  "content": ""
}

const assignDeveloperForm = {
  "developer": ""
}

const changeStateForm = {
  "nextState": computed(() => {
    switch (issue.value.status) {
      case "NEW": return "ASSIGNED"
      case "ASSIGNED": return "FIXED"
      case "FIXED": return "RESOLVED"
      case "RESOLVED": return "CLOSED"
      case "CLOSED": return "REOPENED"
      case "REOPENED": return "ASSIGNED"
      default: return "NEW"
    }
  })
}

const canChangeState = computed(() => {
  switch (issue.value.status) {
    case "NEW": return false
    case "ASSIGNED": return store.state.username === issue.value.assignee
    case "FIXED": return store.getters.hasRole('TESTER') || store.getters.hasRole('ADMIN')
    case "RESOLVED": return store.getters.hasRole('PROJECT_LEAD') || store.getters.hasRole('ADMIN')
    case "CLOSED": return store.getters.hasRole('PROJECT_LEAD') || store.getters.hasRole('ADMIN')
    case "REOPENED": return false
    default: return false
  }
})


const userList = ref([
  {
    "username": ''
  }
])

const recommendedDeveloperList = ref([])
const searchedUserList = ref([])

// const isAdmin = computed(() => store.getters.hasRole('ADMIN'))
const isProjectLead = computed(() => store.getters.hasRole('ADMIN') || store.getters.hasRole('PROJECT_LEAD'))
// const isTester = computed(() => store.getters.hasRole('ADMIN') || store.getters.hasRole('TESTER'))

onMounted(() => {
  initFlowbite();
  // eslint-disable-next-line
  // const modal = FlowbiteInstances.getInstance('Modal', 'assign-developer-modal');
  // window.onpopstate = () => {
  //   modal.hide()
  // }

  getIssue()
  getComments()
  getRecommendedDeveloper()
  getUsers()
})

function searchUsers(event) {
  const keyword = event.target.value

  searchedUserList.value = userList.value
      .filter(user => user.username.toLowerCase().includes(keyword.toLowerCase()))
}

function getIssue() {
  axios.get('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id)
      .then(response => {
        console.log(response.data)
        issue.value = response.data
        // changeStateForm.nextState = issue.value.status
      }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    } else if (error.response.status === HttpStatusCode.Forbidden) {
      alert('You do not have access to this project')
      router.back()
    }
  })
}

function getComments() {
  axios.get('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id + '/comments')
      .then(response => {
        console.log(response.data)
        comments.value = response.data
      }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}

function getUsers() {
  axios.get('/api/projects/' + route.params.project_id + '/developers')
      .then(response => {
        console.log(response.data)
        userList.value = response.data
        searchedUserList.value = userList.value
      }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}

function getRecommendedDeveloper() {
  axios.get('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id + '/assignee/recommended')
      .then(response => {
        console.log(response.data)
        recommendedDeveloperList.value = response.data
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
    getComments()
    commentForm.content = ""
  }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}

function assignDeveloper() {
  axios.put('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id + '/assignee', {
    "assignee": assignDeveloperForm.developer
  }).then(response => {
        console.log(response.data)
        getIssue()
      }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}

function changeState() {
  axios.put('/api/projects/' + route.params.project_id + '/issues/' + route.params.issue_id + '/status', {
    "status": changeStateForm.nextState.value
  }).then(response => {
    console.log(response.data)
    getIssue()
  }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}
</script>
