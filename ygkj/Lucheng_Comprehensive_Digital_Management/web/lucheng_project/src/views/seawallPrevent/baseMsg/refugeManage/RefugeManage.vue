<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:22:02
 * @Description: 避灾点管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\refugeManage\RefugeManage.vue
-->
<template>
  <div class='wrap'>
    <div class="item_box_title">
      <p>避灾点管理</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入名称"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="pointName"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <el-button type="primary" class="right" v-permission="'refugeManage:add'" @click="editTable('新增')()" >新增</el-button>
      <el-button style="margin-right: 10px" v-permission="'refugeManage:export'" class="right" @click="exportTable()">导出</el-button>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="pointName" label="名称" align="center"></el-table-column>
        <el-table-column prop="pointAddress" label="避灾点地址" align="center"></el-table-column>
        <el-table-column prop="pointCapacity" label="可容纳人数" align="center"></el-table-column>
        <el-table-column prop="pointNowCapacity" label="现容纳人数" align="center"></el-table-column>
        <el-table-column prop="contacts" label="联系人" align="center"></el-table-column>
        <el-table-column prop="phone" label="联系方式" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span class="deal_btn deal_btn_look" v-permission="'refugeManage:edit'" @click="editTable('编辑', scope.row.id)">编辑</span>
              <span class="deal_btn deal_btn_del" v-permission="'refugeManage:del'" @click="delTable(scope.row.id)">删除</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="getList"
      />
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
      <refuge-manage-form v-if="editDialog" :id="id" @closeEditDialog="closeEditDialog"></refuge-manage-form>
    </el-dialog>
  </div>
</template>

<script>
import { basMsgBzdApi } from '@/api/seawallPrevent'
import RefugeManageForm from './RefugeManageForm'
import { avoidancePointExport } from "@/utils/export.js";

export default {
  name: 'RefugeManage',
  props: {
    
  },
  components: {
    RefugeManageForm
  },
  data () {
    return {
      pageTotal: 0,
      pageNum:1,
      pageSize:10,
      tableData: [],
      pointName: '',
      id: '',
      title: '新增',
      editDialog: false,
      showEditDialog: false
    };
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList(){
      let res = await basMsgBzdApi.list({
        pointName: this.pointName,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      })
      if(res.code == 0){
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    exportTable() {
      let obj = {
        pointName: this.pointName,
      }
      avoidancePointExport(obj)
    },
    editTable(title, id){
      this.id = id
      this.title = title
      this.showEditDialog = true
      this.editDialog = false
      this.$nextTick(()=>{
        this.editDialog = true
      })
    },
    delTable(id) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      .then(() => {
        basMsgBzdApi.del({ id: id }).then(res=>{
          if(res.code == 0){
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.getList()
          }else{
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    closeEditDialog(){
      this.showEditDialog = false
      this.editDialog = false
      this.getList()
    }
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    @include contentBox();
    height: 100%;
  }
</style>