  <template>
  <body class="min-h-screen">

  <section>
    <h1>Issue List</h1>

    <div class="flex flex-col md:flex-row items-center justify-between space-y-3 md:space-y-0 md:space-x-4 py-6">
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
                   class="outline-none bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2"
                   placeholder="Search Issues"/>
          </div>


          <button id="statusDropdownButton" data-dropdown-toggle="statusDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center" :class="status_selected ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
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
              <li v-for="(status, index) in Object.keys(filter.status)" :key="index">
                <div class="flex items-center p-2 rounded hover:bg-gray-100">
                  <input :id="status-`${status}`" type="checkbox"
                         class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-0"
                         v-model="filter.status[status]">
                  <label for="status-new" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{status}}</label>
                </div>
              </li>
            </ul>
          </div>


          <button id="priorityDropdownButton" data-dropdown-toggle="priorityDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center" :class="priority_selected ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
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
              <li v-for="(priority, index) in Object.keys(filter.priority)" :key="index">
                <div class="flex items-center p-2 rounded hover:bg-gray-100">
                  <input :id="priority-`${priority}`" type="checkbox"
                         class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-0"
                         v-model="filter.priority[priority]">
                  <label for="status-new" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{priority}}</label>
                </div>
              </li>
            </ul>
          </div>


          <button id="assigneeDropdownButton" data-dropdown-toggle="assigneeDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center" :class="filter.assignee || filter.assignee == null ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="68">
            Assignee
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="assigneeDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100">
            <div class="px-3 py-3 text-sm text-gray-900">
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="assignee-me" type="radio" :value="my_name" name="default-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" v-model="filter.assignee">
                <label for="assignee-me" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Me ({{my_name}})</label>
              </div>
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="assignee-unassigned" type="radio" :value="null" name="default-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" v-model="filter.assignee">
                <label for="assignee-unassigned" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Unassigned</label>
              </div>
            </div>

            <div>
              <div class="px-3 pt-3">
                <label for="input-group-search" class="sr-only">Search</label>
                <div class="relative">
                  <div class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none">
                    <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                    </svg>
                  </div>
                  <input type="text" @input="searchUsers($event)" id="input-group-search" class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded bg-gray-50 focus:ring-blue-500 focus:border-blue-500" placeholder="Search user">
                </div>
              </div>

              <ul class="max-h-48 overflow-y-auto p-3 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                <li v-for="(user, index) in searched_user_list" :key="index">
                  <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                    <input :id="`assignee-${index}`" type="radio" :value="user.username" name="default-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" v-model="filter.assignee">
                    <label :for="`assignee-${index}`" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{user.username}}</label>
                  </div>
                </li>
              </ul>

              <label class="flex items-center p-3 text-sm font-medium text-gray-700 border-t border-gray-200 rounded-b-lg bg-gray-50  hover:bg-gray-100 hover:underline" @click="filter.assignee = ``">
                Cancel Selection
              </label>
            </div>
          </div>


          <button id="reporterDropdownButton" data-dropdown-toggle="reporterDropdown"
                  class="mx-2.5 text-black focus:outline-none font-medium rounded text-sm px-2 py-2.5 text-center inline-flex items-center" :class="filter.reporter ? 'bg-blue-200 hover:bg-blue-300' : 'bg-gray-200 hover:bg-gray-300'"
                  type="button" data-dropdown-offset-skidding="68">
            Reporter
            <svg class="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                 viewBox="0 0 10 6">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 4 4 4-4"/>
            </svg>
          </button>

          <!-- Dropdown menu -->
          <div id="reporterDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow-xl outline outline-1 outline-gray-100">
            <div class="px-3 py-3 text-sm text-gray-900">
              <div class="py-2 flex items-center p-1 rounded hover:bg-gray-100">
                <input id="reporter-me" type="radio" :value="my_name" name="default-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" v-model="filter.reporter">
                <label for="reporter-me" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">Me ({{my_name}})</label>
              </div>
            </div>

            <div>
              <div class="px-3 pt-3">
                <label for="input-group-search" class="sr-only">Search</label>
                <div class="relative">
                  <div class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none">
                    <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                    </svg>
                  </div>
                  <input type="text" @input="searchUsers($event)" id="input-group-search" class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded bg-gray-50 focus:ring-blue-500 focus:border-blue-500" placeholder="Search user">
                </div>
              </div>

              <ul class="max-h-48 overflow-y-auto p-3 text-sm text-gray-700 " aria-labelledby="dropdownRadioButton">
                <li v-for="(user, index) in searched_user_list" :key="index">
                  <div class="flex items-center p-1 py-2 rounded hover:bg-gray-100">
                    <input :id="`reporter-${index}`" type="radio" :value="user.username" name="default-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" v-model="filter.reporter">
                    <label :for="`reporter-${index}`" class="w-full ms-2 text-sm font-medium text-gray-900 rounded">{{user.username}}</label>
                  </div>
                </li>
              </ul>

              <label class="flex items-center p-3 text-sm font-medium text-gray-700 border-t border-gray-200 rounded-b-lg bg-gray-50  hover:bg-gray-100 hover:underline" @click="filter.reporter = ``">
                Cancel Selection
              </label>
            </div>
          </div>

            <button   class="mx-2.5 text-gray-700 font-bold py-2 px-4 hover:underline" type="reset" @click="resetFilter">Reset</button>

        </form>
      </div>
      <div
          class="w-full md:w-auto flex flex-col md:flex-row space-y-2 md:space-y-0 items-stretch md:items-center justify-end md:space-x-3 flex-shrink-0">
        <button class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded shadow-lg">Report Issue
        </button>
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
        <th scope="col" class="px-4 py-3 w-1/12">Reporter</th>
        <th scope="col" class="px-4 py-3 w-2/12">ReportedDate</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="issue in issue_list" :key="issue.issue_id" class="border-b-2 hover:bg-gray-100">
        <th scope="row" class="px-4 py-3 font-medium text-blue-700 hover:underline whitespace-nowrap w-2/12 max-w-0">
          <router-link :to="{ name: 'project', params: { project_id: issue.project_id } }">{{ issue.title }}</router-link>
        </th>
        <td class="px-4 py-3 w-4/12">{{ issue.description }}</td>
