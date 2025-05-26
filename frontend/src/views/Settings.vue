<!-- views/Settings.vue -->
<template>
  <el-card>
    <el-form :model="form" label-width="100px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" disabled />
      </el-form-item>
      <el-form-item label="身份证号">
        <el-input v-model="form.idCard" disabled />
      </el-form-item>
      <el-form-item label="链上地址">
        <el-input v-model="form.chainAddress" disabled />
      </el-form-item>
      <el-form-item label="身份">
        <el-input v-model="form.role" disabled />
      </el-form-item>
      <el-form-item label="工号">
        <el-input v-model="form.workId" disabled />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.password" type="password" />
      </el-form-item>
      <el-button type="primary" @click="save">保存</el-button>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/user'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'
import {reactive} from "vue";

const userStore = useUserStore()
const form = reactive({
  id: userStore.user?.id,
  name: userStore.user?.name,
  phone: userStore.user?.phone,
  idCard: userStore.user?.idCard,
  password: '',
  workId: userStore.user?.workId,
  chainAddress: userStore.user?.chainAddress,
  role: userStore.user?.role,
})

const save = async () => {
  const res = await userApi.update(form)
  if (res.code === 200) {
    ElMessage.success('更新成功')
  } else {
    ElMessage.error(res.msg)
  }
}
</script>
