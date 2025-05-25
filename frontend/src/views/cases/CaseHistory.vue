<template>
  <div class="case-history">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>病例历史版本</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-timeline v-loading="loading">
        <el-timeline-item
          v-for="version in versions"
          :key="version.id"
          :timestamp="version.createdAt"
          placement="top">
          <el-card>
            <h4>版本 {{ version.version }}</h4>
            <p class="hash">IPFS Hash: {{ version.ipfsHash }}</p>
            <el-button
              link
              type="primary"
              @click="viewVersion(version)">
              查看此版本
            </el-button>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      title="版本详情"
      width="50%">
      <el-descriptions
        v-if="selectedVersion"
        :column="2"
        border>
        <el-descriptions-item label="版本号">
          {{ selectedVersion.version }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ selectedVersion.createdAt }}
        </el-descriptions-item>
        <el-descriptions-item label="IPFS Hash" :span="2">
          {{ selectedVersion.ipfsHash }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { caseApi } from '../../api'
import type { CaseVersion } from '../../types'

const route = useRoute()
const loading = ref(false)
const versions = ref<CaseVersion[]>([])
const dialogVisible = ref(false)
const selectedVersion = ref<CaseVersion | null>(null)

const loadVersions = async () => {
  const caseId = route.params.id as string
  if (!caseId) {
    ElMessage.error('无效的病例ID')
    return
  }

  loading.value = true
  try {
    const res = await caseApi.viewHistory(caseId)
    if (res.code === 200 && res.data) {
      versions.value = res.data
    } else {
      ElMessage.error(res.msg || '获取历史版本失败')
    }
  } catch (error) {
    console.error('获取历史版本失败:', error)
    ElMessage.error('获取历史版本失败')
  } finally {
    loading.value = false
  }
}

const viewVersion = (version: CaseVersion) => {
  selectedVersion.value = version
  dialogVisible.value = true
}

onMounted(() => {
  loadVersions()
})
</script>

<style scoped>
.case-history {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hash {
  color: #909399;
  font-family: monospace;
  margin: 10px 0;
}

:deep(.el-timeline-item__node) {
  background-color: #409EFF;
}

:deep(.el-timeline-item__tail) {
  border-left-color: #E4E7ED;
}
</style> 