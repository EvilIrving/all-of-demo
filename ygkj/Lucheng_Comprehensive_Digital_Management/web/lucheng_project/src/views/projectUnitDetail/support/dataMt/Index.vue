<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>数据维护</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input placeholder="请输入" @change="getList" prefix-icon="el-icon-search"  v-model="name" style="width: 180px; margin-right: 15px" clearable />
      <el-button  type="primary" class="right" @click="editTable('新增', '')" v-permission="'dataMt:add'" style="margin-right: 24px">新增</el-button>
    </div>

    <div class="tb_wrapper">
      <el-table :data="tableData" ref="chooseTable" stripe border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="materialDate" label="日期" align="center"></el-table-column>
        <el-table-column prop="maintainUnit" label="维护单位" align="center"></el-table-column>
        <el-table-column prop="projectLeader" label="负责人" align="center"></el-table-column>
        <el-table-column prop="particulars" label="详情(问题记录)" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)" v-permission="'dataMt:edit'"
              >编辑</span
              >
              <span class="deal_btn deal_btn_del" @click="delCard(scope.row.id)" v-permission="'dataMt:del'"
              >删除</span
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          :total="pageTotal"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          :page-sizes="[10, 20, 30, 40]"
          @pagination="getList"
      />
    </div>

    <el-dialog :title="title" :visible.sync="showEditDialog" width="960px">
      <data-mt-form v-if="editDialog" :id="id" @closeEditDialog="closeEditDialog"></data-mt-form>
    </el-dialog>

  </div>
</template>

<script>
import dataMtForm from "@/views/projectUnitDetail/support/dataMt/dataMtForm";
import {databasedM} from "@/api/projectUnitDetail";

export default {
  name: "Index",
  props:{},
  components:{
    dataMtForm
  },
  computed:{
    projectInfo(){
      return this.$store.state.projectInfo
    }
  },
  data(){
    return{
      id:'',
      tableData:[],
      name:'',
      title:'新增',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,

      editDialog: false,
      showEditDialog: false,
      fileDialog:false,

    }
  },
  mounted() {
    this.getList()
  },
  methods:{
    async getList() {
      let res = await databasedM.getDataMtList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        pid:this.projectInfo.prcd,
        maintainUnit:this.name
      })
      if (res.code == 0) {
        console.log(res)
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    delCard(id){
      this.$confirm('此操作会删除该条相关所有信息，是否继续','提示',{
        confirmButtonText:'确定',
        cancelButtonText:'取消',
        type:'warning'
      }).then(()=>{
        databasedM.delDataMtList({id:id}).then((res)=>{
          if (res.code == 0) {
            if (this.tableData.length == 1 && this.pageNum > 1) {
              this.pageNum--
            }
            this.getList()
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    editTable(title, id) {
      this.id = id
      this.title = title
      //console.log(id,title)
      this.showEditDialog = true
      this.editDialog = false
      this.$nextTick(() => {
        this.editDialog = true
      })
    },
    handleSelectionChange(val) {
      console.log(val, 'val');
      this.checkItems = val;
      this.num = val.length;
    },
    exportTable() {
      let obj = {
        name: this.name,
        type: this.type,

        prcd:this.projectInfo.prcd
      };
      console.log(obj)
      //exportMtDmPlan(obj);
    },

    closeEditDialog(num) {
      if (num) {
        this.getList()
      }
      this.editDialog = false
      this.showEditDialog = false
    },
  }
}
</script>

<style scoped lang="scss">
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
</style>