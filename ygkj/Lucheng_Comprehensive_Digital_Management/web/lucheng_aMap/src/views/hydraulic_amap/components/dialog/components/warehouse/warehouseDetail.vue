<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-26 19:01:28
 * @Description: 
 * @FilePath: \lucheng\src\views\hydraulic_amap\components\dialog\conponents\components\warehouse\warehouseDetail.vue
-->
<template>
  <div class="disaster">
    <div>
      <div class="disaster-box">
        <div class="disaster-title">仓库名称</div>
        <div class="disaster-info">{{ detail.name || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">仓库类型</div>
        <div class="disaster-info">{{ detail.type || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">联系人</div>
        <div class="disaster-info">{{ detail.chargePerson || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">联系方式</div>
        <div class="disaster-info">{{ detail.chargePhone || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">建造时间</div>
        <div class="disaster-info">{{ detail.bulidTime || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">仓库面积</div>
        <div class="disaster-info">{{ detail.acreage || '-' }}平方米</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">仓库价值</div>
        <div class="disaster-info">{{ detail.materialPrice || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">仓储条件</div>
        <div class="disaster-info">{{ detail.storageConditions || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">仓库结构</div>
        <div class="disaster-info">{{ detail.warehouseStructure || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">交通条件</div>
        <div class="disaster-info">{{ detail.trafficConditions || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">仓库物资</div>
        <div class="disaster-info">{{ detail.warehouseMaterials || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">地理位置</div>
        <div class="disaster-info">{{ detail.geographicalPosition || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-title">详细地址</div>
      <div class="disaster-info">{{ detail.address || '-' }}</div>
    </div>
    <div>
      <div class="disaster-title">备注信息</div>
      <div class="disaster-info disaster-info-explain">
        {{ detail.remarkInformation || '-' }}
      </div>
    </div>
  </div>
</template>

<script>
import { getWaterWarehouses } from '@/api/flow';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      detail: {},
    };
  },
  mounted() {
    this.getWaterLoggingList();
  },
  methods: {
    async getWaterLoggingList() {
      let res = await getWaterWarehouses({
        id: this.code
      });
      if (res.code == 0) {
        this.detail = res.data.list[0];
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.disaster {
  border-top: 1px solid rgba(0, 186, 255, 0.6);
  margin-top: 16px;
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
      padding: 14px 14px 16px;
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