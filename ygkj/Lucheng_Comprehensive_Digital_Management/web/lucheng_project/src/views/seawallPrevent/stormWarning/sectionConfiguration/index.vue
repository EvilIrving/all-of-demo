<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>断面配置</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
          placeholder="请输入"
          prefix-icon="el-icon-search"
          v-model="name"
          style="width: 180px;
          margin-right: 15px"
          @change="getList"
          clearable />
      <el-button  type="primary" class="right" v-permission="'sectionConfiguration:add'" @click="editTable('新增', '')" style="margin-right: 24px">新增</el-button>
    </div>

    <div class="tb_wrapper">
      <el-table :data="tableData" ref="chooseTable" stripe border style="width: 100%" @selection-change="handleSelectionChange">

        <el-table-column type="index" align="center" width="50" ></el-table-column>
        <el-table-column prop="name" label="断面名称" align="center"></el-table-column>
        <el-table-column prop="fetchLength" label="风区长度(m)" align="center"></el-table-column>
        <el-table-column prop="averageDepth" label="堤前平均水深(m)" align="center"></el-table-column>
        <el-table-column prop="windDirection" label="风向(按照规定设置)" align="center"></el-table-column>
        <el-table-column prop="speed" label="风速(自动获取)" align="center"></el-table-column>
        <el-table-column prop="astrotd" label="天文潮数值(m)" align="center"></el-table-column>
        <el-table-column prop="crestElevatio" label="堤顶高程(m)" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
                  v-permission="'sectionConfiguration:edit'"
              >编辑</span
              >
              <span v-permission="'sectionConfiguration:del'" class="deal_btn deal_btn_del" @click="delCard(scope.row.id)"
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
      <configuration-detail v-if="editDialog" :id="id" @closeEditDialog="closeEditDialog"></configuration-detail>
    </el-dialog>

  </div>
</template>

<script>
import configurationDetail from "@/views/seawallPrevent/stormWarning/sectionConfiguration/configurationDetail";
import {stormSurgeWarningApi} from "@/api/seawallPrevent";
export default {
  name: "index",
  props:{},
  components:{
    configurationDetail
  },
  data(){
    return{
      id:'',
      tableData: [''],
      name:'',
      title:'新增',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,

      editDialog: false,
      showEditDialog: false,
    }
  },
  mounted() {
    this.getList()
  },
  methods:{
    editTable(title,id){
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
    async getList(){
      let res = await stormSurgeWarningApi.getSectionList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name:this.name
      })
      if (res.code == 0) {
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    delCard(id){
      this.$confirm('此操作会删除该条目所有信息，是否继续','提示',{
        confirmButtonText:'确定',
        cancelButtonText:'取消',
        type:'warning'
      }).then(()=>{
        stormSurgeWarningApi.delSectionConfiguration({
          id:id
        }).then((res)=>{
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