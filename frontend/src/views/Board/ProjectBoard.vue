<template>
  <div>
    <project-side-bar />
    <router-view class="p-4 sm:ml-64 pt-20"/>
  </div>
</template>


<script setup>
import ProjectSideBar from "@/components/ProjectSideBar.vue";
</script>