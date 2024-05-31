<template>
  <div>
    <div class="flex pl-72">
      <Bar class="mr-64" :width="700" :height="500" :options="statistics.dailyIssueReported.options"
           :data="statistics.dailyIssueReported.data"/>
      <Bar class="" :width="700" :height="500" :options="statistics.monthlyIssueReported.options"
           :data="statistics.monthlyIssueReported.data"/>
    </div>

    <div class="flex pl-60 pt-16">
      <Doughnut class="ml-24 mr-96" :width="600" :height="600" :options="statistics.currentIssueStatus.options"
                :data="statistics.currentIssueStatus.data"/>
      <Doughnut class="" :width="600" :height="600" :options="statistics.currentIssuePriority.options"
                :data="statistics.currentIssuePriority.data"/>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import {useRoute} from "vue-router";
import {computed, onMounted, ref} from "vue";
import {Bar, Doughnut} from 'vue-chartjs'
import {CategoryScale} from 'chart.js';
import Chart from 'chart.js/auto';

const dailyLabel = ref([])
const dailyData = ref([])
const monthlyLabel = ref([])
const monthlyData = ref([])
const statusData = ref([])
const priorityData = ref([])

Chart.register(CategoryScale);

const route = useRoute()

const statistics = computed(() => {
  return {
    dailyIssueReported: {
      data: {
        labels: dailyLabel.value,
        datasets: [
          {
            label: '# Issue Reported',
            data: dailyData.value,
            borderColor: 'rgba(255,99,132,1)',
            backgroundColor: 'rgba(255,99,132,0.5)',
          }
        ]
      },
      options: {
        responsive: false,
        plugins: {
          title: {
            display: true,
            text: 'Daily Issue Reported'
          }
        }
      }
    },

    monthlyIssueReported: {
      data: {
        labels: monthlyLabel.value,
        datasets: [
          {
            label: '# Issue Reported',
            data: monthlyData.value,
            borderColor: 'rgba(54, 162, 235, 1)',
            backgroundColor: 'rgba(54, 162, 235, 0.5)',
          }
        ]
      },
      options: {
        responsive: false,
        plugins: {
          title: {
            display: true,
            text: 'Monthly Issue Reported'
          }
        }
      }
    },

    currentIssueStatus:
        {
          data: {
            labels: ['New', 'Assigned', 'Fixed', 'Resolved', 'Closed', 'Reopened'],
            datasets:
                [{
                  data: statusData.value,
                  backgroundColor: ['rgb(255, 99, 132)', 'rgb(255, 159, 64)', 'rgb(54, 162, 235)', 'rgb(75, 192, 192)', 'rgb(201, 203, 207)', 'rgb(255, 205, 86)']
                }]
          }
          ,
          options: {
            responsive: false,
            plugins:
                {
                  title: {
                    display: true,
                    text:
                        'Current Issue Status'
                  }
                }
          }
        }
    ,

    currentIssuePriority: {
      data: {
        labels: ['Blocker', 'Critical', 'Major', 'Minor', 'Trivial'],
        datasets:
            [{
              data: priorityData.value,
              backgroundColor: ['rgb(255, 99, 132)', 'rgb(255, 159, 64)', 'rgb(255, 205, 86)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)']
            }]
      }
      ,
      options: {
        responsive: false,
        plugins:
            {
              title: {
                display: true,
                text:
                    'Current Issue Priority'
              }
            }
      }
    }
  }
})


function getStatistics() {
  axios.get('/api/projects/' + route.params.project_id + '/statistics')
      .then(response => {
        console.log(response)
        const daily = response.data.dailyIssueCount
        dailyLabel.value = Object.keys(daily).sort()
        dailyData.value = dailyLabel.value.map(day => daily[day])

        const monthly = response.data.monthlyIssueCount
        monthlyLabel.value = Object.keys(monthly).sort()
        monthlyData.value = monthlyLabel.value.map(month => monthly[month])

        const status = response.data.statusCount
        statusData.value = [status.NEW, status.ASSIGNED, status.FIXED, status.RESOLVED, status.CLOSED, status.REOPENED]

        const priority = response.data.priorityCount
        priorityData.value = [priority.BLOCKER, priority.CRITICAL, priority.MAJOR, priority.MINOR, priority.TRIVIAL]
      })
      .catch(error => {
        console.log(error)
        if (error.message.indexOf('Network Error') > -1) {
          alert('Network Error\nPlease Try again later')
        }
      })
}

onMounted(() => {
  getStatistics()


})
</script>