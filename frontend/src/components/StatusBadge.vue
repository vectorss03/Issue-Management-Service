<template>
  <span v-if="status === `NEW`" class="bg-blue-100 text-blue-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">New</span>
  <span v-if="status === `ASSIGNED`" class="bg-yellow-100 text-yellow-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Assigned</span>
  <span v-if="status === `FIXED`" class="bg-pink-100 text-pink-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Fixed</span>
  <span v-if="status === `RESOLVED`" class="bg-green-100 text-green-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full0">Resolved</span>
  <span v-if="status === `CLOSED`" class="bg-gray-100 text-gray-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Closed</span>
  <span v-if="status === `REOPENED`" class="bg-cyan-100 text-cyan-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Reopened</span>
</template>

<script setup>
// eslint-disable-next-line
const props = defineProps({
  status: String
})
</script>