<!--
 * @Date: 2022-04-24 13:49:24
 * @Author: 
 * @Description: 合同管理-合同验收-年度验收
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-06 15:52:51
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\contractAcceptance\AnnualAcceptance.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>年度验收</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList()" v-model="name" style="width: 180px; margin-right: 15px" clearable />
      <span style="font-size: 14px; margin-left: 15px">年份：</span>
      <el-select size="mini" v-model="year" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in yearOptions" :key="item.label" :label="item.label" :value="item.label">
        </el-option>
      </el-select>
      <el-button size="mini" type="primary" class="right" v-permission="'annualacceptance:add'" @click="editTable('新增', '')">新增</el-button>
      <!-- <el-button style="margin-right: 10px;" class="right" @click="exportTable()">导出</el-button> -->
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="propertyUnitName" label="物业管理单位" align="center"></el-table-column>
        <el-table-column prop="appendix" label="验收资料（附件）" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.appendix !== ''">
              <div v-for="(item, index) in scope.row.files" :key="index" @click="preview(item.fileUrl)">
                <i class="el-icon-paperclip" style="font-size:20px;color: #409eff; cursor: pointer"></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>

        </el-table-column>
        <el-table-column prop="runUnitName" label="运行管理单位" align="center"></el-table-column>
        <el-table-column prop="runAcceptanceTime" label="验收时间" align="center"></el-table-column>
        <el-table-column prop="runAcceptanceComments" label="验收意见" align="center"></el-table-column>
        <el-table-column prop="waterAdministrationUnitName" label="水行政主管部门" align="center"></el-table-column>
        <el-table-column prop="waterAcceptanceTime" label="验收时间" align="center"></el-table-column>
        <el-table-column prop="waterAcceptanceComments" label="验收意见" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span class="deal_btn deal_btn_look" @click="editTable('编辑', scope.row.id)" v-permission="'annualacceptance:edit'">编辑</span>
              <span class="deal_btn deal_btn_del" @click="delTable(scope.row.id)" v-permission="'annualacceptance:del'">删除</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getList" />
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="65%">
      <annual-acceptance-form v-if="editDialog" :id="id" @closeEditDialog="closeEditDialog"></annual-acceptance-form>
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { contractOperate } from '@/api/propertyManage'
import AnnualAcceptanceForm from './AnnualAcceptanceForm.vue'
import { exportContractOperate } from "@/utils/export.js";
import LookFile from "@/components/LookFile.vue";
export default {
  name: 'AnnualAcceptance',
  props: {},
  components: {
    AnnualAcceptanceForm,LookFile
  },
  data() {
    return {
      fileDialog: false,
      url: "",
      name: '',
      year: '',
      yearOptions: [
        { label: 2022, value: '2022' },
        { label: 2021, value: '2021' },
        { label: 2020, value: '2020' },
        { label: 2019, value: '2019' },
        { label: 2018, value: '2018' },
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
      let res = await contractOperate.AAlist({
        name: this.name,
        year: this.year,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      })
      if (res.code == 0) {
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    editTable(title, id) {
      this.id = id
      this.title = title
      this.showEditDialog = true
      this.editDialog = false
      this.$nextTick(() => {
        this.editDialog = true
      })
    },
    delTable(id) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        contractOperate.AAdel({
          id: id
        }).then((res) => {
          if (res.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    exportTable() {
      let obj = {
        name: this.name,
        year: this.year,
      };
      exportContractOperate(obj);
    },
    closeEditDialog() {
      this.showEditDialog = false
      this.editDialog = false
      this.getList()
    },
    preview(url) {
      this.url = url;
      this.fileDialog = true;
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