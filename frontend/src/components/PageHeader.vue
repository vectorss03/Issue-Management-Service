<template>
  <nav
      class="bg-white dark:bg-gray-900 fixed w-full z-20 top-0 start-0 border-gray-200 px-4 lg:px-6 py-2.5 border-b-2">
    <div class="flex flex-wrap justify-between items-center mx-auto">
      <router-link to="/" class="flex items-center">
        <img src="https://flowbite.com/docs/images/logo.svg" class="mr-3 h-6 sm:h-9" alt="Flowbite Logo"/>
        <span class="self-center text-xl font-semibold whitespace-nowrap">Issue Management System</span>
      </router-link>
      <div class="flex items-center lg:order-2" v-if="!isLoggedIn">
        <router-link to="/login" class="text-gray-800 hover:bg-gray-50 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 focus:outline-none">
          Log in
        </router-link>
        <router-link to="/account" class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 focus:outline-none">
          Create Account
        </router-link>
      </div>
      <div class="flex items-center lg:order-2" v-else>
        <p class="text-gray-800 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 focus:outline-none">{{getUsername}}</p>
        <button type="button" @click="logout" class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 focus:outline-none">
          Log Out
        </button>
      </div>
    </div>
  </nav>

</template>

<script setup>
import store from "@/vuex/store";
import {computed} from "vue";
import axios from "axios";
import router from "@/router";

const isLoggedIn = computed(() => {
  return store.getters.isLoggedIn
});

const getUsername = computed(() => {
  return store.state.username
});

function logout() {
  axios.get("/api/logout", {
  }).then(response => {
    console.log(response)
    // loginForm.username = ""
    // loginForm.password = ""
    store.commit("setUsername", '')
    alert("logout")
    router.go(0)
  }).catch(error => {
    console.log(error)
    alert("logout failed")
  })
}
</script>