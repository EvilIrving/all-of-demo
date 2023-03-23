<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-27 10:52:53
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\simple\components\SeawallAlarm.vue
-->
<template>
  <div class="warp-box-table">
    <div class="filtrate-box" v-if="type == '海塘报警' || type == '海塘预警'">
      <div class="label">筛选：</div>
      <div class="radio-box">
        <el-radio class="radio-item" @change="handleChange" v-model="radio" label="1">蓝色（{{ seawallData.BlueLevel.length }}）</el-radio>
        <el-radio class="radio-item" @change="handleChange" v-model="radio" label="2">黄色（{{ seawallData.YellowLevel.length }}）</el-radio>
        <el-radio class="radio-item" @change="handleChange" v-model="radio" label="3">橙色（{{ seawallData.OrangeLevel.length }}）</el-radio>
        <el-radio class="radio-item" @change="handleChange" v-model="radio" label="4">红色（{{ seawallData.RedLevel.length }}）</el-radio>
      </div>
    </div>
    <div class="map-table-box" :class="{ 'map-table': type != '海塘报警' && type != '海塘预警' }">
      <primary-map-table :columnList="columnList" :dataList="dataList"> </primary-map-table>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import { mapGetters } from 'vuex';
export default {
  props: {
    type: {
      type: String,
      default: ''
    }
  },
  watch: {
    type: async function (val) {
      await this.setHeader(val);
      await this.processingData();
    },
    simplePotenData: {
      handler: function (val) {
        this.processingData();
      },
      deep: true
    }
  },
  computed: {
    ...mapGetters(['simplePotenData'])
  },
  data() {
    return {
      radio: '1',
      columnList: [],
      dataList: [],
      seawallData: {
        BlueLevel: [],
        YellowLevel: [],
        OrangeLevel: [],
        RedLevel: []
      }
    };
  },
  components: {
    PrimaryMapTable
  },
  mounted() {
    this.setHeader(this.type);
    this.processingData();
  },
  methods: {
    setHeader(val) {
      switch (val) {
        case '海塘报警':
          this.columnList = [
            {
              prop: 'seawallName',
              label: '海塘名称'
            },
            {
              prop: 'tdz',
              label: '实时潮位(m)',
              width: 105
            },
            {
              prop: 'warningTide',
              label: '警戒水位(m)',
              width: 105
            }
          ];
          break;
        case '海塘预警':
          this.columnList = [
            {
              prop: 'seawallName',
              label: '海塘名称'
            },
            {
              prop: 'tdz',
              label: '实时潮位(m)',
              width: 105
            },
            {
              prop: 'warningTide',
              label: '警戒水位(m)',
              width: 105,
              slot: true
            }
          ];
          break;
        case '位移报警':
          this.columnList = [
            {
              prop: 'gcmc',
              label: '海塘名称'
            },
            {
              prop: 'zzjg',
              label: '实时位移(mm)',
              width: 130
            },
            {
              prop: 'maxValue',
              label: '警戒上限位移(mm)',
              width: 130
            },
            {
              prop: 'minValue',
              label: '警戒下限位移(mm)',
              width: 130
            }
          ];
          break;
        default:
          this.columnList = [
            {
              prop: 'gcmc',
              label: '海塘名称',
              width: 90
            },
            {
              prop: 'zzjg',
              label: '实时沉降(mm)',
              width: 130
            },
            {
              prop: 'maxValue',
              label: '警戒上限沉降(mm)',
              width: 130
            },
            {
              prop: 'minValue',
              label: '警戒下限沉降(mm)',
              width: 130
            }
          ];
          break;
      }
    },
    processingData() {
      if (this.type == '海塘报警' || this.type == '海塘预警') {
        if (this.simplePotenData.list.length > 0) {
          this.seawallData.BlueLevel = this.simplePotenData.list.filter((item) => item.status == 2);
          this.seawallData.YellowLevel = this.simplePotenData.list.filter((item) => item.status == 3);
          this.seawallData.OrangeLevel = this.simplePotenData.list.filter((item) => item.status == 4);
          this.seawallData.RedLevel = this.simplePotenData.list.filter((item) => item.status == 5);
          this.radio = '1';
          this.dataList = this.seawallData.BlueLevel.map((item) => {
            item['warningTide'] = item.tideStationBlueLevel;
            return item;
          });
        }
      } else {
        this.dataList = this.simplePotenData || [];
      }
    },
    handleChange() {
      if (this.radio == 1) {
        this.dataList = this.seawallData.BlueLevel.map((item) => {
          item['warningTide'] = item.tideStationBlueLevel;
          return item;
        });
      } else if (this.radio == 2) {
        this.dataList = this.seawallData.YellowLevel.map((item) => {
          item['warningTide'] = item.tideStationYellowLevel;
          return item;
        });
      } else if (this.radio == 3) {
        this.dataList = this.seawallData.OrangeLevel.map((item) => {
          item['warningTide'] = item.tideStationOrangeLevel;
          return item;
        });
      } else {
        this.dataList = this.seawallData.RedLevel.map((item) => {
          item['warningTide'] = item.tideStationRedLevel;
          return item;
        });
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
  display: flex;
  color: #fff;
  .label {
    width: 50px;
  }
  .radio-box {
    width: calc(100% - 50px);
    display: flex;
    justify-content: space-between;
    flex-flow: wrap;
    .radio-item {
      width: 50%;
      margin-right: 0;
      margin-bottom: 12px;
      color: #fff;
    }
  }
}
.map-table-box {
  height: calc(100% - 58px);
  width: 100%;
  &.map-table {
    height: 100%;
  }
}
</style>

<style>
.radio-box .radio-item .el-radio__input.is-checked .el-radio__inner {
  border-color: #00baff;
  background: #00baff;
}
.radio-box .radio-item .el-radio__input.is-checked + .el-radio__label {
  color: #00baff;
}
.map-table-box .table-header {
  background: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
  font-size: 14px;
}
.map-table-box .el-table tr.table-header:hover {
  background: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
}
</style>