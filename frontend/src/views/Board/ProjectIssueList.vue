<template>
  <body class="min-h-screen">

  <section>
    <div class="flex flex-col md:flex-row items-center justify-between space-y-3 md:space-y-0 md:space-x-4 py-6 mx-3">
      <div class="w-full md:w-4/5 m">
        <form class="flex items-left">
          <div class="relative w-1/6 mr-2.5">
            <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
              <svg aria-hidden="true" class="w-5 h-5 text-gray-500" fill="currentColor" viewbox="0 0 20 20"
                   xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd"
                      d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                      clip-rule="evenodd"/>
              </svg>
            </div>
            <input type="text" v-model="filter.keyword"
                   class="outline-none bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded focus:border-blue-500 block w-full pl-10 p-2"
                   placeholder="Search Issues"/>
          </div>


          <button id="statusDropdownButton" data-dropdown-toggle="statusDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center"
                  :class="statusSelected ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="50">
            Status
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="statusDropdown"
               class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100 w-44">
            <ul class="p-3 space-y-1 text-sm text-gray-700" aria-labelledby="dropdownBgHoverButton">
              <li v-for="(status, index) in ['New', 'Assigned', 'Fixed', 'Resolved', 'Closed', 'Reopened']"
                  :key="index">
                <div class="flex items-center p-2 rounded hover:bg-gray-100">
                  <input :id="`status-` + status" type="checkbox" :value="status.toUpperCase()"
                         class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-0"
                         v-model="filter.status">
                  <label :for="`status-` + status"
                         class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ status }}</label>
                </div>
              </li>
            </ul>
          </div>


          <button id="priorityDropdownButton" data-dropdown-toggle="priorityDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center"
                  :class="prioritySelected ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="47">
            Priority
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="priorityDropdown"
               class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100 w-44">
            <ul class="p-3 space-y-1 text-sm text-gray-700" aria-labelledby="dropdownBgHoverButton">
              <li v-for="(priority, index) in ['Blocker', 'Critical', 'Major', 'Minor', 'Trivial']" :key="index">
                <div class="flex items-center p-2 rounded hover:bg-gray-100">
                  <input :id="`priority-` + priority" type="checkbox" :value="priority.toUpperCase()"
                         class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-0"
                         v-model="filter.priority">
                  <label :for="`priority-` + priority"
                         class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ priority }}</label>
                </div>
              </li>
            </ul>
          </div>


          <button id="assigneeDropdownButton" data-dropdown-toggle="assigneeDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center"
                  :class="filter.assignee || filter.assignee == null ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="68">
            Assignee
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="assigneeDropdown"
               class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100">
            <div class="px-3 py-3 text-sm text-gray-900">
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="assignee-me" type="radio" :value="myName" name="default-radio"
                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                       v-model="filter.assignee">
                <label for="assignee-me" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Me
                  ({{ myName }})</label>
              </div>
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="assignee-unassigned" type="radio" :value="null" name="default-radio"
                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                       v-model="filter.assignee">
                <label for="assignee-unassigned" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Unassigned</label>
              </div>
            </div>

            <div>
              <div class="px-3 pt-3">
                <label for="input-group-search" class="sr-only">Search</label>
                <div class="relative">
                  <div class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none">
                    <svg class="w-4 h-4 text-gray-500" aria-hidden="true"
                         xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                    </svg>
                  </div>
                  <input type="text" @input="searchUsers($event)" id="input-group-search"
                         class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded bg-gray-50 focus:border-blue-500"
                         placeholder="Search user">
                </div>
              </div>

              <ul class="max-h-48 overflow-y-auto p-3 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                <li v-for="(user, index) in searchedUserList" :key="index">
                  <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                    <input :id="`assignee-${index}`" type="radio" :value="user.username" name="default-radio"
                           class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                           v-model="filter.assignee">
                    <label :for="`assignee-${index}`"
                           class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                  </div>
                </li>
              </ul>

              <label
                  class="flex items-center p-3 text-sm font-medium text-gray-700 border-t border-gray-200 rounded-b-lg bg-gray-50  hover:bg-gray-100 hover:underline"
                  @click="filter.assignee = ``">
                Cancel Selection
              </label>
            </div>
          </div>


          <button id="fixerDropdownButton" data-dropdown-toggle="fixerDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center"
                  :class="filter.fixer || filter.fixer == null ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="80">
            Fixer
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="fixerDropdown"
               class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100">
            <div class="px-3 py-3 text-sm text-gray-900">
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="assignee-me" type="radio" :value="myName" name="default-radio"
                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                       v-model="filter.fixer">
                <label for="assignee-me" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Me
                  ({{ myName }})</label>
              </div>
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="assignee-unassigned" type="radio" :value="null" name="default-radio"
                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                       v-model="filter.fixer">
                <label for="assignee-unassigned" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Unassigned</label>
              </div>
            </div>

            <div>
              <div class="px-3 pt-3">
                <label for="input-group-search" class="sr-only">Search</label>
                <div class="relative">
                  <div class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none">
                    <svg class="w-4 h-4 text-gray-500" aria-hidden="true"
                         xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                    </svg>
                  </div>
                  <input type="text" @input="searchUsers($event)" id="input-group-search"
                         class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded bg-gray-50 focus:border-blue-500"
                         placeholder="Search user">
                </div>
              </div>

              <ul class="max-h-48 overflow-y-auto p-3 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                <li v-for="(user, index) in searchedUserList" :key="index">
                  <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                    <input :id="`assignee-${index}`" type="radio" :value="user.username" name="default-radio"
                           class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                           v-model="filter.fixer">
                    <label :for="`assignee-${index}`"
                           class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                  </div>
                </li>
              </ul>

              <label
                  class="flex items-center p-3 text-sm font-medium text-gray-700 border-t border-gray-200 rounded-b-lg bg-gray-50  hover:bg-gray-100 hover:underline"
                  @click="filter.fixer = ``">
                Cancel Selection
              </label>
            </div>
          </div>


          <button id="reporterDropdownButton" data-dropdown-toggle="reporterDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center"
                  :class="filter.reporter ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="68">
            Reporter
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="reporterDropdown"
               class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100">
            <div class="px-3 py-3 text-sm text-gray-900">
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="reporter-me" type="radio" :value="myName" name="default-radio"
                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                       v-model="filter.reporter">
                <label for="reporter-me" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Me
                  ({{ myName }})</label>
              </div>
            </div>

            <div>
              <div class="px-3 pt-3">
                <label for="input-group-search" class="sr-only">Search</label>
                <div class="relative">
                  <div class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none">
                    <svg class="w-4 h-4 text-gray-500" aria-hidden="true"
                         xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                    </svg>
                  </div>
                  <input type="text" @input="searchUsers($event)" id="input-group-search"
                         class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded bg-gray-50 focus:border-blue-500"
                         placeholder="Search user">
                </div>
              </div>

              <ul class="max-h-48 overflow-y-auto p-3 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                <li v-for="(user, index) in searchedUserList" :key="index">
                  <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                    <input :id="`reporter-${index}`" type="radio" :value="user.username" name="default-radio"
                           class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300"
                           v-model="filter.reporter">
                    <label :for="`reporter-${index}`"
                           class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                  </div>
                </li>
              </ul>

              <label
                  class="flex items-center p-3 text-sm font-medium text-gray-700 border-t border-gray-200 rounded-b-lg bg-gray-50  hover:bg-gray-100 hover:underline"
                  @click="filter.reporter = ``">
                Cancel Selection
              </label>
            </div>
          </div>
          <button class="mx-2.5 text-gray-700 font-bold py-2 px-4 hover:underline" type="reset" @click="resetFilter">
            Reset
          </button>
        </form>
      </div>
      <div
          class="w-full md:w-auto flex flex-col md:flex-row space-y-2 md:space-y-0 items-stretch md:items-center justify-end md:space-x-3 flex-shrink-0">
        <button data-modal-target="report-issue-modal" data-modal-toggle="report-issue-modal"
                class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded shadow-lg">Report Issue
        </button>
      </div>

      <div id="report-issue-modal" data-modal-backdrop="static" tabindex="-1" aria-hidden="true"
           class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
        <div class="relative p-4 w-full max-w-md max-h-full">
          <!-- Modal content -->
          <div class="relative bg-white rounded-lg shadow">
            <!-- Modal header -->
            <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t">
              <h3 class="text-lg font-semibold text-gray-900">
                Report New Issue
              </h3>
              <button type="button"
                      class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center"
                      data-modal-hide="report-issue-modal">
                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                     viewBox="0 0 14 14">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                </svg>
                <span class="sr-only">Close modal</span>
              </button>
            </div>
            <!-- Modal body -->
            <form class="p-4 md:p-5" v-on:submit.prevent="reportIssue">
              <div class="grid gap-4 mb-4 grid-cols-2">
                <div class="col-span-2">
                  <label for="name" class="block mb-2 text-sm font-medium text-gray-900">Title</label>
                  <input type="text" v-model="issueForm.title" name="name" id="name"
                         class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-600 block w-full p-2.5"
                         placeholder="Type issue name" required="">
                </div>
                <div class="col-span-2">
                  <label for="description" class="block mb-2 text-sm font-medium text-gray-900">Description</label>
                  <textarea id="description" v-model="issueForm.description" rows="4"
                            class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:border-blue-500 resize-none"
                            placeholder="Write issue description here"></textarea>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="status"
                         class="block mb-2 text-sm font-medium text-gray-900">Status</label>
                  <select id="status"
                          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full p-2.5"
                          disabled>
                    <option selected>New</option>
                  </select>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="priority"
                         class="block mb-2 text-sm font-medium text-gray-900">Priority</label>
                  <select id="priority" v-model="issueForm.priority"
                          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full p-2.5">
                    <option value="blocker">Blocker</option>
                    <option value="critical">Critical</option>
                    <option value="major">Major</option>
                    <option value="minor">Minor</option>
                    <option value="trivial">Trivial</option>
                  </select>
                </div>
              </div>
              <button type="submit"
                      class="text-white inline-flex items-center bg-blue-600 hover:bg-blue-700 focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                      data-modal-hide="report-issue-modal">
                Save
              </button>
            </form>
          </div>
        </div>
      </div>

    </div>

    <table class="w-full text-sm text-left">
      <thead class="text-sm border-b-2">
      <tr>
        <th scope="col" class="px-4 py-3 w-2/12">Title</th>
        <th scope="col" class="px-4 py-3 w-4/12">Description</th>
        <th scope="col" class="px-4 py-3 w-1/12">Status</th>
        <th scope="col" class="px-4 py-3 w-1/12">Priority</th>
        <th scope="col" class="px-4 py-3 w-1/12">Assignee</th>
        <th scope="col" class="px-4 py-3 w-1/12">Fixer</th>
        <th scope="col" class="px-4 py-3 w-1/12">Reporter</th>
        <th scope="col" class="px-4 py-3 w-1/12">ReportedDate</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="issue in issueList" :key="issue.issue_id" class="border-b-2 hover:bg-gray-100">
        <th scope="row" class="px-4 py-3 font-medium text-blue-700 hover:underline whitespace-nowrap w-2/12 max-w-0">
          <router-link :to="{ name: 'issue-detail', params: { issue_id: issue.issue_id } }">{{
              issue.title
            }}
          </router-link>
        </th>
        <td class="px-4 py-3 w-4/12">{{ issue.description }}</td>
        <!--        <td class="px-4 py-3 w-1/12">-->
        <!--          <span class="px-5 py-1 bg-green-300 rounded-full text-green-800 font-bold dfle">{{issue.status}}</span>-->
        <!--        </td>-->
        <td class="px-4 py-3 w-1/12">
          <StatusBadge :status="issue.status.toUpperCase()"/>
        </td>

        <td class="px-4 py-3 w-1/12">
          <PriorityBadge :priority="issue.priority.toUpperCase()"/>
        </td>
        <td class="px-4 py-3 w-1/12">{{ issue.assignee }}</td>
        <td class="px-4 py-3 w-1/12">{{ issue.fixer }}</td>
        <td class="px-4 py-3 w-1/12">{{ issue.reporter }}</td>
        <td class="px-4 py-3 w-1/12">{{ issue.reported_date }}</td>
      </tr>
      </tbody>
    </table>
  </section>
  </body>
