<template>
  <div class="auth-layout">
    <main>
      <slot />
    </main>
  </div>
</template>

<style scoped>
.auth-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f5f7fa;
}
main {
  width: 400px;
}
</style>
