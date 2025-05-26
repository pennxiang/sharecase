<template>
  <el-card>
    <div class="toolbar">
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-bottom: 12px"
      />
      <el-button type="primary" @click="filterByTime" style="margin-left: 12px">筛选</el-button>
      <el-button @click="resetFilter" style="margin-left: auto">重置</el-button>
    </div>

    <el-table :data="cases" border style="margin-top: 12px" @row-click="viewDetail">
      <el-table-column prop="caseId" label="病例 ID" />
      <el-table-column prop="icdCode" label="ICD 编码" />
      <el-table-column prop="visitTime" label="就诊时间">
        <template #default="{ row }">
          {{ formatTime(row.visitTime) }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        layout="prev, pager, next"
        :total="casesAll.length"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="paginate"
        style="margin-top: 16px; text-align: right"
    />
  </el-card>
</template>

<script setup lang="ts">
import { caseApi } from '@/api'
import { useUserStore } from '@/store/user'
import { format } from 'date-fns'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()
const address = userStore.user?.blockchainAddress!

const cases = ref([])       // 当前页数据
const casesAll = ref([])    // 全部病例
const dateRange = ref<[Date, Date] | null>(null)
const currentPage = ref(1)
const pageSize = 10

const formatTime = (t: number) => format(new Date(t), 'yyyy-MM-dd HH:mm')

function generateMockCasesForPatient(address: string, count = 30) {
  const icdCodes = ['I10', 'E11', 'J45', 'C34', 'K35']
  const list = []
  for (let i = 1; i <= count; i++) {
    const caseId = `MYCASE-${i.toString().padStart(3, '0')}`
    const icdCode = icdCodes[Math.floor(Math.random() * icdCodes.length)]
    const visitTime = new Date(Date.now() - Math.random() * 10000000000).getTime()
    list.push({ caseId, icdCode, patient: address, visitTime })
  }
  return list
}

const paginate = () => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  cases.value = casesAll.value.slice(start, end)
}

const loadCases = async () => {
  if (import.meta.env.DEV) {
    casesAll.value = generateMockCasesForPatient(address)
    paginate()
  } else {
    const res = await caseApi.getCasesByPatient(address)
    if (res.code === 200) {
      casesAll.value = res.data
      paginate()
    }
  }
}

const filterByTime = () => {
  if (!dateRange.value) return
  const [from, to] = dateRange.value
  casesAll.value = casesAll.value.filter(c => {
    return c.visitTime >= from.getTime() && c.visitTime <= to.getTime()
  })
  currentPage.value = 1
  paginate()
}

const resetFilter = () => {
  loadCases()
  dateRange.value = null
}

const viewDetail = (row: any) => {
  router.push(`/cases/detail/${row.caseId}`) // 假设你有详情页路由
}

onMounted(loadCases)
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
}
</style>
