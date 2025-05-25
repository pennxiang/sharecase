<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2><img src="../assets/logo.jpg" style="width: 50px;position: relative; top: 13px;right: 6px">
          登录系统</h2>
      </template>
      
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账号" prop="loginId">
          <el-input v-model="form.loginId" placeholder="请输入账号"></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { userApi } from '../api'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  loginId: '',
  password: ''
})

const rules: FormRules = {
  loginId: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const res = await userApi.login(form.loginId, form.password)
    if (res.code === 200) {
      ElMessage.success('登录成功')
      // 这里应该保存token，假设后端返回的token在res.data中
      localStorage.setItem('token', res.data?.token || '')
      router.push('/dashboard/cases')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  /* background-color: #f5f7fa; */
  padding: 20px 80px; /* 兼容小屏，右侧增加内边距 */
}

.login-card {
  width: 480px;             /* ✅ 加宽卡片 */
  padding: 40px 30px;       /* ✅ 增加内边距 */
  font-size: 16px;          /* ✅ 放大整体文字 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); /* ✅ 加点阴影更美观 */
  border-radius: 8px;
  opacity: 0.9; /* 设置登录框透明度为 0.8，数值范围 0 到 1，越接近 0 越透明 */
}

.el-form-item {
  margin-bottom: 24px;      /* ✅ 增加间距 */
}

.el-input__inner {
  font-size: 16px;
  height: 20px;             /* ✅ 提高输入框高度 */
}

.el-button {
  width: 100%;
  height: 42px;
  font-size: 16px;
}

.el-button + .el-button {
  margin-top: 10px;         /* ✅ 垂直间距而不是横向 */
  margin-left: 0;
}

</style>