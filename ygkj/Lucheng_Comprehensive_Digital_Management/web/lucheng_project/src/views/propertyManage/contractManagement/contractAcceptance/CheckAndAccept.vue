<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-08-17 10:07:59
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\contractAcceptance\CheckAndAccept.vue
-->
<template>
  <div>
    <!---列表--->
    <div class="wrap" v-if="detailType">
      <div class="item_box_title">
        <p>验收申请</p>
      </div>
      <div class="list_search_box">
        <div class="search_left">
          <span style="font-size: 14px">名称：</span>
          <el-input
            placeholder="请输入"
            v-model="dispatchName"
            @change="search"
            clearable
            style="width: 180px; margin: 0 20px 0 15px"
          />
          <span style="font-size: 14px">年份：</span>
          <el-date-picker
            style="width: 180px; margin: 0 20px 0 15px"
            v-model="dispatchYear"
            value-format="yyyy"
            type="year"
            placeholder="选择年"
            @change="search"
          >
          </el-date-picker>
          <span style="font-size: 14px">验收类型：</span>
          <el-select
            style="width: 180px; margin: 0 20px 0 15px"
            v-model="contractType"
            placeholder="请选择"
            @change="search"
          >
            <el-option label="我的申请" value="1"> </el-option>
            <el-option label="我的审批" value="2"> </el-option>
          </el-select>
          <span style="font-size: 14px">审批状态：</span>
          <el-select
            style="width: 180px; margin: 0 20px 0 15px"
            v-model="status"
            clearable
            @change="search"
          >
            <el-option label="审批中" value="1"> </el-option>
            <el-option label="已通过" value="3"> </el-option>
            <el-option label="已驳回" value="2"> </el-option>
          </el-select>
        </div>
        <div class="search_right">
          <el-button
            type="primary"
            @click="editTable('新增')"
            v-if="contractType == 1 && userInfo.userType == 3"
            v-permission="'checkAndAccept:add'"
            >新增</el-button
          >
        </div>
      </div>
      <div class="statistics-box">
        <span class="el-icon-warning"></span>
        <span>申请总数</span>
        <span class="blue">{{ countData.totalNum }}</span>
        <span>待审批</span>
        <span class="orange">{{ countData.approveNum }}</span>
        <span>已通过</span>
        <span class="blue">{{ countData.passNum }}</span>
        <span>已驳回</span>
        <span class="red">{{ countData.rejectNum }}</span>
      </div>
      <div class="tb_wrapper">
        <el-table :data="tableData" border style="width: 100%">
          <!-- <el-table-column type="selection" width="50"> </el-table-column> -->
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="propertyUnitName"
            label="物业管理单位"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="runUnitName"
            label="运行管理单位"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="runUnitReviewerName"
            label="审核人"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="contractName"
            label="合同名称"
            align="center"
          ></el-table-column>
          <el-table-column label="服务期限" align="center" width="160px">
            <template slot-scope="scope">
              <div v-if="scope.row.serviceStartTime">
                <div>{{ scope.row.serviceStartTime }}</div>
                <div>至</div>
                <div>{{ scope.row.serviceEndTime }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="file" label="合同（附件）" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.contractFile">
                <div
                  v-for="item in scope.row.contractFiles"
                  :key="item.id"
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
          <el-table-column prop="file" label="验收资料（附件）" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.acceptanceFile">
                <div
                  v-for="item in scope.row.acceptanceFiles"
                  :key="item.id"
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
          <el-table-column
            prop="problem"
            label="问题描述"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="createTime"
            label="提交时间"
            align="center"
          ></el-table-column>
          <el-table-column label="审核状态" align="center">
            <template slot-scope="scope">
              <span>{{
                scope.row.status == 1
                  ? "审核中"
                  : scope.row.status == 2
                  ? "已驳回"
                  : "已通过"
              }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('查看', scope.row)"
                  v-permission="'checkAndAccept:look'"
                  >查看</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="removeTable(scope.row.id)"
                  v-permission="'checkAndAccept:del'"
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
          :page-sizes="[10, 20, 50, 100]"
          @pagination="contractList"
        />
      </div>
    </div>
    <div v-if="!detailType">
      <check-and-accept-detail
        ref="check"
        @checkType="closeAcceptDetail"
        :id="id"
        :detailData="detailData"
        @lookFile="preview"
      ></check-and-accept-detail>
    </div>
    <el-dialog
      :title="title"
      width="1040px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
      top="10vh"
    >
      <check-and-accept-from
        :title="title"
        @close="closeDialog"
        :id="id"
        :type="contractType"
        v-if="visibleDialog"
      />
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { contractApi } from "@/api/projectUnitDetail.js";
import CheckAndAcceptFrom from "./CheckAndAcceptFrom";
import LookFile from "@/components/LookFile.vue";
import { exportOverhaul } from "@/utils/export.js";
import CheckAndAcceptDetail from "./CheckAndAcceptDetail.vue";
export default {
  data() {
    return {
      dispatchYear: "",
      dispatchName: "",
      status: "",
      contractType: "1",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      title: "新增",
      visibleDialog: false,
      fileDialog: false,
      url: "",
      detailType: true,
      id: "",
      detailData: {},
      countData: {},
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
    userInfo() {
      return this.$sessionData("get", "userInfo");
    },
  },
  components: { CheckAndAcceptFrom, LookFile, CheckAndAcceptDetail },
  mounted() {
    this.contractType = this.userInfo.userType == 3 ? "1" : "2";
    this.contractList();
    this.getCount();
  },
  methods: {
    async getCount() {
      let res = await contractApi.countApplication({
        year: this.dispatchYear,
        status: this.status,
        propertyUnitName: this.dispatchName,
        type: this.contractType,
      });
      if (res.code == 0) {
        this.countData = res.data;
      }
    },
    async contractList() {
      let res = await contractApi.list({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        year: this.dispatchYear,
        status: this.status,
        propertyUnitName: this.dispatchName,
        type: this.contractType,
      });
      if (res.code == 0) {
        this.pageTotal = res.data.totalRows;
        this.tableData = res.data.list;
      }
    },
    preview(url) {
      this.url = url;
      this.fileDialog = true;
    },
    handleClose() {
      this.visibleDialog = false;
    },
    search() {
      this.pageNum = 1;
      this.contractList();
    },
    exportsData() {
      let obj = {
        name: this.dispatchName,
        pid: this.projectDetail.prcd,
        type: 2,
        date: this.dispatchYear,
      };
      exportOverhaul(obj);
    },
    removeTable(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          let res = await contractApi.del({ id });
          if (res.code == 0) {
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.contractList();
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    editTable(title, obj = {}) {
      if (obj.id) {
        this.id = obj.id;
      } else {
        this.id = "";
      }
      if (this.userInfo.userType == 3) {
        this.title = title;
        this.visibleDialog = true;
      } else {
        this.detailData = obj;
        this.$nextTick(() => {
          this.detailType = false;
          console.log(this.$refs.check);
        });
      }
    },
    closeAcceptDetail() {
      this.detailType = true;
      this.contractList();
    },
    closeDialog(data) {
      if (data) {
        this.pageNum = 1;
        this.contractList();
      }
      this.handleClose();
    },
  },
};
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
  .statistics-box {
    margin: 0px 16px 0px 24px;
    height: 32px;
    background-color: rgba(20, 146, 255, 0.08);
    padding-left: 8px;
    font-size: 14px;
    color: #666666;
    display: flex;
    align-items: center;
    > span:nth-child(1) {
      color: #1492ff;
      font-size: 20px;
    }
    > span:nth-child(2) {
      padding-left: 8px;
    }
    .blue {
      color: #1492ff;
      font-size: 16px;
      margin: 0 48px 0 15px;
    }
    .orange {
      color: #feaf37;
      font-size: 16px;
      margin: 0 48px 0 15px;
    }
    .red {
      color: #ff0000;
      font-size: 16px;
      margin: 0 48px 0 15px;
    }
  }
}
</style>