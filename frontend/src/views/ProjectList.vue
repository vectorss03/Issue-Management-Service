<template>
  <div class="mx-auto max-w-screen-xl px-4 lg:px-12 m-7">
    <h1 class="font-bold text-2xl">Projects</h1>
    <div class="flex flex-col md:flex-row items-center justify-between space-y-3 md:space-y-0 md:space-x-4 py-6">
      <div class="w-full md:w-1/4">
        <form class="flex items-center">
          <label for="simple-search" class="sr-only">Search</label>
          <div class="relative w-full">
            <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
              <svg aria-hidden="true" class="w-5 h-5 text-gray-500" fill="currentColor" viewbox="0 0 20 20"
                   xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd"
                      d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                      clip-rule="evenodd"/>
              </svg>
            </div>
            <input type="text" @input="searchProjects($event)"
                   class="outline-none bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2"
                   placeholder="Search Projects"/>
          </div>
        </form>
      </div>
      <div
          class="w-full md:w-auto flex flex-col md:flex-row space-y-2 md:space-y-0 items-stretch md:items-center justify-end md:space-x-3 flex-shrink-0">
        <button data-modal-target="create-project-modal" data-modal-toggle="create-project-modal"
                class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded shadow-lg">Create Project
        </button>
      </div>

      <!-- Main modal -->
      <div id="create-project-modal" data-modal-backdrop="static" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
        <div class="relative p-4 w-full max-w-md max-h-full">
          <!-- Modal content -->
          <div class="relative bg-white rounded-lg shadow">
            <!-- Modal header -->
            <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t">
              <h3 class="text-lg font-semibold text-gray-900">
                Create New Project
              </h3>
              <button type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center" data-modal-hide="create-project-modal">
                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                </svg>
                <span class="sr-only">Close modal</span>
              </button>
            </div>
            <!-- Modal body -->
            <form class="p-4 md:p-5">
              <div class="grid gap-4 mb-4 grid-cols-2">
                <div class="col-span-2">
                  <label for="name" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Title</label>
                  <input type="text" v-model="projectForm.title" name="name" id="name" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5" placeholder="Type project name" required="">
                </div>
                <div class="col-span-2">
                  <label for="description" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Description</label>
                  <textarea id="description" v-model="projectForm.description" rows="4" class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 resize-none" placeholder="Write project description here"></textarea>
                </div>
              </div>
              <button type="button" @click="createProject()" class="text-white inline-flex items-center bg-blue-600 hover:bg-blue-700 focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center" data-modal-hide="create-project-modal">
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
        <th scope="col" class="px-4 py-3 w-1/4">Title</th>
        <th scope="col" class="px-4 py-3 w-2/4">Description</th>
        <th scope="col" class="px-4 py-3 w-1/8">Current Issues</th>
        <th scope="col" class="px-4 py-3 w-1/8">
          <span class="sr-only">Actions</span>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in searched_project_list" :key="row.project_id" class="border-b-2 hover:bg-gray-100">
        <th scope="row" class="px-4 py-3 font-medium text-blue-700 hover:underline whitespace-nowrap w-1/4 max-w-0">
          <router-link :to="{ name: 'project', params: { project_id: row.project_id } }">{{row.title}}</router-link>
        </th>
        <td class="px-4 py-3 w-2/4">{{row.description}}</td>
        <td class="px-4 py-3 w-1/8">0</td>
        <td class="px-4 py-3 w-1/8">
          <span class="sr-only">Actions</span>
        </td>
      </tr>
      </tbody>
    </table>
  </div>


</template>

<script setup>
import {inject, onMounted, ref} from "vue";
import {initFlowbite} from 'flowbite'

const project_list = ref()
const projectForm = {
  "title": "",
  "description": "",
}

const searched_project_list = ref(project_list)
function searchProjects(event) {
  const keyword = event.target.value

  searched_project_list.value = project_list.value.filter(project => project.title.toLowerCase().includes(keyword.toLowerCase()))
}

const axios = inject('axios')  // inject axios

function getProjects() {
  axios.get('/api/projects')
      .then(response => {
        console.log(response.data)
        project_list.value = response.data
      })
}

function createProject() {
  axios.post('/api/projects', {
    "title": projectForm.title,
    "description": projectForm.description,
  }).then((response) => {
    console.log(response.data)
    getProjects()
  })
}

onMounted(() => {
  console.log("mounted")
  getProjects()
  initFlowbite();
})

</script>