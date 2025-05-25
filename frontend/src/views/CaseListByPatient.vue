<template>
  <PageWrapper :loading="loading">
    <!-- 顶部筛选栏 -->
    <template #header>
      <el-input v-model="address" placeholder="患者地址" style="width: 300px" />
      <el-date-picker
          v-model="timeRange"
          type="datetimerange"
          value-format="YYYY-MM-DDTHH:mm:ss"
          style="margin-left: 10px"
      />
      <el-button type="primary" @click="loadCases">查询</el-button>
    </template>

    <!-- 表格主体 -->
    <el-table :data="caseList" border>
      <el-table-column prop="caseId" label="Case ID" />
      <el-table-column prop="icdCode" label="ICD编码" />
      <el-table-column prop="visitTime" label="就诊时间" />
      <el-table-column prop="doctorAddress" label="医生地址" />
      <el-table-column prop="ipfsHash" label="IPFS哈希" />
    </el-table>
  </PageWrapper>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { caseApi } from '@/api'
import type { CaseDetail } from '@/types'
import PageWrapper from '@/components/PageWrapper.vue'
import { ElMessage } from 'element-plus'

const address = ref('')
const timeRange = ref<[Date, Date] | null>(null)
const caseList = ref<CaseDetail[]>([])
const loading = ref(false)

const loadCases = async () => {
  if (!address.value || !timeRange.value) {
    ElMessage.warning('请填写完整信息')
    return
  }
  const [from, to] = timeRange.value
  loading.value = true
  try {
    const res = await caseApi.getCasesByPatientAndTime(
        address.value,
        from.toISOString(),
        to.toISOString()
    )
    caseList.value = res.data ?? []
  } catch (e) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}
</script>
