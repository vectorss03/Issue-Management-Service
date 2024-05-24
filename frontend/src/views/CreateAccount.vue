<template>
  <div class="mx-auto max-w-screen-xl px-4 lg:px-12 m-7">
    <h1 class="font-bold text-2xl mx-auto max-w-sm m-5 text-center">Create Account</h1>

    <form class="max-w-sm mx-auto px-3 py-5 rounded-lg border" v-on:submit.prevent="createAccount">
      <div class="mb-6">
        <label class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Username</label>
        <input v-model="createAccountForm.username" type="text" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full p-2.5" placeholder="username" required />
      </div>

      <div class="mb-6">
        <label class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
        <input v-model="createAccountForm.password" type="password" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full p-2.5" placeholder="•••••••••" required />
      </div>
<!--      <div class="mb-6">-->
<!--        <label for="confirm_password" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Confirm password</label>-->
<!--        <input type="password" id="confirm_password" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full p-2.5" placeholder="•••••••••" required />-->
<!--      </div>-->

      <label class="block mb-2 text-sm font-medium text-gray-900">Email</label>
      <div class="relative mb-6">
        <div class="absolute inset-y-0 start-0 flex items-center ps-3.5 pointer-events-none">
          <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 16">
            <path d="m10.036 8.278 9.258-7.79A1.979 1.979 0 0 0 18 0H2A1.987 1.987 0 0 0 .641.541l9.395 7.737Z"/>
            <path d="M11.241 9.817c-.36.275-.801.425-1.255.427-.428 0-.845-.138-1.187-.395L0 2.6V14a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V2.5l-8.759 7.317Z"/>
          </svg>
        </div>
        <input v-model="createAccountForm.email" type="email" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full ps-10 p-2.5" placeholder="your_email@mail.com" required />
      </div>
      <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:outline-none font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Create Account</button>
    </form>
  </div>
</template>

<script setup>
import axios from "axios";
import router from "@/router";

const createAccountForm = {
  "username": "",
  "password": "",
  "email": "",
}

function createAccount() {
  axios.get("/api/account", {
    params: {
      "username": createAccountForm.username,
      "password": createAccountForm.password,
      "email": createAccountForm.email
    }
  }).then(response => {
    console.log(response)
    alert("account created successfully")
    router.push(sessionStorage.getItem("FROM_URL"))
  }).catch(error => {
    console.log(error)
    alert("create account failed")
  })
}
</script>