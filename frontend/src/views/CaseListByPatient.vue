<template>
  <el-card>
    <div class="toolbar">
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
      />
      <el-button type="primary" @click="filterByTime" style="margin-left: 12px">
        筛选
      </el-button>
      <el-button @click="resetFilter" style="margin-left: auto">重置</el-button>
    </div>

    <el-table :data="cases" border style="margin-top: 12px">
      <el-table-column prop="caseId" label="病例 ID" />
      <el-table-column prop="icdCode" label="ICD 编码" />
      <el-table-column prop="ipfsHash" label="ipfs地址" />
      <el-table-column prop="visitTime" label="就诊时间">
        <template #default="{ row }">
          {{ formatTime(row.visitTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewPdf(row)">
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="paginate"
        style="margin-top: 16px; text-align: right"
    />
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { format } from 'date-fns'
import { useUserStore } from '@/store/user'
import { caseApi } from '@/api'

const userStore = useUserStore()
const address = ref<String>('')

const dateRange = ref<[Date, Date] | null>(null)
const currentPage = ref(1)
const pageSize = 10

const cases = ref([])
const total = ref(0)

const formatTime = (t: number) => format(new Date(t), 'yyyy-MM-dd HH:mm')

const loadCases = async () => {
  if (!address.value) {
    ElMessage.warning('用户地址为空，无法加载病例')
    return
  }

  console.log('当前 address:', address.value)
  try {
    if (dateRange.value) {
      const [from, to] = dateRange.value
      const res = await caseApi.getCasesByPatientAndTime(
          address.value,
          from.getTime().toString(),
          to.getTime().toString()
      )
      if (res.code === 0) {
        cases.value = res.data.slice((currentPage.value - 1) * pageSize, currentPage.value * pageSize)
        total.value = res.data.length
      }
    } else {
      const res = await caseApi.getCasesByPatient(address.value)
      console.log('后端响应结果:', res)
      if (res.code === 0) {
        cases.value = res.data.slice((currentPage.value - 1) * pageSize, currentPage.value * pageSize)
        total.value = res.data.length
      }
    }
  } catch (err) {
    ElMessage.error('加载失败')
  }
}

const paginate = (page: number) => {
  currentPage.value = page
  loadCases()
}

const filterByTime = () => {
  currentPage.value = 1
  loadCases()
}

const resetFilter = () => {
  dateRange.value = null
  currentPage.value = 1
  loadCases()
}

const viewPdf = (row: any) => {
  if (!row.ipfsHash) {
    ElMessage.warning('该病例未上传 PDF')
    return
  }
  window.open(`http://127.0.0.1:8080/ipfs/${row.ipfsHash}`, '_blank')
}

onMounted(() => {
  address.value = userStore.user?.chainAddress || ''
  loadCases()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
}
</style>
