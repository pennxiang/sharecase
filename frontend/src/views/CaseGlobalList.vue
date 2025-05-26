<template>
  <div class="case-list-page">
    <div class="toolbar">
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-right: 12px"
      />

      <el-input
          v-model="keyword"
          placeholder="搜索关键词"
          @keyup.enter="handleFilter"
          style="flex: 1; margin-right: 12px"
          clearable
      />

      <el-button type="primary" @click="handleFilter">筛选</el-button>

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
      <el-table-column prop="doctorAddress" label="医生地址" />
      <el-table-column prop="patientAddress" label="患者地址" />
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
        :total="total"
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
import { caseApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()
const isDoctor = userStore.user?.role === 'doctor'

const allCases = ref<any[]>([])
const cases = ref<any[]>([])
const keyword = ref('')
const dateRange = ref<[Date, Date] | null>(null)
const currentPage = ref(1)
const pageSize = 15
const total = ref(0)

const formatTime = (timestamp: number) =>
    format(new Date(timestamp), 'yyyy-MM-dd HH:mm')

const viewCase = (row: any) => {
  const hash = row.ipfsHash
  if (!hash) {
    ElMessage.warning('该病例未上传 PDF')
    return
  }
  window.open(`http://127.0.0.1:8080/ipfs/${hash}`, '_blank')
}

const handleFilter = () => {
  let filtered = [...allCases.value]

  if (dateRange.value) {
    const [from, to] = dateRange.value
    filtered = filtered.filter(c => c.visitTime >= from.getTime() && c.visitTime <= to.getTime())
  }

  if (keyword.value.trim()) {
    const kw = keyword.value.trim().toLowerCase()
    filtered = filtered.filter(c =>
        c.caseId.toLowerCase().includes(kw) ||
        c.icdCode.toLowerCase().includes(kw)
    )
  }

  total.value = filtered.length
  currentPage.value = 1
  cases.value = filtered.slice(0, pageSize)
}

const paginate = (page: number) => {
  currentPage.value = page
  const start = (page - 1) * pageSize
  const end = start + pageSize
  handleFilter() // 确保 filter 后的数据正确分页
  cases.value = allCases.value
      .filter((c) => {
        const [from, to] = dateRange.value || [null, null]
        const kw = keyword.value.trim().toLowerCase()
        return (
            (!from || c.visitTime >= from.getTime()) &&
            (!to || c.visitTime <= to.getTime()) &&
            (!kw || c.caseId.toLowerCase().includes(kw) || c.icdCode.toLowerCase().includes(kw))
        )
      })
      .slice(start, end)
}

onMounted(async () => {
  try {
    const res = await caseApi.getAllCases()
    if (res.code === 0 || res.code === 200) {
      allCases.value = res.data
      total.value = res.data.length
      handleFilter()
    } else {
      ElMessage.error(res.msg)
    }
  } catch (err) {
    ElMessage.error('病例加载失败')
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
