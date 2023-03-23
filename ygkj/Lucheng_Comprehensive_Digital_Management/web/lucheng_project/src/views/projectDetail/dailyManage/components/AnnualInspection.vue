<!--
 * @Author: wanxin
 * @LastEditTime: 2022-03-08 09:30:32
 * @Descripttion: 年度检查
 * @FilePath: \lucheng_project\src\views\projectDetail\dailyManage\components\AnnualInspection.vue
-->
<template>
  <div class="people-box">
    <div class="people-right">
      <div class="info_option" style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
          margin-bottom: 20px;
        ">
        <template>
          <span style="font-size: 14px; margin-left: 15px">检查报告：</span>
          <el-input
            placeholder="请输入名称"
            prefix-icon="el-icon-search"
            @change="getList"
            v-model="filesName"
            style="width: 240px; margin-right: 15px"
            clearable
          />
        </template>
      </div>
      <div class="tb_wrapper">
        <el-table :data="tableData" border style="width: 100%">
          <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
          <el-table-column prop="tm" label="检查完成时间" align="center"></el-table-column>
          <el-table-column prop="checkName" label="带队检查人" align="center"></el-table-column>
          <el-table-column prop="joinName" label="参加人员" align="center"></el-table-column>
          <el-table-column prop="hidCount" label="隐患数量(个)" align="center"></el-table-column>
          <el-table-column align="center" label="检查照片" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <i v-if="scope.row.checkPic" class="el-icon-document-remove" style="color:#1492ff;font-size:16px" @click="window.open(scope.row.aid)"></i>
                <span v-else>无</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="检查报告" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <i v-if="scope.row.aid" class="el-icon-document-remove" style="color:#1492ff;font-size:16px" @click="window.open(scope.row.mtnPlan)"></i>
                <span v-else>无</span>
              </div>
            </template>
          </el-table-column>
          <!-- <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span class="deal_btn deal_btn_look" @click="editTable('编辑', scope.row.id)">编辑</span>
                <span class="deal_btn deal_btn_del" @click="del(scope.row.id)">删除</span>
              </div>
            </template>
          </el-table-column> -->
        </el-table>
        <Pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[5, 10, 15, 20]" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import { annualInspectionApi } from "@/api/projectDetail";
export default {
  name: 'FundDeclaration',
  data() {
    return {
      tableLoading: false,
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      filesName: '',
      projectType: 1,
      tableData:[]
    };
  },
  mounted() {
    this.getList()
  },
  components: {},
  methods: {
    //表格
    async getList() {
    //   const seawallCode = this.$route.path === '/maintenance' ? this.$store.state.seawallDetail.prcd : '';
      let opt = {
        projectCode: this.$store.state.seawallDetail.prcd,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        projectType: this.projectType,
        seawallName: this.$store.state.seawallDetail.seawallName,
      }
      this.tableLoading = true;
      await annualInspectionApi.list(opt).then((res) => {
        if (res.code == 0) {
          this.tableLoading = false;
          this.tableData = res.data.list;
          this.pageTotal = res.data.totalRows;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.people-box {
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  .people-right {
    @include contentBox();
    width: 100%;
  }
}
.el-select {
  padding-left: 10px;
}
</style>