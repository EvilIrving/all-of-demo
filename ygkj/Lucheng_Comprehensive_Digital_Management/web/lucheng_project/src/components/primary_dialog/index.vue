<template>
  <div class="primary-dialog">
    <el-dialog
      :visible.sync="visible"
      :width="width"
      top="10vh"
      :before-close="handleClose"
      :modal="false"
      :fullscreen="fullscreen"
      :close-on-click-modal="false"
    >
      <div class="tilte-area" slot="title">
        <p class="main-title">{{ mainTitle }}</p>
      </div>
      <div class="dialog-area">
        <slot></slot>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PrimaryDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "900px",
    },
    mainTitle: {
      type: String,
      default: "测试",
    },
    fullscreen: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  components: {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/theme.scss";
.primary-dialog {
  .tilte-area {
    @include flexbox;
    @include flexAC;
    padding: 16px 20px;
    font-size: 18px;
    color: rgba(255, 255, 255, 0.88);
    background: #1492ff;
    // box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.5);
    border-radius: 4px 4px 0 0;
  }
  ::v-deep .el-dialog {
    border-radius: 4px 4px 0 0;
    background: rgba(0, 0, 0, 0.8);
    .el-dialog__header {
      padding: 0;
      background: rgba(0, 0, 0, 0);
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__body {
      background: rgba(0, 0, 0, 0.4);
    }
  }
}
</style>
<style lang="scss">
.primary-dialog .el-input__inner {
  height: 44px;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 4px;
  border-radius: 4px;
  color: #fff;
  border: 0;
}
.primary-dialog .el-select .el-input__inner {
  border: 0;
}
.primary-dialog {
  .el-table {
    background: rgba(0, 0, 0, 0);
    .el-table__row {
      &:hover {
        background: rgba(0, 0, 0, 0);
      }
    }
    &::before {
      height: 0;
    }
  }
  .el-table td,
  .el-table th.is-leaf {
    border: none;
  }
  .el-table th {
    // background: $primaryGradual;
  }
  .el-table tr {
    background-color: #f7f8f8;
    &:hover {
      background: rgba(0, 0, 0, 0);
    }
  }

  .el-table th,
  .el-table tr {
    color: #fff;
    font-size: 14px;
    font-weight: normal;
  }

  .map-dialog .el-table tr {
    background-color: transparent;
  }

  .el-table--striped .el-table__body tr.el-table__row--striped td {
    &:hover {
      background: rgba(0, 0, 0, 0.2);
    }
    background: rgba(0, 0, 0, 0.2);
  }
  .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: rgba(0, 0, 0, 0) !important;
  }
}

.water-rain .primary-dialog .el-dialog .el-dialog__body,
.water-disaster .primary-dialog .el-dialog,
.water-disaster .primary-dialog .el-dialog .el-dialog__body {
  background: rgba(29, 54, 49, 0.7);
}
</style>