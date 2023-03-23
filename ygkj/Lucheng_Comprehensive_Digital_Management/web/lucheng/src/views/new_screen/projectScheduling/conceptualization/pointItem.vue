<template>
  <div class="riverway-center-item">
    <div class="div-width-left" :class="positionClass" v-show="properties.show">
      <div v-if="checkInfo.includes('名称')">{{ detailData.name }}</div>
      <div class="scale-div" v-if="properties.level && checkInfo.includes('水位')">水位: {{ properties.level }}</div>
      <div class="scale-div" v-if="detailData.codeType !== '水库工程' && properties.flow && checkInfo.includes('流量')">
        流量: {{ properties.flow }}m³/s
      </div>
    </div>
    <el-popover placement="top" trigger="hover" popper-class="point-sluice-popover">
      <div class="riverway__wrapper">
        <div class="riverway__title">{{ detailData.name }}</div>
        <div class="riverway__content">
          <div class="riverway__content--props">
            <div v-if="properties.label" class="riverway-props-label">
              <span> {{ properties.label }}</span>
            </div>
            <template v-else>
              <div v-for="(prop, key) of properties.props" :key="key" class="riverway-props-item">
                <span class="item-label">{{ key }}</span>
                <span class="item-data" :style="{ color: key == '汛限水位' ? '#eec80b' : '#1cfff5' }">
                  {{ prop == 'null' ? '-' : prop == 'null m' ? '- m' : prop }}
                </span>
              </div>
            </template>
          </div>
        </div>
      </div>
      <img
        v-show="properties.show"
        class="img-box"
        @click="onPointClick"
        slot="reference"
        :src="imgSrcMap[properties.type][properties.status]"
        alt=""
      />
    </el-popover>
    <PrimaryDialog
      ref="PrimaryDialog"
      :visible="motionDialogShow"
      :main-title="detailData.name"
      width="1200px"
      @handleClose="closeMotionDialogShow"
    >
      <MotionDialog :colseDialog="motionDialogShow" :pointData="detailData" />
    </PrimaryDialog>
  </div>
</template>

