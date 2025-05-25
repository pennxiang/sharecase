<template>
  <PageWrapper :loading="loading">
    <template #header>
      <el-input
          v-model="icdCode"
          placeholder="请输入 ICD 编码（如 I10）"
          style="width: 200px; margin-right: 12px"
      />
      <el-button type="primary" @click="search">查询</el-button>
    </template>

    <el-table :data="caseList" border stripe>
      <el-table-column prop="caseId" label="病例ID" width="180" />
      <el-table-column prop="title" label="标题" />
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
import { useUserStore } from '@/stores/user'
import type { CaseInfo } from '@/types'

const icdCode = ref('')
const caseList = ref<CaseInfo[]>([])
const loading = ref(false)
const userStore = useUserStore()

const search = async () => {
  if (!icdCode.value.trim()) {
    ElMessage.warning('请输入 ICD 编码')
    return
  }
  loading.value = true
  try {
    const res = await caseApi.getCasesByIcd(userStore.user?.chainAddress || '', icdCode.value.trim())
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
