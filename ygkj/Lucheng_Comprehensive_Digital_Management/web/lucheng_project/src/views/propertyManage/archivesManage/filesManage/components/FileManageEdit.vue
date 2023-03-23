<template>
  <div class="user-form">
    <el-form
      ref="fileManageEdit"
      :model="fileForm"
      :rules="filelRules"
      label-width="0"
    >
      <div class="form_item file_item">
        <el-row :gutter="20">
          <el-col :span="6">
            <span>名称</span>
          </el-col>
          <el-col :span="6">
            <span>归档编号</span>
          </el-col>
          <el-col :span="6">
            <span>文件大小</span>
          </el-col>
          <el-col :span="6">
            <span>文件类型</span>
          </el-col>
        </el-row>
        <div class="file_list">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item prop="fileName">
                <el-input
                  v-model="fileForm.fileName"
                  placeholder="请输入文件名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-input
                  placeholder="归档编号"
                  v-model="fileForm.id"
                  :disabled="true"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-input
                  placeholder="文件大小"
                  v-model="fileForm.fileSize"
                  :disabled="true"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-input
                  placeholder="文本类型"
                  v-model="fileForm.fileType"
                  :disabled="true"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="form_item file_item">
        <el-row :gutter="20">
          <el-col :span="6">
            <span>所属目录</span>
          </el-col>
        </el-row>
        <div class="file_list">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="">
                <el-cascader
                  style="width: 100%"
                  v-model="fileForm.projectCode"
                  :options="options"
                  :props="{
                    checkStrictly: true,
                    value: 'id',
                    label: 'name',
                    children: 'children',
                  }"
                ></el-cascader>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>

      <div class="form_item tag_item">
        <p>编辑标签</p>
        <div class="tag_tip">
          温馨提示：标签支持您按各种标准（如用途、所有者或项目）对资源进行分类；
        </div>
        <!-- <div class="tag_num">共添加标签：8<span class="right">新建标签</span></div> -->
        <el-checkbox-group v-model="checkboxGroup1" ref="tagTypeRef">
          <el-checkbox-button
            v-for="tag in tagOptions"
            :label="tag.id"
            :key="tag.id"
            >{{ tag.name }}</el-checkbox-button
          >
        </el-checkbox-group>
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
import {
  getLabelDataApi,
  getUpFileListApi,
  updateUpFileApi,
} from "@/api/propertyManage";
import { getFileTree } from "@/api/propertyManage";
export default {
  name: "FileManageEdit",
  props: {
    achievementId: {
      type: String | Number,
      default: '0'
    }
  },
  components: {},
  data() {
    return {
      fileId: "",
      checkboxGroup1: [],
      tagOptions: [],
      fileForm: {
        fileName: "",
        id: "",
        fileSize: "",
        fileType: "",
        projectCode: [],
      },
      filelRules: {
        fileName: [
          { required: true, message: "请输入文件名称", trigger: "blur" },
        ],
      },
      options: [],
    };
  },
  mounted() {
    this._getFileList();
  },
  computed: {
    headers: function () {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
  },
  methods: {
    async _getFileList() {
      let res = await getFileTree();
      if (res.code == 0) {
        this.options = res.data[0].children;
      }
    },
    async onSubmit() {
      let opt = {
        id: this.fileId,
        fileName: this.fileForm.fileName,
        labelIds: this.checkboxGroup1.join(","),
        achievementId: this.achievementId,
        AfterachievementId:
          this.fileForm.projectCode.length > 0
            ? this.fileForm.projectCode[this.fileForm.projectCode.length - 1]
            : "",
      };
      await updateUpFileApi(opt).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "保存成功",
            type: "success",
          });
          this.$emit("cancelDialog");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    resetForm() {
      this.fileId = "";
      this.checkboxGroup1 = [];
      this.tagOptions = [];
      this.$refs.fileManageEdit.resetFields();
    },
    async getFormData(id) {
      this.fileId = id;
      await getLabelDataApi({ fetchAll: true }).then((res) => {
        if (res.code == 0) {
          this.tagOptions = res.data.list;
        }
      });
      await getUpFileListApi({
        id: this.fileId,
      }).then((res) => {
        if (res.code == 0 && res.data.list.length > 0) {
          const { id, fileName, fileSize, fileType } = res.data.list[0];
          this.fileForm = {
            id,
            fileName,
            fileSize,
            fileType,
            projectCode: []
          };
          if (res.data.list[0].labelIds && res.data.list[0].labelIds != "") {
            this.checkboxGroup1 = res.data.list[0].labelIds
              .split(",")
              .map(Number);
          }
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
  padding-top: 8px;
  .el-form {
    // width: 100%;
    // background: #fff;

    .form_item {
      width: 100%;
      background: #fff;
      padding: 16px;
      margin-bottom: 8px;
      .el-row {
        span {
          display: inline-block;
          padding-bottom: 6px;
        }
      }
    }
    .file_item {
      padding: 16px 16px 8px 16px;
      .file_list {
        max-height: 96px;
        overflow-x: hidden;
        overflow-y: auto;
        .file_del {
          color: red;
          cursor: pointer;
        }
      }
    }
    .tag_item {
      p {
        font-size: 18px;
        font-weight: bold;
        color: #000;
      }
      .tag_tip {
        height: 33px;
        line-height: 32px;
        text-align: center;
        font-size: 14px;
        color: #0694ff;
        background: #f5fbff;
        border: 1px solid #0091ff;
        margin: 12px 0;
      }
      .tag_num {
        line-height: 32px;
        margin-bottom: 12px;
        span {
          color: #fff;
          padding: 0 16px;
          border-radius: 4px;
          background: #01a59c;
          cursor: pointer;
        }
      }
    }
    .el-checkbox-group {
      .el-checkbox-button {
        margin: 4px 6px;
      }
    }
    .form-footer {
      padding-right: 20px;
    }
  }
}
</style>
<style lang="scss">
.user-form .el-upload {
  display: block;
}
.user-form .el-upload-dragger {
  width: 100%;
}
.user-form .el-checkbox-group {
  max-height: 84px;
  overflow-y: auto;
}
.user-form .el-form-item {
  margin-bottom: 8px;
}
</style>
