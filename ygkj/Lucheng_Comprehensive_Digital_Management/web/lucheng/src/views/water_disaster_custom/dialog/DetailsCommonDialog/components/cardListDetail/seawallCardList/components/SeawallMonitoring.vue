<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-07 16:43:19
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\SeawallMonitoring.vue
-->
<template>
  <div class="danger-box">
    <div class="danger-tap">
      <div
        :class="{ set_cursor: Boolean(item.cursor), active: index == tabAction }"
        v-for="(item, index) in tabList"
        :key="item.name"
      >
        <div @click="checkTab(index)">{{ item.name }}</div>
      </div>
    </div>
    <div class="table-box">
      <water v-if="tabAction == 0" :code="code" />
      <hyetal v-if="tabAction == 1" :code="code" />
      <!-- <flow v-if="tabAction == 2" :code="code" /> -->
      <!-- <waterQuality v-if="tabAction == 3" :code="code" /> -->
      <ProjectVideo v-if="tabAction == 2" :code="code" />
      <displacement v-if="tabAction == 3" :code="code" />
      <sedimentation v-if="tabAction == 4" :code="code" />
      <!-- <panoramagram v-if="tabAction == 4" :code="code" /> -->
    </div>
  </div>
</template>

<script>
import VTable from '../../../table/VTable.vue';
import ProjectVideo from '@/components/screen_project_dialog/components/ProjectVideo.vue';
import water from './components/water.vue';
import flow from './components/flow.vue';
import hyetal from './components/hyetal.vue';
import waterQuality from './components/waterQuality.vue';
import panoramagram from './components/panoramagram.vue';
import displacement from './components/displacement.vue';
import sedimentation from './components/sedimentation.vue';
import { getSeawallListSt } from '@/api/seawall.js';
import { precipitation } from '@/api/dataScreen/floodJudge/index';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  components: { VTable, ProjectVideo, water, waterQuality, hyetal, flow, panoramagram, displacement, sedimentation },
  data() {
    return {
      tabList: [
        { name: '潮位', cursor: false },
        { name: '雨量', cursor: false },
        { name: '视频', cursor: false },
        { name: '位移', cursor: false },
        { name: '沉降', cursor: false }
      ],
      tabAction: 0
    };
  },
  async mounted() {
    await this.getListSt();
  },
  methods: {
    checkTab(index) {
      this.tabAction = index;
    },
    async getListSt() {
      let res = await getSeawallListSt({
        projectCode: this.code,
        stType: 1,
        projectType: 1
      });
      if (res.code === 0) {
        console.log(res.data.length, '潮位');
        this.tabList[0].cursor = res.data.length === 0 ? true : false;
      }

      let res2 = await getSeawallListSt({
        projectCode: this.code,
        stType: 2,
        projectType: 1
      });
      if (res2.code === 0) {
        console.log(res2.data.length, '雨量');
        this.tabList[1].cursor = res2.data.length === 0 ? true : false;
      }
      await precipitation.getVideoList(this.code).then((res4) => {
        if (res4.code == 0) {
          this.tabList[2].cursor = res4.data.length === 0 ? true : false;
        }
      });
      let res3 = await getSeawallListSt({
        projectCode: this.code,
        stType: 5,
        projectType: 1
      });
      if (res3.code == 0) {
        let displacement = res3.data.filter((item) => item.stcd.indexOf('DI_') != -1) || [];
        let sedimentation = res3.data.filter((item) => item.stcd.indexOf('ES_') != -1) || [];
        displacement.length === 0 ? (this.tabList[3].cursor = true) : (this.tabList[3].cursor = false);
        sedimentation.length === 0 ? (this.tabList[4].cursor = true) : (this.tabList[4].cursor = false);
      }
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
      div {
        width: 100%;
        height: 100%;
      }
      &.set_cursor {
        cursor: not-allowed;
      }
      &.set_cursor div {
        pointer-events: none;
      }
    }
  }
}
.table-box {
  height: 530px;
}
</style>
<style lang="scss">
.query-box {
  padding: 10px 0 16px;
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
  .el-date-editor {
    border: 1px solid #fff;
    border-radius: 4px;
    margin: 0 10px 0px 0px;
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