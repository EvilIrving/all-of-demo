<!--
 * @Author: dyn
 * @Date: 2021-11-15 09:43:13
 * @LastEditTime: 2021-11-17 14:39:38
 * @LastEditors: Please set LastEditors
 * @Description: 小标题标签
 * @FilePath: \cangnan-app\src\components\TitleTag.vue
-->
<template>
  <div class="title-tag">
    <span>{{ text }}</span>
  </div>
</template>

<script>
export default {
  name: "TitleTag",
  props: {
    text: String,
  },
  data() {
    return {};
  },
  components: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
@import "@/style/util";
.title-tag {
  font-size: 20px;
  color: #1f1f1f;
  font-weight: bold;
  padding-left: 8px;
  border-left: 4px solid $themeColor;
}
</style>