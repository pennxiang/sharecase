<template>
  <div class="case-detail">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>病例详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-descriptions
        v-loading="loading"
        :column="2"
        border>
        <el-descriptions-item label="患者ID">
          {{ caseDetail.patientId }}
        </el-descriptions-item>
        <el-descriptions-item label="医生ID">
          {{ caseDetail.doctorId }}
        </el-descriptions-item>
        <el-descriptions-item label="ICD编码">
          {{ caseDetail.icdCode }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ caseDetail.createdAt }}
        </el-descriptions-item>
        <el-descriptions-item label="诊断" :span="2">
          {{ caseDetail.diagnosis }}
        </el-descriptions-item>
        <el-descriptions-item label="治疗方案" :span="2">
          {{ caseDetail.treatment }}
        </el-descriptions-item>
      </el-descriptions>

      <div class="actions">
        <el-button
          type="primary"
          @click="viewHistory">
          查看历史版本
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { caseApi } from '../../api'
import type { CaseDetail } from '../../types'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const caseDetail = ref<CaseDetail>({
  patientId: 0,
  doctorId: 0,
  icdCode: '',
  diagnosis: '',
  treatment: ''
})

const loadCaseDetail = async () => {
  const id = Number(route.params.id)
  if (!id) {
    ElMessage.error('无效的病例ID')
    return
  }

  loading.value = true
  try {
    // 这里需要根据实际API调整，可能需要传入更多参数
    const res = await caseApi.getDetail(id, '')
    if (res.code === 200 && res.data) {
      caseDetail.value = res.data
    } else {
      ElMessage.error(res.msg || '获取病例详情失败')
    }
  } catch (error) {
    console.error('获取病例详情失败:', error)
    ElMessage.error('获取病例详情失败')
  } finally {
    loading.value = false
  }
}

const viewHistory = () => {
  router.push(`/dashboard/case/history/${route.params.id}`)
}

onMounted(() => {
  loadCaseDetail()
})
</script>

<style scoped>
.case-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 