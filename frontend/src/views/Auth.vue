<template>
  <AuthLayout>
    <el-card>
      <el-form :model="form" label-width="80px" @submit.native.prevent="login">
        <el-form-item label="账号">
          <el-input v-model="form.loginId" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </AuthLayout>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { userApi } from '@/api'
import { useUserStore } from '@/store/user'
import {reactive} from "vue";

const form = reactive({ loginId: '', password: '' })
const router = useRouter()
const userStore = useUserStore()

const login = async () => {
  const res = await userApi.login(form.loginId, form.password)
  if (res.code === 200) {
    userStore.setUser(res.data)
    router.push('/cases/time')
  } else {
    ElMessage.error(res.msg)
  }
}
</script>
