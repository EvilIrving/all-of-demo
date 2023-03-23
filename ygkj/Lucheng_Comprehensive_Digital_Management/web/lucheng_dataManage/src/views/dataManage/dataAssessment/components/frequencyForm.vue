<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 更新频次表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="link-box">
    <el-form ref="form" :model="form" :rules="rules" label-width="96px">
      <el-form-item label="更新频次" prop="frequencyUnit">
        <el-select v-model="form.frequencyUnit">
          <el-option
            v-for="item in frequencyArr"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset('form')">取 消</el-button>
      <el-button type="primary" @click="handleSubmit('form')">保存</el-button>
    </div>
  </div>
</template>

<script>
import { updateFrequency } from "_a/dataManage/dataAssessment/dataAssessment";
import { MessageCustom } from "_l/message";

export default {
  name: "frequencyForm",
  props: {
    selectRows: {
      type: Array,
    },
    dataSourceId: {
      type: String,
    },
  },
  components: {},
  data() {
    return {
      form: {
        frequencyUnit: null,
      },
      rules: {
        frequencyUnit: [this.$ruler("更新频次")],
      },
      frequencyArr: [
        {
          label: "每天",
          value: 1,
        },
        {
          label: "每月",
          value: 2,
        },
        {
          label: "每季度",
          value: 3,
        },
        {
          label: "每年",
          value: 4,
        },
      ],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    setForm() {},

    reset() {
      this.$refs["form"].resetFields();
    },
    change(e) {
      this.$forceUpdate();
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
      this.$refs[name].validate((valid) => {
        if (valid) {
          let tableNameList = [];
          this.form.dataSourceId = this.dataSourceId;
          this.selectRows.map((item) => {
            tableNameList.push(item.tableName);
          });
          this.form.tableNameList = tableNameList;
          //console.log(this.form);
          updateFrequency(this.form).then((res) => {
            if (res.code) {
              MessageCustom("success", "保存成功");
            } else {
              MessageCustom("error", "保存失败");
            }
            this.$emit("closeDialogAndRefresh");
          });
        }
      });
    },
  },
};
</script> 

<style lang='scss' scoped>
::v-deep .el-select {
  width: 100%;
}
.dialog-footer {
  text-align: right;
}
</style>
