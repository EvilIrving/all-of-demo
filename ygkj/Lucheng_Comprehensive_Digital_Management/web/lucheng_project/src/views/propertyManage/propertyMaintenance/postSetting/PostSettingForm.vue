<!--
 * @Date: 2022-04-13 20:29:14
 * @Author: 
 * @Description: 
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-16 15:16:46
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyMaintenance\postSetting\PostSettingForm.vue
-->
<template>
  <div class="user-form">
    <div class="item_box_name">
      <p>基础信息</p>
    </div>
    <el-form ref="postSettingForm" status-icon inline-message label-position="right" :model="postSettingForm" :rules="rules" label-width="120px">
      <!-- id -->
      <el-form-item label="id" prop="id" v-show="false"></el-form-item>
      <div class="form_item">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属工程：" prop="projectName">

              <el-input disabled v-model="postSettingForm.projectName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工程类别：" prop="projectCategory">

              <el-input disabled v-model="postSettingForm.projectCategory"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-bottom: 15px;">
          <el-col :span="12">
            <el-form-item label="所属街道：" prop="townAdnm">

              <el-input disabled v-model="postSettingForm.townAdnm"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规模：" prop="projectScale">
              <el-input disabled v-model="postSettingForm.projectScale"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="item_box_name">
          <p>相关负责人</p>
        </div>
        <!--    townAdnm 所属乡镇（街道）
                type  单位类型 1-运行单位 2-物业单位
                projectScale  工程规模
                projectName   工程名称
                projectCategory 工程类别
                projectCode 工程编码

                负责人添加逻辑：
                新增负责人时添加名字 文件 电话 职位,设置最后一个showAdd为true显示添加按钮，第一个不显示删除按钮，添加index字段
                删除时根据index进行删除
          -->
        <section>
          <ul>
            <li v-for="(item, index) in postSettingForm.array" :key="index">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="姓名：" :prop="item.leader">
                    <el-input v-model="item.leader" placeholder="请输入姓名"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="职务：" :prop="item.leaderPost">
                    <el-select v-model="item.leaderPost" clearable placeholder="请选择职务">
                      <el-option v-for="item in responseOptions" :key="item.label" :value="item.label" :label="item.label"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="联系方式：" :prop="item.leaderPhone">
                    <el-input v-model="item.leaderPhone" placeholder="请输入联系方式"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="证书文件：" :prop="item.leaderCertificatefileName">
                    <unpload-file :multiple='false' @review='handlePreview' @success="handleSuccess($event,item.index)" @remove="handleRemove($event,item.index)" accept=".jpg,.jpeg,.png,.pdf,.JPG,.JPEG,.PNG,.PDF" :size="15" :fileList="item.fileList"></unpload-file>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item>
                    <el-button v-show="index !== 0" size="mini" type="danger" plain @click.prevent="removeDomain(item)">删除</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-button v-show="item.showAdd" style="margin-left:120px" size="mini" type="primary" plain @click="addDomain">添加负责人</el-button>
              <div class="lines"></div>
            </li>
          </ul>
        </section>
      </div>
    </el-form>
    <div class="footer_btn">
      <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
      <el-button @click="closeEditDialog()">取消</el-button>
    </div>
    <!-- <el-dialog class="file_dialog" title="文件预览" :visible.sync="fileDialog">
      <look-file v-if="fileDialog" :url="previewUrl"></look-file>
    </el-dialog> -->
  </div>
</template>

