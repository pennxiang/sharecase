<template>
  <PageWrapper>
    <template #header>
      <el-input v-model="hash1" placeholder="请输入第一个病例的 IPFS Hash" style="width: 300px; margin-right: 12px" />
      <el-input v-model="hash2" placeholder="请输入第二个病例的 IPFS Hash" style="width: 300px; margin-right: 12px" />
      <el-button type="primary" @click="compare">对比</el-button>
    </template>

    <el-table v-if="diffData && Object.keys(diffData).length" :data="diffEntries" border stripe>
      <el-table-column prop="field" label="字段" width="150" />
      <el-table-column prop="value1" label="病例1" />
      <el-table-column prop="value2" label="病例2" />
    </el-table>

    <div class="pdf-preview">
      <iframe v-if="hash1" :src="pdfUrl(hash1)" width="48%" height="500px" />
      <iframe v-if="hash2" :src="pdfUrl(hash2)" width="48%" height="500px" />
    </div>
  </PageWrapper>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import PageWrapper from '@/components/PageWrapper.vue'
import { caseApi } from '@/api'

const hash1 = ref('')
const hash2 = ref('')
const diffData = ref<Record<string, any> | null>(null)

const compare = async () => {
  if (!hash1.value || !hash2.value) {
    ElMessage.warning('请填写两个 IPFS Hash')
    return
  }
  try {
    const res = await caseApi.compareCases(hash1.value, hash2.value)
    if (res.code === 0) {
      diffData.value = res.data || {}
    } else {
      ElMessage.error(res.msg)
    }
  } catch (e) {
    ElMessage.error('对比失败')
  }
}

const diffEntries = computed(() => {
  if (!diffData.value) return []
  return Object.entries(diffData.value).map(([field, [value1, value2]]) => ({ field, value1, value2 }))
})

const pdfUrl = (hash: string) => `https://ipfs.io/ipfs/${hash}`
</script>

<style scoped>
.pdf-preview {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-top: 24px;
}
</style>
