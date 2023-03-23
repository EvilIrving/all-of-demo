<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 所属部门表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="link-box">
    <el-form ref="form" :model="form" :rules="rules" label-width="96px">
      <el-form-item label="所属部门" prop="deptId">
        <el-cascader
          v-model="form.deptId"
          placeholder="所属部门"
          :options="deptTreeData"
          :props="{
            children: 'children',
            label: 'name',
            value: 'id',
            checkStrictly: true,
            emitPath: false,
          }"
          :show-all-levels="false"
        ></el-cascader>
      </el-form-item>
      <!-- <el-tree
        :data="deptTreeData"
        node-key="id"
        ref="deptTree"
        :props="{
          children: 'children',
          label: 'name',
        }"
        show-checkbox
      /> -->
    </el-form>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset('form')">取 消</el-button>
      <el-button type="primary" @click="handleSubmit('form')">保存</el-button>
    </div>
  </div>
</template>

<script>
import { getDeptTree } from "_a/admin/user/user";
import { addDataAccessDept } from "_a/dataManage/dataAssessment/dataAssessment";
import { MessageCustom } from "_l/message";

export default {
  name: "departForm",
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
        deptId: null,
      },
      rules: {
        deptId: [this.$ruler("所属部门")],
      },
      deptTreeData: [],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getDeptTreeData();
  },
  methods: {
    getDeptTreeData() {
      getDeptTree().then((res) => {
        if (res.data) {
          this.deptTreeData = res.data;
        }
      });
    },

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
      // 获取选中叶子节点
      // let resIds = [];
      // this.$refs.deptTree.getCheckedNodes(true, false).forEach((e) => {
      //   resIds.push(e.id);
      // });
      // console.log(resIds);
      this.$refs[name].validate((valid) => {
        if (valid) {
          let tableNameList = [];
          this.form.dataSourceId = this.dataSourceId;
          this.selectRows.map((item) => {
            tableNameList.push(item.tableName);
          });
          this.form.tableNameList = tableNameList;
          //console.log(this.form);
          addDataAccessDept(this.form).then((res) => {
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
.el-tree {
  margin-bottom: 24px;
}
.el-cascader {
  width: 100%;
}
::v-deep .el-select {
  width: 100%;
}
.dialog-footer {
  text-align: right;
}
</style>
