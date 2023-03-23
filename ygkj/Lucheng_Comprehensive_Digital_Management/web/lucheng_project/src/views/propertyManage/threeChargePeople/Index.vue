<!--
 * @Date: 2022-04-13 11:14:38
 * @Author: 
 * @Description: 三个责任人
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-12-06 09:59:06
 * @FilePath: \lucheng_project\src\views\propertyManage\threeChargePeople\Index.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>三个责任人</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">工程类型：</span>
      <el-select size="mini" v-model="type" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in typeOptions" :key="item.label" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">工程规模：</span>
      <el-select size="mini" v-model="scale" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in scaleOptions" :key="item.label" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">工程名称：</span>
      <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList()" v-model="name" style="width: 180px; margin-right: 15px" clearable />
      <!-- <el-button type="primary" class="right" @click="editTable('新增', '')">新增</el-button> -->
      <!-- <el-button size="mini" style="margin-right: 10px;" class="right" v-permission="'threeChargePeople:export'" @click="exportTable()">导出</el-button> -->
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" ref="chooseTable" stripe border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="projectName" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="cityAdnm" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryAdnm" label="所在县" align="center"></el-table-column>
        <el-table-column prop="townAdnm" label="所属街道" align="center"></el-table-column>
        <el-table-column prop="projectScale" label="规模" align="center"></el-table-column>
        <el-table-column prop="update_time" label="更新时间" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span class="deal_btn deal_btn_look" @click="look(scope.row.id)" v-permission="'threeChargePeople:look'">查看责任人</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getList" />
    </div>
    <el-dialog title="查看"  :visible.sync="showEditDialog" width="70%">
      <charge-people-detail v-if="editDialog" :id="id" @closeEditDialog="closeEditDialog" />
    </el-dialog>
  </div>
</template>

<script>
import { chargePeopleApi } from '@/api/propertyManage'
import ChargePeopleDetail from './ChargePeopleDetail.vue'
import { exportWareHouses } from "@/utils/export.js";
export default {
  name: 'threeChargePeopleList',
  props: {},
  components: {
    ChargePeopleDetail,
  },
  data() {
    return {
      name: '',
      type: '',
      typeOptions: [
        { label: '全部', value: '' },
        { label: '水库', value: '水库' },
        { label: '水闸', value: '水闸' },
        { label: '海塘', value: '海塘' },
        { label: '堤防', value:  '堤防' },
        { label: '泵站', value: '泵站' },
      ],
      scale: '',
      scaleOptions: [
        { label: '全部', value: '' },
        { label: '大（1）型', value: 1 },
        { label: '大（2）型', value: 2 },
        { label: '中型', value: 3 },
        { label: '小（1）型', value: 4 },
        { label: '小（2）型', value: 5 },
      ],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: '',
      title: '新增',
      editDialog: false,
      showEditDialog: false,
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      let res = await chargePeopleApi.queryByPage({
        projectName: this.name,
        type: this.type,
        projectScale: this.scale,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      })
      if (res.code == 0) {
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    look(id) {
      this.id = id
      this.showEditDialog = true
      this.editDialog = false
      this.$nextTick(() => {
        this.editDialog = true
      })
    },
    exportTable() {
      let obj = {
        name: this.name,
        type: this.type,
      };
      exportWareHouses(obj);
    },
    handleSelectionChange(val) {
      console.log(val, 'val');
      this.checkItems = val;
      this.num = val.length;
    },
    closeEditDialog() {
      this.showEditDialog = false
      this.editDialog = false
      this.getList()
    },
  },
}
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
</style>