<template>
  <div class="sluice-box">
    <div class="ou-river">
      <img src="@/assets/images/distributionMap/oujiangx.png" alt="" />
    </div>
    <div class="sluice-list">
      <div :class="item.riverSluice" v-for="(item, index) in latestGatage" :key="item.wagaCode">
        <div class="sluice-icon" :class="item.sluiceIcon"></div>
        <el-popover placement="top" trigger="hover" popper-class="point-sluice-popover">
          <div class="riverway__wrapper">
            <div class="riverway__title">{{ item.wagaName }}</div>
            <div class="riverway__content">
              <div class="riverway__content--props">
                <div class="riverway-props-item">
                  <span class="item-label">闸上水位：</span>
                  <span class="item-data">{{ item.upz || '-' }}m</span>
                </div>
                <div class="riverway-props-item">
                  <span class="item-label">闸下水位：</span>
                  <span class="item-data">{{ item.dwz || '-' }}m</span>
                </div>
                <div class="riverway-props-item">
                  <span class="item-label">过闸流量：</span>
                  <span class="item-data">{{ item.ll || '-' }}m³/s</span>
                </div>
                <div class="riverway-props-item">
                  <span class="item-label">闸门开度：</span>
                  <span class="item-data">{{ item.ovs || '-' }}m</span>
                </div>
              </div>
            </div>
          </div>
          <div
            slot="reference"
            class="sluice-name"
            @click="openWagaDialog(item)"
            :class="{ 'blue-name': index === 4 || index > 5, 'long-name': item.wagaName.length >= 5 }"
          >
            {{ item.wagaName }}
            {{ item.gaorNum }}孔
          </div>
        </el-popover>
        <div class="sluice-address" :class="item.sluiceAddress">{{ item.riverName }}</div>
      </div>
    </div>
    <div class="other-river">
      <div class="first-river">
        <div class="feng-river-door">
          <img src="@/assets/images/distributionMap/fengmh.png" alt="" />
        </div>
        <img class="wenrth" src="@/assets/images/distributionMap/wenrth.png" alt="" />
      </div>
      <div class="qidu-river">
        <img src="@/assets/images/distributionMap/qidn.png" alt="" />
      </div>
    </div>
    <PrimaryDialog
      :mainTitle="activeWagaName"
      :hasAnalysis="false"
      :visible="AddCardDialog"
      @handleClose="HandleClose"
      :appendToBody="true"
      :modal="true"
      width="1000px"
    >
      <div class="dialog-content" v-if="AddCardDialog">
        <water-disaster-distribution-map-dialog :pointData="pointData" />
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import { SluiceDistributionMap } from '@/api/bigScreenPart';
import { mapGetters } from 'vuex';
import { PrimaryDialog } from '@/components';
import WaterDisasterDistributionMapDialog from '@/views/WatershedSchedule/components/WaterDisasterDistributionMapDialog.vue';
export default {
  computed: {
    ...mapGetters(['sluiceActive'])
  },
  components: {
    PrimaryDialog,
    WaterDisasterDistributionMapDialog
  },
  watch: {
    sluiceActive: {
      handler: function (val) {
        console.log(val);
        if (val) {
          this.setSluiceClassName(val);
        }
      },
      deep: true
    }
  },
  data() {
    return {
      originalData: [], //水闸数据
      latestGatage: [], //处理之后的水闸数据
      AddCardDialog: false, // 弹框是否显示
      activeWagaName: '', //弹框标题
      pointData: {} //弹框数据
    };
  },
  mounted() {
    this.getLatestGatage();
  },
  methods: {
    //关闭弹框
    HandleClose() {
      this.AddCardDialog = false;
    },
    //打开弹框
    openWagaDialog(item) {
      this.activeWagaName = item.wagaName;
      this.pointData = item;
      this.AddCardDialog = true;
    },
    //请求水闸数据
    async getLatestGatage() {
      let res = await SluiceDistributionMap.latestGatage();
      if (res.code === 0) {
        this.originalData = res.data;
        this.setSluiceClassName();
      }
    },
    //设置class类名
    setSluiceClassName(type) {
      this.latestGatage = this.originalData.map((item, index) => {
        let riverSluice = '';
        let sluiceIcon = '';
        let sluiceAddress = '';
        //根据设计图的位置，设置相对应位置的初始样式
        if (index < 4 || index === 5) {
          riverSluice = 'feng-river-sluice';
          sluiceIcon = 'purple-sulice';
          if (index < 4) {
            sluiceAddress = 'rivername-short';
          }
          if (index == 5) {
            riverSluice += ' marginRight85';
            sluiceAddress = 'rivername-long';
          }
          //根据开度数据设置水闸是否转动，水流是否流动
          if (Number(item.ovs) !== 0) {
            riverSluice += ' flow-feng-river-sluice';
            sluiceIcon += ' rotate';
          }
        } else if (index === 4 || (index > 5 && index < 13)) {
          riverSluice = 'woQi-river-sluice';
          sluiceIcon = 'blue-sulice';
          sluiceAddress = 'rivername-blue';
          if (index == 12) {
            riverSluice += ' marginRight269';
          }
          if (Number(item.ovs) !== 0) {
            riverSluice += ' flow-woQi-river-sluice';
            sluiceIcon += ' rotate';
          }
        } else {
          riverSluice = 'qidu-river-sluice';
          sluiceIcon = 'blue-sulice';
          sluiceAddress = 'rivername-blue';
          if (Number(item.ovs) !== 0) {
            riverSluice += ' flow-qidu-river-sluice';
            sluiceIcon += ' rotate';
          }
        }
        //修改这个水闸的测站名称
        if (item.wagaCode == '3350ee98-1c7b-11ea-8760-6c92bf66b51e') {
          item.wagaName = '河口大闸';
        }
        //根据水闸分析选项设置样式
        if (type && type.length > 0) {
          switch (type[0]) {
            case '控运分析':
              if (item.kyStatus == 1) {
                sluiceIcon += ' red_sluice';
              } else if (item.kyStatus == 2) {
                sluiceIcon += ' gray_sluice';
              }
              break;
            case '负荷分析':
              if (item.llStatus == 1) {
                sluiceIcon += ' yellow_sluice';
              } else if (item.llStatus == 2) {
                sluiceIcon += ' red_sluice';
              } else if (item.llStatus == -1) {
                sluiceIcon += ' gray_sluice';
              }
              break;
            default: {
              if (item.psStatus == 1) {
                sluiceIcon += ' red_sluice';
              }
              break;
            }
          }
        }
        return {
          ...item,
          riverSluice,
          sluiceIcon,
          sluiceAddress
        };
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import '../index.scss';
.sluice-box {
  position: relative;
  .ou-river {
    width: 100%;
    height: 186px;
    background: url(~assets/new_images/projectScheduling/ou-river.png) no-repeat;
    background-size: 100% 100%;
    text-align: center;
    img {
      width: 152px;
      margin-top: 50px;
    }
  }
  .other-river {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    margin-top: 375px;
    .first-river {
      width: 1392px;
      height: 204px;
      background: url(~assets/new_images/projectScheduling/wenRuitang-river.png) no-repeat;
      background-size: 100% 100%;
      display: flex;
      align-items: center;
      .wenrth {
        width: 298px;
        margin-left: 145px;
      }
      .feng-river-door {
        width: 650px;
        height: 165px;
        background: url(~assets/new_images/projectScheduling/feng-river-door.png) no-repeat;
        background-size: 100% 100%;
        text-align: center;
        margin-left: 72px;
        img {
          width: 273px;
          margin-top: 42px;
        }
      }
    }
    .qidu-river {
      width: 526px;
      height: 164px;
      background: url(~assets/new_images/projectScheduling/qidu-river.png) no-repeat;
      background-size: 100% 100%;
      position: absolute;
      right: 0;
      text-align: center;
      img {
        width: 310px;
        margin-top: 42px;
      }
    }
  }
  .sluice-list {
    position: absolute;
    top: 144px;
    padding-left: 176px;
    z-index: 22;
    display: flex;
    > div {
      width: 45px;
      margin-right: 35px;
      position: relative;
      padding-top: 30px;
      .sluice-icon {
        width: 65px;
        height: 65px;
        margin-left: -10px;
        &.purple-sulice {
          background: url(~assets/images/distributionMap/purple-sulice.png) no-repeat;
          background-size: 100% 100%;
        }
        &.blue-sulice {
          background: url(~assets/images/distributionMap/blue-sulice.png) no-repeat;
          background-size: 100% 100%;
        }
        &.gray_sluice {
          background: url(~assets/images/distributionMap/gray_sluice.png) no-repeat;
          background-size: 100% 100%;
        }
        &.red_sluice {
          background: url(~assets/images/distributionMap/red_sluice.png) no-repeat;
          background-size: 100% 100%;
        }
        &.yellow_sluice {
          background: url(~assets/images/distributionMap/yellow_sluice.png) no-repeat;
          background-size: 100% 100%;
        }

        &.rotate {
          animation: rotate 3s linear infinite;
        }
        @keyframes rotate {
          from {
            transform: rotate(0deg);
          }
          to {
            transform: rotate(360deg);
          }
        }
      }
      .sluice-name {
        width: 52px;
        height: 86px;
        background: linear-gradient(180deg, #6e93ff 0%, #4478dd 100%);
        border-radius: 11px;
        border: 2px solid #99d6ff;
        margin-top: 40px;
        margin-left: -4px;
        font-weight: 600;
        color: rgba(255, 255, 255, 0.88);
        font-size: 18px;
        padding-top: 10px;
        line-height: 22px;
        text-align: center;
        cursor: pointer;
        &.blue-name {
          background: linear-gradient(180deg, #2cbdbc 0%, #04948e 100%);
          border: 2px solid #53f2ec;
        }
        &.long-name {
          font-size: 16px;
        }
      }
      .sluice-address {
        width: 38px;
        font-size: 22px;
        font-weight: 600;
        color: rgba(255, 255, 255, 0.88);
        line-height: 24px;
        height: 148px;
        margin: 35px 0 0 3px;
        text-align: center;
        padding-left: 6px;
        writing-mode: vertical-lr; /*从左向右 从右向左是 writing-mode: vertical-rl;*/
        &.rivername-blue {
          background: url(~assets/images/distributionMap/rivername-blue.png) no-repeat;
          background-size: 100% 100%;
        }
        &.rivername-short {
          background: url(~assets/images/distributionMap/rivername-short.png) no-repeat;
          background-size: 100% 100%;
        }
        &.rivername-long {
          height: 210px;
          background: url(~assets/images/distributionMap/rivername-long.png) no-repeat;
          background-size: 100% 100%;
        }
      }
      &.marginRight85 {
        margin-right: 85px;
      }
      &.marginRight269 {
        margin-right: 269px;
      }
      &.feng-river-sluice {
        height: 474px;
        background: url(~assets/new_images/projectScheduling/feng-river-sluice.png) no-repeat;
        background-size: 100% 100%;
        &.flow-feng-river-sluice {
          background: url(~assets/new_images/projectScheduling/flow-qidu-river-sluice.png) no-repeat;
          background-size: 100% 100%;
        }
      }
      &.woQi-river-sluice {
        height: 454px;
        background: url(~assets/new_images/projectScheduling/woQi-river-sluice.png) no-repeat;
        background-size: 100% 100%;
        &.flow-woQi-river-sluice {
          background: url(~assets/new_images/projectScheduling/flow-woQi-river-sluice.png) no-repeat;
          background-size: 100% 100%;
        }
      }
      &.qidu-river-sluice {
        height: 474px;
        background: url(~assets/new_images/projectScheduling/qidu-river-sluice.png) no-repeat;
        background-size: 100% 100%;
        &.flow-qidu-river-sluice {
          background: url(~assets/new_images/projectScheduling/flow-qidu-river-sluice.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }
  }
}
</style>