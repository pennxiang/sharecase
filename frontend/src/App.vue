<script setup lang="ts">
// import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <router-view></router-view>
</template>

<style>
html, body {
  /* 替换为用户指定的背景图 */
  background-image: url('/public/homepage-back.jpg'); /* 假设背景图放在public目录下 */
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  height: 100%;
}
/* 可以添加医院风格的颜色变量 */
:root {
  /* 保留医院风格颜色变量 */
  --hospital-primary: #007BFF;
  --hospital-secondary: #F8F9FA;
  --hospital-text: #333333;
}

</style>
