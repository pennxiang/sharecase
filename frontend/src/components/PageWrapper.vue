<template>
  <el-card shadow="hover">
    <!-- 顶部搜索栏 -->
    <div class="header">
      <slot name="header" />
    </div>

    <!-- 主体内容（如表格、图表） -->
    <div v-loading="loading" class="body">
      <slot />
    </div>
  </el-card>
</template>

<script setup lang="ts">
defineProps<{
  loading?: boolean
}>()
</script>

<style scoped>
.header {
  margin-bottom: 16px;
}
</style>
