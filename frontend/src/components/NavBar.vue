<template>
  <div class="navbar">
    <div class="title">电子病例共享系统</div>
    <div class="actions">
      <el-dropdown>
        <span class="el-dropdown-link">
          {{ userStore.user?.loginId || '用户' }}
          <el-icon><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="switchUser('doctor')">模拟医生</el-dropdown-item>
            <el-dropdown-item @click="switchUser('patient')">模拟病人</el-dropdown-item>
            <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const logout = () => {
  userStore.logout()
  router.push('/auth')
}

const switchUser = (role: 'doctor' | 'patient') => {
  userStore.mockLogin(role)
  location.reload()
}

</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background: #fff;
  border-bottom: 1px solid #eee;
}
</style>
