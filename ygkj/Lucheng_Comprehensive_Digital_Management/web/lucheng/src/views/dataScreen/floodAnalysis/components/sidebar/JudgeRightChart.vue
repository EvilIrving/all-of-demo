<!-- 防汛研判右侧 -->
<template>
  <div class="sidebar-box">
    <!-- 水库纳蓄能力 -->
    <div class="reservoir-capacity sidebar-list">
      <p><i></i>水库纳蓄能力</p>
      <el-table :data="receiveTable" style="width: 100%" height="174" highlight-current-row>
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="resName" label="名称"></el-table-column>
        <el-table-column prop="amount" label="纳蓄能力(mm)"> </el-table-column>
      </el-table>
    </div>
    <!-- 水闸排水量 -->
    <div class="reservoir-capacity sidebar-list">
      <p><i></i>水闸排水量</p>
      <el-table :data="sluiceTableData1" style="width: 100%" height="174" highlight-current-row>
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="wagaName" label="名称"></el-table-column>
        <el-table-column prop="desLockDisc" label="排水能力"> </el-table-column>
      </el-table>
    </div>
    <!-- 泵站排水量 -->
    <div class="reservoir-capacity sidebar-list">
      <p><i></i>泵站排水量</p>
      <el-table :data="sluiceTableData2" style="width: 100%" height="174" highlight-current-row>
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pustName" label="名称"></el-table-column>
        <el-table-column prop="insFlow" label="排水能力"> </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { precipitation } from "@/api/dataScreen/floodJudge/index";
export default {
  name: "JudgeRightChart",
  data() {
    return {
      receiveTable: [],
      sluiceTableData1: [],
      sluiceTableData2: []
    };
  },
  mounted() {
    this.$nextTick(()=> {
      this.countStorage();
      this.getCountStorage();
      this.listPustProj();
    })
  },
  methods: {
    //水库纳蓄能力
    async countStorage() {
      let opt = { type: 2 }
      let res = await precipitation.countStorage(opt);
      if (res.code == 0) {
        res.data.map(item => {
          if (!item.amount) {
            item.amount = '-';
          }
        })
        this.receiveTable = res.data;
      }
    },
    // 水闸排水量
    async getCountStorage() {
      let opt = {
        pageNum: 1,
        pageSize: 99999
      }
      let res = await precipitation.floodProjectListWagaProj(opt);
      if (res.code == 0) {
        res.data.list.map(item => {
          if (!item.desLockDisc) {
            item.desLockDisc = '-';
          }
        })
        this.sluiceTableData1 = res.data.list;
      }
    },
    //泵站排水量
    async listPustProj() {
      let opt = {
        pageNum: 1,
        pageSize: 99999
      }
      let res = await precipitation.listPustProj(opt);
      if (res.code == 0) {
        res.data.list.map(item => {
          if (!item.insFlow) {
            item.insFlow = '-';
          }
        })
        this.sluiceTableData2 = res.data.list;
      }
    }
  }
}
</script>
<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
</style>
<style lang='scss' scoped>
.sidebar-box {
  div {
    width: 100%;
    p {
      height: 32px;
      line-height: 32px;
      color: #20d1e9;
      i {
        display: inline-block;
        width: 16px;
        height: 16px;
        background: url(../../../../../assets/images/judge-icon.png) no-repeat;
        vertical-align: middle;
        margin-top: -2px;
        margin-right: 8px;
      }
    }
    div {
      height: calc(100% - 32px);
    }
  }
}
</style>