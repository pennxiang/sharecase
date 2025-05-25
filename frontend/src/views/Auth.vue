<template>
  <el-card class="auth-card">
    <el-tabs v-model="activeTab">
      <!-- 登录面板 -->
      <el-tab-pane label="登录" name="login">
        <el-form :model="loginForm" label-width="80px">
          <el-form-item label="账号">
            <el-input v-model="loginForm.loginId" placeholder="身份证号或手机号" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="loginForm.password" show-password />
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="handleLogin">登录</el-button>
      </el-tab-pane>

      <!-- 注册面板 -->
      <el-tab-pane label="注册" name="register">
        <el-form :model="registerForm" :rules="registerRules" label-width="80px" ref="registerRef">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="registerForm.name" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerForm.password" show-password />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="registerForm.phone" />
          </el-form-item>
          <el-form-item label="身份证" prop="idCard">
            <el-input v-model="registerForm.idCard" />
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="handleRegister">注册</el-button>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const route = useRoute()
const activeTab = ref('login')
const userStore = useUserStore()
const registerRef = ref<FormInstance>()

const loginForm = ref({
  loginId: '',
  password: ''
})

const registerForm = ref({
  name: '',
  password: '',
  phone: '',
  idCard: ''
})

const registerRules: FormRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  try {
    const res = await userApi.login(loginForm.value.loginId, loginForm.value.password)
    if (res.code === 0) {
      ElMessage.success('登录成功')
      const userRes = await userApi.findByIdCard(loginForm.value.loginId)
      userStore.setUser(userRes.data)
      const redirect = route.query.redirect as string || '/'
      router.push(redirect)
    } else {
      ElMessage.error(res.msg)
    }
  } catch (e) {
    ElMessage.error('登录失败')
  }
}

const handleRegister = async () => {
  if (!registerRef.value) return
  await registerRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const res = await userApi.register(registerForm.value)
      if (res.code === 0) {
        ElMessage.success('注册成功，请登录')
        loginForm.value.loginId = registerForm.value.idCard
        loginForm.value.password = registerForm.value.password
        activeTab.value = 'login'
      } else {
        ElMessage.error(res.msg)
      }
    } catch (e) {
      ElMessage.error('注册失败')
    }
  })
}
</script>

<style scoped>
.auth-card {
  width: 400px;
  margin: 80px auto;
}
</style>