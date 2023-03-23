<!--
 * @Author: wqn
 * @Date: 2022-09-27 14:08:23
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-27 14:08:23
 * @Description: 值班管理
-->
<template>
  <div class="wrap">
    <div class="content">
      <div class="item_box_title">
        <p>值班管理</p>
      </div>
      <div class="on_duty_container">
        <div class="action_bar">
          <div class="search_bar">
            <div class="search_item">
              <span>时间筛选：</span>
              <el-date-picker v-model="date" value-format="yyyy-MM" type="month" size="small" placeholder="选择月" style="width: 140px;" @change="getTableData"></el-date-picker>
            </div>
            <div class="search_item">
              <span>名称：</span>
              <el-input placeholder="请输入" v-model="name" size="small" style="width: 160px;" @change="getTableData">
                <i slot="suffix" class="el-input__icon el-icon-search" style="cursor: pointer;" @click="getTableData"></i>
              </el-input>
            </div>
          </div>
          <div class="button_bar">
            <el-popover placement="top" width="180" v-model="visible">
              <el-date-picker v-model="date_" value-format="yyyy-MM" type="month" size="small" placeholder="选择下载模板的年月" style="width: 180px;"></el-date-picker>
              <div style="text-align: right; margin: 5px 0 0 0">
                <el-button size="mini" type="text" @click="visible = false">取消</el-button>
                <el-button type="primary" size="mini" @click="downloadTemplate">确定</el-button>
              </div>
              <el-button type="primary" size="small" slot="reference">下载模板</el-button>
            </el-popover>
            <el-button type="primary" size="small" @click="importTemplate" style="margin-left: 20px;">导入模板</el-button>
            <input id="selectOnDutyTemplateInput" ref="selectOnDutyTemplateInput" type="file" style="display: none" @change="importChange">
          </div>
        </div>
        <div class="table_wrapper">
          <div class="table_inner" ref="tableContainer">
            <el-table :data="tableData" stripe border style="width: 100%" :header-cell-style="rowClass" :height="tableHeight">
              <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
              <el-table-column label="日期">
                <el-table-column prop="dutyDate"></el-table-column>
                <el-table-column prop="week"></el-table-column>
              </el-table-column>
              <el-table-column label="值班领导" prop="leaderName"></el-table-column>
              <el-table-column label="日间值班人员" prop="managerName"></el-table-column>
              <el-table-column label="夜间值班人员" prop="ngihtManagerName"></el-table-column>
              <el-table-column label="当班主任" prop="dutyChiefName"></el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <span class="table_text_button" @click="edit(scope.row)">编辑</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="table-paging">
            <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getTableData" />
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="编辑" :visible.sync="showEditDialog" :close-on-click-modal="false" :before-close="handleClose">
      <el-form ref="onDutyForm" :model="onDutyForm" :rules="rules" class="form-container" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="日期:" prop="dutyDate">
              <el-input v-model="onDutyForm.dutyDate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="值班领导:" prop="leaderName">
              <el-input v-model="onDutyForm.leaderName" placeholder="单位负责人" @focus="openPersonSelectedDailog('值班领导')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="日间值班人:" prop="managerName">
              <el-input v-model="onDutyForm.managerName" placeholder="请输入" @focus="openPersonSelectedDailog('日间值班人')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="夜间值班人:" prop="ngihtManagerName">
              <el-input v-model="onDutyForm.ngihtManagerName" placeholder="单位负责人" @focus="openPersonSelectedDailog('夜间值班人')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="当班主任:" prop="dutyChiefName">
              <el-input v-model="onDutyForm.dutyChiefName" placeholder="请输入" @focus="openPersonSelectedDailog('当班主任')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="comfirm">确 定</el-button>
      </span>
    </el-dialog>
    <PersonnelListDailog v-if="personnelDialogShow" PersonnelType="巡查人员" :visible.sync="personnelDialogShow" :selectPersonList="[]" specifiedArea="鹿城区" @saveEmit="onSavePersonnel" />
  </div>
</template>

