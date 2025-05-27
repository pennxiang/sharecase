<template>
  <div class="chat-page">
    <div class="chat-window">
      <div class="chat-messages" ref="messageContainer">
        <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
          <strong>{{ msg.role === 'user' ? '你' : 'AI' }}：</strong>
          <span class="text">{{ msg.content }}</span>
        </div>
      </div>
      <div class="chat-input">
        <el-input
            v-model="input"
            type="textarea"
            :rows="2"
            placeholder="请输入你的问题，按 Enter 发送"
            @keyup.enter.exact="sendMessage"
        />
        <el-button
            type="primary"
            style="margin-top: 10px;"
            :loading="loading"
            @click="sendMessage"
        >
          发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import axios from 'axios'

interface Message {
  role: 'user' | 'assistant'
  content: string
}

const input = ref('')
const messages = ref<Message[]>([])
const loading = ref(false)
const messageContainer = ref<HTMLElement | null>(null)

const scrollToBottom = async () => {
  await nextTick()
  if (messageContainer.value) {
    messageContainer.value.scrollTop = messageContainer.value.scrollHeight
  }
}

const sendMessage = async () => {
  const content = input.value.trim()
  if (!content) return

  messages.value.push({ role: 'user', content })
  input.value = ''
  loading.value = true
  await scrollToBottom()

  try {
    const res = await axios.post('https://api.deepseek.com/chat/completions', {
      model: 'deepseek-chat',
      messages: messages.value.map(m => ({
        role: m.role,
        content: m.content
      }))
    }, {
      headers: {
        Authorization: 'Bearer sk-d442bbb72e7945a3953cfddd2bec997a',
        'Content-Type': 'application/json'
      }
    })

    const reply = res.data.choices?.[0]?.message?.content || 'AI 暂无回复'
    messages.value.push({ role: 'assistant', content: reply })
  } catch (err) {
    messages.value.push({ role: 'assistant', content: '❌ 请求失败，请检查网络或 API 配置。' })
    console.error(err)
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}
</script>

<style scoped>
.chat-page {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.chat-window {
  width: 100%;
  max-width: 700px;
  background: #f9f9f9;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chat-messages {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding-right: 5px;
}

.message {
  margin-bottom: 12px;
  white-space: pre-wrap;
  word-break: break-word;
}
.message.user {
  text-align: right;
}
.message.assistant {
  text-align: left;
}

.chat-input {
  display: flex;
  flex-direction: column;
}
</style>
