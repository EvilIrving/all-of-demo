<template>
  <el-dialog
    v-loading="loading"
    element-loading-text="拼命导出中。。。"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
    top="10vh"
    width="1200px"
    destroy-on-close
    append-to-body
    :visible="visible"
    :before-close="beforeClose"
    :close-on-click-modal="false"
    :fullscreen="fullscreen"
    class="data-detail-dialog"
  >
    <div class="data-detail-dialog__title" slot="title">
      <p class="data-detail-dialog__title--label">
        <slot name="title">{{ title }}</slot>
      </p>
      <!-- <i @click="changeFullscreen" :class="['el-icon-full-screen', 'full-screen-icon']"></i> -->
    </div>
    <template v-if="title == '救援队伍'">
      <ReservoirCardList />
    </template>
    <template v-else-if="title == '救援仓库'">
      <warehouse-index />
    </template>
    <template v-else>
      <div v-if="$slots['stat-tabs']" class="data-detail-dialog__statistics-tabs">
        <slot name="stat-tabs"></slot>
      </div>
      <div v-if="$slots.stat" class="data-detail-dialog__statistics">
        <slot name="stat"></slot>
      </div>
      <div v-if="$slots.query" class="data-detail-dialog__query">
        <slot name="query"></slot>
      </div>
      <div class="data-detail-dialog__content">
        <slot></slot>
      </div>
      <div class="data-detail-dialog__bottom">
        <slot name="bottom"></slot>
      </div>
    </template>
  </el-dialog>
</template>
<script>
import ReservoirCardList from './cardListDetail/ReservoirCardList/ReservoirIndex.vue';
import warehouseIndex from './cardListDetail/warehouseCardList/warehouseIndex.vue';
export default {
  name: 'DataDetailDialog',
  props: {
    title: { type: String, default: '详表' },
    visible: { type: Boolean, default: false },
    beforeClose: { type: Function, default: () => {} },
    loading: { type: Boolean, default: false },
    fullscreen: { type: Boolean, default: false }
  },
  components: { ReservoirCardList, warehouseIndex },
  methods: {
    close() {
      this.$emit('update:visible', false);
    },
    changeFullscreen() {
      this.$emit('changeFullScreen');
    }
  }
};
</script>

<style lang="scss">
.data-detail-dialog .el-dialog {
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  height: 833px;
  overflow: hidden;
  background: url(~assets/images/WaterDisastersImage/dialog-bg.png) no-repeat;
  background-size: 100% 100%;
  padding: 28px 20px;
  box-sizing: border-box;
  .el-dialog__headerbtn {
    top: 39px;
    right: 30px;
    font-size: 18px;
    .el-dialog__close {
      color: #fff;
    }
  }
  &.is-fullscreen {
    height: 100%;
  }
  .el-dialog__header {
    background: url(~assets/images/WaterDisastersImage/dialog-title-bg.png) no-repeat;
    background-size: 100% 100%;
    height: 42px;
    padding: 0;
    .data-detail-dialog__title {
      height: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-right: 40px;
      .data-detail-dialog__title--label {
        color: #fff;
        font-size: 18px;
        letter-spacing: 0;
        padding-left: 45px;
      }
      .full-screen-icon {
        color: #00c1ff;
        cursor: pointer;
      }
    }
  }
  .el-dialog__body {
    min-height: 100px;
    margin: 0 2px;
    padding: 2px;
    padding-top: 0;
    // border-bottom: 0.0625rem solid #00c1ff;
    // background-image: linear-gradient(180deg, #1c3357 0%, #0b2045 100%);
    // border: 1px solid #00c1ff;
    border-top: 0;
    flex: 1;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    .data-detail-dialog__statistics-tabs,
    .data-detail-dialog__statistics,
    .data-detail-dialog__query {
      display: flex;
      margin: 0 16px;
      align-items: center;
    }
    .data-detail-dialog__statistics {
      border-bottom: 1px solid #409eff66;
    }
    .data-detail-dialog__query {
      padding: 8px 0;
      color: #fff;
      flex-wrap: wrap;
    }
    .data-detail-dialog__content {
      overflow: auto;
      display: flex;
      flex-direction: column;
      flex-wrap: nowrap;
      flex-grow: 1;
      border-top: 0;
      padding: 0 16px;
      // box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
      // background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
    }
  }
  &:after {
    content: '';
    position: absolute;
    height: calc(100% + 2px);
    width: 100%;
    top: 0;
    left: 0;
    // background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
    //   linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
    //   linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
    //   linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
    //   linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
    //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
    //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
    //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
    // /*设置大小*/
    background-size: 18px 2px, 2px 18px;
    z-index: -1;
  }
  .data-detail-dialog__bottom {
    > div {
      display: flex;
      > div {
        margin-left: auto;
      }
    }
  }
  .bottom__pagination {
    margin: 8px 0;
    color: #fff;
    .el-pagination {
      .el-pagination__total {
        color: #fff;
      }
      .el-pagination__jump {
        color: #fff;
      }
      .el-pager {
        > li {
          background: #0000;
          color: #fff;
          &.active {
            background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
            border-radius: 2px;
          }
        }
      }
    }
  }
  .el-table {
    // 设置勾选框样式
    .el-checkbox__inner {
      border: 0.0625rem solid #00c1ff;
      background-color: #00c1ff;
      background: rgba($color: #000000, $alpha: 0);
    }
    th,
    td {
      padding: 8px 0;
      color: #fff;
      font-size: 12px;
    }
    th {
      padding: 8px 0;
      font-size: 14px;
      color: #00c1ff;
      background-color: rgba(41, 147, 180, 0.1);
    }
  }
}
</style>

<style lang="scss">
/* 筛选输入样式调整 */
.data-detail-dialog {
  .el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  .el-input-number {
    width: 5.25rem;
  }
  .el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  .el-select .el-input .el-select__caret {
    color: #fff;
  }
  input::-webkit-input-placeholder {
    color: #fff;
  }
  .is-checked .el-checkbox__label {
    color: #70adf8;
  }
  .el-radio {
    margin-right: 15px;
  }
  .el-radio__label {
    color: #fff;
  }
  .is-checked .el-checkbox__inner {
    border-color: #70adf8;
    color: #70adf8;
    &::after {
      border-color: #70adf8;
    }
  }
  .el-date-editor .el-range-input {
    background: none;
    color: #fff;
  }
  .el-date-editor .el-range-separator {
    color: #fff;
    line-height: 1.5rem;
  }
  .el-date-editor .el-range__icon {
    color: #fff;
    line-height: 1.4rem;
  }
  .el-date-editor .el-range__close-icon {
    color: #fff;
    line-height: 1.4rem;
  }
  .el-date-editor .el-icon-date {
    line-height: 1;
  }
  .el-table {
    .ascending .sort-caret.ascending {
      border-bottom-color: #1cfff5;
    }
    .descending .sort-caret.descending {
      border-top-color: #1cfff5;
    }
    th {
      color: #1cfff5;
    }
  }
}
</style>
