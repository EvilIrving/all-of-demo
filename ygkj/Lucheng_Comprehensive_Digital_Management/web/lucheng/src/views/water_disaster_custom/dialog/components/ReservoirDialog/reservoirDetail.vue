<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-26 10:36:13
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\components\ReservoirDialog\reservoirDetail.vue
-->
<template>
  <div class="disaster">
    <div>
      <div class="disaster-box">
        <div class="disaster-title">队伍名称</div>
        <div class="disaster-info">{{ detail.name || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">抢险专业</div>
        <div class="disaster-info">{{ detail.rescueSpecialty || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">单位性质</div>
        <div class="disaster-info">{{ detail.unitNature || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">编制人数</div>
        <div class="disaster-info">{{ detail.organizationNumber || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">现有人数</div>
        <div class="disaster-info">{{ detail.existingNumber || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">联系方式</div>
        <div class="disaster-info">{{ detail.chargePhone || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">传真</div>
        <div class="disaster-info">{{ detail.chargePhone || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">组织形式</div>
        <div class="disaster-info">{{ detail.organizationalForm || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-box">
        <div class="disaster-title">救援领域</div>
        <div class="disaster-info">{{ detail.rescueField || '-' }}</div>
      </div>
      <div class="disaster-box">
        <div class="disaster-title">救援规模能力</div>
        <div class="disaster-info">{{ detail.rescueCapability || '-' }}</div>
      </div>
    </div>
    <div>
      <div class="disaster-title">详细地址</div>
      <div class="disaster-info">{{(detail.areaName + detail.address) || '-' }}</div>
    </div>
    <div>
      <div class="disaster-title">备注信息</div>
      <div class="disaster-info disaster-info-explain">
        {{ detail.remarks || '-' }}
      </div>
    </div>
  </div>
</template>

<script>
import { getTeamList } from '@/api/flow';
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
    this.getList();
  },
  methods: {
    async getList() {
      let res = await getTeamList({
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