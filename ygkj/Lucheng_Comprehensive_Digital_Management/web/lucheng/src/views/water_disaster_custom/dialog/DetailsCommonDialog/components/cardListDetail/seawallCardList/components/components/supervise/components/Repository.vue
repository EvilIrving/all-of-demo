<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-13 11:30:43
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\supervise\components\Repository.vue
-->
<template>
  <div class="page-patrol">
    <!-- <div class="select-box">
      <div class="label">工程类型：</div>
      <div class="item">
        <el-select size="small" class="select" clearable v-model="value" placeholder="请选择">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
        </el-select>
      </div>
    </div> -->
    <v-table :tableData="tableData" :operation="true" :dynamicColumnSetting="dynamicColumnSetting">
      <template v-slot:operation="row">
        <span @click="look(row)" style="color:#00BAFF;cursor: pointer;">查看</span>
      </template>
    </v-table>
    <div class="bottom__pagination">
      <el-pagination
        :current-page.sync="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="totalPage"
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
      />
    </div>
    <lookFile @close='lookFileType = false' :appendType='true' :visible='lookFileType' :fileSrc='fileSrc' type="png" />
  </div>
</template>

<script>
import { knowledgeList } from '@/api/seawall.js';
import lookFile from '@/components/look_pdf/lookFile.vue'
import VTable from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/table/VTable.vue';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      lookFileType: false,
      fileSrc: '',
      type: '',
      dynamicColumnSetting: [
        {
          prop: 'canonicalName',
          label: '规范名称'
        }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1
    };
  },
  components: {
    VTable,
    lookFile
  },
  mounted() {
    this.getPagePatrol();
  },
  methods: {
    async getPagePatrol() {
      let res = await knowledgeList({
        pageNum: this.pageNum,
        pageSize: this.pageSize
      });
      if (res.code == 0 && res.data) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    onSizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1;
      this.getPagePatrol();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getPagePatrol();
    },
    look(row) {
      this.fileSrc = row.data.appendixUrl
      this.lookFileType = true
    }
  }
};
</script>

<style lang="scss" scoped>
.select-box {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  .label {
    color: #fff;
    font-size: 14px;
  }
  .item {
    .select {
      border: 1px solid #ffffff;
      border-radius: 4px;
    }
  }
}
.page-patrol {
  .bottom__pagination {
    padding-top: 10px;
    text-align: right;
  }
}
</style>
