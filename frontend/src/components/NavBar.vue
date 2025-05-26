<template>
  <div class="navbar">
    <div class="left-placeholder" /> <!-- 用于撑开左侧空间 -->
    <div class="title">电子病例共享系统</div>
    <div class="actions">
      <el-dropdown>
        <span class="el-dropdown-link">
          {{ userStore.user?.loginId || '用户' }}
          <el-icon><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
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
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  height: 64px;
  padding: 0 20px;
  background-image: url('/background.jpg');
  background-size: cover;
  background-position: center;
  color: #fff;
  border-bottom: 1px solid #eee;
}

/* 左右两边撑开空间，使标题居中 */
.left-placeholder,
.actions {
  width: 150px;
}

.title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-size: 20px;
  font-weight: bold;
  color: #000;
}
</style>
