<template>
  <div class="el-breamcrumb" aria-label="Breadcrumb" role="navigation">
    <slot name='default'></slot>
  </div>
</template>

<script>
export default {
  name: 'Breadcrumb',
  props: {
    separator: {
      type: String,
      default: '/'
    },
    separatorClass: {
      type: String,
      default: ""
    }
  },
  provide() {
    return {
      elBreadcrumb: this
    }
  },
  mounted() {
    const items = document.querySelectorAll('.el-breadcrumb__item')
    if (items.length) {
      items[items.length - 1].setAttribute('aria-current', 'page')
    }
  },
};
</script>

<style scoped lang="scss">
.el-breadcrumb {
  font-size: 14px;
  line-height: 1;
  &::after,
  &::before {
    display: table;
    content: "";
  }
  &::after {
    clear: both;
  }
}
</style>
