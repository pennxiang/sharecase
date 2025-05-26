<template>
  <div ref="chartRef" style="width: 100%; height: 400px"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import { onMounted, watch, ref, onBeforeUnmount } from 'vue'

interface Props {
  data: { name: string; value: number }[]
  title?: string
}

const props = defineProps<Props>()
const chartRef = ref<HTMLDivElement | null>(null)
let chartInstance: echarts.ECharts | null = null

const initChart = () => {
  if (!chartRef.value) return

  chartInstance = echarts.init(chartRef.value)

  const option: echarts.EChartsOption = {
    title: {
      text: props.title || '',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: props.data.map(item => item.name),
      axisTick: { alignWithLabel: true }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '数量',
        type: 'bar',
        data: props.data.map(item => item.value),
        itemStyle: {
          borderRadius: [4, 4, 0, 0]
        }
      }
    ]
  }

  chartInstance.setOption(option)
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

watch(() => props.data, () => {
  chartInstance?.dispose()
  initChart()
}, { deep: true })

const resizeChart = () => {
  chartInstance?.resize()
}

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  chartInstance?.dispose()
})
</script>