</template>

<script setup>
import {computed, inject, onMounted, ref, watch} from 'vue'
import {initFlowbite} from 'flowbite'
import {useRoute} from "vue-router";
import StatusBadge from "@/components/StatusBadge.vue";
import PriorityBadge from "@/components/PriorityBadge.vue";
import qs from "qs";
import store from "@/vuex/store";

const axios = inject('axios')
const route = useRoute()

const myName = computed(() => store.state.username)
const userList = ref([
  {
    "username": '',
  }
])

const searchedUserList = ref([])

const issueList = ref([])
const issueForm = {
  "title": "",
  "description": "",
  "priority": "major"
}

const filter = ref({
  "keyword": "",
  "status": [],
  "priority": [],
  "assignee": "",
  "fixer": "",
  "reporter": "",
})

const statusSelected = computed(() => {
  return filter.value.status.length > 0
})
const prioritySelected = computed(() => {
  return filter.value.priority.length > 0
})
let searchTimeout = null

onMounted(() => {
  initFlowbite();
  getIssues()
  getUsers()
})

watch(filter.value, () => searchIssues())

function resetFilter() {
  filter.value = {
    "keyword": "",
    "status": [],
    "priority": [],
    "assignee": "",
    "fixer": "",
    "reporter": "",
  }
}

