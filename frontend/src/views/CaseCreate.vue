<template>
  <PageWrapper :loading="submitting">
    <el-button type="primary" @click="dialogVisible = true" style="margin-bottom: 20px">
      填写病例表单
    </el-button>

    <el-dialog v-model="dialogVisible" title="新增病例" width="800px" top="5vh" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="病例标题" prop="title">
          <el-input v-model="form.title" placeholder="如：高血压初诊病例" />
        </el-form-item>
        <el-form-item label="ICD 编码" prop="icdCode">
          <el-input v-model="form.icdCode" placeholder="如：I10" />
        </el-form-item>
        <el-form-item label="主诉" prop="chiefComplaint">
          <el-input v-model="form.chiefComplaint" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="现病史" prop="presentIllness">
          <el-input v-model="form.presentIllness" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="既往史" prop="pastHistory">
          <el-input v-model="form.pastHistory" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="初步诊断" prop="diagnosis">
          <el-input v-model="form.diagnosis" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="医生建议" prop="doctorAdvice">
          <el-input v-model="form.doctorAdvice" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="所属医院" prop="hospitalAddress">
          <el-input v-model="form.hospitalAddress" />
        </el-form-item>
        <el-form-item label="上传报告">
          <el-upload
              :show-file-list="false"
              :http-request="uploadPdf"
              accept="application/pdf"
          >
            <el-button type="primary">选择 PDF 文件</el-button>
          </el-upload>
          <span v-if="uploadedFileName" style="margin-left: 10px">{{ uploadedFileName }}</span>
          <el-button v-if="form.ipfsHash" type="success" link @click="openPreview">
            预览 PDF
          </el-button>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交病例</el-button>
      </template>
    </el-dialog>
  </PageWrapper>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { FormInstance, FormRules, UploadRequestOptions } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { caseApi } from '@/api'
import type { CaseDetail } from '@/types'
import PageWrapper from '@/components/PageWrapper.vue'
import axios from 'axios'

const formRef = ref<FormInstance>()
const submitting = ref(false)
const dialogVisible = ref(false)
const userStore = useUserStore()
const uploadedFileName = ref('')

const form = ref<Partial<CaseDetail>>({
  title: '',
  icdCode: '',
  chiefComplaint: '',
  presentIllness: '',
  pastHistory: '',
  diagnosis: '',
  doctorAdvice: '',
  hospitalAddress: '',
  ipfsHash: ''
})

const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  icdCode: [{ required: true, message: '请输入ICD编码', trigger: 'blur' }],
  chiefComplaint: [{ required: true, message: '请输入主诉', trigger: 'blur' }],
  diagnosis: [{ required: true, message: '请输入初步诊断', trigger: 'blur' }]
}

const uploadPdf = async (option: UploadRequestOptions) => {
  const formData = new FormData()
  formData.append('file', option.file)
  uploadedFileName.value = option.file.name
  try {
    const res = await axios.post('/api/ipfs/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.code === 0) {
      form.value.ipfsHash = res.data.data
      ElMessage.success('上传成功')
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

const openPreview = () => {
  const hash = form.value.ipfsHash
  if (hash) {
    window.open(`https://ipfs.io/ipfs/${hash}`, '_blank')
  }
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    if (!form.value.ipfsHash) {
      ElMessage.warning('请先上传 PDF 检查报告')
      return
    }
    submitting.value = true
    try {
      const detail = {
        ...form.value,
        patientAddress: userStore.user?.chainAddress || '',
        doctorAddress: userStore.user?.chainAddress || ''
      } as CaseDetail
      const res = await caseApi.createCase(detail)
      if (res.code === 0) {
        ElMessage.success('病例提交成功')
        dialogVisible.value = false
      } else {
        ElMessage.error(res.msg)
      }
    } catch (e) {
      ElMessage.error('提交失败')
    } finally {
      submitting.value = false
    }
  })
}
</script>
