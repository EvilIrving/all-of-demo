<!--
 * @Date: 2021-11-09 13:37:54
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-20 18:00:51
 * @FilePath: \lucheng_project\src\views\seawallPrevent\riskEarlyWarning\ruleConfiguration\thresholdDialog.vue
-->
<template>
  <el-dialog :visible.sync="visible" width="890px" :before-close="close" top="10vh" :close-on-click-modal="false" title="工程配置" class="list-dialog" append-to-body>
    <div>
      <el-row :gutter="20">
        <el-col :span="8" style="margin-top: 10px">
          <p style="float: left; line-height: 28px; height: 28px">
            {{ type == "seawall" ||  type == "engineeringAll" ? "工程" : "测站" }}名称
          </p>
          <el-input style="margin-left: 15px; width: 70%" placeholder="请输入内容" size="mini" v-model="seawallName" @change="getList" clearable />
        </el-col>
      </el-row>
    </div>
    <p v-if="!isSingle" class="now-choose right" style="color: #858585; line-height: 36px">
      当前选中<strong class="green">{{ num }}</strong>个工程
    </p>
    <p v-else class="now-choose right" style="color: #858585; line-height: 36px">
      当前选中：<strong class="green">{{ singleName }}</strong>
    </p>
    <!-- <el-table v-if="type == 'seawall'" :data="dataList" ref="chooseTable" stripe class="deep-table" style="width: 100%" height="450" border header-row-class-name="table-header" v-loading="tableLoading"
      element-loading-background="rgba(0, 0, 0, 0.5)" element-loading-text="数据正在加载中" element-loading-spinner="el-icon-loading" @selection-change="handleSelectionChange">
      <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
      <el-table-column prop="seawallName" label="工程名称" align="center">
        <template slot-scope="scope">
          <div style="color: #1492ff; cursor: pointer">
            {{ scope.row.seawallName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="areaName" label="行政区划" align="center"></el-table-column>
      <el-table-column label="海塘等级" align="center">
        <template slot-scope="scope">
          <div>
            <span v-if="scope.row.seawallLevel === '1'">1级海塘</span>
            <span v-if="scope.row.seawallLevel === '2'">2级海塘</span>
            <span v-if="scope.row.seawallLevel === '3'">3级海塘</span>
            <span v-if="scope.row.seawallLevel === '4'">4级海塘</span>
            <span v-if="scope.row.seawallLevel === '5'">5级海塘</span>
            <span v-if="scope.row.seawallLevel === '6'">规模以下</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" type="selection" width="55">
      </el-table-column>
    </el-table> -->

    <el-table v-if="type == 'seawall' || type === 'engineeringAll'" :data="dataList" ref="chooseTable" stripe class="deep-table" style="width: 100%" height="450" border header-row-class-name="table-header" v-loading="tableLoading" element-loading-background="rgba(0, 0, 0, 0.5)" element-loading-text="数据正在加载中" element-loading-spinner="el-icon-loading" @selection-change="handleSelectionChange">
      <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
      <el-table-column prop="project_name" label="工程名称" align="center"></el-table-column>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
    </el-table>
    
    <el-table v-else-if="type == 'PP'" :data="dataList" ref="chooseTable" stripe class="deep-table" style="width: 100%" height="450" border header-row-class-name="table-header" v-loading="tableLoading" element-loading-background="rgba(0, 0, 0, 0.5)" element-loading-text="数据正在加载中" element-loading-spinner="el-icon-loading" @selection-change="handleSelectionChange">
      <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
      <el-table-column prop="stationName" label="工程名称" align="center"></el-table-column>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
    </el-table>

    <el-table v-else :data="dataList" :highlight-current-row="isSingle" ref="chooseTable" stripe class="deep-table" style="width: 100%" height="450" border header-row-class-name="table-header" v-loading="tableLoading" element-loading-background="rgba(0, 0, 0, 0.5)" element-loading-text="数据正在加载中" element-loading-spinner="el-icon-loading" @selection-change="handleSelectionChange" @current-change="handleCurrentChange">
      <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
      <el-table-column label="测站名称" align="center">
        <template slot-scope="scope">
          <div style="color: #1492ff; cursor: pointer">
            {{ scope.row.stName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column v-if="!isSingle" align="center" type="selection" width="55">
      </el-table-column>
    </el-table>
    <div slot="footer">
      <el-row>
        <div class="form-footer">
          <el-button class="form-btn btn-cancel" size="mini" @click="close">关闭</el-button>
          <el-button class="form-btn btn-save" size="mini" @click="setThresholdVal">
            确定
          </el-button>
        </div>
      </el-row>
    </div>
  </el-dialog>
</template>
<script>
import {
  seawallListApi,
  ruleConfiguration,
  getAlleEngineering
} from "@/api/seawallPrevent";
export default {
  props: {
    type: String,
    visible: Boolean,
    checkedStations: String,
    stType: [String, Number],
    isSingle: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    visible: function (val) {
      this.show = val;
      if (val) {
        this.thresholdVal = null;
        this.getList();
      }
    }
  },
  data() {
    return {
      stName: "",
      show: false,
      seawallName: "",
      grade: "",
      safetyConclusion: "",
      thresholdVal: null,
      tableLoading: false,
      checkItems: [],
      dataList: [],
      total: 0,
      page: 1,
      rows: 10,
      num: 0,
      singleName: ""
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let checkIds = [];
      if (this.checkedStations != "") {
        checkIds = this.checkedStations.split(",");
      }
      this.tableLoading = true;
      let opt = [];
      switch (this.type) {
        case "seawall":
        case "engineeringAll":
          opt = {};
          if (this.seawallName !== "") opt.projectName = this.seawallName;
          await getAlleEngineering(opt).then(res => {
            if (res.code == 0) {
              this.dataList = res.data;
              // 选中
              this.$nextTick(() => {
                if (checkIds.length > 0) {
                  this.dataList.forEach(row => {
                    for (var i = 0; i < checkIds.length; i++) {
                      if (checkIds[i] === row.project_code) {
                        this.$refs.chooseTable.toggleRowSelection(row, true);
                      }
                    }
                  });
                }
              });
              this.tableLoading = false;
            }
          });
          break;
        // case "seawall":
        //   opt = {
        //     seawallName: this.seawallName,
        //     grade: this.grade,
        //     safetyConclusion: this.safetyConclusion
        //   };
        //   await seawallListApi.list(opt).then(res => {
        //     if (res.code == 0) {
        //       this.dataList = res.data.list;
        //       // 选中
        //       this.$nextTick(() => {
        //         if (checkIds.length > 0) {
        //           this.dataList.forEach(row => {
        //             for (var i = 0; i < checkIds.length; i++) {
        //               if (checkIds[i] === row.seawallCode) {
        //                 this.$refs.chooseTable.toggleRowSelection(row, true);
        //               }
        //             }
        //           });
        //         }
        //       });
        //       this.tableLoading = false;
        //     }
        //   });
        //   break;
        case "CZ":
          opt = {
            stType: this.stType,
            stName: this.seawallName
          };
          let res = await ruleConfiguration.stListByType(opt);
          if (res.code == 0) {
            this.dataList = res.data;
            // 选中
            this.$nextTick(() => {
              if (checkIds.length > 0) {
                this.dataList.forEach(row => {
                  for (var i = 0; i < checkIds.length; i++) {
                    if (checkIds[i] == row.stCode) {
                      if (this.isSingle) {
                        this.$refs.chooseTable.setCurrentRow(row);
                        this.singleName = row.stName;
                      } else {
                        this.$refs.chooseTable.toggleRowSelection(row, true);
                      }
                    }
                  }
                });
              }
            });
            this.tableLoading = false;
          }
          break;
        case "PP":
          opt = {
            // stationType: this.type,
            minutes:60,
            stationName: this.seawallName
          };
          await ruleConfiguration.rainlistByType(opt).then(res => {
            if (res.code == 0) {
              this.dataList = res.data;
              // 选中
              this.$nextTick(() => {
                if (checkIds.length > 0) {
                  this.dataList.forEach(row => {
                    for (var i = 0; i < checkIds.length; i++) {
                      if (checkIds[i] == row.stcd) {
                        if (this.isSingle) {
                          this.$refs.chooseTable.setCurrentRow(row);
                          this.singleName = row.stationName;
                        } else {
                          this.$refs.chooseTable.toggleRowSelection(row, true);
                        }
                      }
                    }
                  });
                }
              });
              this.tableLoading = false;
            }
          })
          break;
        default:
          opt = {
            stType: this.type,
            stName: this.seawallName
          };
          await ruleConfiguration.stList(opt).then(res => {
            if (res.code == 0) {
              this.dataList = res.data;
              // 选中
              this.$nextTick(() => {
                if (checkIds.length > 0) {
                  this.dataList.forEach(row => {
                    for (var i = 0; i < checkIds.length; i++) {
                      if (checkIds[i] == row.stCode) {
                        this.$refs.chooseTable.toggleRowSelection(row, true);
                      }
                    }
                  });
                }
              });
              this.tableLoading = false;
            }
          });
          break;
      }
    },
    handleSelectionChange(val) {
      console.log(val, 'Selection');
      this.checkItems = val;
      this.num = val.length;
    },
    handleCurrentChange(val) {
      console.log(val, 'Current');
      this.checkItems = [val];
      this.singleName = val.stName;
    },
    // 设置阈值
    setThresholdVal() {
      if (this.checkItems.length == 0) {
        this.$message.warning("请勾选需要的测站！");
      } else {
        this.close();
        this.$emit("setThresholdId", this.checkItems);
      }
    },
    // 关闭
    close() {
      this.$emit("close");
    }
  }
};
</script>
<style lang="scss" scoped></style>