<!--        <td class="px-4 py-3 w-1/12">-->
<!--          <span class="px-5 py-1 bg-green-300 rounded-full text-green-800 font-bold dfle">{{issue.status}}</span>-->
<!--        </td>-->
        <td class="px-4 py-3 w-1/12">{{ issue.status }}</td>
        <td class="px-4 py-3 w-1/12">{{ issue.priority }}</td>
        <td class="px-4 py-3 w-1/12">{{ issue.assignee }}</td>
        <td class="px-4 py-3 w-1/12">{{ issue.reporter }}</td>
        <td class="px-4 py-3 w-2/12">{{ issue.reported_date }}</td>
      </tr>
      </tbody>
    </table>


  </section>
  </body>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import {initFlowbite} from 'flowbite'

const filter = ref({
  "keyword": "",
  "status": {
    "new": false,
    "assigned": false,
    "fixed": false,
    "resolved": false,
    "closed": false,
    "reopened": false,
  },
  "priority": {
    "block": false,
    "critical": false,
    "major": false,
    "minor": false,
    "trivial": false
  },
  "assignee": "",
  "reporter": "",
})

const user_list = [
  {
    "username": "hysk",
  },
  {
    "username": "khw",
  },
  {
    "username": "kss",
  },
  {
    "username": "knn",
  },
]

const issue_list = [
  {
    "issue_id": 1,
    "title": "First Issue",
    "description": "Hello world!",
    "status": "new",
    "priority": "major",
    "assignee": null,
    "reporter": "hysk",
    "reported_date": "2024-05-22 01:42:26"
  },
  {
    "issue_id": 2,
    "title": "Second Issue",
    "description": "Issue for test",
    "status": "closed",
    "priority": "critical",
    "assignee": "hysk",
    "reporter": "hysk",
    "reported_date": "2024-05-21 01:42:26"
  }
]

watch(filter.value, () => searchIssues())

const status_selected = computed(() => {
  return Object.values(filter.value.status).some(state => state)
})

const priority_selected = computed(() => {
  return Object.values(filter.value.priority).some(state => state)
})

const my_name = "hysk"

const searched_user_list = ref(user_list.filter(user => user.username !== my_name))

function searchUsers(event) {
  const keyword = event.target.value

  searched_user_list.value = user_list
      .filter(user => user.username !== my_name)
      .filter(user => user.username.toLowerCase().includes(keyword.toLowerCase()))
}

let searchTimeout = null
function searchIssues() {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => search(), 500)
}

function search() {
  console.log("send query to server")
}

function resetFilter() {
  filter.value = {
    "keyword": "",
    "status": {
      "new": false,
      "assigned": false,
      "fixed": false,
      "resolved": false,
      "closed": false,
      "reopened": false,
    },
    "priority": {
      "block": false,
      "critical": false,
      "major": false,
      "minor": false,
      "trivial": false
    },
    "assignee": "",
    "reporter": "",
  }
}

// initialize components based on data attribute selectors
onMounted(() => {
  initFlowbite();
})
</script>