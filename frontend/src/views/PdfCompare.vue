<template>
  <el-card>
    <el-form :inline="true" @submit.prevent>
      <el-form-item label="Hash1">
        <el-input v-model="hash1" placeholder="请输入第一个IPFS哈希" clearable />
      </el-form-item>
      <el-form-item label="Hash2">
        <el-input v-model="hash2" placeholder="请输入第二个IPFS哈希" clearable />
      </el-form-item>
      <el-button type="primary" @click="compare">对比</el-button>
    </el-form>

    <el-table :data="diffList" border v-if="diffList.length">
      <el-table-column prop="field" label="字段" />
      <el-table-column prop="value1" label="版本1" />
      <el-table-column prop="value2" label="版本2" />
    </el-table>

    <el-empty description="两个版本无差异" v-else style="margin-top: 20px" />
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { caseApi } from '@/api'

const hash1 = ref('')
const hash2 = ref('')
const diffList = ref<{ field: string; value1: string; value2: string }[]>([])

const fieldMap: Record<string, string> = {
  title: '病例标题',
  icdCode: 'ICD 编码',
  chiefComplaint: '主诉',
  presentIllness: '现病史',
  pastHistory: '既往史',
  diagnosis: '初步诊断',
  doctorAdvice: '医生建议',
  hospitalAddress: '所属医院地址',
  visitTime: '就诊时间'
}

const compare = async () => {
  if (!hash1.value || !hash2.value) {
    ElMessage.warning('请输入两个 IPFS 哈希')
    return
  }

  try {
    const res = await caseApi.compareCases(hash1.value, hash2.value)
    if (res.code === 0 || res.code === 200) {
      diffList.value = Object.entries(res.data).map(([k, [v1, v2]]) => ({
        field: fieldMap[k] || k,
        value1: v1,
        value2: v2
      }))
    } else {
      ElMessage.error(res.msg || '对比失败')
    }
  } catch (err) {
    ElMessage.error('接口异常')
  }
}
</script>
