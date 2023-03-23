<template>
  <div class="user-form">
    <el-form ref="fileManageForm" label-width="0">
      <span class="import-duty" @click="exportTemplate">导入模板下载</span>
      <div class="form_item">
        <el-upload
          drag
          :show-file-list="false"
          :headers="headers"
          :on-success="handleSucess"
          :action="uploadUrl"
          accept=".xlsx"
          name="file"
          :data="{
            pid: projectInfo.prcd,
          }"
          multiple
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
            <div class="el-upload__tip" slot="tip">支持文件xlsx等格式</div>
          </div>
        </el-upload>
      </div>

      <div class="form_item file_item">
        <el-row :gutter="20">
          <el-col :span="8">
            <span>名称</span>
          </el-col>
          <el-col :span="8">
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
            <el-col :span="8">
              <el-form-item>
                <el-input
                  readonly
                  v-model="file.fileName"
                  placeholder="请输入文件名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item>
                <el-input
                  placeholder="文件大小"
                  readonly
                  :value="file.fileSize"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item>
                <el-input
                  placeholder="文本类型"
                  readonly
                  :value="file.fileType"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <el-form-item>
                <span>成功</span>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 按钮 -->
      <div class="el-dialog__footer">
        <el-row :gutter="20">
          <el-form-item>
            <div class="form-footer">
              <el-button
                class="form-btn save-btn"
                size="mini"
                @click="$emit('cancelDialog')"
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
import { beOnDuty } from "@/api/projectUnitDetail";
import { exportDuty } from "@/utils/export.js";
export default {
  name: "inportForm",
  props: {},
  components: {},
  computed: {
    headers: function () {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      achievementId: "",
      checkboxGroup1: [],
      tagOptions: [],
      fileList: [],
      uploadUrl: "",
    };
  },
  created() {
    this.uploadUrl = `${this.$config.baseUrl}/project-manage/bhDutyRecord/importDuty`;
    console.log(this.uploadUrl);
  },
  methods: {
    handleSucess(file) {
      if (file.code == 0) {
        this.$message.success("导入成功");
        this.fileList.push({
          fileName: file.data.fileName,
          fileSize: file.data.size,
          fileType: file.data.suffix,
        });
      } else {
        this.$message.error(file.message);
      }
    },
    exportTemplate() {
      let obj = {
        pid: "-",
      };
      exportDuty(obj);
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
.import-duty {
  color: #0694ff;
  cursor: pointer;
}
.user-form {
  width: 100%;
  padding-top: 8px;
  .el-form {
    // width: 100%;
    // background: #fff;

    .form_item {
      background: #fff;
      padding: 16px 0;
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