function searchUsers(event) {
  const keyword = event.target.value

  searchedUserList.value = userList.value
      .filter(user => user.username !== store.state.username)
      .filter(user => user.username.toLowerCase().includes(keyword.toLowerCase()))
}


function searchIssues() {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => search(), 500)
}

function search() {
  console.log("send query to server")
  console.log(filter.value.status)
  axios.get('/api/test', {
    params: filter.value,
    paramsSerializer: params => {
      return qs.stringify(params, {arrayFormat: "comma"});
    }
  }).then(response => {
    console.log(response)
  }).catch(error => {
    // console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}


function getIssues() {
  axios.get('/api/projects/' + route.params.project_id + '/issues')
      .then(response => {
        console.log(response.data)
        issueList.value = response.data
      }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}

function getUsers() {
  axios.get('/api/projects/' + route.params.project_id + '/users')
      .then(response => {
        console.log(response.data)
        userList.value = response.data
        searchedUserList.value = userList.value.filter(user => user.username !== store.state.username)
      }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}

function reportIssue() {
  axios.post('/api/projects/' + route.params.project_id + '/issues', {
    "title": issueForm.title,
    "description": issueForm.description,
    "priority": issueForm.priority
  }).then((response) => {
    console.log(response.data)
    issueForm.title = ""
    issueForm.description = ""
    issueForm.priority = "major"
    getIssues()
  }).catch(error => {
    console.log(error)
    if (error.message.indexOf('Network Error') > -1) {
      alert('Network Error\nPlease Try again later')
    }
  })
}
</script>