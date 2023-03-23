<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>督办中心</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <!-- <el-input
        placeholder="请输入名称"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="pointName"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <el-button type="primary" class="right" @click="editTable('新增')" >新增</el-button> -->
      <!-- <el-button style="margin-right: 10px" class="right" @click="exportTable()">导出</el-button> -->
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="projectName" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="createName" label="督办人" align="center"></el-table-column>
        <el-table-column prop="createTime" label="督办时间" align="center"></el-table-column>
        <el-table-column prop="deadline" label="截止时间" align="center"></el-table-column>
        <el-table-column prop="problemDescribe" label="问题描述" align="center"></el-table-column>
        <el-table-column label="问题附件" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.problemFileList">
              <div v-for="(item, index) in scope.row.problemFileList" :key="index" @click="preview(item.fileUrl)">
                <i class="el-icon-link" style="color:#409eff;cursor: pointer;"></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column label="督办状态" align="center">
          <template slot-scope="scope">
            <div>
              <!-- 0未处置 1已处置 -1超期未处置 -->
              <span v-if="scope.row.status == 0">未处置</span>
              <span v-if="scope.row.status == 1">已处置</span>
              <span v-if="scope.row.status == -1">超期未处置</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="workerName" label="整改人" align="center"></el-table-column>
        <el-table-column prop="workTime" label="整改时间" align="center"></el-table-column>
        <el-table-column prop="workDescribe" label="整改说明" align="center"></el-table-column>
        <el-table-column prop="file" label="整改附件" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.workFileList">
              <div v-for="(item, index) in scope.row.workFileList" :key="index" @click="preview(item.fileUrl)">
                <i class="el-icon-link" style="color:#409eff;cursor: pointer;"></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span class="deal_btn deal_btn_look" @click="look('详情', scope.row.id)" v-permission="'supervision:look'">详情</span>
              <span v-if="scope.row.updateFlag" class="deal_btn deal_btn_del" v-permission="'supervision:dispose'" @click="editTable('督办', scope.row.id)">处置</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getList" />
    </div>
    <el-dialog v-if="showEditDialog" :title="title" :visible.sync="showEditDialog" width="60%">
      <SupervisionForm v-if="isEdit" :id="id" @closeEditDialog="closeDialog"></SupervisionForm>
      <SupervisionLookForm v-if="isLook" :id="id" @closeEditDialog="closeDialog"></SupervisionLookForm>
    </el-dialog>
    <el-dialog title="查看文件" fullscreen :visible.sync="showFileDialog" width="85%">
      <look-file v-if="showFile" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { supervisionCenter } from "@/api/seawallPrevent";
import SupervisionForm from "./SupervisionForm.vue";
import SupervisionLookForm from "./SupervisionLookForm.vue";
import LookFile from "@/components/LookFile.vue";
import { getQueryString } from "@/utils/utils"
import { validateTicketApi } from "@/api/index";
export default {
  name: "Supervision",
  props: {},
  components: {
    SupervisionForm,
    LookFile,
    SupervisionLookForm,
  },
  inject: ['reload'],
  data() {
    return {
      showFileDialog: false,
      showFile: "",
      url: "",
      hasHandle: true,
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      pointName: "",
      id: "",
      title: "新增",
      isEdit: false,
      showEditDialog: false,
      loading: false,
    };
  },
  mounted() {
    let { ticket, workId, overseeId } = this.$route.query
    if (ticket !== undefined) {
      this.getToken(ticket)
    }
    this.getList();
  },
  methods: {
    async getToken(ticket) {
      let res = await validateTicketApi({
        ticket: ticket
      })
      if (res.code === 0) {
        this.$message.success('登录成功')
        this.$sessionData("set", "userInfo", res.data);
      }
    },
    async getList() {
      let { overseeId } = this.$route.query
      let opt = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }
      overseeId !== undefined ? opt.id = overseeId : ''
      let res = await supervisionCenter.list(opt);
      if (res.code == 0) {
        res.data.list.map((i) => {
          if (i.status !== 1) {
            i.workerName = "";
            if (i.insideList.length !== 0) {
              i.insideList.map((j) => {
                if (j.workerName) {
                  i.workerName += j.workerName;
                }
              });
            }
            if (i.outsideList.length !== 0) {
              i.outsideList.map((j) => {
                if (j.workerName) {
                  i.workerName += "," + j.workerName;
                }
              });
            }
          } else {
            if (i.insideList.length !== 0) {
              i.workerName = "";
              i.insideList.map((j) => {
                j.workerPhone == i.reportPhone
                  ? (i.workerName = j.workerName)
                  : (i.workerName = "");
              });
            }
            if (i.outsideList.length !== 0) {
              i.outsideList.map((j) => {
                j.workerPhone == i.reportPhone
                  ? (i.workerName = j.workerName)
                  : (i.workerName = "");
              });
            }
          }

          // if (i.reportPhone) {
          //   if (i.insideList.length !== 0) {
          //     i.insideList.map((j) => {
          //       j.workerPhone == i.reportPhone
          //         ? (i.workerName = j.workerName)
          //         : (i.workerName = "");
          //     });
          //   }
          //   if (i.outsideList.length !== 0) {
          //     i.outsideList.map((j) => {
          //       j.workerPhone == i.reportPhone
          //         ? (i.workerName = j.workerName)
          //         : (i.workerName = "");
          //     });
          //   }
          // } else {
          //   if (i.insideList.length !== 0) {
          //     i.insideList.map((j) => {
          //       i.workerName = j.workerPhone;
          //     });
          //   }
          //   if (i.outsideList.length !== 0) {
          //     i.outsideList.map((j) => {
          //       i.workerName = j.workerPhone;
          //     });
          //   }
          // }
        });
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    look(title, id) {
      this.id = id;
      this.showEditDialog = this.isLook = true;
      this.isEdit = false;
      this.title = title;
    },
    editTable(title, id) {
      this.id = id;
      this.showEditDialog = this.isEdit = true;
      this.isLook = false;
      this.title = title;
    },
    closeDialog() {
      this.showEditDialog = this.isEdit = this.isLook = false;
      this.getList();
    },
    preview(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$message.warning("暂无附件");
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
</style>
