
<!--
 * @Author: ly
 * @Date: 2021-08-11 10:18:02
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-12 10:58:28
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\project_supervision\dialog\newContentDialog.vue
-->
<template>
  <el-dialog
    :visible.sync="visible"
    width="1100px"
    center
    :before-close="close"
    :close-on-click-modal="false"
    top="10vh"
    :title="title"
    class="list-dialog scroll-dialog"
  >
    <el-form ref="templateForm" :model="form" :rules="rules">
      <el-row :gutter="20">
        <primary-subhead class="head" text="基本资料" />
        <el-col :span="8">
          <el-form-item prop="projectName" label="项目名称">
            <el-input
              v-model="form.projectName"
              placeholder="请输入项目名称"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="projectCode" label="项目编号">
            <el-input
              v-model="form.projectCode"
              placeholder="请输入项目编号"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="adcd" label="行政区划">
            <el-select
              v-model="form.adcd"
              placeholder="请选择"
              :disabled="type == 'view'"
              @change="areaChange"
            >
              <el-option
                :label="item.label"
                :value="item.value"
                :key="index"
                v-for="(item, index) in areaOptions"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="year" label="年份">
            <el-date-picker
              :disabled="type == 'view'"
              v-model="form.year"
              format="yyyy"
              value-format="yyyy"
              type="year"
              placeholder="选择年"
              :picker-options="pickerOptions"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="riverId" label="河道名称">
            <el-select
              v-model="form.riverId"
              value-key="id"
              placeholder="请输入河流名称搜索"
              :disabled="type == 'view'"
              filterable
              :filter-method="riverFilter"
              @change="riverChange"
              clearable
            >
              <el-option
                :label="item.label"
                :value="item.value"
                :key="index"
                v-for="(item, index) in riverData"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="type == 'add'" :span="8">
          <el-form-item prop="projectStage" label="项目阶段">
            <el-select v-model="form.projectStage" placeholder="请选择">
              <el-option
                :label="item.label"
                :value="item.value"
                :key="index"
                v-for="(item, index) in projectPhaseList"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 项目审批表 -->
      <el-row :gutter="20">
        <primary-subhead class="head" text="项目审批表" />
        <el-col :span="8">
          <el-form-item prop="unitName" label="单位名称">
            <el-input
              v-model="form.unitName"
              placeholder="请输入单位名称"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="legalRepresent" label="法人代表">
            <el-input
              v-model="form.legalRepresent"
              placeholder="请输入法人代表"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="phone" label="联系电话">
            <el-input
              v-model="form.phone"
              placeholder="请输入联系电话"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="involvedWaterName" label="涉及水域名称">
            <el-input
              v-model="form.involvedWaterName"
              placeholder="请输入涉及水域名称"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="occupyWaterTime" label="占用水域时间">
            <el-input
              v-model="form.occupyWaterTime"
              placeholder="请输入占用水域时间"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="occupyWaterArea" label="占用水域面积">
            <el-input
              v-model="form.occupyWaterArea"
              placeholder="请输入占用水域面积"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="isNarrow" label="是否缩窄现状河宽">
            <el-select
              v-model="form.isNarrow"
              placeholder="请选择"
              :disabled="type == 'view'"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            prop="isAffectSafety"
            label="是否破坏堤防结构、影响水工程安全"
          >
            <el-select
              v-model="form.isAffectSafety"
              placeholder="请选择"
              :disabled="type == 'view'"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="filleArea" label="补偿面积">
            <el-input
              v-model="form.filleArea"
              placeholder="请输入"
              type="text"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item prop="remark" label="其他(多行文字)">
            <el-input
              v-model="form.remark"
              placeholder="请输入"
              :rows="3"
              type="textarea"
              :readonly="type == 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 相关文件/申请材料 -->
      <el-row :gutter="20">
        <primary-subhead class="head" text="相关文件/申请材料" />
        <el-col :span="12">
          <el-form-item prop="fileIds" label="附件上传">
            <el-upload
              :disabled="type == 'view'"
              class="upload-demo"
              :action="$config.fileUploadUrl"
              name="uploadFile"
              ref="upload"
              multiple
              drag
              :file-list="fileList"
              :on-change="fileChange"
              :headers="headers"
              :on-success="handleAvatarSuccess"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
            >
              <i class="el-icon-upload" style="color: #008a83"></i>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="processLimit">
            <p class="item-label">
              地图选点：
              <img
                class="icon-check-map"
                src="@/assets/images/mapsearch.png"
                alt=""
                @click="openSelectMap()"
              />
            </p>
            <el-col :span="12">
              <el-form-item prop="longitude">
                <p class="item-label">经度</p>
                <el-input
                  v-model="form.longitude"
                  placeholder="请输入"
                  type="text"
                  :readonly="type == 'view'"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="latitude">
                <p class="item-label">纬度</p>
                <el-input
                  v-model="form.latitude"
                  placeholder="请输入"
                  type="text"
                  :readonly="type == 'view'"
                  disabled
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 保存按钮 -->
    <div slot="footer" v-if="type !== 'view'">
      <div class="form-footer">
        <el-button class="form-btn btn-cancel" @click="close">取消</el-button>
        <el-button class="form-btn btn-save" @click="addProject"
          >保存</el-button
        >
      </div>
    </div>
    <!-- 地图选点 -->
    <primary-dialog
      mainTitle="地图操作"
      :visible="mapVisible"
      @handleClose="handleMapClose"
      width="1100px"
      class="map-dialog"
    >
      <div class="dialog-content">
        <select-map
          :pointArr="pointArray"
          v-if="mapVisible"
          @handleClose="handleMapClose"
          :isDraw="true"
          @saveData="saveData"
        />
      </div>
    </primary-dialog>
  </el-dialog>
