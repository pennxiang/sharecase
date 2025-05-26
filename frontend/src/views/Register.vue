<template>
  <AuthLayout>
    <el-card class="login-card">
      <template #header>
        <h2>注册</h2>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名" clearable />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" clearable />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" clearable />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password clearable />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirm">
          <el-input v-model="form.confirm" type="password" show-password clearable />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="医生" value="doctor" />
            <el-option label="患者" value="patient" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button type="text" @click="router.push('/auth')">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </AuthLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import { userApi } from '@/api'
import AuthLayout from '@/layouts/AuthLayout.vue'

const router = useRouter()
const formRef = ref<FormInstance>()

const form = reactive({
  name: '',
  phone: '',
  idCard: '',
  password: '',
  confirm: '',
  role: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirm: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_, value) =>
          value === form.password ? Promise.resolve() : Promise.reject('两次密码不一致'),
      trigger: 'blur'
    }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const handleRegister = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return
    const { confirm, ...user } = form

    try {
      const res = await userApi.register(user)
      if (res.code === 0 || res.code === 200) {
        ElMessage.success('注册成功，请登录')
        router.push('/auth')
      } else {
        ElMessage.error(res.msg)
      }
    } catch (err: any) {
      ElMessage.error(err?.msg || '注册失败')
    }
  })
}
</script>

<style scoped>
.login-card {
  width: 40vw;
  min-width: 300px;
  max-width: 600px;
  margin: 0 auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  padding: 20px;
}
h2 {
  text-align: center;
  margin: 0;
}
</style>
