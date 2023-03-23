<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-30 10:56:26
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\components\subsidiaryFacilities.vue
-->
<template>
  <div class="disaster">
    <template v-if="detail.stake == 0">
      <div>
        <div class="disaster-title">建筑物名称</div>
        <div class="disaster-info">{{ detail.data.proName || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">主体工程</div>
        <div class="disaster-info">{{ detail.data.prnm || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">类型</div>
        <div class="disaster-info">{{ detail.data.proType || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">简介</div>
        <div class="disaster-info">{{ detail.data.proInfo || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">照片</div>
        <div class="disaster-info">
          <template v-if="detail.data.imgFileURL !== null">
            <el-image class="disaster-info-image" :src="detail.data.imgFileURL" :preview-src-list="[detail.data.imgFileURL]">
            </el-image>
          </template>
          <span v-else>-</span>
        </div>
      </div>
    </template>
    <template v-if="detail.stake == 1">
      <div>
        <div class="disaster-title">桩号</div>
        <div class="disaster-info">{{ detail.data.stake || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">主体工程</div>
        <div class="disaster-info">{{ detail.prnm || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">断面类型</div>
        <div class="disaster-info">{{ detail.data.sectionType || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">断面图</div>
        <div class="disaster-info">
          <template v-if="detail.data.stakeFileURL">
            <el-image v-if="detail.data.stakeFileURL.indexOf('pdf') === -1" class="disaster-info-image" :src="detail.data.stakeFileURL" :preview-src-list="[detail.data.stakeFileURL]">
            </el-image>
            <span v-else style="cursor:pointer" @click="clickToLook(detail.data)">点击查看</span>
          </template>
          <span v-else>-</span>
        </div>
      </div>
      <div>
        <div class="disaster-title">图号</div>
        <div class="disaster-info">{{ detail.data.drawingNo || '-' }}</div>
      </div>
    </template>
    <template v-if="detail.stake == 2">
      <div>
        <div class="disaster-title">工程名称</div>
        <div class="disaster-info">{{ detail.data.proName || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">主体工程</div>
        <div class="disaster-info">{{ detail.prnm || '-' }}</div>
      </div>
      <div>
        <div class="disaster-title">工程等别</div>
        <div class="disaster-info">{{ detail.data.project_scale || '-' }}</div>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  props: {
    detail: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {};
  },
  mounted() {
    console.log(this.detail);
  },
  methods: {
    clickToLook(detail) {
      window.open(detail.stakeFileURL)
    },
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