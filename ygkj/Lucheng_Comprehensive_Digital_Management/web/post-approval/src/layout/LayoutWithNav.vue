<script setup lang="ts">
import { RouterView, useRoute, useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import { login } from '@/api/test';
import { storage } from '@/utils/storage';
import BasicLayout from './BasicLayout/index.vue';
const route = useRoute();
const router = useRouter();
onMounted(() => {
  /* 模拟登录 */
  if (process.env.NODE_ENV !== 'production') {
    login({ username: '13858859582', password: 'ygkj0818' }).then(res => {
      if (res.code == 0) {
        storage.set('userInfo_obj', { ...res.data, timestamp: new Date().getTime() });
      }
    });
  }
});
const onClickTab = ({ title }: { title: '未处理' | '已处理' }) => {
  router.push(
    {
      未处理: '/unprocessed',
      已处理: '/processed'
    }[title]
  );
};
const activeTab = ref<number>(route.path == '/unprocessed' ? 0 : 1);
const goPublicity = () => {
  router.push('/publicity');
};
</script>
<template>
  <BasicLayout>
    <div class="post-approval">
      <van-tabs v-model:active="activeTab" @click-tab="onClickTab">
        <van-tab title="未处理">
          <RouterView />
        </van-tab>
        <van-tab title="已处理">
          <RouterView />
        </van-tab>
      </van-tabs>
    </div>
    <div class="btn-box" @click="goPublicity"></div>
  </BasicLayout>
</template>

<style lang="scss" scoped>
.btn-box {
  width: 100px;
  height: 100px;
  position: fixed;
  bottom: 180px;
  right: 60px;
  border-radius: 50%;
  background: url('../assets/images/publicity.png') no-repeat;
  background-size: 100% 100%;
}
</style>