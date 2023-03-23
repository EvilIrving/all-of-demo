<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-01-10 14:35:20
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\SeawallDanger.vue
-->
<template>
  <div class="danger-box">
    <div class="danger-tap">
      <div @click="checkTab(index)" :class="{ active: index == tabAction }" v-for="(item, index) in tabList" :key="item">
        {{ item }}
      </div>
    </div>
    <div class="table-box">
      <PagePatrol :code="code" v-show="tabAction == 0" />
      <PageDanger :code="code" v-show="tabAction == 1" />
      <PageGreatDanger :code="code" v-show="tabAction == 2" />
    </div>
  </div>
</template>

<script>
import PagePatrol from './components/PagePatrol.vue';
import PageDanger from './components/PageDanger.vue';
import PageGreatDanger from './components/PageGreatDanger.vue';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  components: { PagePatrol, PageGreatDanger, PageDanger },
  data() {
    return {
      tabList: ['工程巡查', '工程隐患', '历史重大险情'],
      tabAction: 0
    };
  },
  methods: {
    checkTab(index) {
      this.tabAction = index;
    }
  }
};
</script>

<style lang="scss" scoped>
.danger-box {
  padding-top: 22px;
  .danger-tap {
    display: flex;
    > div {
      width: 114px;
      height: 32px;
      border: 1px solid rgba(255, 255, 255, 0.6);
      line-height: 32px;
      text-align: center;
      margin-right: 16px;
      color: rgba(255, 255, 255, 0.8);
      cursor: pointer;
      &.active {
        color: #00baff;
        background: rgba(72, 152, 251, 0.1);
        box-shadow: 0px 0px 5px 0px #00baff inset;
        border-color: rgba(0, 186, 255, 0.6);
      }
    }
  }
}
.table-box {
  margin-top: 16px;
}
</style>
<style lang="scss">
.water-from-box {
  padding-bottom: 10px;
  padding-top: 0px;
  .el-form-item__label {
    color: #fff;
  }
  .el-select {
    border: 1px solid #fff;
    border-radius: 4px;
  }
  .el-date-editor {
    border: 1px solid #fff;
    border-radius: 4px;
    margin: 0 10px 5px 0px;
  }
  .el-range-separator {
    background: #000;
    color: #fff;
  }
  .el-range-input {
    background: #000;
    color: #fff;
  }
}
.water-table-box {
  .table-wrap {
    .el-table {
      .table-header {
        th {
          background: #239def;
          color: #fff;
        }
      }
    }
  }
}
</style>