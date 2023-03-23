<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 数据表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="form_content">
    <el-form
      ref="form"
      :model="form"
      :rules="readonly ? {} : rules"
      label-width="96px"
      label-position="top"
    >
      <el-form-item
        v-for="item in headerList"
        :key="item.columnName"
        :label="item.displayName"
        :prop="item.columnName"
        :rules="getColumnRules(item)"
      >
        <!--日期类型 -->
        <template v-if="item.dataType == 91">
          <el-date-picker
            v-model="form[item.columnName]"
            type="date"
            value-format="yyyy-MM-dd"
            :readonly="readonly"
          >
          </el-date-picker>
        </template>
        <!--时间类型 -->
        <template v-else-if="item.dataType == 92">
          <el-time-picker
            v-model="form[item.columnName]"
            value-format="HH:mm:ss"
            :readonly="readonly"
          >
          </el-time-picker>
        </template>
        <!--日期时间类型 -->
        <template v-else-if="item.dataType == 93">
          <el-date-picker
            v-model="form[item.columnName]"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :readonly="readonly"
          >
          </el-date-picker>
        </template>
        <template
          v-else-if="
            item.dataType == -5 ||
            item.dataType == 2 ||
            item.dataType == 3 ||
            item.dataType == 4 ||
            item.dataType == 5 ||
            item.dataType == 6 ||
            item.dataType == 7 ||
            item.dataType == 8
          "
        >
          <!--数值类型 -->
          <el-input-number
            v-model="form[item.columnName]"
            controls-position="right"
            :readonly="readonly"
          ></el-input-number>
        </template>
        <!--布尔类型 -->
        <template v-else-if="item.dataType == -7">
          <el-select v-model="form[item.columnName]">
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
          <el-input v-model="form[item.columnName]" :readonly="readonly" />
        </template>
      </el-form-item>
    </el-form>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset('form')">取 消</el-button>
      <el-button type="primary" @click="handleSubmit('form')" v-if="!readonly"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
import {
  addColumnData,
  updateColumnData,
} from "_a/dataManage/dataSheetManage/dataSheetManage";
import { MessageError, MessageSuccess } from "_l/message";
export default {
  name: "dataSourceForm",
  props: {
    readonly: {
      type: Boolean,
      default: false,
    },
    dialogTitle: {
      type: String,
    },
    headerList: {
      type: Array,
      default: [],
    },
    dataSourceId: {
      type: String,
    },
    tableName: {
      type: String,
    },
  },
  components: {},
  data() {
    return {
      form: {},
      rules: {},
      oldForm: {},
      booleanArr: [
        {
          label: "true",
          value: true,
        },
        {
          label: "false",
          value: false,
        },
      ],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    // 获取是否必填验证
    getColumnRules(item) {
      if (item.nullable != 1) {
        return {
          required: true,
          message: "请输入" + item.columnName + "",
          trigger: "blur",
        };
      } else {
        return null;
      }
    },

    // 获取input类型
    getInputType(item) {
      if (
        item.dataType == -5 ||
        item.dataType == 2 ||
        item.dataType == 3 ||
        item.dataType == 4 ||
        item.dataType == 5 ||
        item.dataType == 6 ||
        item.dataType == 7 ||
        item.dataType == 8
      ) {
        return "number";
      } else {
        return "text";
      }
    },

    // 获取小数位数
    getFloat(item) {
      if (item.decimalDigits > 0) {
        // console.log(
        //   Math.pow(0.1, item.decimalDigits).toFixed(item.decimalDigits)
        // );
        return Math.pow(0.1, item.decimalDigits).toFixed(item.decimalDigits);
      } else {
        return false;
      }
    },

    reset() {
      this.$refs["form"].resetFields();
    },
    setForm(row) {
      this.form = JSON.parse(JSON.stringify(row.data));
      this.oldForm = JSON.parse(JSON.stringify(row.data));
    },
    /**
     * 重置表单
     * @param name
     */
    handleReset(name) {
      this.$emit("close");
      this.$refs[name].resetFields();
    },
    /**
     * 提交表单
     * @param name
     */
    handleSubmit(name) {
      //console.log(this.form);
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.dialogTitle === "新增数据") {
            addColumnData({
              // colList: this.headerList,
              data: this.form,
              dataSourceId: this.dataSourceId,
              tableName: this.tableName,
            }).then((res) => {
              if (res.code) {
                MessageSuccess("add");
              } else {
                MessageError("add");
              }
              this.$emit("closeDialogAndRefresh");
            });
          } else {
            updateColumnData({
              // colList: this.headerList,
              data: this.form,
              oldData: this.oldForm,
              dataSourceId: this.dataSourceId,
              tableName: this.tableName,
            }).then((res) => {
              if (res.code) {
                MessageSuccess("upd");
              } else {
                MessageError("upd");
              }
              this.$emit("closeDialogAndRefresh");
            });
          }
        }
      });
    },
  },
};
</script> 

<style lang='scss' scoped>
.form_content {
  max-height: 650px;
  padding: 20px;
  overflow-y: auto;
  ::v-deep .el-select {
    width: 100%;
  }
}
.dialog-footer {
  text-align: right;
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
