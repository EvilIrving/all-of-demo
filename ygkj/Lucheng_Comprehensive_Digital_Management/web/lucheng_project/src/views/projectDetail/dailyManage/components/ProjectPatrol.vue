<!--
 * @Author: wanxin
 * @LastEditTime: 2022-03-07 17:57:16
 * @Descripttion: 工程巡查
 * @FilePath: \lucheng_project\src\views\projectDetail\dailyManage\components\projectPatrol.vue
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
          <span style="font-size: 14px">巡查开始时间：</span>
          <el-date-picker
            v-model="slectedTimes"
            type="datetimerange"
            @change="getList"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            >
          </el-date-picker>

          <span style="font-size: 14px; margin-left: 15px">是否有隐患：</span>
          <el-select v-model="hiddenTroubleCount" placeholder="请选择" @change="getList">
            <el-option v-for="item in options" :key="item.value" :label="item.name" :value="item.value" placeholder="请选择">
            </el-option>
          </el-select>
          <!-- <span style="font-size: 14px; margin-left: 15px">文件名称：</span>
          <el-input
            placeholder="请输入名称"
            prefix-icon="el-icon-search"
            @change="getList"
            v-model="filesName"
            style="width: 240px; margin-right: 15px"
            clearable
          /> -->
        </template>
      </div>
      <div class="tb_wrapper">
        <el-table :data="tableData" border style="width: 100%">
          <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
          <el-table-column prop="patrolBegintime" label="巡查开始时间" align="center"></el-table-column>
          <el-table-column prop="patrolEndtime" label="巡查结束时间" align="center"></el-table-column>
          <el-table-column prop="realName" label="巡查人员" align="center"></el-table-column>
          <el-table-column prop="isTrouble" label="是否有隐患" align="center"></el-table-column>
          <!-- <el-table-column label="文件名称" align="center">
              <template slot-scope="scope">
              <div class="deal_box">
                <i v-if="scope.row.mfuAcceptAid" class="el-icon-document-remove" style="color:#1492ff;font-size:16px" @click="lookFile(scope.row.fileUrl)"></i>
                <span v-else>无</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="巡查轨迹" align="center">
            <template slot-scope="scope">
              <div class="deal_box">
                <i v-if="scope.row.mfuAcceptAid" class="el-icon-document-remove" style="color:#1492ff;font-size:16px" @click="lookFile(scope.row.fileUrl)"></i>
                <span v-else>无</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="mfuAcceptRecord" label="速度" align="center"></el-table-column>
          <el-table-column prop="completeTime" label="路程(米)" align="center"></el-table-column>
          <el-table-column prop="completeTime" label="总历时" align="center"></el-table-column> -->
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
import { ProjectPatrolApi } from "@/api/projectDetail";
export default {
  name: 'FundDeclaration',
  data() {
    return {
      tableLoading: false,
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      slectedTimes: [],
      hiddenTroubleCount: '',
      options: [{ name: '有', value: 1 }, { name: '没有', value: 0 }],
      filesName: "",
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
        console.log(this.$store.state.seawallDetail);
    //   const seawallCode = this.$route.path === '/maintenance' ? this.$store.state.seawallDetail.prcd : '';
      let opt = {
        // seawallCode: seawallCode,
        projectCode: this.$store.state.seawallDetail.prcd,
        projectType: this.projectType,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        startTime: this.slectedTimes ? this.slectedTimes[0] : '',
        endTime: this.slectedTimes ? this.slectedTimes[1] : '',
        seawallName: this.$store.state.seawallDetail.seawallName,
        hiddenTroubleCount: this.hiddenTroubleCount
      }
      this.tableLoading = true;
      await ProjectPatrolApi.list(opt).then((res) => {
        if (res.code == 0) {
          this.tableLoading = false;
          this.tableData = res.data.list;
          this.pageTotal = res.data.totalRows;
        }
      });
    },
    del(id) {},
    // lookFile(fileUrl) {
    //   window.open(fileUrl);
    // },
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