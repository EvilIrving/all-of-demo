<template>
  <div class="basic-layout">
    <van-nav-bar v-if="showNav" class="basic-layout__nav" :title="title" left-arrow safe-area-inset-top>
      <template #left>
        <van-icon name="arrow-left" @click="onBackClick" fixed />
      </template>
    </van-nav-bar>
    <div class="basic-layout__content">
      <slot>
        <RouterView></RouterView>
      </slot>
    </div>
  </div>
</template>
<script lang="ts">
import { RouterView } from 'vue-router'
import { defineComponent, computed } from 'vue';
import { RouteLocationRaw, useRoute, useRouter } from 'vue-router';
export default defineComponent({
  components: { RouterView },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const title = computed(() => route.meta.title);
    const showNav = computed(() => route.meta.showNav || false);
    const onBackClick = () => {
      if (route.meta.defaultBack == '-1' || !route.meta.defaultBack) router.go(-1);
      else if (route.meta.defaultBack) router.replace(route.meta.defaultBack as RouteLocationRaw);
    };
    return { onBackClick, title, showNav };
  }
});
</script>
<style lang="scss">
.basic-layout {
  height: 100%;

  .van-nav-bar {
    .van-badge__wrapper.van-icon.van-icon-arrow-left {
      font-size: 40px;
    }

    .van-nav-bar__title {
      line-height: 40px;
    }
  }

  .basic-layout__content {
    height: 100%;
  }
}
</style>
