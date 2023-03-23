<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-23 16:35:51
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\components\SurroundingAnalysis.vue
-->
<template>
  <div class="water-box">
    <div class="water-from">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="距离范围：">
          <el-select size="small" v-model="form.region" placeholder="请选择" @change="peripheralAnalysis">
            <el-option :label="item.label" :value="item.value" v-for="item in options" :key="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="files-box flex-box">
      <div class="files-box-left">
        <div class="tree-box">
          <el-tree
            ref="tree"
            :default-expand-all='true'
            accordion
            :data="data"
            :highlight-current="true"
            node-key="name"
            :props="defaultProps"
            :current-node-key="defaultChecked"
            @node-click="handleNodeClick"
          >
            <div class="custom-tree-node" slot-scope="{ node }">
              <div class="node-file">
                <span>{{ node.label }}</span>
              </div>
            </div>
          </el-tree>
        </div>
      </div>
      <div class="files-box-right">
        <div class="table-box water-table-box">
          <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import { dispatchApi } from '@/api/dataScreen/rescueSupport/dispatch.js';
export default {
  props: {
    lat: {
      type: String | Number,
      default: ''
    },
    lng: {
      type: String | Number,
      default: ''
    }
  },
  components: { PrimaryMapTable },
  data() {
    return {
      columnList: [
        {
          prop: 'projectName',
          label: '名称'
        },
        {
          width: 160,
          prop: 'bas',
          label: '流域'
        },
        {
          prop: 'analysisType',
          label: '类别'
        },
        {
          prop: 'engScal',
          label: '工程等级'
        }
      ],
      dataList: [],
      options: [
        {
          value: '1',
          label: '1KM'
        },
        {
          value: '2',
          label: '2KM'
        },
        {
          value: '3',
          label: '3KM'
        }
      ],
      form: {
        region: '3'
      },
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      defaultChecked: '',
      selectNode:''
    };
  },
  mounted() {
    this.peripheralAnalysis();
  },
  methods: {
    handleNodeClick(val) {
      this.selectNode = val.name;
      if (val.name == '全部') {
        this.dataList = this.formatTableData(val.data);
      } else {
        if (val.codes) {
          this.dataList = this.formatTableData(val.data);
        }
      }
    },
    getWaterLevels() {},
    async peripheralAnalysis() {
      const opt = {
        latitude: this.lat,
        longitude: this.lng,
        distance: this.form.region
      };
      await dispatchApi.peripheralAnalysis(opt).then((res) => {
        if (res.code == 0) {
          this.data = res.data;
          this.defaultChecked = res.data[0].name;
          this.dataList = this.formatTableData(res.data[0].data);
          this.$nextTick(function () {
            this.$refs.tree.setCurrentKey(this.defaultChecked);
          });
        }
      });
    },
    //格式化表格数据
    formatTableData(data) {
      data.map((item) => {
        item.projectName = item.projectName || item.stName || item.name || item.cameraName;
        item.code = item.cameraIndexCode || item.projectCode || item.stCode;
        item.type = item.analysisType;
        return item;
      });
      return data;
    }
  }
};
</script>

<style lang="scss" scoped>
.water-content-box {
  display: flex;
  justify-content: space-between;
  .water-content-title {
    display: flex;
    align-items: center;
    position: relative;
    margin-bottom: 32px;
    span {
      color: #fff;
    }
    img {
      margin-right: 10px;
    }
    &::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 100%;
      height: 4px;
      background: url(~assets/images/long_line.png) no-repeat;
      background-size: 100% 100%;
    }
  }
}
.flex-box {
  display: flex;
  justify-content: space-between;
}
.files-box {
  padding-top: 24px;
  .files-box-left {
    width: 296px;
    .tree-box {
      .node-file {
        display: flex;
        .file-icon {
          width: 13px;
          height: 14px;
          margin-right: 8px;
        }
        .files-icon {
          margin-right: 8px;
          width: 16px;
          height: 14px;
        }
      }
    }
  }
  .files-box-right {
    width: calc(100% - 312px);
    .table-box {
      height: 640px;
      .bottom__pagination {
        padding-top: 10px;
        text-align: right;
      }
    }
  }
}
</style>

<style lang="scss">
.water-from {
  padding-top: 24px;
  .el-form-item__label {
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

<style lang='scss'>
.files-box-left .tree-box .el-tree {
  background: transparent;
  color: #ffffff;
}
.files-box-left .tree-box .el-tree-node__content {
  margin-bottom: 5px;
}
.files-box-left .tree-box .el-tree-node__content:hover {
  background: transparent;
}
.files-box-left .tree-box .el-tree-node:focus > .el-tree-node__content {
  background: linear-gradient(90deg, rgba(0, 196, 255, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
  color: #00baff;
}
.files-box-left .tree-box .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: linear-gradient(90deg, rgba(0, 196, 255, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
  color: #00baff;
}
.files-box-right .table-box .el-table .el-table__row:hover {
  background: url(~assets/images/WaterDisastersImage/seawall-table-row-icon.png) no-repeat;
  background-size: 100% 100%;
}
.files-box-right .table-box .el-table .el-table__row {
  box-sizing: border-box;
  height: 50px;
}
.files-box-right .table-box .el-table .el-table__row.table-warm-row {
  height: 44px;
  background: url(~assets/images/WaterDisastersImage/seawall-table-row-icon.png) no-repeat;
  background-size: 100% 100%;
}
</style>