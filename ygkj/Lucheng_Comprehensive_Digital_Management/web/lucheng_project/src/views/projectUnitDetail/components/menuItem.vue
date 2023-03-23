<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-06-07 11:41:55
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\components\menuItem.vue
-->
<template>
  <div @click.stop="selectThirdMenu(itemChild)" v-if="!itemChild.hide">
    <div
      class="menu_item"
      :class="{
        menu_item_child: !showIcon,
        active: $parent.itemActive == itemChild.path,
      }"
    >
      <img
        v-if="$parent.itemActive == itemChild.path && showIcon"
        src="@/assets/images/menu_style1.png"
        alt=""
      />
      <img
        v-if="$parent.itemActive != itemChild.path && showIcon"
        src="@/assets/images/menu_style0.png"
        alt=""
      />
      <span class="menu_item_word" :title="itemChild.title">{{
        itemChild.title
      }}</span>
      <i
        v-if="
          itemChild.children &&
          itemChild.children.length &&
          $parent.isShowMenu != itemChild.path
        "
        class="el-icon-plus arrow"
        style="color: #999"
        @click.stop="actionMenu(itemChild)"
      ></i>
      <i
        v-if="
          itemChild.children &&
          itemChild.children.length &&
          $parent.isShowMenu == itemChild.path
        "
        class="el-icon-minus arrow"
        style="color: #999"
        @click.stop="actionMenu(itemChild)"
      ></i>
    </div>
    <div v-show="showChlidMenu">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    itemChild: Object,
    showIcon: {
      type: Boolean,
      default: true,
    },
  },
  beforeCreate() {
    this.$parent.steps.push(this);
  },
  created() {
    this.setShowChlidMenu(this.$parent.isShowMenu);
  },
  watch: {
    "$parent.isShowMenu": function (val) {
      this.setShowChlidMenu(val);
    },
  },
  data() {
    return {
      showChlidMenu: false,
    };
  },
  methods: {
    setShowChlidMenu(val) {
      if (
        this.itemChild.children &&
        this.itemChild.children.length &&
        val == this.itemChild.path
      ) {
        this.showChlidMenu = true;
      } else {
        this.showChlidMenu = false;
      }
    },
    selectThirdMenu(item) {
      if(item.path == this.$parent.itemActive) {
        return false
      }
      this.$parent.setActiveItem(item);
      this.$parent.setShowMenu(item.path);
      this.$router.push(item.path);
      this.$emit("setPageTitle", item);
    },
    actionMenu(item) {
      if (this.$parent.isShowMenu == item.path) {
        this.$parent.setShowMenu("");
      } else {
        this.$parent.setShowMenu(item.path);
        this.$parent.setActiveItem(item);
        // this.$emit("setPageTitle", item);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.menu_item {
  position: relative;
  .arrow {
    position: absolute;
    top: 12px;
    right: 18px;
    z-index: 99;
  }
  .arrow.close {
    transform: rotate(180deg);
  }
}
.menu_item_child {
  padding-left: 100px !important;
  height: 30px !important;
  line-height: 30px !important;
}
</style>