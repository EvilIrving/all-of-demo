<!--
 * @Author: ly
 * @Date: 2021-08-11 09:32:00
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-01 11:37:14
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\project_supervision\index.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>批后监管</p>
    </div>
    <div class="content-wrap">
      <el-tabs v-model="activeName">
        <el-tab-pane label="批后监管" name="first"></el-tab-pane>
        <el-tab-pane label="举报公示" name="second"></el-tab-pane>
      </el-tabs>
      <div>
        <el-row class="statistics-card-box">
          <div class="statistics-card-sty">
            <i class="supervision-icon-01" />
            <div>
              <p>监管总次数</p>
              <p>
                <span style="color: #2fbeb3">{{
                  homePageStatisticsData.inspectionNum || "-"
                }}</span
                >次
              </p>
            </div>
          </div>
          <div class="statistics-card-sty">
            <i class="supervision-icon-02" />
            <div>
              <p>发现问题数</p>
              <p>
                <span style="color: #f7b500">{{
                  homePageStatisticsData.findProblem || "-"
                }}</span
                >个
              </p>
            </div>
          </div>
          <div class="statistics-card-sty">
            <i class="supervision-icon-03" />
            <div>
              <p>未整改问题数</p>
              <p>
                <span style="color: #fa6400">{{
                  homePageStatisticsData.unDealProblem || "-"
                }}</span
                >个
              </p>
            </div>
          </div>

          <div class="statistics-card-sty">
            <i class="supervision-icon-04" />
            <div>
              <p>逾期未整改</p>
              <p>
                <span style="color: #e02020">{{
                  homePageStatisticsData.outTimeProblem || "-"
                }}</span
                >次
              </p>
            </div>
          </div>

          <div class="statistics-card-sty">
            <i class="supervision-icon-05" />
            <div>
              <p>移交执法部门数</p>
              <p>
                <span style="color: #e02020">{{
                  homePageStatisticsData.hiddenDangerNum || "-"
                }}</span
                >次
              </p>
            </div>
          </div>
        </el-row>
      </div>
      <div class="list_search_box">
        <div class="search_left">
          <span style="font-size: 14px; color: #666666">名称：</span>
          <el-input
            placeholder="请输入"
            v-model="projectName"
            @change="getList"
            clearable
            style="width: 180px; margin: 0 20px 0 0px"
          />
          <span style="font-size: 14px; color: #666666">监管状态：</span>
          <el-select
            style="width: 180px; margin: 0 20px 0 0px"
            v-model="regulatoryStatus"
            placeholder="请选择"
            @change="getList"
          >
            <el-option
              :label="item.label"
              :value="item.value"
              :key="index"
              v-for="(item, index) in regulatoryStatusList"
            />
          </el-select>
          <span style="font-size: 14px; color: #666666">项目阶段：</span>
          <el-select
            style="width: 180px; margin: 0 20px 0 0px"
            v-model="projectStage"
            placeholder="请选择"
            @change="getList"
          >
            <el-option
              :label="item.label"
              :value="item.value"
              :key="index"
              v-for="(item, index) in projectPhaseList"
            />
          </el-select>
        </div>
        <div class="search_right">
          <el-button
            type="primary"
            size="small"
            @click="exportDetail"
            v-permission="'projectSupervision:export'"
            >导出</el-button
          >
          <el-button
            type="primary"
            size="small"
            @click="openDialog"
            v-if="activeName == 'first'"
            v-permission="'projectSupervision:add'"
            >新增项目</el-button
          >
          <el-button
            type="primary"
            size="small"
            @click="Configuration"
            v-if="activeName == 'first'"
            v-permission="'projectSupervision:task'"
            >配置任务</el-button
          >
        </div>
      </div>
      <div class="tb_wrapper">
        <!-- 列表 -->
        <el-table :data="tableData" stripe border style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="80"
            align="center"
            :index="indexMethod"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="projectName"
            label="项目名称"
            align="center"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="adnm"
            label="行政区划"
            align="center"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="basin"
            label="所属流域"
            align="center"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="riverName"
            label="所属河道"
            align="center"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="address"
            label="地址"
            align="center"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="inspectionNum"
            label="巡查次数"
            align="center"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="lastInspectionTime"
            label="最后一次巡查时间"
            align="center"
          >
            <template slot-scope="scope">
              <span>
                {{
                  scope.row.lastInspectionTime
                    ? new Date(scope.row.lastInspectionTime).format(
                        "yyyy-MM-dd hh:mm"
                      )
                    : "-"
                }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="监管状态" align="center">
            <template slot-scope="scope">
              <span
                :class="[
                  statusClassDict[scope.row.regulatoryStatus],
                  'status-class',
                ]"
              >
                {{ regulatoryStatusDict[scope.row.regulatoryStatus] || "" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="项目阶段" prop="proState" align="center">
            <!-- <template slot-scope="scope">
              <span>{{ scope.row.projectStage == '1' ? '前期阶段' : scope.row.projectStage == '2' ? '施工阶段' : '完工阶段' }}</span>
            </template> -->
          </el-table-column>
          <el-table-column prop="z" label="操作" align="center" width="160">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="lookDetails(scope.row)"
                  v-permission="'projectSupervision:look'"
                  >详情</span
                >
                <span
                  v-if="activeName == 'first'"
                  class="deal_btn deal_btn_edit"
                  @click="editDetails(scope.row)"
                  v-permission="'projectSupervision:edit'"
                  >修改</span
                >
                <span
                  v-if="activeName == 'first'"
                  class="deal_btn deal_btn_del"
                  @click="deleteDialog(scope.row)"
                  v-permission="'projectSupervision:del'"
                  >删除</span
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页查询 -->
        <div class="pagination-box">
          <el-pagination
            class="right"
            background
            layout="total,sizes, prev, pager, next"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="pageSize"
            @current-change="pageChange"
            :total="total"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
    </div>
    <!-- 新增弹框 -->
    <NewContentDialog
      v-if="dialogShow"
      :visible="dialogShow"
      :areaOptions="areaOptions"
      :dialogDialog="dialogDialog"
      :type="addType"
      @getList="getList"
      @close="closeDialog"
    />
    <!-- 配置任务 -->
    <ConfigurationTasks
      v-if="allocationDialogShow"
      :visible="allocationDialogShow"
      @close="closeAllocationDialog"
    />
    <!-- 查看详情 -->
    <ViewDetailsDialog
      v-if="ViewDetailsDialogShow"
      :projectData="projectData"
      :visible.sync="ViewDetailsDialogShow"
      :activeTapName="activeName"
    />
  </div>
</template>

<script>
import { PrimaryView, PrimaryTable } from "@/components/";
import { projectSupervisionApi } from "@/api/projectSupervision";
import ConfigurationTasks from "./components/ConfigurationTasks";
import ViewDetailsDialog from "./dialog/viewDetailsDialog";
import NewContentDialog from "./dialog/newContentDialog";
import { exportDetail } from "@/utils/utils";
import config from "@/config/config";

export default {
  name: "project_supervision",
  components: {
    PrimaryView,
    PrimaryTable,
    NewContentDialog,
    ViewDetailsDialog,
    ConfigurationTasks,
  },
  data() {
    return {
      activeName: "first",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      areaCode: null,
      projectStage: null,
      regulatoryStatus: null,
      tabList: ["项目监管"],
      projectName: "",
      // 行政区划数据
      areaOptions: [],
      // 监管状态
      regulatoryStatusList: [
        { value: null, label: "全部" },
        { value: 1, label: "待监管" },
        { value: 5, label: "无问题" },
        { value: 2, label: "待整改" },
        { value: 3, label: "已逾期" },
        { value: 4, label: "已移交" },
        { value: 6, label: "已完工" },
      ],
      // 项目流程
      projectPhaseList: [
        { value: null, label: "全部" },
        { value: 1, label: "前期阶段" },
        { value: 2, label: "施工阶段" },
        { value: 3, label: "完工阶段" },
      ],
      // 配置任务弹窗
      allocationDialogShow: false,
      // 查看详情
      ViewDetailsDialogShow: false,
      tableData: [],
      // 弹窗
      addType: "add",
      dialogShow: false,
      dialogRectificationShow: false,
      dialogExamineShow: false,
      homePageStatisticsData: {},
      projectData: {},
      dialogDialog: {},
      regulatoryStatusDict: [
        "",
        "待监管",
        "待整改",
        "已逾期",
        "已移交",
        "无问题",
        "已完工",
        "已停止",
      ],
      statusClassDict: [
        "",
        "light-yellow",
        "light-red",
        "light-red",
        "light-green",
        "light-green",
        "dark-yellow",
        "deep-dark-yellow",
      ],
    };
  },
  mounted() {
    this.homeOverView(); // 首页统计
    this.getAdcdList(); // 行政区划接口
    this.getList(); //列表数据
  },
  methods: {
    async exportDetail() {
      const url = `${config.baseUrl}/river-service/blueProj/exportDetailTable`;
      const param = {
        key: this.projectName,
        areaCode: this.areaCode,
        projectStage: this.projectStage,
        regulatoryStatus: this.regulatoryStatus,
      };
      exportDetail(url, param);
    },
    // 首页统计
    async homeOverView() {
      let res = await projectSupervisionApi.homeOverView();
      if (res.code == 0) {
        this.homePageStatisticsData = res.data;
      }
    },
    // 查看详情
    lookDetails(val) {
      this.addTilte = "查看项目信息";
      this.projectData = val;
      this.ViewDetailsDialogShow = true;
    },
    editDetails(val) {
      this.dialogShow = true;
      this.isLook = false;
      this.addType = "edit";
      this.addTilte = "修改项目信息";
      this.dialogDialog = val;
    },
    // 关闭详情
    closeViewDetailsDialog() {
      this.ViewDetailsDialogShow = false;
    },
    // 配置任务
    Configuration() {
      this.allocationDialogShow = true;
    },
    // 关闭弹窗
    closeAllocationDialog() {
      this.allocationDialogShow = false;
    },
    // 查看详情
    viewDetails(val) {
      this.isLook = true;
      this.dialogShow = true;
      this.dialogDialog = val;
    },
    // 行政区划
    async getAdcdList() {
      let res = await projectSupervisionApi.getAdcdList({
        areaCode: "330302",
        level: 4,
      });
      if (res.code == 0) {
        this.areaOptions = [
          {
            value: null,
            label: "全部",
          },
        ];
        res.data.forEach((item) => {
          this.areaOptions.push({
            value: item.adcd,
            label: item.adnm,
          });
        });
      }
    },
    // 序号
    indexMethod(index) {
      return (this.pageNum - 1) * this.pageSize + index + 1;
    },
    // 获取列表
    async getList() {
      let opt = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        key: this.projectName,
        projectStage: this.projectStage,
        regulatoryStatus: this.regulatoryStatus,
      };
      let res = await projectSupervisionApi.projectList(opt);
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.total = res.data.totalRows;
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList();
    },
    // 新增接口
    openDialog() {
      this.addTilte = "新增项目监管";
      this.addType = "add";
      this.dialogDialog = {};
      this.isLook = false;
      this.dialogShow = true;
    },
    // 翻页
    pageChange(val) {
      this.pageNum = val;
      this.getList();
    },
    // 关闭新增弹窗
    closeDialog() {
      this.dialogShow = false;
    },
    // 删除工程接口
    deleteDialog(val) {
      this.$confirm("您确定删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(async () => {
        await projectSupervisionApi.delete({ id: val.id }).then((res) => {
          if (res.code == 0) {
            this.$message({ message: "删除成功", type: "success" });
            this.getList();
          } else {
            this.$message({
              message: "服务器异常,请联系管理员",
              type: "warning",
            });
          }
        });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .content-wrap {
    padding: 10px 20px 23px 24px;
    .list_search_box {
      @include flexbox;
      @include flexAI(center);
      @include flexJC(space-between);
      padding-top: 24px;
    }
    .tb_wrapper {
      padding: 24px 0 0 0;
      .pagination-box {
        padding-top: 24px;
      }
    }
  }
}

.statistics-card-box {
  display: flex;
  // @include flexflows(column, nowrap);
}
.statistics-card-sty {
  background: #f8f8f8;
  display: flex;
  justify-content: center;
  padding: 20px 10px;
  margin-right: 20px;
  margin-bottom: 15px;
  border-radius: 4px;
  flex: 1;
  &:last-child {
    margin-right: 0;
  }
  > i {
    width: 64px;
    height: 64px;
    &.supervision-icon-01 {
      background: url("../../../assets/images/supervision-icon-01.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-02 {
      background: url("../../../assets/images/supervision-icon-02.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-03 {
      background: url("../../../assets/images/supervision-icon-03.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-04 {
      background: url("../../../assets/images/supervision-icon-04.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-05 {
      background: url("../../../assets/images/supervision-icon-05.png")
        no-repeat;
      background-size: 100% 100%;
    }
  }
  > div {
    margin-left: 10px;
    margin-top: 7px;
    > p:nth-child(2) {
      margin-top: 15px;
      > span {
        font-size: 24px;
        margin-right: 10px;
      }
    }
  }
}
</style>