<template>
  <aside id="logo-sidebar" class="fixed left-0 z-24 w-64 pt-20 h-screen transition-transform -translate-x-full bg-white border-r border-gray-200 sm:translate-x-0" aria-label="Sidebar">
    <div class="h-auto px-3 pb-4 overflow-y-auto bg-white">
      <ul class="space-y-2">
        <li>
          <h1 class="flex items-center p-2 text-gray-900 font-bold text-sm mb-3">My Project</h1>
        </li>
<!--        <li>-->
<!--          <router-link :to="{ name: 'home' }" replace class="flex items-center p-2 text-gray-900 rounded hover:bg-gray-100 group">-->
<!--            <svg class="w-[24px] h-[24px] text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">-->
<!--              <path fill-rule="evenodd" d="M11.293 3.293a1 1 0 0 1 1.414 0l6 6 2 2a1 1 0 0 1-1.414 1.414L19 12.414V19a2 2 0 0 1-2 2h-3a1 1 0 0 1-1-1v-3h-2v3a1 1 0 0 1-1 1H7a2 2 0 0 1-2-2v-6.586l-.293.293a1 1 0 0 1-1.414-1.414l2-2 6-6Z" clip-rule="evenodd"/>-->
<!--            </svg>-->
<!--            <span class="flex-1 ms-3 whitespace-nowrap">Home</span>-->
<!--          </router-link>-->
<!--        </li>-->
        <li>
          <router-link :to="{ name: 'issues' }" replace class="flex items-center p-2 text-gray-900 rounded hover:bg-gray-100 group">
            <svg class="w-[24px] h-[24px] text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
              <path fill-rule="evenodd" d="M9 2.221V7H4.221a2 2 0 0 1 .365-.5L8.5 2.586A2 2 0 0 1 9 2.22ZM11 2v5a2 2 0 0 1-2 2H4v11a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2h-7ZM8 16a1 1 0 0 1 1-1h6a1 1 0 1 1 0 2H9a1 1 0 0 1-1-1Zm1-5a1 1 0 1 0 0 2h6a1 1 0 1 0 0-2H9Z" clip-rule="evenodd"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Issues</span>
          </router-link>
        </li>
        <li>
          <router-link :to="{ name: 'analysis' }" replace class="flex items-center p-2 text-gray-900 rounded hover:bg-gray-100 group">
            <svg class="w-[24px] h-[24px] text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
              <path d="M13.5 2c-.178 0-.356.013-.492.022l-.074.005a1 1 0 0 0-.934.998V11a1 1 0 0 0 1 1h7.975a1 1 0 0 0 .998-.934l.005-.074A7.04 7.04 0 0 0 22 10.5 8.5 8.5 0 0 0 13.5 2Z"/>
              <path d="M11 6.025a1 1 0 0 0-1.065-.998 8.5 8.5 0 1 0 9.038 9.039A1 1 0 0 0 17.975 13H11V6.025Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Analysis</span>
          </router-link>
        </li>
        <li :class="isAdmin ? '' : 'hidden'">
          <div data-modal-target="add-user-modal" data-modal-toggle="add-user-modal" class="flex items-center p-2 text-gray-900 rounded hover:bg-gray-100 group">
            <svg class="w-[24px] h-[24px] text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
              <path fill-rule="evenodd" d="M8 4a4 4 0 1 0 0 8 4 4 0 0 0 0-8Zm-2 9a4 4 0 0 0-4 4v1a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2v-1a4 4 0 0 0-4-4H6Zm7.25-2.095c.478-.86.75-1.85.75-2.905a5.973 5.973 0 0 0-.75-2.906 4 4 0 1 1 0 5.811ZM15.466 20c.34-.588.535-1.271.535-2v-1a5.978 5.978 0 0 0-1.528-4H18a4 4 0 0 1 4 4v1a2 2 0 0 1-2 2h-4.535Z" clip-rule="evenodd"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Add Users</span>
          </div>
        </li>
      </ul>
    </div>
  </aside>

  <div id="add-user-modal" data-modal-backdrop="static" tabindex="-1" aria-hidden="true"
       class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
    <div class="relative p-4 w-full max-w-md max-h-full">
      <!-- Modal content -->
      <div class="relative bg-white rounded-lg shadow">
        <!-- Modal header -->
        <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t">
          <h3 class="text-lg font-semibold text-gray-900">Add User</h3>
          <button type="button" data-modal-hide="add-user-modal"
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
        <form class="p-4 md:p-5" v-on:submit.prevent="addUser">
          <div class="grid gap-4 mb-4 grid-cols-2">
            <div class="col-span-2">
              <ul>
                <li v-for="(userRole, index) in ['Admin', 'Project_Lead', 'Developer', 'Tester']" :key="index">
                  <div class="flex items-center p-2 rounded hover:bg-gray-100">
                    <input :id="`userRole-` + userRole" type="checkbox" :value="userRole.toUpperCase()"
                           class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-0" v-model="userRoles">
                    <label :for="`userRole-` + userRole"
                           class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ userRole }}</label>
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
                    <input :id="`user-${user.username}`" type="radio" :value="user.username" name="default-radio" v-model="addUserForm.username"
                           class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300" required>
                    <label :for="`user-${user.username}`"
                           class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                  </div>
                </li>
              </ul>

            </div>
          </div>
          <button type="submit"
                  class="text-white inline-flex items-center bg-blue-600 hover:bg-blue-700 focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center">
            Add
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import store from "@/vuex/store";
import {computed, inject, onMounted, ref} from "vue";
import {initFlowbite} from "flowbite";
import {useRoute} from "vue-router";

const axios = inject('axios')
const route = useRoute()

const addUserForm = {
  "username": "",
}

const userRoles = ref([])

const userList = ref([
  {
    "username": ''
  }
])
const searchedUserList = ref([])

const isAdmin = computed(() => {
  return store.getters.hasRole('ADMIN')
})

onMounted(() => {
  initFlowbite();
  getUsers()
})


function searchUsers(event) {
  const keyword = event.target.value

  searchedUserList.value = userList.value
      .filter(user => user.username !== store.state.username)
      .filter(user => user.username.toLowerCase().includes(keyword.toLowerCase()))
}

function getUsers() {
  axios.get('/api/projects/' + route.params.project_id + '/users/join')
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

function addUser() {
  if (userRoles.value.length < 1) {
    alert('User roles must be selected')
    return
  }

  axios.post('/api/projects/' + route.params.project_id + '/users', {
    "username": addUserForm.username,
    "userRoles": userRoles.value,
  })
      .then(response => {
        console.log(response.data)
        addUserForm.username = ""
        userRoles.value = []
        location.reload()
      }).catch(error => {
    console.log(error)
  })
}
</script>