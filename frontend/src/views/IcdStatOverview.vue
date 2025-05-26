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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { caseApi } from '@/api'
import BarChart from '@/components/BarChart.vue'

const stats = ref<{ name: string; value: number }[]>([])
const dateRange = ref<[Date, Date] | null>(null)

const loadStats = async () => {
  if (!dateRange.value) {
    ElMessage.warning('请选择日期范围')
    return
  }

  const [from, to] = dateRange.value.map(d =>
      d.toISOString().split('.')[0] // => "2024-05-01T00:00:00"
  )

  try {
    const res = await caseApi.getIcdStatsByTimeRange(from, to)
    if (res.code === 0 || res.code === 200) {
      stats.value = Object.entries(res.data).map(([code, count]) => ({
        name: code,
        value: count as number
      }))
    } else {
      ElMessage.error(res.msg || '统计失败')
    }
  } catch (err) {
    ElMessage.error('接口异常')
  }
}

// 默认加载最近一个月的数据
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
