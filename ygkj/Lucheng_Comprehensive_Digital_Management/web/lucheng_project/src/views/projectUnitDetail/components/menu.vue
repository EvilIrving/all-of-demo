<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-19 16:03:21
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\components\menu.vue
-->
<template>
  <div>
    <div>
      <!-- <menu-item :itemChild="item" :itemActive="itemActive" :isShowMenu='isShowMenu' /> -->
      <slot></slot>
    </div>
  </div>
</template>

<script>
import menuItem from "./menuItem.vue";
export default {
  props: {
    menuList: Array,
    defaultPath: "",
  },
  components: { menuItem },
  // data() {
  //   return {
  //     itemActive: "",
  //     isShowMenu: "",
  //   };
  // },
  data() {
    return {
      steps: [],
      itemActive: "",
      isShowMenu: "",
    };
  },
  watch: {
    steps(steps) {
      steps.forEach((child, index) => {
        child.index = index;
      });
    },
    defaultPath: function(val){
      this.itemActive = val
      this.isShowMenu = this.itemActive
    }
  },
  created() {
    if (this.defaultPath) {
      this.itemActive = this.defaultPath;
    } else {
      this.itemActive =  "";
    }
    this.isShowMenu = this.itemActive
  },
  methods: {
    setActiveItem(item) {
      this.itemActive = item ? item.path : "";
    },
    setShowMenu(item) {
      this.isShowMenu = item;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>