<script>
import axios from "axios";
import config from "@/config/config.js";
import { sessionData } from "@/plugins/storage";
import {
  onDutyListApi,
  onDutyEditApi,
  onDutyImportApi
} from "@/api/systemManagement.js";
import PersonnelListDailog from "@/views/occupyApprove/project_supervision/dialog/personnelListDailog.vue";
export default {
  name: "onDutyManagement",
  props: {},
  components: {
    PersonnelListDailog
  },
  data() {
    return {
      visible: false,
      date: new Date().format("yyyy-MM"),
      date_: "",
      name: "",
      tableHeight: 0,
      tableData: [],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      showEditDialog: false,
      onDutyForm: {
        id: "",
        dutyDate: "",
        leader: "",
        leaderName: "",
        manager: "",
        managerName: "",
        ngihtManager: "",
        ngihtManagerName: "",
        dutyChief: "",
        dutyChiefName: ""
      },
      rules: {
        // leader: [{ required: true, message: "必填项", trigger: "blur" }],
        // dayWatchman: [{ required: true, message: "必填项", trigger: "blur" }],
        // nightWatchman: [{ required: true, message: "必填项", trigger: "blur" }],
        // headTeacher: [{ required: true, message: "必填项", trigger: "blur" }]
      },
      selectedType: "",
      personnelDialogShow: false
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getTableData();
  },
  mounted() {
    this.getTableHeight();
    window.addEventListener("resize", this.getTableHeight);
  },
  destroyed() {
    window.removeEventListener("resize", this.getTableHeight);
  },
  methods: {
    /**
     * @description 设置列表头样式
     */
    rowClass({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 1) return { display: "none" };
    },
    /**
     * @description 获取表格高度
     */
    getTableHeight() {
      this.$nextTick(() => {
        if (this.$refs.tableContainer)
          this.tableHeight = this.$refs.tableContainer.clientHeight;
      });
    },
    /**
     * @description 获取表格数据
     */
    getTableData() {
      onDutyListApi({
        yearAndMonth: this.date,
        userName: this.name,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }).then(res => {
        if (res.code === 0) {
          this.tableData = res.data.list || [];
          this.pageTotal = res.data.totalRows || 0;
        }
      });
    },
    /**
     * @description 下载模板
     */
    downloadTemplate() {
      if (this.date_) {
        axios({
          method: "get",
          responseType: "blob",
          url:
            config.baseUrl + "/project-manage/dutyManagement/downloadTemplate",
          params: {
            yearAndMonth: this.date_
          },
          headers: {
            authorization: sessionData("get", "userInfo")
              ? sessionData("get", "userInfo").accessToken || ""
              : ""
          }
        }).then(({ data }) => {
          const blob = new Blob([data]); // 把得到的结果用流对象转一下
          var a = document.createElement("a"); //创建一个<a></a>标签
          a.href = URL.createObjectURL(blob); // 将流文件写入a标签的href属性值
          a.download = this.date_ + "值班模板.xls"; //设置文件名
          a.style.display = "none"; // 障眼法藏起来a标签
          document.body.appendChild(a); // 将a标签追加到文档对象中
          a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
          a.remove(); // 一次性的，用完就删除a标签
          this.visible = false;
        });
      } else {
        this.$message.warning("请选择下载模板的年月");
      }
    },
    /**
     * @description 导入模板
     */
    importTemplate() {
      const selectOnDutyTemplateInput = document.getElementById(
        "selectOnDutyTemplateInput"
      );
      selectOnDutyTemplateInput.click();
    },
    /**
     * @description 导入
     */
    async importChange() {
      const fileObj = this.$refs.selectOnDutyTemplateInput;
      const file = fileObj.files[0];
      let param = new FormData();
      param.append("file", file);
      await axios
        .post(
          config.baseUrl + "/project-manage/dutyManagement/importTemplate",
          param,
          {
            headers: {
              authorization: sessionData("get", "userInfo")
                ? sessionData("get", "userInfo").accessToken || ""
                : "",
              "Content-type": "multipart/form-data"
            }
          }
        )
        .then(({ data }) => {
          if (data.code === 0) {
            this.$message.success("导入成功");
            this.getTableData();
          } else {
            this.$message({
              message: "导入失败: " + data.message,
              type: "error",
              duration: 5000
            });
          }
        });
      document.getElementById("selectOnDutyTemplateInput").value = null;
    },
    /**
     * @description 打开编辑弹窗
     */
    edit(row) {
      this.onDutyForm.id = row.id;
      this.onDutyForm.dutyDate = row.dutyDate;
      this.onDutyForm.leader = row.leader;
      this.onDutyForm.leaderName = row.leaderName;
      this.onDutyForm.manager = row.manager;
      this.onDutyForm.managerName = row.managerName;
      this.onDutyForm.ngihtManager = row.ngihtManager;
      this.onDutyForm.ngihtManagerName = row.ngihtManagerName;
      this.onDutyForm.dutyChief = row.dutyChief;
      this.onDutyForm.dutyChiefName = row.dutyChiefName;
      this.showEditDialog = true;
    },
    /**
     * @description 关闭编辑弹窗
     */
    handleClose() {
      this.onDutyForm.id = "";
      this.onDutyForm.dutyDate = "";
      this.onDutyForm.leader = "";
      this.onDutyForm.leaderName = "";
      this.onDutyForm.manager = "";
      this.onDutyForm.managerName = "";
      this.onDutyForm.ngihtManager = "";
      this.onDutyForm.ngihtManagerName = "";
      this.onDutyForm.dutyChief = "";
      this.onDutyForm.dutyChiefName = "";
      this.$refs["onDutyForm"].resetFields();
      this.showEditDialog = false;
    },
    /**
     * @description 取消编辑
     */
    cancel() {
      this.handleClose();
    },
    /**
     * @description 确认编辑
     */
    comfirm() {
      this.$refs["onDutyForm"].validate(valid => {
        if (valid) {
          onDutyEditApi(this.onDutyForm).then(res => {
            if (res.code === 0) {
              this.$message.success("编辑成功");
              this.handleClose();
              this.getTableData();
            } else {
              this.$message.error("编辑失败");
            }
          });
        } else {
          return false;
        }
      });
    },
    /**
     * @description 打开人员选择框
     */
    openPersonSelectedDailog(type) {
      this.selectedType = type;
      this.personnelDialogShow = true;
    },
    /**
     * @description 保存选中的人员
     */
    onSavePersonnel(e) {
      let personData = e[0] || {};
      if (this.selectedType === "值班领导") {
        this.onDutyForm.leader = personData.id || "";
        this.onDutyForm.leaderName = personData.userName || "";
      } else if (this.selectedType === "日间值班人") {
        this.onDutyForm.manager = personData.id || "";
        this.onDutyForm.managerName = personData.userName || "";
      } else if (this.selectedType === "夜间值班人") {
        this.onDutyForm.ngihtManager = personData.id || "";
        this.onDutyForm.ngihtManagerName = personData.userName || "";
      } else if (this.selectedType === "当班主任") {
        this.onDutyForm.dutyChief = personData.id || "";
        this.onDutyForm.dutyChiefName = personData.userName || "";
      }
    }
  }
};
</script>
<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  padding: 25px;
  box-sizing: border-box;
  .content {
    @include contentBox();
    height: 100%;
    box-sizing: border-box;
    .on_duty_container {
      box-sizing: border-box;
      height: calc(100% - 57px);
      padding: 17px 24px 29px 24px;
      .action_bar {
        display: flex;
        justify-content: space-between;
        margin-bottom: 18px;
        .search_bar {
          display: flex;
          .search_item {
            span {
              font-size: 14px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #666666;
            }
            + .search_item {
              margin-left: 24px;
            }
          }
        }
      }
      .table_wrapper {
        height: calc(100% - 50px);
        .table_inner {
          height: calc(100% - 56px);
          ::v-deep .el-table {
            th.gutter {
              display: none !important;
            }
            .cell {
              text-align: center;
            }
          }
          .table_text_button {
            height: 20px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #1492ff;
            line-height: 20px;
            cursor: pointer;
          }
        }
        .table-paging {
          margin-top: 24px;
        }
      }
    }
  }
}
</style>