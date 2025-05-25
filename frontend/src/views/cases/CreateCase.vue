<template>
  <div class="create-case">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>创建病例</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="case-form">
        <el-form-item label="患者ID" prop="patientId">
          <el-input-number
            v-model="form.patientId"
            :min="1"
            controls-position="right" />
        </el-form-item>

        <el-form-item label="医生ID" prop="doctorId">
          <el-input-number
            v-model="form.doctorId"
            :min="1"
            controls-position="right" />
        </el-form-item>

        <el-form-item label="ICD编码" prop="icdCode">
          <el-input
            v-model="form.icdCode"
            placeholder="请输入ICD编码" />
        </el-form-item>

        <el-form-item label="诊断" prop="diagnosis">
          <el-input
            v-model="form.diagnosis"
            type="textarea"
            :rows="4"
            placeholder="请输入诊断内容" />
        </el-form-item>

        <el-form-item label="治疗方案" prop="treatment">
          <el-input
            v-model="form.treatment"
            type="textarea"
            :rows="4"
            placeholder="请输入治疗方案" />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="submitForm"
            :loading="loading">
            创建病例
          </el-button>
          <el-button @click="resetForm">重置</el-button>
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
import { caseApi } from '../../api'
import type { CaseDetail } from '../../types'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive<CaseDetail>({
  patientId: 0,
  doctorId: 0,
  icdCode: '',
  diagnosis: '',
  treatment: ''
})

const rules: FormRules = {
  patientId: [
    { required: true, message: '请输入患者ID', trigger: 'blur' },
    { type: 'number', min: 1, message: '患者ID必须大于0', trigger: 'blur' }
  ],
  doctorId: [
    { required: true, message: '请输入医生ID', trigger: 'blur' },
    { type: 'number', min: 1, message: '医生ID必须大于0', trigger: 'blur' }
  ],
  icdCode: [
    { required: true, message: '请输入ICD编码', trigger: 'blur' }
  ],
  diagnosis: [
    { required: true, message: '请输入诊断内容', trigger: 'blur' }
  ],
  treatment: [
    { required: true, message: '请输入治疗方案', trigger: 'blur' }
  ]
}

const submitForm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    const res = await caseApi.create(form)
    if (res.code === 200) {
      ElMessage.success('创建病例成功')
      router.push('/dashboard/cases')
    } else {
      ElMessage.error(res.msg || '创建病例失败')
    }
  } catch (error) {
    console.error('创建病例失败:', error)
    ElMessage.error('创建病例失败，请检查表单')
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
}
</script>

<style scoped>
.create-case {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.case-form {
  max-width: 600px;
  margin: 0 auto;
}

.el-button + .el-button {
  margin-left: 10px;
}
</style> 