<!--
 * @Author: zhongg
 * @LastEditTime: 2022-04-13 19:45:16
 * @LastEditors: dtb
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\propertyManage\archivesManage\authorityManage\components\DirectoryForm.vue
-->
<template>
  <div class="user-form">
    <el-form
      ref="directoryForm"
      :model="directoryForm"
      :rules="directoryRules"
      label-width="120px"
    >
      <!-- id -->
      <el-form-item label="id" prop="id" v-show="false"></el-form-item>
      <div class="form_item">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="文档目录" prop="name">
              <el-input
                v-model="directoryForm.name"
                placeholder="请输入文档目录"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目录显示顺序" prop="sort">
              <el-input
                v-model="directoryForm.sort"
                placeholder="请输入目录显示顺序"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="topId != '-1'">
          <el-col :span="12">
            <el-form-item :label="labelName" prop="projectCode">
              <el-select
                style="width: 100%"
                v-model="directoryForm.projectCode"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in options"
                  :key="item.seawallCode"
                  :label="item.seawallName"
                  :value="item.seawallCode"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!-- 按钮 -->
      <div class="el-dialog__footer">
        <el-row :gutter="20">
          <el-form-item>
            <div class="form-footer">
              <el-button
                class="form-btn cancel-btn"
                size="mini"
                @click="$emit('cancelDialog')"
                >取消</el-button
              >
              <el-button class="form-btn save-btn" size="mini" @click="onSubmit"
                >确定</el-button
              >
            </div>
          </el-form-item>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getDirectoryListApi } from "@/api/propertyManage";
import { getProjectsApi } from "@/api/projectDetail";
import { seawallListApi } from "@/api/seawallPrevent";
export default {
  name: "DirectoryForm",
  props: {
    topId: {
      type: String,
      default: "0",
    },
    fileId: {
      type: String | Number,
      default: "0",
    },
    projectCode: {
      type: String | Number,
      default: "",
    },
  },
  components: {},
  data() {
    return {
      directoryForm: {
        id: "",
        name: "",
        sort: "",
        parentId: "",
        projectCode: "",
      },
      directoryRules: {
        name: [{ required: true, message: "请输入文档目录", trigger: "blur" }],
        projectCode: [
          { required: true, message: "请选择工程", trigger: "change" },
        ],
      },
      options: [],
    };
  },
  mounted() {
    console.log(this.topId);
    if (this.topId == 0) {
      this.getList();
    } else {
      let type = this.fileId == 1 ? "水库" : this.fileId == 2 ? "水闸" : "堤防";
      this.getProjects(type);
    }
  },
  computed: {
    headers: function () {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
    labelName: function () {
      switch (this.fileId) {
        case "0":
          return "海塘工程";
        case "1":
          return "水库工程";
        case "2":
          return "水闸工程";
        default:
          return "提防工程";
      }
    },
  },
  methods: {
    async getList() {
      let res = await seawallListApi.list();
      if (res.code === 0) {
        this.options = res.data.list;
      }
    },
    async getProjects(type) {
      let res = await getProjectsApi({
        type,
      });
      console.log(res);
      if (res.code == 0) {
        this.options = res.data.map((item) => {
          return {
            seawallName: item.wagaName || item.resName || item.dikeName,
            seawallCode: item.wagaCode || item.resCode || item.dikeCode,
            ...item,
          };
        });
      }
    },
    onSubmit() {
      this.$refs.directoryForm.validate((valid) => {
        if (valid) {
          if (this.topId == -1) {
            this.directoryForm.projectCode = this.projectCode;
          }
          this.$emit("addDirectory", this.directoryForm);
        }
      });
    },
    resetForm() {
      this.$refs.directoryForm.resetFields();
    },
    getParentData(parentId) {
      this.directoryForm.parentId = parentId;
    },
    async getFormData(id, parentId) {
      await getDirectoryListApi({ id }).then((res) => {
        if (res.code == 0) {
          const { id, name, sort, parentId, projectCode } = res.data.list[0];
          this.directoryForm = {
            id,
            name,
            sort,
            parentId,
            projectCode,
          };
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.el-dialog__footer {
  border-top: 2px solid #dcdfe6;
  padding: 0;
  .el-form-item {
    margin-bottom: 0;
  }
}
.user-form {
  width: 100%;
  .el-form {
    width: 100%;
    background: #fff;
    padding: 8px 0;
    .form_item {
      margin-bottom: 8px;
      .el-form-item {
        margin-bottom: 8px;
      }
    }
  }
}
</style>
