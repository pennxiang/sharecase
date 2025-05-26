<!-- views/PdfCompare.vue -->
<template>
  <el-card>
    <el-form :inline="true" @submit.prevent>
      <el-form-item label="Hash1">
        <el-input v-model="hash1" />
      </el-form-item>
      <el-form-item label="Hash2">
        <el-input v-model="hash2" />
      </el-form-item>
      <el-button type="primary" @click="compare">对比</el-button>
    </el-form>

    <el-table :data="diffList" border v-if="diffList.length">
      <el-table-column prop="field" label="字段" />
      <el-table-column prop="value1" label="版本1" />
      <el-table-column prop="value2" label="版本2" />
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { caseApi } from '@/api'
import {ref} from "vue";
const hash1 = ref('')
const hash2 = ref('')
const diffList = ref([])

const compare = async () => {
  if (!hash1.value || !hash2.value) return
  const res = await caseApi.compareCases(hash1.value, hash2.value)
  if (res.code === 200) {
    diffList.value = Object.entries(res.data).map(([k, [v1, v2]]) => ({
      field: k,
      value1: v1,
      value2: v2
    }))
  }
}
</script>