</template>

<script>
import { PrimarySubhead, PrimaryDialog, SelectMap } from "@/components";
// import { statementApi, dynamicManagementApi } from "@/api/dailyBusiness";
import { projectSupervisionApi } from "@/api/projectSupervision";
import { getFileList } from "@/api/login";

const rules = {
  projectName: [
    { required: true, message: "请填写项目名称。", trigger: "blur" },
  ],
  adcd: [{ required: true, message: "请填写行政区划。", trigger: "blur" }],
  year: [{ required: true, message: "请填写年份。", trigger: "blur" }],
  unitName: [{ required: true, message: "请填写单位名称。", trigger: "blur" }],
  riverId: [{ required: true, message: "请填写河道名称。", trigger: "blur" }],
  involvedWaterName: [
    { required: true, message: "请填写涉及水域名称。", trigger: "blur" },
  ],
};

export default {
  props: {
    areaOptions: Array,
    dialogDialog: Object,
    type: String,
    visible: Boolean,
    id: [String, Number],
  },
  components: { PrimarySubhead, PrimaryDialog, SelectMap },
  computed: {
    /** @returns {{authorization:string}} */
    headers() {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = { authorization: userInfo.accessToken };
      return opt;
    },
    /** @returns {any[]} */
    pointArray() {
      return this.form.longitude && this.form.latitude
        ? [this.form.longitude, this.form.latitude]
        : [];
    },
    /** @returns {string} */
    title() {
      return this.type === "add"
        ? "新增项目监管"
        : this.type === "edit"
        ? "编辑项目监管"
        : "查看项目监管";
    },
  },
  data() {
    return {
      pickerOptions: {
        disabledDate: (/** @type {Date}*/ date) => {
          return date.getFullYear() > new Date().getFullYear();
        },
      },
      // 项目流程
      projectPhaseList: [
        { value: null, label: "全部" },
        { value: 1, label: "前期阶段" },
        { value: 2, label: "施工阶段" },
        { value: 3, label: "完工阶段" },
      ],
      rules: Object.freeze(rules),
      // 所属河流
      riverData: [],
      allRiverList: [],
      // 新增表单数据
      form: {
        projectName: "",
        projectCode: "",
        adcd: "",
        year: "",
        riverId: "",
        riverName: "",
        unitName: "",
        legalRepresent: "",
        phone: "",
        involvedWaterName: "",
        occupyWaterTime: "",
        latitude: "",
        longitude: "",
        isNarrow: 0,
        isAffectSafety: 0,
        remark: "",
        occupyWaterArea: "",
        fileIds: "", //附件上传
        filleArea: "",
        projectStage: "",
      },
      dialogImageUrl: "",
      fileList: [],
      mapVisible: false,
      fileObjList: [],
    };
  },
  mounted() {
    this.$nextTick(async () => {
      if (this.dialogDialog) {
        this.form.id = this.dialogDialog.id;
        this.form.projectName = this.dialogDialog.projectName;
        this.form.projectCode = this.dialogDialog.projectCode;
        this.form.adcd = this.dialogDialog.adcd;
        this.form.year =
          this.dialogDialog.year == null
            ? ""
            : this.dialogDialog.year.toString();
        this.form.riverId = this.dialogDialog.riverId;
        this.form.riverName = this.dialogDialog.riverName;
        this.form.unitName = this.dialogDialog.unitName;
        this.form.legalRepresent = this.dialogDialog.legalRepresent;
        this.form.phone = this.dialogDialog.phone;
        this.form.involvedWaterName = this.dialogDialog.involvedWaterName;
        this.form.occupyWaterTime = this.dialogDialog.occupyWaterTime;
        this.form.latitude = this.dialogDialog.latitude;
        this.form.longitude = this.dialogDialog.longitude;
        this.form.isNarrow = this.dialogDialog.isNarrow;
        this.form.isAffectSafety = this.dialogDialog.isAffectSafety;
        this.form.remark = this.dialogDialog.remark;
        this.form.occupyWaterArea = this.dialogDialog.occupyWaterArea;
        if (this.dialogDialog.fileIds) {
          await getFileList({ ids: this.dialogDialog.fileIds }).then((res) => {
            if (res.code == 0) {
              this.fileObjList = [];
              this.fileList = res.data.data.map((item) => {
                this.fileObjList.push({
                  name: item.fileName,
                  fileUrl: item.fileUrl,
                  fileId: item.id,
                });
                return {
                  name: item.fileName,
                  url: item.fileUrl,
                };
              });
            }
          });
        }
        this.form.filleArea = this.dialogDialog.filleArea;
      }
      this.getRiver();
    });
  },
  methods: {
    riverFilter(query = "") {
      this.form.riverId = query;
      let arr = this.allRiverList.filter((item) => {
        return item.label.includes(query);
      });
      if (arr.length > 50) {
        this.riverData = arr.slice(0, 50);
      } else {
        this.riverData = arr;
      }
    },
    riverChange(val) {
      if (!val) {
        this.riverFilter();
      }
    },
    areaChange() {
      this.form.riverName = "";
      this.form.riverId = "";
      this.getRiver();
    },
    // 根据行政区划选择河流
    async getRiver() {
      this.allRiverList = [];
      let res = await projectSupervisionApi.getRiverListByAreaCode({
        // areaCode: this.form.adcd,
      });
      if (res.code == 0) {
        this.allRiverList = res.data.list.map((item) => ({
          label: item.name,
          value: item.id,
        }));
        if (this.dialogDialog && this.form.riverName) {
          this.riverFilter(this.form.riverName);
        } else {
          this.riverFilter();
        }
      }
    },
    // 新增接口
    async addProject() {
      this.$refs.templateForm.validate(async (valid) => {
        if (valid) {
          let arr = [];
          this.fileObjList.forEach((val) => {
            arr.push(val.fileId);
          });
          this.form.fileIds = arr.join(",");
          const river = this.allRiverList.filter(
            (item) => item.value == this.form.riverId
          );
          const area = this.areaOptions.filter(
            (item) => item.value == this.form.adcd
          );
          this.form.adnm = area.length > 0 ? area[0].label : "";
          this.form.riverName = river.length > 0 ? river[0].label : "";
          let res;
          if (this.form.id) {
            res = await projectSupervisionApi.updateProject(this.form);
          } else {
            res = await projectSupervisionApi.addProject(this.form);
          }
          if (res.code == 0) {
            this.$message({ message: res.message, type: "success" });
            this.$emit("close");
            this.$emit("getList");
          } else {
            this.$message({ message: res.message, type: "error" });
          }
        } else return false;
      });
    },
    // 阻止upload的自己上传，进行再操作
    handleChange(file) {
      this.addForm.image = URL.createObjectURL(file.raw);
    },
    beforeupload(file) {
      const isSize = file.size / 1024 / 1024 < 10;
      if (!isSize) {
        this.$message.error("上传文件大小不能超过 10MB!");
      }
      return isSize;
    },
    handleAvatarSuccess(res) {
      if (res.data.success) {
        this.$message({ message: "上传成功!", type: "success" });
      } else {
        this.$message({ message: "上传失败!", type: "error" });
      }
    },
    handleRemove(file, fileList) {
      this.dealFileArr(fileList);
    },
    handlePictureCardPreview(file) {
      if (file.response) {
        window.open(file.response.data.filePath);
      } else {
        window.open(file.url);
      }
    },
    // 地图选点
    openSelectMap() {
      this.mapVisible = true;
    },
    handleMapClose() {
      this.mapVisible = false;
    },
    saveData(data) {
      let mapData = data;
      if (data && data.length > 0) {
        this.form.latitude = mapData[1];
        this.form.longitude = mapData[0];
        this.mapVisible = false;
      } else {
        this.form.latitude = "";
        this.form.longitude = "";
        this.$message({ message: "请选择一个点", type: "info" });
      }
    },
    // 重置
    resetForm() {
      this.$refs.templateForm.resetFields();
    },
    // 关闭
    close() {
      this.resetForm();
      this.$emit("close");
    },
    uploadFile(file) {
      this.formDate.append("file", file.file);
    },
    dealFileArr(fileList) {
      if (fileList.length > 0) {
        this.fileObjList = fileList.map((item) => {
          return {
            name: item.name,
            fileUrl: item.response ? item.response.data.filePath : "",
            fileId: item.response ? item.response.data.id : "",
          };
        });
      } else {
        this.fileObjList = [];
      }
    },
    fileChange(file, fileList) {
      this.dealFileArr(fileList);
      // this.fileList.push(fileList);
    },
  },
};
</script>
<style lang="scss">
@import "@/style/listDialog.scss";

.scroll-dialog .el-form {
  // height: 600px !important;
  overflow-y: auto;
  padding-right: 15px;
}

.subhead-wrap {
  padding-left: 13px;
  margin: 20px 0 0 0;
  box-sizing: border-box;
}

.head .subhead {
  color: #000000 !important;
}

.primary-dialog.map-dialog .el-dialog__body {
  padding: 0 !important;
}

.dialog-content {
  height: 640px;
}

.icon-check-map {
  width: 23px;
  height: 27px;
  vertical-align: middle;
  cursor: pointer;
}
</style>
