<template>
  <div class="profile">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button type="primary" @click="toggleEdit">
            {{ isEditing ? '取消编辑' : '编辑信息' }}
          </el-button>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        :disabled="!isEditing">
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" disabled />
        </el-form-item>

        <el-form-item label="工号" prop="workId">
          <el-input v-model="form.workId" />
        </el-form-item>

        <template v-if="isEditing">
          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="form.newPassword"
              type="password"
              placeholder="不修改请留空" />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="不修改请留空" />
          </el-form-item>
        </template>

        <el-form-item v-if="isEditing">
          <el-button
            type="primary"
            @click="handleSubmit"
            :loading="loading">
            保存修改
          </el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { userApi } from '../api'
import type { User } from '../types'

const formRef = ref<FormInstance>()
const loading = ref(false)
const isEditing = ref(false)

const form = reactive({
  name: '',
  phone: '',
  idCard: '',
  workId: '',
  newPassword: '',
  confirmPassword: ''
})

const validatePass2 = (rule: any, value: string, callback: any) => {
  if (form.newPassword && !value) {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.newPassword) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules: FormRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  newPassword: [
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validatePass2, trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  loading.value = true
  try {
    // 这里需要根据实际情况获取用户ID
    const userId = localStorage.getItem('userId')
    if (!userId) {
      ElMessage.error('未找到用户信息')
      return
    }

    const res = await userApi.findById(Number(userId))
    if (res.code === 200 && res.data) {
      const { name, phone, idCard, workId } = res.data
      Object.assign(form, { name, phone, idCard, workId })
    } else {
      ElMessage.error(res.msg || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    const userData: User = {
      name: form.name,
      phone: form.phone,
      idCard: form.idCard,
      workId: form.workId
    }

    if (form.newPassword) {
      userData.password = form.newPassword
    }

    const res = await userApi.update(userData)
    if (res.code === 200) {
      ElMessage.success('更新成功')
      isEditing.value = false
      form.newPassword = ''
      form.confirmPassword = ''
    } else {
      ElMessage.error(res.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error('更新失败，请检查表单')
  } finally {
    loading.value = false
  }
}

const toggleEdit = () => {
  if (isEditing.value) {
    isEditing.value = false
    loadUserInfo() // 重新加载用户信息
  } else {
    isEditing.value = true
  }
}

const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  loadUserInfo()
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-form {
  max-width: 500px;
  margin: 0 auto;
}

.el-button + .el-button {
  margin-left: 10px;
}
</style> 