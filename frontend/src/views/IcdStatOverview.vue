<template>
  <PageWrapper :loading="loading">
    <!-- 顶部筛选栏 -->
    <template #header>
      <el-date-picker
          v-model="timeRange"
          type="datetimerange"
          value-format="YYYY-MM-DDTHH:mm:ss"
          start-placeholder="起始时间"
          end-placeholder="结束时间"
      />
      <el-button type="primary" @click="loadStats" style="margin-left: 10px">查询统计</el-button>
    </template>

    <!-- 表格结果 -->
    <el-table :data="statList" border>
      <el-table-column prop="icdCode" label="ICD 编码" />
      <el-table-column prop="count" label="出现次数" />
    </el-table>
  </PageWrapper>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { caseApi } from '@/api'
import PageWrapper from '@/components/PageWrapper.vue'
import { ElMessage } from 'element-plus'

interface IcdStatRow {
  icdCode: string
  count: number
}

const timeRange = ref<[Date, Date] | null>(null)
const statList = ref<IcdStatRow[]>([])
const loading = ref(false)

const loadStats = async () => {
  if (!timeRange.value) {
    ElMessage.warning('请选择时间范围')
    return
  }

  const [from, to] = timeRange.value
  loading.value = true
  try {
    const res = await caseApi.getIcdStatsByTimeRange(
        from.toISOString(),
        to.toISOString()
    )
    if (res.code === 0) {
      statList.value = Object.entries(res.data || {}).map(([icdCode, count]) => ({
        icdCode,
        count: Number(count)
      }))
    } else {
      ElMessage.error(res.msg)
    }
  } catch (e) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}
</script>
