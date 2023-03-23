<template>
  <div class="user-form">
    <el-form ref="fileManageForm" label-width="0">
      <div class="form_item">
        <el-upload
          class="upload-demo"
          drag
          :show-file-list="false"
          :headers="headers"
          :on-success="handleSucess"
          :action="$config.fileUploadUrl"
          name="uploadFile"
          :data="{
            appName: 'archiveStatis',
          }"
          multiple
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
            <div class="el-upload__tip" slot="tip">
              支持文件doc、pdf、mp4、xsls、jpg、png、avi、rmvb等格式
            </div>
          </div>
        </el-upload>
      </div>

      <div class="form_item file_item">
        <el-row :gutter="20">
          <el-col :span="6">
            <span>名称</span>
          </el-col>
          <el-col :span="6">
            <span>归档编号</span>
          </el-col>
          <el-col :span="5">
            <span>文件大小</span>
          </el-col>
          <el-col :span="5">
            <span>文件类型</span>
          </el-col>
          <el-col :span="2">
            <span>操作</span>
          </el-col>
        </el-row>
        <div class="file_list">
          <el-row :gutter="20" v-for="file in fileList" :key="file.id">
            <el-col :span="6">
              <el-form-item>
                <el-input
                  v-model="file.fileName"
                  placeholder="请输入文件名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="6">
              <el-form-item>
                <el-input placeholder="id" :disabled="true" :value="file.id" type="hidren"></el-input>
              </el-form-item>
            </el-col> -->
            <el-col :span="6">
              <el-form-item>
                <el-input
                  placeholder="归档编号"
                  v-model="file.fileNo"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item>
                <el-input
                  placeholder="文件大小"
                  :disabled="true"
                  :value="file.fileSize"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item>
                <el-input
                  placeholder="文本类型"
                  :disabled="true"
                  :value="file.fileType"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <el-form-item>
                <span class="file_del" @click="fileDel(file.id)">删除</span>
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
import { getLabelDataApi, addDirectoryApi } from "@/api/propertyManage";
export default {
  name: "FileManageForm",
  props: {},
  components: {},
  data() {
    return {
      achievementId: "",
      checkboxGroup1: [],
      tagOptions: [],
      fileList: [],
    };
  },
  mounted() {},
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
    handleSucess(file) {
      if (file.code == 0) {
        this.fileList.push({
          fileName: file.data.originalName,
          id: file.data.id,
          fileNo: file.data.fileNo,
          fileSize: file.data.fileSize,
          fileType: file.data.ext,
        });
      }else{
        this.$message.error(file.data)
      }
    },
    fileDel(id) {
      for (let i = 0; i < this.fileList.length; i++) {
        if (this.fileList[i].id == id) {
          this.fileList.splice(i, 1);
        }
      }
    },
    async onSubmit() {
      if (this.fileList.length > 0) {
        let opt = {
          id: this.achievementId,
          fileStr: JSON.stringify(this.fileList),
          labelIds: this.checkboxGroup1.join(","),
        };
        await addDirectoryApi(opt).then((res) => {
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
      } else {
        this.$message.error("请先上传文件！");
      }
    },
    resetForm() {
      this.achievementId = "";
      this.checkboxGroup1 = [];
      this.tagOptions = [];
      this.fileList = [];
    },
    async getFormData(id) {
      this.achievementId = id;
      await getLabelDataApi({ fetchAll: true }).then((res) => {
        if (res.code == 0) {
          this.tagOptions = res.data.list;
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
