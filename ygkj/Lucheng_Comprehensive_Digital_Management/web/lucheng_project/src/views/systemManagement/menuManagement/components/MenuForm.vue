<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-01 14:34:32
 * @Description: 用户信息表单
 * @FilePath: \lucheng_project\src\views\systemManagement\menuManagement\components\MenuForm.vue
-->
<template>
  <div class="dialog_wrap">
    <el-form
      ref="postForm"
      :model="postForm"
      :rules="rules"
      class="form-container"
    >
      <el-form-item label-width="30%" label="资源名称" prop="resName">
        <el-input
          v-model="postForm.resName"
          placeholder="请输入资源名称"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item label-width="30%" label="资源类型" prop="resType">
        <el-select
          v-model="postForm.resType"
          placeholder="请选择资源类型"
          style="width: 70%"
          clearable
        >
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.name"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="postForm.resType != '3'"
        label-width="30%"
        label="路径"
        prop="resUrl"
      >
        <el-input
          v-model="postForm.resUrl"
          placeholder="请输入路径"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item
        v-if="postForm.resType == '1'"
        label-width="30%"
        label="重定向"
        prop="redirect"
      >
        <el-input
          v-model="postForm.redirect"
          placeholder="请输入重定向路径"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item
        v-if="postForm.resType != '3'"
        label-width="30%"
        label="标识"
        prop="name"
      >
        <el-input
          v-model="postForm.name"
          placeholder="请输入标识"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item
        v-if="postForm.resType != '3'"
        label-width="30%"
        label="组件"
        prop="component"
      >
        <el-input
          v-model="postForm.component"
          placeholder="请输入组件"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item
        v-if="postForm.resType == '1'"
        label-width="30%"
        label="隐藏二级菜单"
      >
        <el-switch v-model="postForm.hideSecond" style="width: 70%"></el-switch>
      </el-form-item>
      <el-form-item
        v-if="postForm.resType == '3'"
        label-width="30%"
        label="功能标识"
        prop="permission"
      >
        <el-input
          v-model="postForm.permission"
          placeholder="请输入功能标识"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item
        v-if="postForm.resType != '3'"
        label-width="30%"
        label="排序"
        prop="sort"
      >
        <el-input
          v-model="postForm.sort"
          placeholder="请输入排序"
          type="number"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item label-width="30%" label="备注" prop="remark">
        <el-input
          v-model="postForm.remark"
          placeholder="请输入备注"
          type="textarea"
          rows="2"
          style="width: 70%"
        />
      </el-form-item>
      <div class="footer_btn">
        <el-button
          v-loading="loading"
          type="primary"
          class="btn_save"
          @click="submitForm('postForm')"
          >保存</el-button
        >
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { systemMenuSaveApi, sysResDetailApi } from "@/api/systemManagement.js";
export default {
  name: "UnitForm",
  props: {
    id: [String, Number],
    pId: [String, Number],
    flag: [String, Number],
  },
  components: {},
  data() {
    return {
      loading: false,
      typeList: [
        {
          name: "目录",
          value: "1",
        },
        {
          name: "菜单",
          value: "2",
        },
        {
          name: "功能",
          value: "3",
        },
      ],
      adcdList: [],
      postForm: {
        pId: "",
        resName: "",
        resType: "3",
        resUrl: "",
        redirect: "",
        name: "",
        component: "",
        sort: "",
        remark: "",
        hideSecond: false,
        permission: "",
      },
      rules: {
        resType: [{ required: true, message: "必填项", trigger: "blur" }],
        resName: [{ required: true, message: "必填项", trigger: "blur" }],
        name: [{ required: true, message: "必填项", trigger: "blur" }],
        component: [{ required: true, message: "必填项", trigger: "blur" }],
      },
      saveType: false,
    };
  },
  mounted() {
    console.log(this.pId);
    Promise.all([]).then(() => {
      if (this.id) {
        this.getData();
      }
    });
  },
  methods: {
    async getData() {
      let res = await sysResDetailApi({
        id: this.id,
      });
      if (res.code == 0) {
        this.postForm = res.data;
      }
    },
    submitForm(postForm) {
      this.$refs[postForm].validate((valid) => {
        if (valid) {
          if (this.saveType) {
            return false;
          }
          this.saveType = true;
          this.loading = true;
          this.postForm.pId = this.pId;
          if (this.id) {
            this.postForm.id = this.id;
          } else {
            this.postForm.flag = this.flag;
          }
          const formData = this.postForm;
          this.save(formData);
        } else {
          return false;
        }
      });
    },
    async save(data) {
      let res = await systemMenuSaveApi(data);
      if (res.code == 0) {
        this.loading = false;
        this.$message({
          message: "修改成功",
          type: "success",
          duration: "1000",
        });
        this.closeEditDialog(1);
      } else {
        this.loading = false;
        this.$message.error(res.message);
      }
      this.saveType = false;
    },
    closeEditDialog(type) {
      this.$emit("closeEditDialog", type);
    },
  },
};
</script>

<style lang='scss' scoped>
.dialog_wrap {
}
</style>