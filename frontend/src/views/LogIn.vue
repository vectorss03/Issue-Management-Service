<template>
  <div class="mx-auto max-w-screen-xl px-4 lg:px-12 m-7">
    <h1 class="font-bold text-2xl mx-auto max-w-sm m-5 text-center">Log In</h1>

    <form class="max-w-sm mx-auto px-3 py-5 rounded-lg border" v-on:submit.prevent="login">
      <div class="mb-5">
        <input type="text" v-model="loginForm.username" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="Username" required />
      </div>
      <div class="mb-5">
        <input type="password" v-model="loginForm.password" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="Password" required />
      </div>
<!--      <div class="flex items-start mb-5">-->
<!--        <div class="flex items-center h-5">-->
<!--          <input id="remember" type="checkbox" value="" class="w-4 h-4 border border-gray-300 rounded bg-gray-50 focus:ring-3 focus:ring-blue-300 dark:bg-gray-700 dark:border-gray-600 dark:focus:ring-blue-600 dark:ring-offset-gray-800 dark:focus:ring-offset-gray-800" required />-->
<!--        </div>-->
<!--        <label for="remember" class="ms-2 text-sm font-medium text-gray-900 dark:text-gray-300">Remember me</label>-->
<!--      </div>-->
      <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:outline-none font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Submit</button>
    </form>
  </div>
</template>

<script setup>
import axios from "axios";
import router from "@/router";
import store from "@/vuex/store";

const loginForm = {
  "username": "",
  "password": ""
}

function login() {
  axios.get("/api/login", {
    params: {
      "username": loginForm.username,
      "password": loginForm.password
    }
  }).then(response => {
    console.log(response)
    // loginForm.username = ""
    // loginForm.password = ""
    store.commit("setUsername", loginForm.username)
    router.push(sessionStorage.getItem("FROM_URL"))
  }).catch(error => {
    console.log(error)
    alert("login failed")
  })
}
</script>