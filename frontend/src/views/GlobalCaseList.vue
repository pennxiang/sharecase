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
      <el-button type="primary" @click="loadCases">筛选</el-button>
    </template>

    <el-table :data="caseList" border stripe>
      <el-table-column prop="caseId" label="病例ID" width="180" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="icdCode" label="ICD编码" width="100" />
      <el-table-column prop="visitTime" label="就诊时间" width="180" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="text" @click="preview(scope.row.ipfsHash)">查看PDF</el-button>
        </template>
      </el-table-column>
    </el-table>
  </PageWrapper>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageWrapper from '@/components/PageWrapper.vue'
import { caseApi } from '@/api'
import type { CaseDetail } from '@/types'

const timeRange = ref<[string, string]>([])
const caseList = ref<CaseDetail[]>([])
const loading = ref(false)

const loadCases = async () => {
  if (!timeRange.value || timeRange.value.length !== 2) {
    ElMessage.warning('请选择时间范围')
    return
  }
  loading.value = true
  try {
    const res = await caseApi.getCasesByTimeRange(timeRange.value[0], timeRange.value[1])
    if (res.code === 0) {
      caseList.value = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  } catch (e) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const preview = (hash: string) => {
  if (!hash) return
  window.open(`https://ipfs.io/ipfs/${hash}`, '_blank')
}
</script>
