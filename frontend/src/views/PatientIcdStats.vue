<template>
  <el-card>
    <BarChart :title="'我的ICD分布'" :data="stats" />
  </el-card>
</template>

<script setup lang="ts">
import { caseApi } from '@/api'
import { useUserStore } from '@/store/user'
import BarChart from '@/components/BarChart.vue'
import { onMounted, ref } from 'vue'

const stats = ref([])
const userStore = useUserStore()

function generateMockPatientIcdStats() {
  return {
    I10: 8,
    E11: 5,
    J45: 3,
    C34: 2,
    K35: 1
  }
}

const loadStats = async () => {
  if (import.meta.env.DEV) {
    const mock = generateMockPatientIcdStats()
    stats.value = Object.entries(mock).map(([code, count]) => ({
      name: code,
      value: count
    }))
  } else {
    const res = await caseApi.getPatientIcdStats(
        userStore.user?.blockchainAddress!,
        '2020-01-01',
        '2099-12-31'
    )
    if (res.code === 200) {
      stats.value = Object.entries(res.data).map(
          ([code, count]) => ({ name: code, value: count })
      )
    }
  }
}

onMounted(loadStats)
</script>
