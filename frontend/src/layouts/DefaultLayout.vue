<template>
  <el-container style="min-height: 100vh">
    <el-aside width="200px">
      <SideMenu />
    </el-aside>

    <el-container>
      <el-header height="60px">
        <NavBar />
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import SideMenu from '@/components/Menu.vue'
import NavBar from '@/components/NavBar.vue'
</script>