<script>
import unploadFile from '@/components/uploadFile.vue'
import { operationUnit } from '@/api/propertyManage'
import LookFile from "@/components/LookFile.vue";
export default {
  name: "PostSettingForm",
  props: {
    id: {
      type: String | Number,
      default: "",
    },
    deptId: [String]
  },
  components: { unploadFile, LookFile },
  data() {
    return {
      headers: {
        Authorization: this.$sessionData("get", "userInfo").accessToken,
      },
      postSettingForm: {},
      rules: {},
      projectCategory: '',
      dutyArr: [],
      responseOptions: [
        { label: '单位负责人', value: 'unit' },
        { label: '技术负责人', value: 'technica' },
        // { label: '项目负责人', value: 'project' },
        { label: '巡查负责人', value: 'patrol' }
      ],
      fileDialog: false,
      previewUrl: "",
    };
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
    // this.getNameList()
    // this.getStreetList()
  },
  computed: {},
  methods: {
    removeDomain(item) {
      let arr = this.postSettingForm.array
      arr.forEach((i, index) => {
        i.index = index
      })
      arr.splice(item.index, 1)
      arr[arr.length - 1].showAdd = true
      this.$nextTick(() => {
        this.postSettingForm.array = arr
      })
    },
    addDomain() {
      // if (this.postSettingForm.array[0].name === '') {
      //   this.$message.warning('请先填写第一个相关负责人');
      //   return;
      // }
      this.postSettingForm.array.forEach(element => {
        element.showAdd = false
      });
      if (this.postSettingForm.array.length < 4) {
        this.postSettingForm.array.push({
          leader: '',
          leaderPhone: '',
          leaderPost: '',
          showAdd: true,
          index: this.postSettingForm.array.length
        });
      } else {
        this.$message.warning("仅能添加4类职位");
      }
    },
    handleSuccess(res, index) {
      // console.log(res.file, 'res');
      // console.log(index, 'index');
      if (res.file.code === 0) {
        this.postSettingForm.array[index].leaderCertificatefile = res.file.data.id
      }
    },
    handleRemove(res, index) {
      this.postSettingForm.array[index].leaderCertificatefile = ''
      // this.postSettingForm.leaderCertificatefileName = ''
      // this.postSettingForm.fileurl = ''
      // this.postSettingForm.filename = ''
    },
    handlePreview(res) {
      // console.log(res, 'res');
      this.previewUrl = res.response ? res.response.data.filePath : res.url
      // this.fileDialog = true;
      window.open(this.previewUrl)
    },
    async getDetail() {
      let res = await operationUnit.queryPosition({
        id: this.id,
      })
      if (res.code === 0) {
        if (res.data.array && res.data.array.length) {
          res.data.array.map((item, index) => {
            item.fileList = [{
              name: item.leaderCertificatefileName,
              url: item.leaderCertificatefileUrl,
            }]
            item.index = index
            item.showAdd = index === res.data.array.length - 1 ? true : false
          });
        } else {
          res.data.array = [{
            leader: '',
            leaderPhone: '',
            leaderPost: '',
            index: 0,
            showAdd: true,
          }]
        }
        this.postSettingForm = res.data
      }
    },
    submitForm() {
      this.$refs.postSettingForm.validate((valid) => {
        if (valid) {
          this.postSettingForm.type = 2
          this.postSettingForm.id = this.id
          console.log(this.postSettingForm, 'this.postSettingForm');
          // this.postSettingForm.unitLeader = 
          this.save(this.postSettingForm);
        }
      });
    },
    async save(data) {
      let array = []
      array = data.array.map(element => {
        return {
          leader: element.leader,
          leaderPhone: element.leaderPhone,
          leaderPost: element.leaderPost,
          leaderCertificatefile: element.leaderCertificatefile
        }
      });
      const {
        townAdnm,
        type,
        projectScale,
        projectName,
        projectCategory,
        projectCode,
        id
      } = data;
      let temp = {
        jsonarray: JSON.stringify(array),
        townAdnm,
        type,
        projectScale,
        projectName,
        projectCategory,
        projectCode,
        id,
        deptId: this.deptId
      };
      let res = await operationUnit.saveOrUpdatePosition(temp);
      if (res.code == 0) {
        this.$message({
          message: "保存成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.closeEditDialog();
            this.$emit("getList");
          },
        });
      } else {
        this.$message.error("保存失败！");
      }
    },
    resetForm() {
      this.$refs.postSettingForm.resetFields();
    },
    closeEditDialog() {
      this.$emit("closeEditDialog");
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
.is-plain {
  background: #fff;
}
.item_box_name {
  // @includes flexbox();
  // @includes flexAI(center);
  // @includes flexJC(space-between);
  // border-bottom: 1px solid #eee;
  height: 25px;
  padding-right: 16px;
  margin-bottom: 4px;
  p {
    font-size: 16px;
    font-weight: 600;
    color: #333333;
    height: 16px;
    line-height: 16px;
    border-left: 4px solid #1492ff;
    border-radius: 2px;
    padding-left: 10px;
  }
}
.el-dialog__body {
  padding: 10px 20px 30px;
}
.lines {
  width: 100%;
  height: 1px;
  background: #f5f5f5;
  margin: 5px 0 10px;
}
.el-input.is-disabled .el-input__inner {
  background-color: #ffffff;
  color: #4e5158;
  cursor: not-allowed;
}
.upload-demo {
  .upload-btn {
    display: flex;
    align-items: center;
    img {
      width: 14px;
      height: 13px;
      margin-right: 8px;
    }
  }
  .el-upload__tip {
    margin-top: 0;
    color: #969799;
    line-height: 18px;
  }
}
</style>

<style lang="scss">
.file_dialog {
  z-index: 2030 !important;
}
</style>
