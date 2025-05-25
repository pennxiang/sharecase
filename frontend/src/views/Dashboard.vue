<template>
  <el-container class="dashboard-container">
    <el-aside width="200px">
      <el-menu
        router
        :default-active="$route.path"
        class="dashboard-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF">
        
        <el-menu-item index="/dashboard/cases">
          <el-icon><Document /></el-icon>
          <span>病例管理</span>
        </el-menu-item>
        
        <el-menu-item index="/dashboard/case/create">
          <el-icon><Plus /></el-icon>
          <span>创建病例</span>
        </el-menu-item>
        
        <el-menu-item index="/dashboard/statistics">
          <el-icon><TrendCharts /></el-icon>
          <span>疾病统计</span>
        </el-menu-item>
        
        <el-menu-item index="/dashboard/profile">
          <el-icon><User /></el-icon>
          <span>个人信息</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-left">
          <el-icon class="toggle-sidebar" @click="toggleSidebar">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
          <h2>共享病例系统</h2>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              {{ userName }}
              <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Document,
  Plus,
  TrendCharts,
  User,
  Expand,
  Fold,
  CaretBottom
} from '@element-plus/icons-vue'

const router = useRouter()
const isCollapse = ref(false)
const userName = ref('用户') // 这里应该从用户状态或存储中获取

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/dashboard/profile')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        router.push('/login')
        ElMessage.success('已退出登录')
      }).catch(() => {})
      break
  }
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
  transition: width 0.3s;
}

.dashboard-menu {
  height: 100%;
  border-right: none;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.toggle-sidebar {
  font-size: 20px;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style> 