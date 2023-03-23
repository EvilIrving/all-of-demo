<!--
 * @Author: dyn
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: dyn
 * @Description: 数据源表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="link-box">
    <el-form ref="form" :model="form" :rules="rules" label-width="96px">
      <el-form-item label="资源目录" prop="dataResourceId">
        <vue-treeselect
          v-model="form.dataResourceId"
          :options="treeData"
          placeholder="请选择资源目录"
          :normalizer="normalizer"
          @input="change($event)"
        />
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
import { getDirectoryTreeTableList } from "_a/dataManage/resourceDirectory/resourceDirectory";
import { linkResource } from "_a/dataManage/dataSheetManage/dataSheetManage";
import { MessageCustom } from "_l/message";
import VueTreeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "linkForm",
  props: {
    selectRows: {
      type: Array,
    },
  },
  components: { VueTreeselect },
  data() {
    return {
      form: {
        dataResourceId: null,
      },
      rules: {
        dataResourceId: [this.$ruler("资源目录")],
      },
      treeData: [],
      normalizer(node) {
        return {
          id: node.id,
          label: node.resourceName,
          children: node.subList ? node.subList : [],
        };
      },
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getDirectoryTreeList();
  },
  methods: {
    // 获取左侧目录树数据
    getDirectoryTreeList() {
      getDirectoryTreeTableList(true).then((res) => {
        if (res.data) {
          this.treeData = res.data;
        }
      });
    },

    reset() {
      this.$refs["form"].resetFields();
    },
    change(e) {
      this.$forceUpdate();
    },
    treeSelect(node, instanceId) {
      this.form.dataResourceId = node.id;
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
          this.form.relList = this.selectRows;
          //console.log(this.form);
          linkResource(this.form).then((res) => {
            if (res.code) {
              MessageCustom("success", "关联成功");
            } else {
              MessageCustom("error", "关联失败");
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
.dialog-footer {
  text-align: right;
}
</style>
