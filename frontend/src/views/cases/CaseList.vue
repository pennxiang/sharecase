<template>
  <div class="case-list">
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="身份证号">
          <el-input v-model="searchForm.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="ICD编码">
          <el-input v-model="searchForm.icdCode" placeholder="请输入ICD编码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table
      v-loading="loading"
      :data="caseList"
      border
      style="width: 100%">
      <el-table-column prop="patientId" label="患者ID" width="100" />
      <el-table-column prop="icdCode" label="ICD编码" width="120" />
      <el-table-column prop="diagnosis" label="诊断" />
      <el-table-column prop="treatment" label="治疗方案" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button
            link
            type="primary"
            @click="viewDetail(row)">
            查看详情
          </el-button>
          <el-button
            link
            type="primary"
            @click="viewHistory(row)">
            历史版本
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { caseApi } from '../../api'
import type { CaseDetail } from '../../types'

const router = useRouter()
const loading = ref(false)
const caseList = ref<CaseDetail[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  idCard: '',
  icdCode: ''
})

const loadCases = async () => {
  loading.value = true
  try {
    // 这里需要根据实际API调整
    const res = await caseApi.getDetail(1, searchForm.icdCode)
    if (res.code === 200 && res.data) {
      // 假设返回的是数组，实际需要根据API调整
      caseList.value = Array.isArray(res.data) ? res.data : [res.data]
      total.value = caseList.value.length
    } else {
      ElMessage.error(res.msg || '获取病例列表失败')
    }
  } catch (error) {
    console.error('获取病例列表失败:', error)
    ElMessage.error('获取病例列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadCases()
}

const resetSearch = () => {
  searchForm.idCard = ''
  searchForm.icdCode = ''
  handleSearch()
}

const viewDetail = (row: CaseDetail) => {
  router.push(`/dashboard/case/${row.id}`)
}

const viewHistory = (row: CaseDetail) => {
  router.push(`/dashboard/case/history/${row.id}`)
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadCases()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadCases()
}

onMounted(() => {
  loadCases()
})
</script>

<style scoped>
.case-list {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 