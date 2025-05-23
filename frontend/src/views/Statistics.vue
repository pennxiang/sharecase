<template>
  <div class="statistics">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>疾病统计分析</span>
          <div class="date-range">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :shortcuts="shortcuts"
              value-format="YYYY-MM-DD"
              @change="handleDateChange" />
            <el-switch
              v-model="isAsc"
              active-text="升序"
              inactive-text="降序"
              @change="loadData" />
          </div>
        </div>
      </template>

      <div v-loading="loading" class="charts-container">
        <div ref="barChartRef" class="chart"></div>
        <div ref="pieChartRef" class="chart"></div>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import type { EChartsType } from 'echarts'
import { caseApi } from '../api'
import type { HotDiseaseDTO } from '../types'

const loading = ref(false)
const isAsc = ref(false)
const dateRange = ref<[string, string]>(['', ''])
let barChart: EChartsType | null = null
let pieChart: EChartsType | null = null

const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    }
  }
]

const initCharts = () => {
  const barChartDom = document.querySelector('.chart') as HTMLElement
  const pieChartDom = document.querySelectorAll('.chart')[1] as HTMLElement
  
  barChart = echarts.init(barChartDom)
  pieChart = echarts.init(pieChartDom)
  
  window.addEventListener('resize', handleResize)
}

const handleResize = () => {
  barChart?.resize()
  pieChart?.resize()
}

const updateCharts = (data: HotDiseaseDTO[]) => {
  // 柱状图配置
  barChart?.setOption({
    title: {
      text: '疾病分布统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.diseaseName),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '病例数'
    },
    series: [
      {
        name: '病例数',
        type: 'bar',
        data: data.map(item => item.count),
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  })

  // 饼图配置
  pieChart?.setOption({
    title: {
      text: '疾病占比分析',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '疾病分布',
        type: 'pie',
        radius: '50%',
        data: data.map(item => ({
          name: item.diseaseName,
          value: item.count
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })
}

const loadData = async () => {
  if (!dateRange.value[0] || !dateRange.value[1]) {
    ElMessage.warning('请选择日期范围')
    return
  }

  loading.value = true
  try {
    const res = await caseApi.getDiseaseStats(
      dateRange.value[0],
      dateRange.value[1],
      isAsc.value
    )
    if (res.code === 200 && res.data) {
      updateCharts(res.data)
    } else {
      ElMessage.error(res.msg || '获取统计数据失败')
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  } finally {
    loading.value = false
  }
}

const handleDateChange = () => {
  loadData()
}

onMounted(() => {
  initCharts()
  // 设置默认日期范围为最近一个月
  const end = new Date()
  const start = new Date()
  start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
  dateRange.value = [
    start.toISOString().split('T')[0],
    end.toISOString().split('T')[0]
  ]
  loadData()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  barChart?.dispose()
  pieChart?.dispose()
})
</script>

<style scoped>
.statistics {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.date-range {
  display: flex;
  align-items: center;
  gap: 20px;
}

.charts-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
}

.chart {
  flex: 1;
  min-width: 400px;
  height: 400px;
}
</style> 