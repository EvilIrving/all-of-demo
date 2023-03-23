<!--
 * @Author: ly
 * @Date: 2021-08-11 10:18:02
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-07 09:51:11
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\project_supervision\components\ConfigurationTasks.vue
-->
<template>
  <el-dialog
    :visible.sync="visible"
    width="1100px"
    center
    :before-close="close"
    top="10vh"
    :title="title"
    class="list-dialog scroll-dialog"
  >
    <!-- 内容区域 -->
    <div style="height: 460px; margin-top: 8px; background-color: #fafafa">
      <el-row :gutter="20" style="margin-bottom: 8px">
        <!-- 左侧菜单栏 -->
        <el-col :span="5" class="left-title" style="padding: 0">
          <div
            :class="
              stageData == '开工阶段' ? 'left-title-sty' : 'left-title-style'
            "
            @click="stageClick('开工阶段')"
          >
            <i class="el-icon-document" />
            <span>开工阶段</span>
          </div>
          <div
            :class="
              stageData == '施工阶段' ? 'left-title-sty' : 'left-title-style'
            "
            @click="stageClick('施工阶段')"
          >
            <i class="el-icon-document" />
            <span>施工阶段</span>
          </div>
          <div
            :class="
              stageData == '完工阶段' ? 'left-title-sty' : 'left-title-style'
            "
            @click="stageClick('完工阶段')"
          >
            <i class="el-icon-document" />
            <span>完工阶段</span>
          </div>
        </el-col>
        <!-- 内容区域 -->
        <el-col :span="19">
          <div class="bg-purple">
            <p>异常信息配置条目（{{ tableData.length }}）</p>
            <p style="cursor: pointer" @click="openDialog('', '新增')">
              新增配置
            </p>
          </div>
          <div class="grid-content">
            <el-table
              :data="tableData"
              style="width: 100%"
              height="272px"
              :header-cell-style="{
                background: '#1492ff',
                color: '#fff',
                height: '48px',
              }"
            >
              <el-table-column label="序号" type="index" width="80" />
              <el-table-column label="异常信息条目名称" prop="entryName" />
              <el-table-column label="操作" width="220">
                <template slot-scope="scope">
                  <span
                    style="padding: 0 5px; color: #1492ff; cursor: pointer"
                    @click="openDialog(scope.row, '修改')"
                    >修改</span
                  >
                  <span
                    style="
                      padding: 0 5px;
                      color: rgb(210, 161, 0);
                      cursor: pointer;
                    "
                    @click="deleteDialog(scope.row.id, '删除')"
                    >删除</span
                  >
                  <i
                    style="padding: 0 5px; color: #1492ff; cursor: pointer"
                    class="icon-ascending-order"
                    @click="goUpDown(scope.row, 'up')"
                  />
                  <i
                    style="padding: 0 5px; color: #1492ff; cursor: pointer"
                    class="icon-descending-order"
                    @click="goUpDown(scope.row, 'down')"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- 新增配置信息弹窗 -->
    <NewConfigurationDialog
      ref="Configuration"
      v-if="newDialogShow"
      :stageData="stageData"
      :configurationObj="configurationObj"
      :newDialogData="newDialogData"
      :visible="newDialogShow"
      @exInfoList="exInfoList"
      @close="closeDialog"
    />
  </el-dialog>
</template>

<script>
import { projectSupervisionApi } from "@/api/projectSupervision";
import NewConfigurationDialog from "./NewConfigurationDialog";

export default {
  props: {
    type: String,
    visible: Boolean,
    title: {
      type: String,
      default: "任务配置",
    },
    id: [String, Number],
    isLook: {
      type: Boolean,
      default: true,
    },
  },
  components: { NewConfigurationDialog },
  data() {
    return {
      stageData: "开工阶段",
      tableData: [],
      newDialogShow: false,
      newDialogData: null,
      configurationObj: {},
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.exInfoList();
    });
  },
  methods: {
    // 升降序
    async goUpDown(data, val) {
      let obj = {
        id: data.id,
        upOrDown: val,
      };
      let res = await projectSupervisionApi.upOrDown(obj);
      if (res.code == 0) {
        this.exInfoList();
      }
    },
    // 删除该条信息
    deleteDialog(val) {
      this.$confirm("您确定删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(async () => {
        await projectSupervisionApi
          .deleteExInfo({ exceptionInformationIds: val })
          .then((res) => {
            if (res.code == 0) {
              this.$message({ message: "删除成功", type: "success" });
              this.exInfoList();
            } else {
              this.$message({
                message: "服务器异常,请联系管理员",
                type: "warning",
              });
            }
          });
      });
    },
    // 根据条件筛选获取异常配置项目列表
    async exInfoList() {
      this.tableData = [];
      let str =
        this.stageData == "开工阶段"
          ? "1"
          : this.stageData == "施工阶段"
          ? "2"
          : "3";
      let res = await projectSupervisionApi.exInfoList({ projectStage: str });
      if (res.code == 0) {
        this.tableData = res.data.list;
      }
    },
    // 阶段选择
    stageClick(val) {
      this.stageData = val;
      this.exInfoList();
    },
    // 打开弹窗
    openDialog(data, val) {
      this.newDialogData = val;
      this.configurationObj = data;
      this.newDialogShow = true;
    },
    // 关闭弹窗
    closeDialog() {
      this.newDialogShow = false;
    },
    // 关闭
    close() {
      this.$emit("close");
    },
  },
};
</script>

<style lang="scss">
@import "@/style/listDialog.scss";
.bg-purple {
  display: flex;
  justify-content: space-between;
  height: 50px;
  align-items: center;
  padding: 5px 20px;
  background: #ffffff;
  border: 1px solid #e9e9e9;
  > p:nth-child(1) {
    font-size: 16px;
    color: rgba(0, 0, 0, 0.48);
    letter-spacing: 0;
    font-weight: 600;
  }
  > p:nth-child(2) {
    color: #fff;
    letter-spacing: 0;
    font-weight: 600;
    background: #1492ff;
    border-radius: 4px;
    width: 93px;
    height: 44px;
    text-align: center;
    line-height: 44px;
  }
}
.grid-content {
  height: 312px;
  background: #ffffff;
  border: 1px solid #e9e9e9;
  margin-top: 8px;
  padding: 16px;
  box-sizing: border-box;
}
.left-title {
  height: 382px;
  padding: 10px 0;
  min-height: 36px;
  background: #ffffff;
  border: 1px solid #e9e9e9;
}
.left-title-sty {
  font-size: 20px;
  color: #1492ff;
  letter-spacing: 0;
  font-weight: 600;
  padding: 20px 10px;
  border-right: 5px solid #1492ff;
}
.left-title-style {
  font-size: 20px;
  color: rgba(0, 0, 0, 0.48);
  letter-spacing: 0;
  font-weight: 600;
  padding: 20px 10px;
}
.el-icon-document {
  font-size: 24px;
  margin: 0 15px;
}
.form-footer {
  margin-top: 8px;
}
</style>