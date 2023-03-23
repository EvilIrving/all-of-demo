<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-15 15:41:01
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\simple\components\SeawallList.vue
-->
<template>
  <div class="warp-box-table">
    <div class="filtrate-box">
      <div class="title">{{ simpleData.name }}</div>
      <el-popover class="popover" popper-class="popover-box" v-model="actionPointBtnActive" placement="bottom" trigger="hover">
        <el-tree class="tree-box" :data="treeData" show-checkbox default-expand-all node-key="value" ref="tree" highlight-current :props="defaultProps" @check-change="handleTreeChange">
        </el-tree>
        <i slot="reference" @click="handlePointBtnActive" class="icon-sty" :class="actionPointBtnActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1'" />
      </el-popover>
    </div>
    <div class="map-table-box">
      <div class="select-tree-box">
        <el-cascader class="select-cascader" popper-class="cascader-list" v-model="cascaderValue" :options="data" @change="handleCascaderChange"></el-cascader>
      </div>
      <div class="primary-map-table">
        <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
      </div>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import { mapGetters } from 'vuex';
import { alarmPeople } from '@/api/bigScreenPart';
import { drawPolygon } from '@/utils/mapUtil.js';
export default {
  props: {},
  computed: {
    ...mapGetters(['simpleData']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  data() {
    return {
      checkTable: true,
      columnList: [
        {
          prop: 'name',
          label: '名称'
        }
      ],
      cascaderValue: [6, 2],
      dataList: [],
      actionPointBtnActive: false,
      treeData: [
        {
          value: 1,
          label: '保护范围'
        },
        {
          value: 6,
          label: '救援要素',
          children: [
            {
              value: 2,
              label: '救援仓库'
            },
            {
              value: 3,
              label: '救援队伍'
            },
            {
              value: 4,
              label: '避灾点'
            }
          ]
        },
        {
          value: 5,
          label: '社会经济要素',
          children: [
            {
              value: '医院',
              label: '医院'
            },
            {
              value: '学校',
              label: '学校'
            },
            {
              value: '商超',
              label: '商超'
            },
            {
              value: '车站',
              label: '车站'
            },
            {
              value: '加油站',
              label: '加油站'
            },
            {
              value: '银行',
              label: '银行'
            },
            {
              value: '餐馆',
              label: '餐馆'
            },
            {
              value: '住宿',
              label: '住宿'
            },
            {
              value: '景点',
              label: '景点'
            },
            {
              value: '充电站',
              label: '充电站'
            }
          ]
        }
      ],
      data: [
        {
          value: 6,
          label: '救援要素',
          children: [
            {
              value: 2,
              label: '救援仓库'
            },
            {
              value: 3,
              label: '救援队伍'
            },
            {
              value: 4,
              label: '避灾点'
            }
          ]
        },
        {
          value: 5,
          label: '社会经济要素',
          children: [
            {
              value: '医院',
              label: '医院'
            },
            {
              value: '学校',
              label: '学校'
            },
            {
              value: '商超',
              label: '商超'
            },
            {
              value: '车站',
              label: '车站'
            },
            {
              value: '加油站',
              label: '加油站'
            },
            {
              value: '银行',
              label: '银行'
            },
            {
              value: '餐馆',
              label: '餐馆'
            },
            {
              value: '住宿',
              label: '住宿'
            },
            {
              value: '景点',
              label: '景点'
            },
            {
              value: '充电站',
              label: '充电站'
            }
          ]
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      type: 2,
      typeList: [],
      pointPolygonLayer: null
    };
  },
  watch: {
    simpleData: {
      handler: function (val) {
        this.removeMapLayer();
        this.cascaderValue = [1];
        this.type = 2;
        this.seawallAmbitus(false, 2);
        this.$nextTick(() => {
          this.$refs.tree.setCheckedKeys([1]);
          this.getRangebyCode(1);
        });
      },
      deep: true
    }
  },
  components: {
    PrimaryMapTable
  },
  beforeDestroy() {
    this.removeMapLayer();
  },
  mounted() {
    this.seawallAmbitus(false, 2);
    this.$nextTick(() => {
      this.$refs.tree.setCheckedKeys([1]);
      this.getRangebyCode(1);
    });
  },
  methods: {
    removeMapLayer() {
      this.map.drawClusterPoint([], {}, 'pondingLayer');
      this.map.drawClusterPoint([], {}, 'digReservoir1Layer');
      this.map.drawClusterPoint([], {}, 'flowLayer');
      this.map.removeMapLayer('protective-range');
      let arr = this.data[1].children;
      arr.map((item) => {
        console.log(item.value);
        this.setStyleMap(item.value, {}, []);
      });
    },
    handleTreeChange(data, type) {
      if (type) {
        if (data.value != 6 || data.value != 5) {
          this.type = data.value;
          this.seawallAmbitus(true, data.value);
          if (data.value == 1) {
            this.getRangebyCode(1);
          }
        }
      } else {
        if (data.value != 6) {
          if (data.value == 2) {
            this.map.drawClusterPoint([], {}, 'pondingLayer');
          } else if (data.value == 3) {
            this.map.drawClusterPoint([], {}, 'digReservoir1Layer');
          } else if (data.value == 4) {
            this.map.drawClusterPoint([], {}, 'flowLayer');
          } else if (data.value == 1) {
            this.map.removeMapLayer('protective-range');
          } else {
            this.setPoint([], data.value);
          }
        }
      }
    },
    async getRangebyCode(type) {
      let res = await alarmPeople.getRangebyCode({
        prcd: this.simpleData.seawallCode,
        type: type
      });
      if (res.code == 0) {
        if (res.data.length > 0) {
          let point = JSON.parse(res.data[0].point);
          this.pointPolygonLayer = drawPolygon([point], {});
          this.map.addMapLayer(this.pointPolygonLayer, 'protective-range');
        }
      }
    },
    handleCascaderChange(data) {
      if (data[0] == 6) {
        this.type = data[1];
        this.seawallAmbitus(false, data[1]);
      } else {
        console.log(data);
        this.type = data[1];
        this.seawallAmbitus(false, data[1]);
      }
    },
    handlePointBtnActive(e) {
      e.stopPropagation();
      this.actionPointBtnActive = !this.actionPointBtnActive;
    },
    setPoint(arr, type) {
      let pointList = [];
      if (typeof type == 'number') {
        pointList = arr.map((item) => {
          let obj = {
            name: item.name || item.pointName,
            lat: Number(item.latitude),
            lng: Number(item.longitude),
            code: item.id,
            type: this.setType(type),
            props: {}
          };
          if (type == 2) {
            obj.props = {
              仓库面积: `${item.acreage}㎡`,
              仓库价值: `${item.materialPrice.toFixed(2)}亿`,
              联系人: `${item.chargePerson}`,
              联系方式: `${item.chargePhone}`
            };
          } else if (type == 3) {
            obj.props = { 现有人数: `${item.existingNumber}人`, 联系方式: `${item.chargePhone}` };
          } else if (type == 4) {
            obj.props = {
              可容纳人数: `${item.pointCapacity || '-'}人`,
              避灾点地址: `${item.pointAddress}`
            };
          }
          return obj;
        });
      } else {
        pointList = arr.map((item) => {
          let obj = {
            name: item.name,
            lat: +item.latitude,
            lng: +item.longitude,
            type: this.setType(type),
            props: { 地址: `${item.address}` }
          };
          return obj;
        });
      }
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: '',
        srcs: ''
      };
      if (type == 2) {
        style.srcs = require('@/assets/images/legend/icon-warehouse-juhe.png');
        style.src = require('@/assets/images/legend/icon-warehouse.png');
        this.map.drawClusterPoint(pointList, style, 'pondingLayer');
      } else if (type == 3) {
        style.srcs = require('@/assets/images/dianwei-juhe.png');
        style.src = require('@/assets/images/legend/shuiku-juhe.png');
        this.map.drawClusterPoint(pointList, style, 'digReservoir1Layer');
      } else if (type == 4) {
        style.srcs = require('@/assets/images/legend/icon-disaster-juhe.png');
        style.src = require('@/assets/images/legend/icon-disaster.png');
        this.map.drawClusterPoint(pointList, style, 'flowLayer');
      } else {
        this.setStyleMap(type, style, pointList);
      }
    },
    setStyleMap(type, style, pointList) {
      style.src = require(`@/assets/images/legend/society-icon-${type}.png`);
      style.srcs = require(`@/assets/images/legend/society-icon-${type}-juhe.png`);
      this.map.drawClusterPoint(pointList, style, `flowLayer-${type}`);
    },
    setType(type) {
      switch (type) {
        case 1:
          return '保护范围';
        case 2:
          return '救援仓库';
        case 3:
          return '救援队伍';
        case 4:
          return '避灾点';
        default:
          return '社会经济要素';
      }
    },
    async seawallAmbitus(pointType, type) {
      let obj = {};
      if (typeof type == 'number') {
        obj = {
          type: this.type,
          seawallCode: this.simpleData.seawallCode,
          radius: 10
        };
      } else {
        obj = {
          seawallCode: this.simpleData.seawallCode,
          type: 5,
          keyWord: this.type
        };
      }
      let res = await alarmPeople.seawallAmbitus(obj);
      if (res.code == 0) {
        if (pointType) {
          if (res.data && res.data.length > 0) {
            this.setPoint(res.data, type);
          }
        } else {
          if (res.data && res.data.length > 0) {
            let dataList = res.data.map((item) => {
              return {
                name: item.name || item.pointName
              };
            });
            this.dataList = dataList.filter((item) => item.name != '公厕');
          } else {
            this.dataList = [];
          }
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.warp-box-table {
  height: 100%;
}
.filtrate-box {
  padding: 0 10px 10px 0;
  position: relative;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  &::after {
    content: '';
    width: 100%;
    height: 4px;
    position: absolute;
    left: 0;
    bottom: 0;
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview.png) no-repeat;
    background-size: 100% 100%;
  }
  .title {
    display: flex;
    align-items: center;
    padding-left: 26px;
    position: relative;
    width: 90%;
    &::after {
      content: '';
      width: 20px;
      height: 20px;
      position: absolute;
      left: 0;
      top: 0;
      background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
      background-size: 100% 100%;
    }
  }
  .popover {
    width: 26px;
  }
}
.map-table-box {
  height: calc(100% - 58px);
  width: 100%;
  .primary-map-table {
    margin-top: 10px;
    height: calc(100% - 30px);
  }
  .select-tree-box {
    width: 100%;
    .select-cascader {
      border: 1px solid rgba(0, 186, 255, 0.57);
      border-radius: 2px;
      height: 34px;
      width: 100%;
      line-height: 34px;
    }
  }
}
.icon-sty {
  outline: 0 !important;
  cursor: pointer;
}
</style>

<style lang="scss">
.popover-box.el-popover {
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  box-shadow: none;
  border-color: transparent;
  width: 173px !important;
  border-image: linear-gradient(180deg, rgba(35, 227, 251, 1), rgba(35, 157, 239, 1)) 1 1;
  position: relative;
}
.tree-box {
  background-color: transparent;
  color: #ffffff;
}
.tree-box .el-tree-node__content:hover {
  background-color: transparent;
}
.tree-box .el-tree-node__content:hover .el-tree-node__label {
  color: #00baff;
}
.tree-box .el-checkbox__inner {
  border-color: #00baff;
  background-color: transparent;
}
.tree-box .el-checkbox__input.is-checked .el-checkbox__inner {
  border-color: #00baff;
  background-color: #026fe0;
}
.tree-box .el-checkbox__input.is-focus .el-checkbox__inner {
  border-color: #00baff;
}
.tree-box.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: transparent;
}
.tree-box .el-tree-node.is-checked .el-tree-node__content .el-tree-node__label {
  color: #00baff;
}
.tree-box .el-tree-node:focus > .el-tree-node__content {
  background-color: transparent;
}
.selsect-list .item {
  height: 20px;
  padding: 0px 6px;
  font-size: 12px;
  line-height: 20px;
  border-radius: 2px;
  box-sizing: border-box;
  border: 1px solid #dcdee0;
  background-color: transparent;
  color: #ffffff;
  margin-right: 12px;
}
.selsect-list .item .el-icon-close {
  color: #969799;
  font-size: 12px;
}
.selsect-list .item.el-tag .el-tag__close:hover {
  color: #ffffff;
  background-color: transparent;
}
.select-cascader .el-input__inner {
  height: 34px;
  line-height: 34px;
  background-color: transparent;
}
.cascader-list .el-cascader-node.in-active-path .el-cascader-node__label,
.cascader-list .el-cascader-node.is-active .el-cascader-node__label {
  color: #00baff;
}
</style>