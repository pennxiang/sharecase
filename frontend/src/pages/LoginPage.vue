<template>
  <div class="login-container">
    <h2>用户登录</h2>
    <form @submit.prevent="handleLogin">
      <input v-model="username" placeholder="用户名" required />
      <input v-model="password" type="password" placeholder="密码" required />
      <button type="submit">登录</button>
      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';

const username = ref('');
const password = ref('');
const errorMsg = ref('');

const userStore = useUserStore();
const router = useRouter();

const handleLogin = async () => {
  try {
    await userStore.login(username.value, password.value);
    router.push('/'); // 登录成功跳转首页
  } catch (error: any) {
    errorMsg.value = error.message || '登录失败';
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 2rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  background: white;
}
input {
  display: block;
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.6rem;
}
button {
  width: 100%;
  padding: 0.6rem;
}
.error {
  color: red;
  margin-top: 1rem;
}
</style>
