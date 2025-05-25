<template>
  <PageWrapper :loading="loading">
    <template #header>
      <el-date-picker
          v-model="timeRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DDTHH:mm:ss"
          style="margin-right: 12px"
      />
      <el-button type="primary" @click="loadStats">统计</el-button>
    </template>

    <div v-if="Object.keys(chartData).length">
      <v-chart :option="chartOption" autoresize style="height: 400px" />
    </div>
  </PageWrapper>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue'
import { caseApi } from '@/api'
import { ElMessage } from 'element-plus'
import PageWrapper from '@/components/PageWrapper.vue'
import { useUserStore } from '@/stores/user'
import VChart from 'vue-echarts'

const userStore = useUserStore()
const timeRange = ref<[string, string]>([])
const chartData = ref<Record<string, number>>({})
const loading = ref(false)

const loadStats = async () => {
  if (!timeRange.value || timeRange.value.length !== 2) {
    ElMessage.warning('请选择起止时间')
    return
  }
  loading.value = true
  try {
    const res = await caseApi.getPatientIcdStats(userStore.user?.chainAddress!, timeRange.value[0], timeRange.value[1])
    if (res.code === 0) {
      chartData.value = res.data || {}
    } else {
      ElMessage.error(res.msg)
    }
  } catch (e) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const chartOption = computed(() => {
  return {
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        name: '疾病类型',
        type: 'pie',
        radius: '60%',
        data: Object.entries(chartData.value).map(([name, value]) => ({ name, value })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
})
</script>
