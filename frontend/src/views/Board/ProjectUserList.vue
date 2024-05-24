<template>
  <section>
    <button data-modal-target="add-user-modal" data-modal-toggle="add-user-modal"
            class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded shadow-lg">
      Add User
    </button>

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
                      <input :id="`assignee-${user.username}`" type="radio" :value="user.username" name="default-radio" v-model="addUserForm.username"
                             class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300">
                      <label :for="`assignee-${user.username}`"
                             class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{ user.username }}</label>
                    </div>
                  </li>
                </ul>

              </div>
            </div>
            <button type="submit"
                    class="text-white inline-flex items-center bg-blue-600 hover:bg-blue-700 focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                    data-modal-hide="add-user-modal">
              Add
            </button>
          </form>
        </div>
      </div>
    </div>
  </section>


</template>

<script setup>
import store from "@/vuex/store";
import {inject, onMounted, ref} from "vue";
import {initFlowbite} from "flowbite";
import {useRoute} from "vue-router";

const axios = inject('axios')
const route = useRoute()

const addUserForm = {
  "username": "",
}

const userList = ref([
  {
    "username": ''
  }
])
const searchedUserList = ref([])

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
  axios.post('/api/projects/' + route.params.project_id + '/users', {
    "username": addUserForm.username
  })
  .then(response => {
    console.log(response.data)
    addUserForm.username = ""
    getUsers()
  }).catch(error => {
    console.log(error)
  })
}
</script>