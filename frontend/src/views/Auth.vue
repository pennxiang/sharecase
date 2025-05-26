<template>
  <AuthLayout>
    <el-card class="login-card">
      <template #header>
        <h2>登录</h2>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" @submit.native.prevent="login">
        <el-form-item label="账号" prop="loginId">
          <el-input v-model="form.loginId" placeholder="请输入账号" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button type="text" @click="goToRegister">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </AuthLayout>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { login } from '@/services/authApi'
import { useUserStore } from '@/store/user'
import AuthLayout from "@/layouts/AuthLayout.vue";

const router = useRouter()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const form = reactive({
  loginId: '',
  password: ''
})

const rules: FormRules = {
  loginId: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value?.validate(async (valid) => {
    if (valid) {
      const res = await login(form.loginId, form.password)
      console.log('登录结果:', res)
      if (res.code === 0) {
        userStore.setUser(res.data)  // 这里 data 必须是 user 对象
        ElMessage.success(res.msg)   // ✅ 正确弹出提示
        router.push('/cases/patient')   // ✅ 跳转
      } else {
        ElMessage.error(res.msg)     // 错误提示
      }
    }
  })
}

const goToRegister = () => {
  router.push('/auth/register')
}
</script>

<style scoped>
.login-card {
  /* 复用 AuthLayout */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  padding: 20px;
}
h2 {
  text-align: center;
  margin: 0;
}
</style>
