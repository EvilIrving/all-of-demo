<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-28 10:41:33
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\SubsidiaryList.vue
-->
<template>
  <div class="files-box flex-box">
    <div class="files-box-left">
      <div class="tree-box">
        <el-tree ref="tree" :default-expand-all="true" accordion :data="data" :highlight-current="true" node-key="id" :props="defaultProps" :current-node-key="defaultChecked" @node-click="handleNodeClick">
          <div class="custom-tree-node" slot-scope="{ node }">
            <div class="node-file">
              <span>{{ node.label }}</span>
            </div>
          </div>
        </el-tree>
      </div>
    </div>
    <div class="files-box-right">
      <div class="table-box water-table-box" v-if="tableType">
        <primary-map-table :columnList="dynamicColumnSetting" :dataList="tableData"></primary-map-table>
      </div>
      <div class="table-box water-table-box" v-else>
        <v-table :tableData="tableData" :dynamicColumnSetting="dynamicColumnSetting">
          <template v-slot:stakeFileURL="row">
            <span v-if="row.data.stakeFileURL" style="cursor: pointer; color: #00c1ff" @click="lookFiles(row.data.stakeFileURL)">查看</span>
            <span style="cursor: pointer" v-else>-</span>
          </template>
        </v-table>
        <div class="bottom__pagination">
          <el-pagination @current-change="onCurrentChange" :page-size="8" layout="total, prev, pager, next" :total="totalPage">
          </el-pagination>
        </div>
      </div>
    </div>
    <lookFiles v-if="visible" @close="visible = false" :appendType="true" :visible="visible" :type="fileType" :fileSrc="fileSrc" />
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import { bmAccBuildList, loadPage, selectAncillaryBuilding } from '@/api/seawall.js';
import lookFiles from '@/components/look_pdf/lookFile.vue';
import VTable from '../../../table/VTable.vue';
export default {
  components: { PrimaryMapTable, VTable, lookFiles },
  props: {
    code: {
      type: String,
      default: ''
    },
    detailData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      fileType: 'pdf',
      dynamicColumnSetting: [
        {
          prop: 'proName',
          label: '建筑物名称'
        },
        {
          prop: 'prnm',
          label: '主体工程'
        },
        {
          prop: 'proType',
          label: '类型'
        },
        {
          prop: 'proArea',
          label: '面积(㎡)'
        },
        {
          prop: 'proInfo',
          label: '简介'
        }
      ],
      tableData: [],
      data: [
        {
          id: '1',
          name: '附属设施',
          type: '1',
          children: [
            {
              id: '1-1',
              name: '水闸',
              children: []
            },
            {
              id: '1-2',
              name: '泵站',
              children: []
            },
            // {
            //   id: '1-3',
            //   name: '管涵',
            //   children: []
            // },
            {
              id: '1-4',
              name: '倒吸虹',
              children: []
            },
            {
              id: '1-5',
              name: '管理房',
              children: []
            },
            {
              id: '1-6',
              name: '测站平台',
              children: []
            }
          ]
        },
        {
          id: '2',
          name: '交叉建筑物',
          type: '2',
          children: [
            {
              id: '2-2',
              name: '码头',
              type: '2',
              children: []
            },
            {
              id: '2-4',
              name: '桥梁',
              type: '2',
              children: []
            },
            {
              id: '2-1',
              name: '旱闸',
              type: '2',
              children: []
            },
            {
              id: '2-3',
              name: '海塘道路交叉出入口',
              type: '2',
              children: []
            },
            {
              id: '2-5',
              name: '箱涵',
              type: '2',
              children: []
            },
            {
              id: '2-6',
              name: '规模以下水闸',
              type: '2',
              children: []
            },

          ]
        },
        {
          id: '3',
          name: '桩号',
          type: '3',
          children: []
        }
        // {
        //   id: 4,
        //   name: '桥梁',
        //   children: []
        // }
      ],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      defaultChecked: '1',
      defaultCheckedName: '',
      tableType: true,
      pageNum: 1,
      totalPage: 0,
      fileSrc: '',
      visible: false
    };
  },
  mounted() {
    this.$nextTick(function () {
      this.$refs.tree.setCurrentKey(this.defaultChecked);
    });
    this.getSelectAncillaryBuilding();
  },
  methods: {
    handleNodeClick(data) {
      this.defaultCheckedName = data.name;
      if (data.type == 2) {
        this.tableType = true;
        if (this.defaultCheckedName == '交叉建筑物') {
          this.defaultCheckedName = '';
        }
        this.getTreeTable();
      } else if (data.type == 3) {
        this.tableType = false;
        this.pageNum = 1;
        this.getLoadPage();
      } else {
        this.tableType = true;
        if (this.defaultCheckedName == '管理房' || this.defaultCheckedName == '测站平台') {
          this.getTreeTable();
        } else {
          this.getSelectAncillaryBuilding();
        }
      }
    },
    async getTreeTable() {
      let res = await bmAccBuildList({
        prcd: this.code,
        proType: this.defaultCheckedName
      });
      if (res.code == 0) {
        this.dynamicColumnSetting = [
          {
            prop: 'proName',
            label: '建筑物名称'
          },
          {
            prop: 'prnm',
            label: '主体工程'
          },
          {
            prop: 'proType',
            label: '断面类型'
          },
          {
            prop: 'proArea',
            label: '面积(㎡)'
          },
          {
            prop: 'proInfo',
            label: '简介'
          }
        ];
        let tableData = res.data;
        if (this.defaultCheckedName != '管理房' || this.defaultCheckedName != '测站平台') {
          this.tableData = tableData.filter((item) => item.proType != '管理房' || item.proType != '测站平台');
        } else {
          this.tableData = tableData.filter((item) => item.proType == '管理房' || item.proType != '测站平台');
        }
      }
    },
    async getLoadPage() {
      let res = await loadPage({ prcd: this.code, pageNum: this.pageNum, pageSize: 8 });
      if (res.code == 0) {
        this.dynamicColumnSetting = [
          {
            prop: 'stake',
            label: '桩号'
          },
          {
            prop: 'prnm',
            label: '主体工程'
          },
          {
            prop: 'sectionType',
            label: '断面类型'
          },
          {
            prop: 'drawingNo',
            label: '图号'
          },
          {
            prop: 'stakeFileURL',
            label: '断面图',
            slot: true
          }
        ];
        this.tableData = res.data.list.map((item) => {
          item['prnm'] = this.detailData.seawallName;
          return item;
        });
        this.totalPage = res.data.totalRows;
      }
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getLoadPage();
    },
    lookFiles(data) {
      this.fileType = data.indexOf('pdf') !== -1 ? 'pdf' : 'png'
      if (this.fileType === 'pdf') {
        window.open(data)
      } else {
        this.fileSrc = data;
        this.visible = true;
      }

    },
    async getSelectAncillaryBuilding() {
      let res = await selectAncillaryBuilding({ prcd: this.code, projectCategory: this.defaultCheckedName });
      if (res.code == 0) {
        this.dynamicColumnSetting = [
          {
            prop: 'proName',
            label: '工程名称'
          },
          {
            prop: 'project_scale',
            label: '工程等别'
          }
        ];
        this.tableData = res.data;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.flex-box {
  display: flex;
  justify-content: space-between;
  height: 530px;
}
.seawall-title {
  display: flex;
  align-items: center;
  padding-bottom: 10px;
  position: relative;
  color: #ffffff;
  .img {
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
    background-size: 100% 100%;
    width: 20px;
    height: 20px;
    margin-right: 6px;
  }
  &::after {
    content: '';
    position: absolute;
    width: 100%;
    height: 4px;
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview.png) no-repeat;
    background-size: 100% 100%;
    bottom: 0;
  }
}
.files-box {
  padding-top: 24px;
  .files-box-left {
    width: 412px;
    .tree-box {
      margin-top: 24px;
      .node-file {
        display: flex;
        .file-icon {
          width: 13px;
          height: 14px;
          margin-right: 8px;
          background: url(~assets/images/WaterDisastersImage/seawall-file-icon.png) no-repeat;
          background-size: 100% 100%;
        }
        .files-icon {
          margin-right: 8px;
          width: 16px;
          height: 14px;
          background: url(~assets/images/WaterDisastersImage/seawall-files-icon.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }
  }
  .files-box-right {
    width: calc(100% - 428px);
    > .table-box {
      margin-top: 24px;
      height: 100%;
      .bottom__pagination {
        padding-top: 20px;
        text-align: right;
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
.water-table-box {
  .table-box {
    .el-table {
      .has-gutter {
        th {
          background: #239def;
          color: #fff;
        }
      }
    }
  }
}
</style>