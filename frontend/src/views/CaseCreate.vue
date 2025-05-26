<template>
  <div class="case-create-page">
    <el-card>
      <el-form :model="form" label-width="120px">
        <el-form-item label="病例标题" required>
          <el-input v-model="form.title" />
        </el-form-item>

        <el-form-item label="ICD 编码" required>
          <el-input v-model="form.icdCode" />
        </el-form-item>

        <el-form-item label="患者地址" required>
          <el-input v-model="form.patient" />
        </el-form-item>

        <el-form-item label="主诉症状">
          <el-input type="textarea" v-model="form.chiefComplaint" />
        </el-form-item>

        <el-form-item label="初步诊断">
          <el-input type="textarea" v-model="form.diagnosis" />
        </el-form-item>

        <el-form-item label="医生建议">
          <el-input type="textarea" v-model="form.doctorAdvice" />
        </el-form-item>

        <el-form-item>
          <el-button @click="goBack">返回</el-button>
          <el-button type="primary" @click="submit">提交病例</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { caseApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const goBack = () => {
  router.back()
}

const form = reactive({
  title: '',
  icdCode: '',
  patient: '',
  chiefComplaint: '',
  diagnosis: '',
  doctorAdvice: '',
  doctor: userStore.user?.blockchainAddress || '', // 医生地址
  visitTime: Date.now()
})

const submit = async () => {
  if (!form.title || !form.icdCode || !form.patient) {
    ElMessage.warning('请填写完整信息')
    return
  }

  const res = await caseApi.create(form)
  if (res.code === 200) {
    ElMessage.success('病例创建成功')
    router.push('/cases/time')
  } else {
    ElMessage.error(res.msg || '提交失败')
  }
}
</script>

<style scoped>
.case-create-page {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
}
</style>
