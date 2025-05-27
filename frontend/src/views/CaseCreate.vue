<template>
  <div class="case-create-page">
    <el-card>
      <el-form :model="form" label-width="120px" label-position="left">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>

        <el-form-item label="ICD 编码" required>
          <el-input v-model="form.icdCode" />
        </el-form-item>

        <el-form-item label="医院 ID">
          <el-input v-model="form.hospitalAddress" />
        </el-form-item>

        <el-form-item label="病人 ID" required>
          <el-input v-model="form.patientAddress" />
        </el-form-item>

        <el-form-item label="医生 ID">
          <el-input v-model="form.doctorAddress" disabled />
        </el-form-item>

        <el-form-item label="主诉">
          <el-input type="textarea" v-model="form.chiefComplaint" />
        </el-form-item>

        <el-form-item label="现病史">
          <el-input type="textarea" v-model="form.presentIllness" />
        </el-form-item>

        <el-form-item label="既往史">
          <el-input type="textarea" v-model="form.pastHistory" />
        </el-form-item>

        <el-form-item label="初步诊断">
          <el-input type="textarea" v-model="form.diagnosis" />
        </el-form-item>

        <el-form-item label="医生建议">
          <el-input type="textarea" v-model="form.doctorAdvice" />
        </el-form-item>

        <el-form-item label="创建时间">
          <el-date-picker
              v-model="form.visitTime"
              type="datetime"
              placeholder="选择时间"
              style="width: 100%"
          />
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
  hospitalAddress: '',
  patientAddress: '',
  doctorAddress: userStore.user?.chainAddress || '',
  chiefComplaint: '',
  presentIllness: '',
  pastHistory: '',
  diagnosis: '',
  doctorAdvice: '',
  visitTime: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString().slice(0, 19)
})

const submit = async () => {
  // console.log("正在提交病例...",form)
  // console.log('提交内容:', JSON.stringify(form, null, 2))

  if (!form.title || !form.icdCode || !form.patientAddress) {
    ElMessage.warning('请填写完整信息')
    return
  }

  const res = await caseApi.create(form)
  if (res.code === 0 || res.code === 200) {
    // console.log('接口响应', res)
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
