<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>采购管理</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">仓库名称：</span>
      <el-select v-model="name" placeholder="请选择">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-button  type="primary" class="right" @click="editTable('新增', '')" style="margin-right: 24px">新增</el-button>
    </div>

    <div class="tb_wrapper">
      <el-table :data="tableData" ref="chooseTable" stripe border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" align="center" width="50" ></el-table-column>
        <el-table-column prop="name" label=申请人 align="center"></el-table-column>
        <el-table-column prop="fetchLength" label="采购内容" align="center"></el-table-column>
        <el-table-column prop="averageDepth" label="采购方式" align="center"></el-table-column>
        <el-table-column prop="windDirection" label="申请时间" align="center"></el-table-column>
        <el-table-column prop="speed" label="当前执行人" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                  class="deal_btn deal_btn_look"
                  @click="lookTable('查看', scope.row.id)"
              >查看</span
              >
              <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
              >编辑</span
              >
              <span class="deal_btn deal_btn_del" @click="delCard(scope.row.id)"
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
export default {
  name: "Index",
  props:{},
  components:{},
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
      options:[],
    }
  },
  mounted() {
  },
  methods:{
    getList(){

    },
    closeEditDialog(){

    },
    delCard(){

    },
    editTable(){

    },
    handleSelectionChange(){

    },
    lookTable(){

    }

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