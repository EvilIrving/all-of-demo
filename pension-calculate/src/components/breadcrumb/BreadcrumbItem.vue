<template>
  <span class="el-breadcrumb__item">
    <span :class="['el-breadcrumb__inner',to?'is-link':'']" ref="link" role="link">
      <slot></slot>
    </span>
    <i class="el-breadcrumb__separator" :class="separatorClass" v-if="separatorClass"></i>
    <span v-else class="el-breadcrumb__separator" role="presentation">{{separator}}</span>
  </span>
</template>

<script>
export default {
  name: 'BreadcrumbItem',
  props: {
    to: {},
    replace: Boolean
  },
  inject: ['elBreadcrumb'],
  data() {
    return {
      separator: '',
      separatorClass: '',
    };
  },
  mounted() {
    this.separator = this.elBreadcrumb.separator
    this.separatorClass = this.elBreadcrumb.separatorClass
    const link = this.$refs.link
    link.setAttribute('role', 'link')
    link.addEventListener('click', _ => {
      const { to, $router } = this
      if (!to || !$router) return
      this.replace ? $router.replace(to) : $router.push(to)
    })
  },
};
</script>

<style scoped lang="scss">
.el-breadcrumb__separator {
  margin: 0 9px;
  font-weight: 700;
  color: #c0c4cc;
  &[class*="icon"] {
    margin: 0 6px;
    font-weight: 400;
  }
}
.el-breadcrumb__item {
  float: left;
  &:last-child .el-breadcrumb__inner {
    &:hover,
    a,
    a:hover {
      font-weight: 400;
      color: #606266;
      cursor: text;
    }
    .el-breadcrumb__separator {
      display: none;
    }
  }
}
.el-breadcrumb__inner {
  color: #606266;
  a,
  &.is-link {
    font-weight: 700;
    text-decoration: none;
    transition: color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    color: #303133;

    &:hover {
      color: #409eff;
      cursor: pointer;
    }
  }
}
</style>
