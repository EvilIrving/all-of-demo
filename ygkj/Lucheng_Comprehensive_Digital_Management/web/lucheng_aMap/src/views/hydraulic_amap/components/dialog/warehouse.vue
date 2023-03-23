<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-31 16:25:41
 * @Description: 
 * @FilePath: \lucheng_aMap\src\views\hydraulic_amap\components\dialog\warehouse.vue
-->
<template>
  <div class="warehouse">
    <div class="danger-tap">
      <div
        @click="checkTab(index)"
        :class="{ active: index == tapOptions }"
        v-for="(item, index) in tapList"
        :key="item"
      >
        {{ item }}
      </div>
    </div>
    <div v-if="code">
      <warehouseDetail :code='code' v-if="tapOptions == 0" />
      <warehouseFacility :code='code' v-if="tapOptions == 1" />
    </div>
  </div>
</template>

<script>
import warehouseDetail from './components/warehouse/warehouseDetail.vue';
import warehouseFacility from './components/warehouse/warehouseFacility.vue';
export default {
  props: {
    disasterData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      tapList: ['详情', '设备'],
      tapOptions: 0,
      code: '',
    };
  },
  mounted(){
    console.log(this.disasterData, 'this.disasterDatathis.disasterDatathis.disasterData')
    this.code = this.disasterData.code
  },
  components: { warehouseDetail, warehouseFacility },
  methods: {
    checkTab(index) {
      this.tapOptions = index;
    }
  }
};
</script>

<style lang="scss" scoped>
.warehouse {
  width: 100%;
}
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
</style>