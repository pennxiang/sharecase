// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig(() => {
  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
      },
    },
    base: '/',
    server: {
      proxy: {
        '/api': {
          target: 'http://localhost:8888', // 开发代理地址
          changeOrigin: true,
          rewrite: path => path.replace(/^\/api/, '')
        }
      }
    },
    build: {
      outDir: 'dist',  // 打包输出目录
      sourcemap: false // 是否生成 .map 调试文件（建议 false）
    }
  }
})
