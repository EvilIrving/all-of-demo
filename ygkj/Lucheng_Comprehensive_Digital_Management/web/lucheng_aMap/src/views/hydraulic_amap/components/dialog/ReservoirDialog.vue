<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2022-06-10 19:12:54
 * @LastEditors: dtb
 * @Description: 水库蓄水量落点弹窗
 * @FilePath: \lucheng_aMap\src\views\hydraulic_amap\components\dialog\ReservoirDialog.vue
-->
<template>
  <div class="detail-table">
    <!-- 弹窗标题 -->
    <!-- 内容区域 -->
    <div class="primary-dialog__content">
      <div>
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
        <div class="dialog__content">
          <reservoirDetail :code="resCode" v-if="tapOptions == 0 && resCode" />
          <reservoirPerson :code="resCode" v-if="tapOptions == 1" />
          <reservoirFacility :code="resCode" v-if="tapOptions == 2" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import reservoirDetail from "./components/ReservoirDialog/reservoirDetail.vue";
import reservoirPerson from "./components/ReservoirDialog/reservoirPerson.vue";
import reservoirFacility from "./components/ReservoirDialog/reservoirFacility.vue";
export default {
  name: "ReservoirDialog",
  data() {
    return {
      tapList: ["详情", "人员", "设备"],
      tapOptions: 0,
      resCode: "",
    };
  },
  props: {
    pointData: {
      type: Object,
      default: {},
    },
  },
  components: {
    reservoirDetail,
    reservoirPerson,
    reservoirFacility,
  },
  mounted() {
    this.resCode = this.pointData.code || "";
  },
  computed: {},
  methods: {
    checkTab(index) {
      this.tapOptions = index;
    },
  },
};
</script>
<style lang="scss" scoped>
.dialog__content {
  height: 600px;
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