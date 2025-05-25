<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="aside">
      <div class="logo">共享病例平台</div>
      <el-menu router default-active="$route.path">

        <el-sub-menu index="/menu/common">
          <template #title>通用功能</template>
          <el-menu-item index="/search">模糊搜索</el-menu-item>
          <el-menu-item index="/cases/stat">平台ICD排行</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/menu/patient">
          <template #title>患者功能</template>
          <el-menu-item index="/cases/patient">我的病例</el-menu-item>
          <el-menu-item index="/cases/patient/icd-stat">ICD统计</el-menu-item>
          <el-menu-item index="/cases/patient/by-icd">按疾病查看</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/menu/doctor">
          <template #title>医生功能</template>
          <el-menu-item index="/cases/create">创建病例</el-menu-item>
          <el-menu-item index="/cases/patient/compare">PDF对比</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/menu/admin">
          <template #title>管理功能</template>
          <el-menu-item index="/cases/time">全平台病例</el-menu-item>
        </el-sub-menu>

      </el-menu>
    </el-aside>

    <!-- 主体区域 -->
    <el-container>
      <el-header class="header">
        <div class="search-box">
          <el-input
              v-model="globalKeyword"
              placeholder="搜索病例..."
              size="small"
              prefix-icon="el-icon-search"
              @keyup.enter="goSearch"
          />
        </div>
        <div class="header-right">
          <el-button v-if="user" type="text" @click="logout">退出登录</el-button>
          <router-link v-else to="/auth">登录</router-link>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { computed, ref } from 'vue'

const userStore = useUserStore()
const router = useRouter()
const user = computed(() => userStore.user)

const logout = () => {
  userStore.logout()
  router.push('/auth')
}

const globalKeyword = ref('')
const goSearch = () => {
  if (globalKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: globalKeyword.value.trim() } })
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #2d8cf0;
  color: white;
  padding-top: 20px;
}

.logo {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
  color: #fff;
}

.header {
  background: #f4f4f4;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.search-box {
  width: 300px;
}

.main {
  background: #f8f8f8;
  padding: 20px;
  height: calc(100vh - 60px);
  overflow-y: auto;
}
</style>