<template>
  <div class="case-list-page">
    <div class="toolbar">
      <!-- 日期选择器放左 -->
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 150px; margin-right: 12px"
      />

      <!-- 搜索框占满中间空间 -->
      <el-input
          v-model="keyword"
          placeholder="搜索关键词"
          @keyup.enter="searchCases"
          style="flex: 1; margin-right: 12px"
          clearable
      />

      <!-- 筛选按钮 -->
      <el-button type="primary" @click="filterByTime">筛选</el-button>

      <!-- 创建按钮右对齐 -->
      <el-button
          type="success"
          icon="Plus"
          class="create-btn"
          v-if="isDoctor"
          @click="router.push('/cases/create')"
      >
        创建病例
      </el-button>
    </div>

    <el-table :data="cases" border style="margin-top: 20px">
      <el-table-column prop="caseId" label="病例 ID" />
      <el-table-column prop="icdCode" label="ICD 编码" />
      <el-table-column prop="doctor" label="医生地址" />
      <el-table-column prop="patient" label="患者地址" />
      <el-table-column prop="visitTime" label="就诊时间">
        <template #default="{ row }">
          {{ formatTime(row.visitTime) }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        style="margin-top: 20px; text-align: right"
        layout="prev, pager, next"
        :total="casesAll.length"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="paginate"
    />
  </div>
</template>

<script setup lang="ts">
import { caseApi } from '@/api'
import { useUserStore } from '@/store/user'
import { format } from 'date-fns'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'

const router = useRouter()
const userStore = useUserStore()
const isDoctor = userStore.user?.role === 'doctor'

const cases = ref([])              // 当前页展示数据
const casesAll = ref([])           // 全部数据（mock 或后端）
const keyword = ref('')
const dateRange = ref<[Date, Date] | null>(null)

const currentPage = ref(1)
const pageSize = 15

const formatTime = (timestamp: number) =>
    format(new Date(timestamp), 'yyyy-MM-dd HH:mm')

const paginate = () => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  cases.value = casesAll.value.slice(start, end)
}

const searchCases = async () => {
  if (!keyword.value) {
    currentPage.value = 1
    paginate()
    return
  }
  const res = await caseApi.searchCases(keyword.value)
  if (res.code === 200) {
    casesAll.value = res.data
    currentPage.value = 1
    paginate()
  } else {
    ElMessage.error(res.msg)
  }
}

const filterByTime = async () => {
  if (!dateRange.value) return
  const [from, to] = dateRange.value.map(d => d.toISOString())
  const res = await caseApi.getCasesByTimeRange(from, to)
  if (res.code === 200) {
    casesAll.value = res.data
    currentPage.value = 1
    paginate()
  } else {
    ElMessage.error(res.msg)
  }
}

function generateMockCases(count = 100) {
  const icdCodes = ['I10', 'E11', 'J45', 'C34', 'K35']
  const doctors = ['0xDOCTOR001', '0xDOCTOR002']
  const patients = ['0xPATIENT001', '0xPATIENT002', '0xPATIENT003']
  const list = []

  for (let i = 1; i <= count; i++) {
    const caseId = `CASE-${i.toString().padStart(3, '0')}`
    const icdCode = icdCodes[Math.floor(Math.random() * icdCodes.length)]
    const doctor = doctors[Math.floor(Math.random() * doctors.length)]
    const patient = patients[Math.floor(Math.random() * patients.length)]
    const visitTime = new Date(Date.now() - Math.random() * 10000000000).getTime()
    list.push({ caseId, icdCode, doctor, patient, visitTime })
  }

  return list
}

const loadAllCases = async () => {
  const res = await caseApi.getCasesByTimeRange('', '')
  if (res.code === 200) {
    casesAll.value = res.data
    paginate()
  }
}

onMounted(() => {
  if (import.meta.env.DEV) {
    casesAll.value = generateMockCases(100)
    paginate()
  } else {
    loadAllCases()
  }
})
</script>

<style scoped>
.case-list-page {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
}

.create-btn {
  margin-left: auto;
}
</style>
