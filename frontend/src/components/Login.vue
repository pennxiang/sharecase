<script setup lang="ts">
import { ref } from 'vue'

const username = ref('')
const password = ref('')
const errorMessage = ref('')

const handleLogin = () => {
  if (!username.value || !password.value) {
    errorMessage.value = '请输入用户名和密码'
    return
  }
  // TODO: 实现登录逻辑
  console.log('登录信息：', { username: username.value, password: password.value })
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <h2>欢迎登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            id="username"
            v-model="username"
            type="text"
            class="form-control"
            placeholder="请输入用户名"
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            id="password"
            v-model="password"
            type="password"
            class="form-control"
            placeholder="请输入密码"
          />
        </div>
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        <button type="submit" class="btn btn-primary btn-block">登录</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--hospital-secondary); /* 使用医院风格颜色变量 */
}

.login-card {
  background: var(--hospital-secondary); /* 使用医院风格颜色变量 */
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

h2 {
  color: var(--hospital-primary); /* 使用医院风格颜色变量 */
  text-align: center;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--hospital-text); /* 使用医院风格颜色变量 */
  font-weight: 500;
}

.btn-block {
  width: 100%;
  margin-top: 1rem;
}

.error-message {
  color: #dc3545; /* 使用通用错误颜色 */
  margin-bottom: 1rem;
  text-align: center;
}
</style>