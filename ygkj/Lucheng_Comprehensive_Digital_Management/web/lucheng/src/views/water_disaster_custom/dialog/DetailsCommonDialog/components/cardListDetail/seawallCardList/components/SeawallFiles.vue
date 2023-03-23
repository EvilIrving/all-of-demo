<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-28 14:25:30
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\SeawallFiles.vue
-->
<template>
  <div class="files-box flex-box">
    <div class="files-box-left">
      <div class="seawall-title">
        <span class="img"></span>
        <span>文件夹</span>
      </div>
      <div class="tree-box">
        <el-tree ref="tree" :default-expand-all="true" accordion :data="data" :highlight-current="true" node-key="id" :props="defaultProps" :current-node-key="defaultChecked" @node-click="handleNodeClick">
          <div class="custom-tree-node" slot-scope="{ node, data }">
            <div class="node-file">
              <span :class="data.children && data.children.length > 0 ? 'files-icon' : 'file-icon'"></span>
              <span>{{ node.label }}</span>
            </div>
          </div>
        </el-tree>
      </div>
    </div>
    <div class="files-box-right">
      <div class="seawall-title">
        <span class="img"></span>
        <span>文件列表</span>
      </div>
      <div class="table-box">
        <v-table :tableData="tableData" v-loading="loading" :operation="true" :dynamicColumnSetting="dynamicColumnSetting">
          <template v-slot:collectionFlag="row">
            <span>{{ row.data.collectionFlag == 1 ? '收藏' : '-' }}</span>
          </template>
          <template v-slot:operation="row">
            <span style="cursor: pointer" @click="lookFiles(row)" v-if="fileTypeList.indexOf(row.data.fileType) != -1">浏览</span>
            <span style="cursor: pointer" v-else @click="download(row)">下载</span>
          </template>
        </v-table>
        <div class="bottom__pagination">
          <el-pagination :current-page.sync="pageNum" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total,prev, pager, next" :total="totalPage" @size-change="onSizeChange" @current-change="onCurrentChange" />
        </div>
      </div>
    </div>
    <lookFiles v-if="visible" @close="visible = false" :appendType="true" :visible="visible" :type="fileType" :fileSrc="fileSrc" :id="fileId" />
  </div>
</template>

<script>
import VTable from '../../../table/VTable.vue';
import lookFiles from '@/components/look_pdf/lookFile.vue';
import { getSeawallAchievementStructure, getSeawallPageList } from '@/api/seawall.js';
export default {
  components: { VTable, lookFiles },
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      pageSize: 8,
      totalPage: 1,
      pageNum: 1,
      dynamicColumnSetting: [
        {
          prop: 'collectionFlag',
          label: '收藏状态',
          slot: true
        },
        {
          prop: 'fileNo',
          label: '归档编号'
        },
        {
          prop: 'fileName',
          label: '文件名称'
        },
        {
          prop: 'fileType',
          label: '文件类型'
        },
        {
          prop: 'achievementName',
          label: '目录归属'
        },
        {
          prop: 'createTime',
          label: '上传时间'
        },
        {
          prop: 'browseNum',
          label: '访问记录'
        }
      ],
      fileTypeList: 'pdf,jpg,png,jpeg,gif',
      tableData: [],
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      loading: false,
      defaultChecked: '',
      fileId: '',
      fileSrc: '',
      fileType: '',
      visible: false
    };
  },
  mounted() {
    this.getTree();
  },
  methods: {
    handleNodeClick(data) {
      this.defaultChecked = data.id;
      this.getTreeTable();
    },
    async getTree() {
      let res = await getSeawallAchievementStructure({ projectCode: this.code });
      if (res.code == 0) {
        if (res.data.length > 0) {
          this.defaultChecked = res.data[0].id;
          this.getTreeTable();
          this.$nextTick(function () {
            this.$refs.tree.setCurrentKey(this.defaultChecked);
          });
        }
        this.data = res.data;
      }
    },
    async getTreeTable() {
      this.loading = true;
      let res = await getSeawallPageList({
        achievementId: this.defaultChecked,
        projectCode: this.code,
        pageSize: this.pageSize,
        pageNum: this.pageNum
      });
      this.loading = false;
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.totalPage = res.data.total;
      }
    },
    lookFiles(row) {
      this.fileSrc = row.data.fileUrl;
      if (row.data.fileType !== 'pdf') {
        this.visible = true;
      } else {
        window.open(this.fileSrc)
      }
    },
    download(row) {
      window.open(row.data.fileUrl)
    },
    onSizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1;
      this.getTreeTable();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getTreeTable();
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
    .table-box {
      margin-top: 24px;
      .bottom__pagination {
        padding-top: 10px;
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
</style>