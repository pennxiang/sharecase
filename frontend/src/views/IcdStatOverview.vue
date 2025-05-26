<template>
  <el-card>
    <BarChart :title="'ICD统计分布'" :data="stats" />
  </el-card>
</template>

<script setup lang="ts">
import { caseApi } from '@/api'
import BarChart from '@/components/BarChart.vue'
import { onMounted, ref } from 'vue'

const stats = ref([])

function generateMockIcdStats() {
  return [
    { name: 'I10', value: 32 },
    { name: 'E11', value: 28 },
    { name: 'J45', value: 21 },
    { name: 'C34', value: 17 },
    { name: 'K35', value: 13 }
  ]
}

const loadStats = async () => {
  if (import.meta.env.DEV) {
    stats.value = generateMockIcdStats()
  } else {
    const res = await caseApi.getGlobalIcdFrequency()
    if (res.code === 200) {
      stats.value = res.data.map(i => ({
        name: i.icdCode,
        value: i.count
      }))
    }
  }
}

onMounted(loadStats)
</script>
