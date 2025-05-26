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
          style="width: 300px; margin-right: 12px"
      />

      <!-- 搜索框占满中间空间 -->
      <el-input
          v-model="keyword"
          placeholder="搜索关键词"
          @keyup.enter="handleFilter"
          style="flex: 1; margin-right: 12px"
          clearable
      />

      <!-- 筛选按钮 -->
      <el-button type="primary" @click="handleFilter">筛选</el-button>

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
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewCase(row)">
            查看
          </el-button>
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
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { format } from 'date-fns'
import { useUserStore } from '@/store/user'
import { getMyCases } from '@/services/caseApi'

const router = useRouter()
const userStore = useUserStore()
const isDoctor = userStore.user?.role === 'doctor'

const cases = ref([])
const dateRange = ref<[Date, Date] | null>(null)
const currentPage = ref(1)
const pageSize = 15

const total = ref(0)
const cases = ref<any[]>()

const casesAll = ref([])
const keyword = ref('')

const formatTime = (timestamp: number) =>
    format(new Date(timestamp), 'yyyy-MM-dd HH:mm')

const paginate = () => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  cases.value = casesAll.value.slice(start, end)
}

const viewCase = (row: any) => {
  const hash = row.ipfsHash
  if (!hash) {
    ElMessage.warning('该病例未上传 PDF')
    return
  }
  window.open(`http://127.0.0.1:8080/ipfs/${hash}`, '_blank')
}

function generateMockCases(count = 100) {
  const icdCodes = ['I10', 'E11', 'J45', 'C34', 'K35']
  const doctors = ['0xDOCTOR001', '0xDOCTOR002']
  const patients = ['0xPATIENT001', '0xPATIENT002', '0xPATIENT003']
  const list = []

  for (let i = 1; i <= count; i++) {
    const caseId = `CASE-${i.toString().padStart(3, '0')}`
    const icdCode = icdCodes[Math.floor(Math.random() * icdCodes.length)]
    const ipfsHash = i === 1 ? 'QmPAWWMGVqjy7oymZXtMjFVi5bGnQ7ekkVMxNwduesnB3X' : null
    const doctor = doctors[Math.floor(Math.random() * doctors.length)]
    const patient = patients[Math.floor(Math.random() * patients.length)]
    const visitTime = new Date(Date.now() - Math.random() * 10000000000).getTime()
    list.push({ caseId, icdCode, ipfsHash, doctor, patient, visitTime })
  }

  return list
}

const handleFilter = () => {
  const hasKeyword = keyword.value.trim() !== ''
  const hasDateRange = !!dateRange.value

  let filtered = generateMockCases(100)

  if (hasDateRange) {
    const [from, to] = dateRange.value!
    filtered = filtered.filter(c => c.visitTime >= from.getTime() && c.visitTime <= to.getTime())
  }

  if (hasKeyword) {
    const kw = keyword.value.trim().toLowerCase()
    filtered = filtered.filter(c =>
        c.caseId.toLowerCase().includes(kw) ||
        c.icdCode.toLowerCase().includes(kw)
    )
  }

  casesAll.value = filtered
  currentPage.value = 1
  paginate()
}

onMounted(() => {
  if (import.meta.env.DEV) {
    casesAll.value = generateMockCases(100)
    paginate()
  } else {
    // 真实环境按需加载
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
