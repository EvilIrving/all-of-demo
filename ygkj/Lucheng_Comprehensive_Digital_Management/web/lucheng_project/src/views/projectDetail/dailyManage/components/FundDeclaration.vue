<!--
 * @Date: 2021-12-09 14:02:47
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-10 09:50:37
 * @FilePath: \lucheng_project\src\views\projectDetail\dailyManage\components\FundDeclaration.vue
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
          <el-date-picker format="yyyy" value-format="yyyy" @change="getList" v-model="slectedType" type="year" placeholder="选择年">
          </el-date-picker>

        </template>
        <!-- <el-button type="primary" class="right" @click="newProject('新增')">新增</el-button>
        <el-button style="margin-right: 10px" class="right" @click="exportTable()">导出</el-button> -->
      </div>
      <div class="tb_wrapper">
        <el-table :data="personalList" border style="width: 100%">
          <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
          <el-table-column prop="dclrFunds" label="申报资金(万元)" align="center"></el-table-column>
          <el-table-column prop="paidamount" label="已支付的资金(万元)" align="center"></el-table-column>
          <el-table-column prop="selffunds" label="自筹资金(万元)" align="center"></el-table-column>
          <el-table-column prop="superiorfunds" label="上级下达资金（万元）" align="center"></el-table-column>
          <el-table-column prop="usedamount" label="已使用的资金(万元)" align="center"></el-table-column>
          <el-table-column align="center" label="申报文件" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <i v-if="scope.row.aid" class="el-icon-document-remove" style="color:#1492ff;font-size:16px" @click="window.open(scope.row.aid)"></i>
                <span v-else>无</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="计划文件" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <i v-if="scope.row.mtnPlan" class="el-icon-document-remove" style="color:#1492ff;font-size:16px" @click="window.open(scope.row.mtnPlan)"></i>
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
        <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 30, 40]" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import { maintenance } from "@/api/projectDetail";
export default {
  name: 'FundDeclaration',
  data() {
    return {
      tableLoading: false,
      mfType: '',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      slectedType: '',
      personalList:[]
    };
  },
  mounted() {
    this.getList()
  },
  components: {},
  methods: {
    //表格
    async getList() {
      const seawallCode = this.$route.path === '/maintenance' ? this.$store.state.seawallDetail.prcd : '';
      let opt = {
        seawallCode: seawallCode,
        pageSize: this.pageSize,
        year: this.slectedType,
        pageNum: this.pageNum
      }
      this.tableLoading = true;
      await maintenance.mfd(opt).then((res) => {
        if (res.code == 0) {
          this.tableLoading = false;
          this.personalList = res.data.list;
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
</style>