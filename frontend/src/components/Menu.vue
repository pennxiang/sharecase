<template>
  <el-menu
      :default-active="active"
      class="el-menu-vertical-demo"
      router
  >
    <el-menu-item index="/cases/time">全平台病例</el-menu-item>
    <el-menu-item index="/cases/patient">我的病例</el-menu-item>
    <el-menu-item index="/cases/patient/compare">病例对比</el-menu-item>
    <el-menu-item index="/cases/stat">ICD 统计</el-menu-item>
    <el-menu-item index="/patient/icd-stat">我的 ICD</el-menu-item>
    <el-menu-item index="/settings">个人设置</el-menu-item>
  </el-menu>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const active = route.path
</script>
