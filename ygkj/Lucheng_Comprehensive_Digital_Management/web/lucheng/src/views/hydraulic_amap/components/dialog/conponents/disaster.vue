<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-13 14:52:04
 * @Description: 
 * @FilePath: \lucheng\src\views\hydraulic_amap\components\dialog\conponents\disaster.vue
-->
<template>
  <div class="disaster" v-loading="flowDataLoading">
    <div>
      <div class="disaster-box">
        <div class="disaster-title">避灾点名称</div>
        <div class="disaster-info">{{ detail.pointName }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">可容纳人数</div>
        <div class="disaster-info">{{ detail.pointCapacity }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">联系人</div>
        <div class="disaster-info">{{ detail.contacts }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">联系方式</div>
        <div class="disaster-info">{{ detail.phone }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-title">避灾点地址</div>
      <div class="disaster-info">{{ detail.pointAddress }}</div>
    </div>
    <div>
      <div class="disaster-title">避灾点说明</div>
      <div class="disaster-info disaster-info-explain">
        {{ detail.pointDirections }}
      </div>
    </div>
    <div>
      <div class="disaster-title">照片</div>
      <div class="disaster-info">
        <template v-if="pointPhotos.length > 0">
          <el-image class="disaster-info-image" v-for="(item,index) in pointPhotos" :key="index" :src="item" :preview-src-list="[item]"> </el-image>
        </template>
        <span v-else>-</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getWaterFlowList } from '@/api/flow.js';
export default {
  props: {
    disasterData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      detail: {},
      flowDataLoading: false,
      pointPhotos: []
    };
  },
  mounted() {
    this.getFlowList();
  },
  methods: {
    getFlowList() {
      this.flowDataLoading = true;
      getWaterFlowList({
        id: this.disasterData.code
      }).then((res) => {
        if (res.code === 0) {
          this.detail = res.data.list[0];
          if(this.detail.pointPhotos){
            this.pointPhotos = this.detail.pointPhotos.split(',')
          }
        }
        this.flowDataLoading = false;
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.disaster {
  border-top: 1px solid rgba(0, 186, 255, 0.6);
  margin-top: 16px;
  width: 100%;
  .disaster-title {
    color: #00baff;
    font-size: 16px;
    width: 160px;
    padding: 14px 0;
    text-align: center;
  }
  > div {
    display: flex;
    align-items: center;
    border: 1px solid rgba(0, 186, 255, 0.6);
    border-top: none;
    .disaster-box {
      display: flex;
      flex: 1;
      border-right: 1px solid rgba(0, 186, 255, 0.6);
      &:last-child {
        border-right: 0;
      }
    }
    .disaster-info {
      color: #ffffff;
      padding: 14px 0 14px 16px;
      border-left: 1px solid rgba(0, 186, 255, 0.6);
      width: calc(100% - 160px);
      .disaster-info-image {
        width: 100px;
        height: 100px;
        margin-right: 8px;
      }
      &.disaster-info-explain {
        line-height: 24px;
      }
    }
  }
}
</style>