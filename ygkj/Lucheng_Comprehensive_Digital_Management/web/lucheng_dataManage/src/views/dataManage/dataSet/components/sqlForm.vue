<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 数据集sql表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="form-content">
    <el-form ref="form" :model="form" :rules="rules" label-width="96px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="数据集名称" prop="dataSetName">
            <el-input
              v-model="form.dataSetName"
              placeholder="请输入数据集名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="数据源">
            <el-input
              v-model="dataSourceName"
              placeholder="请选择数据源"
              @focus="dataSourceSelect"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="SQL查询语句">
            <SqlEditor ref="sqlEditor" :value="sqlQueryStatement"></SqlEditor>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <div class="form-right">
            <div class="right-top">
              <div class="right-btn">
                <span
                  @click="getPreviewData"
                  v-show="this.componentValue == 'preview'"
                  >预览</span
                >
                <span
                  @click="addParameterRow"
                  v-show="this.componentValue == 'parameter'"
                  >新增</span
                >
                <span
                  @click="delParameterRow"
                  v-show="this.componentValue == 'parameter'"
                  >删除</span
                >
                <span
                  @click="delAttributesRow"
                  v-show="this.componentValue == 'attributes'"
                  >删除</span
                >
              </div>
              <el-tabs
                type="border-card"
                v-model="componentValue"
                @tab-click="handleTab"
              >
                <el-tab-pane
                  :label="item.name"
                  :name="item.value"
                  v-for="item in tabList"
                  :key="item.value"
                >
                </el-tab-pane>
              </el-tabs>
            </div>
            <div class="right-main">
              <!-- 预览内容 -->
              <div
                class="tab-preview"
                ref="tableBox"
                v-show="this.componentValue == 'preview'"
              >
                <!-- 表格 -->
                <table-page
                  ref="tablePage"
                  :manualLoad="true"
                  :columns="columns"
                  :data="tableData"
                  :checkBox="false"
                  :pageIngType="false"
                  :height="tableHeight"
                  headerCellClassName="myNowrap"
                >
                  <template slot-scope="{ index }" slot="order">{{
                    index + 1
                  }}</template>
                </table-page>
              </div>

              <!-- 参数内容 -->
              <div
                class="parameter-main"
                v-show="this.componentValue == 'parameter'"
              >
                <!-- 表格 -->
                <el-table
                  ref="parameterTable"
                  :data="parameterData"
                  border
                  @selection-change="handleParameterChange"
                  :height="tableHeight"
                >
                  <el-table-column
                    type="selection"
                    :width="50"
                    align="center"
                  ></el-table-column>
                  <el-table-column prop="paramName" label="名称">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.paramName" />
                    </template>
                  </el-table-column>

                  <el-table-column prop="paramType" label="类型" :width="130">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.paramType">
                        <el-option
                          v-for="item in parameterArr"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </template>
                  </el-table-column>

                  <el-table-column prop="required" label="必填" :width="100">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.required">
                        <el-option
                          v-for="item in requiredArr"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </template>
                  </el-table-column>

                  <el-table-column prop="paramDescribe" label="描述">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.paramDescribe" />
                    </template>
                  </el-table-column>
                </el-table>
              </div>

              <!-- 属性内容 -->
              <div
                class="parameter-main"
                v-show="this.componentValue == 'attributes'"
              >
                <!-- 表格 -->
                <el-table
                  ref="attributesTable"
                  :data="headerList"
                  border
                  @selection-change="handleAttributesChange"
                  :height="tableHeight"
                >
                  <el-table-column
                    type="selection"
                    :width="50"
                    align="center"
                  ></el-table-column>

                  <el-table-column prop="fieldName" label="名称">
                  </el-table-column>

                  <el-table-column prop="fieldType" label="类型" :width="130">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.fieldType">
                        <el-option
                          v-for="item in attributesArr"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="fieldDescribe"
                    label="描述"
                    :width="100"
                  >
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.fieldDescribe" />
                    </template>
                  </el-table-column>
                  <el-table-column prop="fieldFormat" label="数据源格式">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.fieldFormat" />
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>
    <div class="form-content"></div>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset('form')">取 消</el-button>
      <el-button type="primary" @click="handleSubmit('form')" v-if="!readonly"
        >保存</el-button
      >
    </div>

    <!-- 选择数据源弹窗 -->
    <el-dialog
      title="选择数据源"
      v-if="dataSourceShow"
      :visible.sync="dataSourceShow"
      :close-on-click-modal="false"
      :append-to-body="true"
      width="1200px"
      top="10vh"
      class="dataSourceSelectDialog"
      @close="dataSourceClose"
    >
      <DataSourceSelect
        ref="dataSourceSelect"
        :dataSourceId="dataSourceId"
        :dataSourceShow="dataSourceShow"
        @close="dataSourceClose"
        @setDataSource="setDataSource"
      ></DataSourceSelect>
    </el-dialog>

    <!-- 添加表单 -->
    <el-dialog
      v-if="parameterDialog"
      title="设置参数值"
      :visible.sync="parameterDialog"
      :close-on-click-modal="true"
      :append-to-body="true"
      width="600px"
      @close="closeParameterDialog"
    >
      <el-form
        ref="parameterForm"
        :model="parameterForm"
        label-width="96PX"
        label-position="top"
        class="parameterForm"
      >
        <el-form-item
          v-for="(item, index) in parameterData"
          :key="index"
          :label="item.paramName"
          :prop="item.paramName"
          :rules="getColumnRules(item)"
        >
          <!--日期类型 -->
          <template v-if="item.paramType == 6">
            <el-date-picker
              v-model="parameterForm[item.paramName]"
              type="date"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </template>
          <!--时间类型 -->
          <template v-else-if="item.paramType == 7">
            <el-time-picker
              v-model="parameterForm[item.paramName]"
              value-format="HH:mm:ss"
            >
            </el-time-picker>
          </template>
          <!--日期时间类型 -->
          <template v-else-if="item.paramType == 8">
            <el-date-picker
              v-model="parameterForm[item.paramName]"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>
          </template>
          <template
            v-else-if="
              item.paramType == 2 || item.paramType == 3 || item.paramType == 4
            "
          >
            <!--数值类型 -->
            <el-input-number
              v-model="parameterForm[item.paramName]"
              controls-position="right"
            ></el-input-number>
          </template>
          <!--布尔类型 -->
          <template v-else-if="item.paramType == 5">
            <el-select v-model="parameterForm[item.paramName]">
              <el-option
                v-for="item in booleanArr"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
          <!--文本框类型 -->
          <template v-else>
            <el-input v-model="parameterForm[item.paramName]" />
          </template>
        </el-form-item>
      </el-form>
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sureParameter()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTableColumnData,
  addDataSet,
  updateDataSet,
  getDataSetById,
} from "_a/dataManage/dataSet/dataSet";
import { MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";
import DataSourceSelect from "./dataSourceSelect";
import SqlEditor from "_c/SqlEditor/index";
export default {
  name: "sqlForm",
  props: {
    dialogTitle: {
      type: String,
    },
    dialogShow: {
      type: Boolean,
    },
    readonly: {
      type: Boolean,
      default: false,
    },
    setType: {
      type: Number || String,
      default: 1,
    },
    setId: {
      type: String,
    },
  },
  components: { DataSourceSelect, SqlEditor, TablePage },
  data() {
    return {
      form: {
        dataSetName: "",
      },
      rules: {
        dataSetName: [this.$ruler("数据集名称")],
      },
      id: "",
      setDetailId: "",
      dataSourceShow: false,
      oldDataSourceId: "",
      dataSourceId: "",
      dataSourceName: "",
      oldSqlQueryStatement: "",
      sqlQueryStatement: "",

      tabList: [
        {
          name: "预览",
          value: "preview",
        },
        {
          name: "参数",
          value: "parameter",
        },
        {
          name: "属性",
          value: "attributes",
        },
      ],
      componentValue: "preview",
      columns: [],
      tableData: [],
      headerList: [],
      tableHeight: 0,

      parameterCol: [
        {
          label: "名称",
          prop: "paramName",
        },
        {
          label: "类型",
          prop: "paramType",
        },
        {
          label: "必填",
          prop: "required",
        },
        {
          label: "描述",
          prop: "paramDescribe",
        },
      ],
      parameterData: [],

      parameterArr: [
        {
          label: "字符串",
          value: 1,
        },
        {
          label: "数值",
          value: 2,
        },
        {
          label: "整数",
          value: 3,
        },
        {
          label: "小数",
          value: 4,
        },
        {
          label: "布尔值",
          value: 5,
        },
        {
          label: "日期",
          value: 6,
        },
        {
          label: "时间",
          value: 7,
        },
        {
          label: "日期时间",
          value: 8,
        },
      ],

      requiredArr: [
        {
          label: "是",
          value: 1,
        },
        {
          label: "否",
          value: 0,
        },
      ],
      parameterSelection: [], //参数表格选择数据
      parameterDialog: false,
      parameterForm: {}, //参数表单

      attributesArr: [
        {
          label: "字符串",
          value: 1,
        },
        {
          label: "数值",
          value: 2,
        },
        {
          label: "整数",
          value: 3,
        },
        {
          label: "小数",
          value: 4,
        },
        {
          label: "布尔值",
          value: 5,
        },
        {
          label: "日期",
          value: 6,
        },
        {
          label: "时间",
          value: 7,
        },
        {
          label: "日期时间",
          value: 8,
        },
        {
          label: "未知",
          value: 0,
        },
      ],
      attributesData: [], //属性表格数据
      attributesSelection: [], //属性表格选择数据

      booleanArr: [
        {
          label: "是",
          value: true,
        },
        {
          label: "否",
          value: false,
        },
      ],
      flag: false,
    };
  },
  computed: {},
  watch: {
    dialogShow(val) {
      if (val) {
        if (this.dialogTitle === "编辑数据集") {
          this.getDetail();
        }
      }
    },
  },
  created() {},
  mounted() {
    if (this.dialogTitle === "编辑数据集") {
      this.getDetail();
    }
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableBox.offsetHeight;
    });
  },
  methods: {
    // 获取是否必填验证
    getColumnRules(item) {
      if (item.required == 1) {
        return {
          required: true,
          message: "请输入" + item.paramName + "",
          trigger: "blur",
        };
      } else {
        return null;
      }
    },

    reset() {
      this.id = "";
      this.setDetailId = "";
      this.form = {
        dataSetName: "",
      };
      this.oldDataSourceId = "";
      this.dataSourceId = "";
      this.dataSourceName = "";
      this.oldSqlQueryStatement = "";
      this.sqlQueryStatement = "";
      this.componentValue = "preview";
      this.columns = [];
      this.tableData = [];
      this.headerList = [];
      this.parameterData = [];
      this.parameterSelection = [];
      this.parameterForm = {};
      this.attributesData = [];
      this.attributesSelection = [];
      this.$refs.sqlEditor.setTextValue("");
    },

    // 获取详情信息
    getDetail() {
      getDataSetById(this.setId).then((res) => {
        if (res.data) {
          this.id = res.data.id;
          this.form.dataSetName = res.data.dataSetName;
          if (res.data.setDetail) {
            this.setDetailId = res.data.setDetail.id;
            this.oldDataSourceId = res.data.setDetail.dataSourceId;
            this.dataSourceId = res.data.setDetail.dataSourceId;
            this.dataSourceName = res.data.setDetail.dataBaseName;
            this.oldSqlQueryStatement = res.data.setDetail.sqlText;
            this.sqlQueryStatement = res.data.setDetail.sqlText;
          }
          if (res.data.paramList && res.data.paramList.length > 0) {
            this.parameterData = res.data.paramList;
          }
          if (res.data.fieldList && res.data.fieldList.length > 0) {
            this.headerList = res.data.fieldList;
          }
        }
      });
    },

    // 显示数据源选择
    dataSourceSelect() {
      this.dataSourceShow = true;
    },
    // 关闭数据源选择
    dataSourceClose() {
      this.dataSourceShow = false;
    },
    // 数据源选择返回数据
    setDataSource(val) {
      if (val.length > 0) {
        this.dataSourceId = val[0].id;
        this.dataSourceName = val[0].dataBaseName;
      } else {
        this.dataSourceId = "";
        this.dataSourceName = "";
      }
    },

    // tab切换
    handleTab() {},

    //刷新
    refresh() {
      this.$refs.tablePage.refresh();
    },
    // 获取预览数据
    getPreviewData() {
      this.sqlQueryStatement = this.$refs.sqlEditor.getTextValue();
      if (this.dataSourceId == "" || this.sqlQueryStatement == "") {
        this.$message({
          message: "数据源和SQL查询语句不能为空！",
          type: "error",
        });
        return false;
      }
      if (this.parameterData.length > 0) {
        this.parameterDialog = true;
        return false;
      }
      this.runPreviewData();
    },

    // 执行获取预览方法
    runPreviewData() {
      let fieldUpdate = false;
      if (this.oldSqlQueryStatement !== this.sqlQueryStatement) {
        fieldUpdate = true;
      }
      if (this.oldDataSourceId !== this.dataSourceId) {
        fieldUpdate = true;
      }

      this.oldSqlQueryStatement = this.sqlQueryStatement;
      this.oldDataSourceId = this.dataSourceId;

      if (fieldUpdate) {
        this.columns = [];
        this.headerList = [];
      }

      let setDetail = {
        dataSourceId: this.dataSourceId,
        sqlText: this.sqlQueryStatement,
      };
      let paramList = [];
      let param = {};
      let fieldList = [];
      if (this.parameterData.length > 0) {
        paramList = this.parameterData;
        param = this.parameterForm;
      }
      if (this.headerList.length > 0) {
        fieldList = this.headerList;
      }
      this.flag = true;
      // console.log(this.dataSourceId);
      // console.log(this.sqlQueryStatement);
      getTableColumnData({
        dataSetType: "1",
        fieldUpdate: fieldUpdate,
        setDetail: setDetail,
        paramList: paramList,
        param: param,
        fieldList: fieldList,
      }).then((res) => {
        if (res.data) {
          this.flag = false;
          // 处理表头数据
          if (res.data.fieldList && res.data.fieldList.length > 0) {
            this.headerList = res.data.fieldList;
            this.columns.push({ label: "序号", slot: "order", width: 50 });
            res.data.fieldList.map((item) => {
              if (item.fieldType == 5) {
                this.columns.push({
                  label: item.fieldName,
                  prop: item.fieldName,
                  minWidth: 160,
                  renderHeader: this.renderHeader,
                  formatter: this.formatBoolean,
                });
              } else {
                this.columns.push({
                  label: item.fieldName,
                  prop: item.fieldName,
                  minWidth: 160,
                  renderHeader: this.renderHeader,
                });
              }
            });
          } else {
            this.columns = [];
            this.columns.push({ label: "序号", slot: "order", width: 50 });
            this.headerList.map((item) => {
              if (item.fieldType == 5) {
                this.columns.push({
                  label: item.fieldName,
                  prop: item.fieldName,
                  minWidth: 160,
                  renderHeader: this.renderHeader,
                  formatter: this.formatBoolean,
                });
              } else {
                this.columns.push({
                  label: item.fieldName,
                  prop: item.fieldName,
                  minWidth: 160,
                  renderHeader: this.renderHeader,
                });
              }
            });
          }

          //处理表格数据
          if (res.data.res && res.data.res.length > 0) {
            this.tableData = res.data.res;
          } else {
            this.tableData = [];
          }
          this.$refs.tablePage.getTableData(this.tableData);
        }
      });
    },

    sureParameter() {
      this.parameterDialog = false;
      this.runPreviewData();
    },

    // 新增参数行
    addParameterRow() {
      this.parameterData.push({
        paramName: "",
        paramType: 1,
        required: 1,
        paramDescribe: "",
      });
    },

    // 删除参数行
    delParameterRow() {
      if (this.parameterSelection) {
        //将选中数据遍历
        this.parameterSelection.forEach((val, index) => {
          //遍历源数据
          this.parameterData.forEach((v, i) => {
            if (val === v) {
              this.parameterData.splice(i, 1);
              this.$delete(this.parameterForm, v.paramName);
            }
          });
        });
      }
      //清除选中状态
      this.$refs.parameterTable.clearSelection();
    },

    // 表格参数选择
    handleParameterChange(val) {
      this.parameterSelection = val;
    },
    // 表格属性选择
    handleAttributesChange(val) {
      this.attributesSelection = val;
    },
    // 删除属性行
    delAttributesRow() {
      if (this.attributesSelection) {
        //将选中数据遍历
        this.attributesSelection.forEach((val, index) => {
          //遍历源数据
          this.headerList.forEach((v, i) => {
            if (val === v) {
              this.headerList.splice(i, 1);
            }
          });
        });
      }
      //清除选中状态
      this.$refs.attributesTable.clearSelection();
    },

    // 关闭设置参数值弹窗
    closeParameterDialog() {
      this.parameterDialog = false;
    },

    /**
     * 提交表单
     * @param name
     */
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.sqlQueryStatement = this.$refs.sqlEditor.getTextValue();
          if (!this.dataSourceId || this.dataSourceId == "") {
            this.$message({
              message: "请选择数据源！",
              type: "error",
            });
            return false;
          }
          if (!this.sqlQueryStatement || this.sqlQueryStatement == "") {
            this.$message({
              message: "请输入SQL查询语句！",
              type: "error",
            });
            return false;
          }

          if (this.oldSqlQueryStatement !== this.sqlQueryStatement) {
            this.flag = true;
          }
          if (this.oldDataSourceId !== this.dataSourceId) {
            this.flag = true;
          }
          if (this.flag) {
            this.$message({
              message: "请先执行预览确保数据正确！",
              type: "error",
            });
            return false;
          }
          let setDetail = {
            dataSourceId: this.dataSourceId,
            sqlText: this.sqlQueryStatement,
          };
          let paramList = [];
          let param = {};
          let fieldList = [];
          if (this.parameterData.length > 0) {
            paramList = this.parameterData;
            param = this.parameterForm;
          }
          if (this.headerList.length > 0) {
            fieldList = this.headerList;
          }
          if (this.dialogTitle === "新增数据集") {
            addDataSet({
              dataSetType: "1",
              dataSetName: this.form.dataSetName,
              setDetail: setDetail,
              paramList: paramList,
              param: param,
              fieldList: fieldList,
            }).then((res) => {
              if (res.code) {
                MessageSuccess("add");
              } else {
                MessageError("add");
              }
              this.$emit("closeDialogAndRefresh");
              this.reset();
            });
          } else {
            setDetail.id = this.setDetailId;
            setDetail.dataSetId = this.id;
            updateDataSet({
              id: this.id,
              dataSetType: "1",
              dataSetName: this.form.dataSetName,
              setDetail: setDetail,
              paramList: paramList,
              param: param,
              fieldList: fieldList,
            }).then((res) => {
              if (res.code) {
                MessageSuccess("upd");
              } else {
                MessageError("upd");
              }
              this.$emit("closeDialogAndRefresh");
              this.reset();
            });
          }
        }
      });
    },

    handleReset(form) {
      this.$emit("close");
      this.reset();
    },

    // 设置表头
    renderHeader(h, { column }) {
      return [
        h(
          "el-tooltip",
          {
            props: {
              effect: "dark",
              content: column.property,
              placement: "top",
            },
          },
          [h("span", column.property)]
        ),
      ];
    },
    // 格式化布尔值
    formatBoolean(row, column, cellValue) {
      if (cellValue) {
        return "true";
      } else {
        return "false";
      }
    },
  },
};
</script> 

<style lang='scss' scoped>
.form-content {
  width: 100%;
  .form-right {
    height: 450px;
    padding-left: 16px;
    .right-top {
      position: relative;
      .right-btn {
        position: absolute;
        right: 0;
        top: 0;
        z-index: 999;
        cursor: pointer;
        span {
          line-height: 36px;
          margin: 0 10px;
        }
      }
    }
    .right-main {
      height: calc(100% - 41px);
      > div {
        height: 100%;
      }
    }
  }
}
::v-deep .el-tabs--border-card {
  box-shadow: none;
}
::v-deep .el-tabs--border-card > .el-tabs__content {
  padding: 0;
}
.dialog-footer {
  text-align: right;
}
.parameterForm {
  max-height: 550px;
  overflow-y: auto;
}
::v-deep .myNowrap .cell span {
  display: block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.dataSourceSelectDialog {
  ::v-deep .el-dialog__body {
    padding: 0;
  }
}
::v-deep .el-select {
  width: 100%;
}
::v-deep .el-date-editor.el-input,
.el-date-editor.el-input__inner,
.el-input-number--medium {
  width: 100%;
}
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
