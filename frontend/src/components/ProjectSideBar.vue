<template>
  <aside id="logo-sidebar" class="fixed left-0 z-24 w-64 pt-20 h-screen transition-transform -translate-x-full bg-white border-r border-gray-200 sm:translate-x-0" aria-label="Sidebar">
    <div class="h-auto px-3 pb-4 overflow-y-auto bg-white">
      <ul class="space-y-2">
        <li>
          <h1 class="flex items-center p-2 text-gray-900 font-bold text-sm mb-3">My Project</h1>
        </li>
        <li>
          <router-link :to="{ name: 'home' }" replace class="flex items-center p-2 text-gray-900 rounded hover:bg-gray-100 group">
            <svg class="w-[24px] h-[24px] text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
              <path fill-rule="evenodd" d="M11.293 3.293a1 1 0 0 1 1.414 0l6 6 2 2a1 1 0 0 1-1.414 1.414L19 12.414V19a2 2 0 0 1-2 2h-3a1 1 0 0 1-1-1v-3h-2v3a1 1 0 0 1-1 1H7a2 2 0 0 1-2-2v-6.586l-.293.293a1 1 0 0 1-1.414-1.414l2-2 6-6Z" clip-rule="evenodd"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Home</span>
          </router-link>
        </li>
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
        <li>
          <router-link :to="{ name: 'users' }" replace class="flex items-center p-2 text-gray-900 rounded hover:bg-gray-100 group">
            <svg class="w-[24px] h-[24px] text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
              <path fill-rule="evenodd" d="M8 4a4 4 0 1 0 0 8 4 4 0 0 0 0-8Zm-2 9a4 4 0 0 0-4 4v1a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2v-1a4 4 0 0 0-4-4H6Zm7.25-2.095c.478-.86.75-1.85.75-2.905a5.973 5.973 0 0 0-.75-2.906 4 4 0 1 1 0 5.811ZM15.466 20c.34-.588.535-1.271.535-2v-1a5.978 5.978 0 0 0-1.528-4H18a4 4 0 0 1 4 4v1a2 2 0 0 1-2 2h-4.535Z" clip-rule="evenodd"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Users</span>
          </router-link>
        </li>
      </ul>
    </div>
  </aside>
</template>

<script setup>
</script>