<script>
import { cloneObj } from '@/utils/utils';
import { mapGetters } from 'vuex';
import { PrimaryDialog } from '@/components/';
import MotionDialog from '@/views/water_disaster_custom/dialog/MotionDialog/index.vue'; // 概化图tab页面
export default {
  props: {
    position: String,
    pointData: Object,
    sluiceType: Number
  },
  computed: {
    ...mapGetters(['checkInfo']),
    positionClass() {
      switch (this.position) {
        case 'top':
          return 'position-top';
        case 'bottom':
          return 'position-bottom';
        case 'right':
          return 'position-right';
        default:
          return 'position-left';
      }
    }
  },
  watch: {
    pointData: {
      handler: function (val) {
        if (val) {
          this.disposePointData();
        }
      },
      deep: true
    }
  },
  components: {
    MotionDialog,
    PrimaryDialog
  },
  data() {
    return {
      imgSrcMap: {
        小型水库: {
          normal: require('@/assets/new_images/projectScheduling/conceptua-point-01.png'),
          error: require('@/assets/new_images/projectScheduling/conceptua-point-02.png')
        },
        中型水库: {
          normal: require('@/assets/new_images/projectScheduling/conceptua-point-03.png'),
          error: require('@/assets/new_images/projectScheduling/conceptua-point-04.png')
        },
        水闸: {
          normal: require('@/assets/new_images/projectScheduling/conceptua-point-08.png'),
          warning: require('@/assets/new_images/projectScheduling/conceptua-point-07.png'),
          error: require('@/assets/new_images/projectScheduling/conceptua-point-09.png'),
          red: require('@/assets/new_images/projectScheduling/conceptua-point-11.png'),
          green: require('@/assets/new_images/projectScheduling/conceptua-point-10.png')
        },
        测站: {
          normal: require('@/assets/new_images/projectScheduling/conceptua-point-05.png'),
          error: require('@/assets/new_images/projectScheduling/conceptua-point-06.png')
        }
      },
      detailData: {},
      properties: {},
      motionDialogShow: false
    };
  },
  created() {
    this.disposePointData();
  },
  methods: {
    //关闭dialog弹框
    closeMotionDialogShow() {
      this.motionDialogShow = false;
    },
    //打开dialog弹框
    onPointClick() {
      if (this.properties.label) {
        return false;
      }
      this.motionDialogShow = true;
    },
    //处理数字
    isEmpty(value) {
      value = !Number.isNaN(Number(value)) ? Number(value).toFixed(1) : '-';
      return value ? value : '-';
    },
    //处理数据
    disposePointData() {
      let detailData = cloneObj(this.pointData);
      let properties = {};
      detailData.name = detailData.name.replace('（', '(');
      detailData.name = detailData.name.replace('）', ')');
      if (this.sluiceType == 1) {
        properties['show'] = false;
      } else {
        properties['show'] = true;
      }
      if (detailData.overWarnLimit) properties.status = 'error';
      if (detailData.noData) {
        properties.label = '暂无数据';
      }
      if (detailData.codeType == '水闸工程') {
        properties['type'] = '水闸';
        properties.props = {
          闸上水位: this.isEmpty(detailData.upz) + 'm',
          闸下水位: this.isEmpty(detailData.dwz) + 'm',
          过闸流量: this.isEmpty(detailData.flow) + 'm³/s',
          闸门开度: this.isEmpty(detailData.ovs)
        };
        if (this.sluiceType == 1) {
          if (detailData.gatage) {
            properties.status = detailData.status;
            properties['show'] = true;
            properties.props = {
              闸上水位: this.isEmpty(detailData.gatage.upz) + 'm',
              闸下水位: this.isEmpty(detailData.gatage.dwz) + 'm',
              过闸流量: this.isEmpty(detailData.ll) + 'm³/s',
              闸门开度: this.isEmpty(detailData.gatage.ovs)
            };
            if (detailData.name != '仰义水闸') {
              delete properties.label;
            }
          }
        } else {
          if (!detailData.gateOpened) properties.status = 'warning';
        }
      } else if (detailData.codeType == '水库工程') {
        if (detailData.extraInfo.engScal == '中型') {
          properties['type'] = '中型水库';
        } else {
          properties['type'] = '小型水库';
        }
        properties.props = {
          实时水位: this.isEmpty(detailData.waterLevel) + 'm',
          汛限水位: this.isEmpty(detailData.warnLimitLevel) + 'm'
        };
      } else {
        properties['type'] = '测站';
        properties.props = {
          水位: this.isEmpty(detailData.waterLevel) + 'm'
        };
      }
      properties.status = properties.status || 'normal';
      let level = properties.props['闸上水位'] || properties.props['水位'] || properties.props['实时水位'];
      properties.level = level || '';
      properties.flow = detailData.flow;
      this.detailData = detailData;
      this.properties = properties;
    }
  }
};
</script>

<style lang="scss" scoped>
@import '../index.scss';
.div-width-left {
  height: 60px;
  width: 166px;
  > div {
    color: #ffffff;
    font-size: 14px;
    padding-bottom: 8px;
  }
}
.position-top {
  position: absolute;
  top: -65px;
  left: 50%;
  margin-left: -83px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
}
.position-bottom {
  position: absolute;
  bottom: -70px;
  left: 50%;
  margin-left: -83px;
  text-align: center;
}
.position-right {
  position: absolute;
  left: 28px;
  padding-top: 4px;
  text-align: left;
}
.position-left {
  position: absolute;
  right: 28px;
  padding-top: 4px;
  text-align: right;
}
.riverway-center-item {
  position: relative;
  width: 24px;
  height: 24px;
  .img-box {
    width: 100%;
  }
}
</style>

