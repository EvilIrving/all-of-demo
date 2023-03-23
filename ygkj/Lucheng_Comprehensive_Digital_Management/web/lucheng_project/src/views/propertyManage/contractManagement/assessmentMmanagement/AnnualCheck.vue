<!--
 * @Date: 2022-04-26 15:55:33
 * @Author: 
 * @Description: 合同管理-考核管理-年度考评
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-06 15:55:54
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\AnnualCheck.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>年度考评</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">年度：</span>
      <el-date-picker size="mini" v-model="year" value-format="yyyy" format="yyyy" type="year" placeholder="请选择：" @change="getList()">
      </el-date-picker>
      <span style="font-size: 14px; margin-left: 15px">被考评单位：</span>
      <el-select size="mini" v-model="evaluatedUnit" placeholder="请选择被考评单位：" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in evaluatedUnitOptions" :key="item.id" :value="item.id" :label="item.companyName">
        </el-option>
      </el-select>
      <!-- <el-button size="mini" type="primary" class="right" @click="editTable('新增', '')">新增</el-button> -->
      <!-- <el-button size="mini" style="margin-right: 10px;" class="right" @click="exportTable()">导出</el-button> -->
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="year" label="考评时间" align="center"></el-table-column>
        <!-- <el-table-column prop="examUnit" label="考评单位" align="center"></el-table-column> -->
        <el-table-column prop="evaluatedUnit" label="被考评单位" align="center"></el-table-column>
        <el-table-column prop="synthesisScore" label="综合得分" align="center"></el-table-column>
        <el-table-column prop="grade" label="考核评定等级" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <!-- <span class="deal_btn deal_btn_look" @click="editTable('编辑', scope.row.id)">编辑</span> -->
              <span class="deal_btn deal_btn_look" v-permission="'annualCheck:look'" @click="detail(scope.row)">详情</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getList" />
    </div>
  </div>
</template>

<script>
import { regularCheck, propertyUnitApi } from '@/api/propertyManage'
export default {
  name: 'AnnualCheck',
  props: {},
  components: {

  },
  data() {
    return {
      examUnit: '',
      evaluatedUnit: '',
      year: '',
      staffAppraisal: '',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: '',
      title: '新增',
      editDialog: false,
      showEditDialog: false,
      evaluatedUnitOptions: [],

    }
  },
  mounted() {
    this.getList()
    this.getUnitList()
  },
  methods: {
    async getUnitList() {
      let res = await propertyUnitApi.list({ fetchAll: true })
      if (res.code == 0) {
        this.evaluatedUnitOptions = res.data.list
      }
    },
    async getList() {
      let res = await regularCheck.yearList({
        // examUnit: this.examUnit,
        evaluatedUnitId: this.evaluatedUnit,
        year: this.year,
        // staffAppraisal: this.staffAppraisal,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      })
      if (res.code == 0) {
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    detail(rowData) {
      this.$router.replace('/propertyManage/regularCheck?year=' + rowData.year + '&evaluatedUnitId=' + rowData.evaluatedUnitId)
    },
  },
}
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
</style>