<template>
  <div class="auth-layout">
    <div class="auth-container">
      <slot />
    </div>
  </div>
</template>

<style scoped>
.auth-layout {
  display: flex;
  justify-content: center;   /* 水平居中 */
  align-items: center;       /* 垂直居中 */
  height: 100vh;
  background: #f5f7fa;
}

.auth-container {
  width: 40vw;               /* 固定宽度 */
  min-width: 300px;
  max-width: 600px;
}
</style>
