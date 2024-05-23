<template>
  <span v-if="priority === `BLOCKER`" class="bg-red-100 text-red-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Blocker</span>
  <span v-if="priority === `CRITICAL`" class="bg-orange-100 text-orange-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Critical</span>
  <span v-if="priority === `MAJOR`" class="bg-yellow-100 text-yellow-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Major</span>
  <span v-if="priority === `MINOR`" class="bg-green-100 text-green-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full0">Minor</span>
  <span v-if="priority === `TRIVIAL`" class="bg-blue-100 text-blue-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full">Trivial</span>
</template>

<script setup>
// eslint-disable-next-line
const props = defineProps({
  priority: String
})
</script>