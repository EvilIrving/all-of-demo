<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 16:08:11
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\dailyInspection\DailyInspection.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>日常检查</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="name"
          @change="getDailyCheckList"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        />
        <span style="font-size: 14px">时间：</span>
        <el-date-picker
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="date"
          value-format="yyyy-MM-dd"
          type="date"
          placeholder="请选择"
          clearable
          @change="getDailyCheckList"
        >
        </el-date-picker>
      </div>
      <div class="search_right">
        <el-button  type="primary" v-permission="'dailyInspection:export'" @click="exportsData">导出</el-button>
        <!-- <el-button type="primary" @click="editTable('新增')">新增</el-button> -->
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="selection" width="50"> </el-table-column>
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
          fixed
        ></el-table-column>
        <el-table-column
          prop="patrolBegintime"
          label="开始时间"
          width="160"
          align="center"
          fixed
        ></el-table-column>
        <el-table-column
          prop="patrolEndtime"
          label="结束时间"
          width="160"
          align="center"
          fixed
        ></el-table-column>
        <el-table-column
          prop="realName"
          label="巡查员"
          align="center"
          fixed
        >
        <template slot-scope="scope">
            <span>{{scope.row.realName ? scope.row.realName : '-'}}</span>
        </template>
        </el-table-column>
        <el-table-column
          prop="domType"
          label="巡查记录表"
          align="center"
        >
            <template slot-scope="scope">
                <div v-if="scope.row.uploadFiles">
                    <div
                        v-for="(item, index) in scope.row.uploadFiles"
                        :key="index"
                        @click="preview(item.fileUrl)"
                    >
                        <i
                        class="el-icon-link"
                        style="color: #409eff; cursor: pointer"
                        ></i>
                    </div>
                </div>
                <span v-else>无</span>
            </template>
        </el-table-column>
        <!-- <el-table-column
          prop="problemDescribe"
          label="问题类型"
          align="center"
        >
        <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="lookSluiceGate(scope.row.sOrderCode)"
                >详情</span
              >
            </div>
          </template>
        </el-table-column> -->
<!--        <el-table-column
          prop="domType"
          label="文件名"
          width="100"
          align="center"
        >
            <template slot-scope="scope">
            <div class="deal_box">
              &lt;!&ndash; <span
                class="deal_btn deal_btn_look"
                @click="lookSluiceGate(scope.row.sOrderCode)"
                >流程跟踪</span
              > &ndash;&gt;
              <el-popover
                placement="top"
                width="400"
                trigger="click">
                &lt;!&ndash; <el-table :data="gridData">
                    <el-table-column width="150" property="date" label="日期"></el-table-column>
                    <el-table-column width="100" property="name" label="姓名"></el-table-column>
                    <el-table-column width="300" property="address" label="地址"></el-table-column>
                </el-table> &ndash;&gt;
                <div>
                    <div class="popover_title">审核员：XXX<span>2022-03-19 15:00:03</span></div>
                    <div class="popover_content">审核意见:</div>
                </div>
                <div>
                    <div class="popover_title">校核员：XX<span>2022-03-19 15:00:03</span></div>
                    <div class="popover_content">校核意见:</div>
                </div>
                <span class="deal_btn deal_btn_look" slot="reference">流程跟踪</span>
              </el-popover>
            </div>
          </template>
        </el-table-column>-->
        <el-table-column label="巡查轨迹" align="center">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="lookTrajectory(scope.row.id)"
                >查看</span
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="hiddenTroubleCount" label="是否有隐患" width="120" align="center">
          <template slot-scope="scope">
            <div>
              <span v-if="scope.row.hiddenTroubleCount == 0">巡查正常</span>
              <span v-else>巡查异常</span>
            </div>
          </template>
        </el-table-column>

        <!-- <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                >删除</span
              >
            </div>
          </template>
        </el-table-column> -->
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="getDailyCheckList"
      />
    </div>
    <el-dialog
      :title="title"
      width="960px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <Trajectory @close="closeDialog" :id="id" v-if="visibleDialog" />
    </el-dialog>
  </div>
</template>

<script>
import Trajectory from './components/Trajectory.vue'
import { dailyCheckList } from '@/api/projectUnitDetail.js'
import { exportDailyCheck } from "@/utils/export.js";

export default {
  data() {
    return {
      dispatchType: '',
      date: '',
      name: '',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [{gateOpenTm: '1', gateCloseTm: '1'}],
      visibleDialog: false,
      id: '',
      title: '轨迹详情',
      soId: '',
      sluiceDialog: false,
      tunnelSpillwayDialog: false,
    }
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo
    },
  },
  components: {
    Trajectory
  },
  mounted() {
    this.getDailyCheckList()
  },
  methods: {
    async getDailyCheckList() {
      await dailyCheckList({
          date : this.date,
          pid: this.projectDetail.prcd,
          name: this.name,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            console.log(res.data.list);
            this.tableData = res.data.list
            this.pageTotal = res.data.totalRows
          }
        })
    },
    handleClose() {
      this.visibleDialog = false
      this.sluiceDialog = false
      this.tunnelSpillwayDialog = false
    },
    closeDialog() {
      this.visibleDialog = false
      // this.getLoadPageDisDom()
    },
    editTable(type, id) {
      // console.log(id);
      this.id = id
      this.title = type
      this.visibleDialog = true
    },
    lookSluiceGate(soId) {
      this.soId = soId
      this.sluiceDialog = true
    },
    lookSpillwayTunnel(soId) {
      this.soId = soId
      this.tunnelSpillwayDialog = true
    },
    preview(fileUrl) {
      // console.log(fileUrl);
      window.open(fileUrl)
    },
    exportsData() {
      let obj = {
        pid: this.projectDetail.prcd,
        date: this.date,
      };
      exportDailyCheck(obj);
    },
    lookTrajectory(id) {
      this.id = id
      this.visibleDialog = true
    }
  },
}
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .list_search_box {
    @include flexbox;
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
  .point {
      display: inline-block;
      border-radius: 50%;
      width: 6px;
      height: 6px;
      background: #01A59C;
      vertical-align: middle;
      margin-right: 7px;
      &.blue {
        background: #1492FF;
      }
      &.red {
        background: #FF0000;
      }
   }
}
.popover_title {
    span {
        float: right;
    }
}
.popover_content {
    background: rgba(216, 216, 216, 0.19);
    border-radius: 2px;
}
::v-deep .el-dialog__body {
    padding: 0;
}
</style>