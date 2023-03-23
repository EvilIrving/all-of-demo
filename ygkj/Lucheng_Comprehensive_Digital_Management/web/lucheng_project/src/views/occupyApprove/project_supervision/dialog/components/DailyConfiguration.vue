<template>
  <div class="daily-configuration">
    <div class="bg-purples">
      <SelectPerson
        ref="selectPerson"
        :disabled="data.project.regulatoryStatus == 7"
        personnel-type="指定巡查人员"
      />
    </div>

    <!-- 复核监管项配置 -->
    <div class="grid-content">
      <!-- 项目阶段 -->
      <div>
        <p>项目阶段</p>
        <el-radio-group
          v-model="radio"
          @change="radioData"
          :disabled="data.project.regulatoryStatus == 7"
        >
          <el-radio label="1">开工阶段</el-radio>
          <el-radio label="2">施工阶段</el-radio>
          <el-radio label="3">完工阶段</el-radio>
        </el-radio-group>
      </div>

      <!-- 巡检app异常信息配置 -->
      <div>
        <p>巡检app异常信息配置</p>
        <div style="color: #1f1f1f">
          <el-checkbox
            v-model="checkAll"
            :disabled="data.project.regulatoryStatus == 7"
            @change="handleCheckAllChange"
          >
            全选
          </el-checkbox>
          <div style="margin: 15px 0"></div>
          <el-checkbox-group
            v-model="checkedCities"
            :disabled="data.project.regulatoryStatus == 7"
            @change="handleCheckedCitiesChange"
          >
            <div v-for="(item, i) in tableData" :key="i">
              <div style="margin: 15px 0"></div>
              <el-checkbox :label="item.entryName">
                {{ item.entryName }}
              </el-checkbox>
            </div>
          </el-checkbox-group>
        </div>
      </div>
    </div>
    <div class="form-footer">
      <el-button
        class="form-btn"
        size="small"
        v-if="data.project.regulatoryStatus !== 7"
        @click="endInspection"
      >
        结束项目监管

      </el-button>
      <el-button size="small" @click="$emit('close')"> 取消 </el-button>
      <el-button size="small" type="primary" @click="addInspection"
        >派发</el-button
      >
    </div>
  </div>
</template>

<script>
import SelectPerson from "./SelectPerson.vue";
import { projectSupervisionApi } from "@/api/projectSupervision";
export default {
  name: "daily-configuration",
  props: {
    defaultRadio: {
      type: String,
    },
    data: {
      type: Object,
    },
  },
  watch: {
    defaultRadio(val) {
      this.radio = val;
      this.radioData(val);
    },
  },
  data() {
    return {
      radio: "1",
      checkAll: false,
      checkedCities: [],
      cities: [],
      /** @type {{entryName:any}[]} */
      tableData: [],
      isIndeterminate: true,
    };
  },
  components: {
    SelectPerson,
  },
  mounted() {
    this.exInfoList("1");
  },
  methods: {
    endInspection() {
      this.$confirm("此操作将结束监管该项目, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const project = this.data.project;
          projectSupervisionApi
            .updateProject({ id: project.id, regulatoryStatus: 7 })
            .then(() => {
              this.$message({
                type: "success",
                message: "该监管已成功结束!",
              });
            });
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消" });
        });
    },
    radioData(val) {
      this.checkedCities = [];
      this.checkAll = false;
      this.exInfoList(val);
    },
    // 根据条件筛选获取异常配置项目列表
    async exInfoList(val) {
      this.tableData = [];
      this.cities = [];
      let res = await projectSupervisionApi.exInfoList({ projectStage: val });
      if (res.code == 0) {
        this.tableData = res.data.list;
        res.data.list.forEach((item) => {
          this.cities.push(item.entryName);
        });
      }
    },
    // 多选
    handleCheckAllChange(val) {
      this.checkedCities = val ? this.cities : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.cities.length;
    },
    // 派发新增蓝线管理巡查
    async addInspection() {
      const selectPersonList = this.$refs.selectPerson.selectPersonList;
      if (selectPersonList.length == 0) {
        this.$message({ message: "请选择巡查人员！", type: "error" });
      } else {
        if (this.checkedCities.length != 0) {
          let opt = {
            inspector: selectPersonList[0].userName,
            inspectorId: selectPersonList[0].id,
            stage: this.radio,
            problems: this.checkedCities,
            projectId: this.data.project.id,
            projectName:
              this.data.project.projectName == ""
                ? "-"
                : this.data.project.projectName,
          };
          let res = await projectSupervisionApi.addInspection(opt);
          if (res.code == 0) {
            this.$emit("update");
          }
        } else {
          this.$message({
            message: "请选择巡检app异常信息配置！",
            type: "error",
          });
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.daily-configuration {
  padding-left: 16px;
  height: 100%;
  display: flex;
  flex-flow: column nowrap;
  .form-item {
    margin-right: 10px;
    .item-label {
      margin-top: 0;
    }
  }
  .bg-purples {
    border: 1px solid rgba(94, 91, 91, 0.2);
    padding: 10px;
  }
  .grid-content {
    margin-top: 15px;
    padding: 10px;
    background: #fff;
    border: 1px solid rgba(94, 91, 91, 0.2);
    flex: 1;
    > div {
      > p:nth-child(1) {
        font-size: 20px;
        color: rgba(0, 0, 0, 0.88);
        line-height: 24px;
        font-weight: 600;
      }
      > div {
        padding: 10px;
        ::v-deep.el-radio__label {
          color: #c2c2c2;
        }
        ::v-deep.el-radio__input.is-checked + .el-radio__label {
          color: #1f1f1f;
        }
        ::v-deep.el-radio__input.is-checked .el-radio__inner {
          border-color: #1492ff;
          background: #1492ff;
        }
        ::v-deep.el-checkbox__label {
          color: #1f1f1f;
        }
      }
    }
  }
  .form-footer {
    height: 58px;
    line-height: 58px;
    padding-right: 0;
  }
}
</style>