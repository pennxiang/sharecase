<template>
  <el-card>
    <div class="toolbar">
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-right: 12px"
      />
      <el-button type="primary" @click="loadStats">统计</el-button>
    </div>

    <BarChart :title="'ICD统计分布'" :data="stats" />
  </el-card>
</template>

<script setup lang="ts">
import { caseApi } from '@/api'
import BarChart from '@/components/BarChart.vue'
import { onMounted, ref } from 'vue'

const stats = ref([])
const dateRange = ref<[Date, Date] | null>(null)

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
    return
  }

  if (!dateRange.value) return
  const [from, to] = dateRange.value.map(d => d.toISOString())

  const res = await caseApi.getIcdStatsByTimeRange(from, to)
  if (res.code === 200) {
    stats.value = Object.entries(res.data).map(([code, count]) => ({
      name: code,
      value: count
    }))
  }
}

onMounted(() => {
  const now = new Date()
  const oneMonthAgo = new Date()
  oneMonthAgo.setMonth(now.getMonth() - 1)
  dateRange.value = [oneMonthAgo, now]
  loadStats()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
</style>
