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
      <el-button type="primary" @click="loadStats">刷新</el-button>
    </div>

    <BarChart :title="'我的 ICD 分布'" :data="stats" style="margin-top: 20px" />
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { caseApi } from '@/api'
import { useUserStore } from '@/store/user'
import BarChart from '@/components/BarChart.vue'
import {format} from "date-fns";

const userStore = useUserStore()
const stats = ref<{ name: string, value: number }[]>([])
const dateRange = ref<[Date, Date] | null>(null)

const loadStats = async () => {
  const address = userStore.user?.chainAddress
  if (!address) {
    ElMessage.warning('未登录，无法获取统计')
    return
  }

  const [from, to] = dateRange.value || [new Date('2020-01-01'), new Date('2099-12-31')]
  const fromStr = format(from, "yyyy-MM-dd'T'HH:mm:ss")
  const toStr = format(to, "yyyy-MM-dd'T'HH:mm:ss")

  try {
    const res = await caseApi.getPatientIcdStats(address, fromStr, toStr)
    // console.log('后端响应:', res)
    if (res.code === 0 || res.code === 200) {
      stats.value = Object.entries(res.data).map(([code, count]) => ({
        name: code,
        value: count as number
      }))
    } else {
      ElMessage.error(res.msg || '统计失败')
    }
  } catch (err) {
    // console.error('统计接口异常:', err)
    ElMessage.error('统计接口异常')
  }
}

onMounted(loadStats)
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
</style>
