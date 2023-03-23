<template>
  <div class="new_task">
    <van-cell-group>
      <van-field
        v-model="supervisionProjectName"
        is-link
        readonly
        label="工程名称"
        placeholder="请选择工程"
        @click="showPickerPopup(3)"
      />
      <van-field
        v-model="ruleForm.inspectionTime"
        is-link
        readonly
        label="检查时间"
        placeholder="请选择检查时间"
        @click="showTimePicker = true"
      />
      <van-field
        v-model="runUnitName"
        is-link
        readonly
        label="责任单位"
        placeholder="请选择责任单位"
        @click="showPickerPopup(1)"
      />
      <van-field
        v-model="runUnitReviewerName"
        is-link
        readonly
        label="责任人"
        placeholder="请选择责任人"
        @click="showPickerPopup(2)"
      />
      <van-field
        v-model="supervisionStatusName"
        is-link
        readonly
        label="督办状态"
        placeholder="请选择责督办状态"
        @click="showPickerPopup(4)"
      />
      <van-field
        v-model="ruleForm.problems"
        label="问题描述"
        placeholder="请输入问题描述"
        type="textarea"
        rows="2"
        autosize
      />
      <van-field readonly label="隐患图片" placeholder="请上传图片">
        <template #input>
          <div class="upload-preview-box">
            <div v-for="item in fileList" :key="item.id">
              <van-image
                width="70"
                height="70"
                @click="imagePreview(item.filePath)"
                :src="item.filePath"
              />
              <van-icon @click.stop="clearImg(item.id)" class="clear-btn-icon" name="clear" />
            </div>
            <van-uploader
              class="upload-box"
              name="problemsPicture"
              :before-read="beforeRead"
              :after-read="afterRead"
            >
            </van-uploader>
          </div>
        </template>
      </van-field>
      <van-button type="default" :plain="true" @click="goBack">取消</van-button>
      <van-button color="#036EFF" block :loading="loading" @click="submitForm"
        >提交</van-button
      >
    </van-cell-group>
    <van-popup v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showPicker = false"
        @confirm="onConfirm"
      >
        <template #option="option">
          <div>{{ option.name || option.username }}</div>
        </template>
      </van-picker>
    </van-popup>
    <van-popup v-model="showTimePicker" position="bottom">
      <van-datetime-picker
        v-model="currentDate"
        type="datetime"
        :columns-order="['year', 'month', 'day', 'hour', 'minute']"
        :formatter="formatter"
        @confirm="onConfirmTime"
        @cancel="showTimePicker = false"
      />
    </van-popup>
  </div>
</template>

<script>
import {
  getAllProjectApi,
  propertyPeopleApi,
  fileUploadApi,
  supervisedCheck,
} from "@/api/rainfall.js";
import { localData } from "@/plugins/storage";
import VantPicker from "@/components/vantPicker";
export default {
  name: "AddNewTask",
  props: {},
  components: {
    VantPicker,
  },
  computed: {
    userInfo() {
      return localData("get", "userInfo");
    },
  },
  data() {
    return {
      ruleForm: {
        inspectionTime: "",
        problems: "",
        problemsPicture: "",
        supervisionStatus: "",
        responsibleUnit: "",
        responsiblePeople: "",
        supervisionProject: "",
      },
      runUnitName: "",
      runUnitReviewerName: "",
      supervisionStatusName: "",
      supervisionProjectName: "",
      problemsPictureName: "",
      showTimePicker: false,
      timePickerType: "",
      showPicker: false,
      columns: [],
      operationUnit: [],
      operationPeopleList: [],
      supervise: [
        {
          name: "已督促",
          value: 2,
        },
        {
          name: "未督促",
          value: 1,
        },
      ],
      pickerType: 0,
      loading: false,
      currentDate: new Date(),
      projectList: [],
      fileList: [],
    };
  },
  mounted() {
    this.addZwlog(this)
    this.deptList();
    this.getAllProject();
  },
  methods: {
    async getAllProject() {
      let res = await getAllProjectApi();
      if (res.code == 0) {
        this.projectList = res.data.map((item) => {
          return {
            name: item.project_name,
            ...item,
          };
        });
      }
    },
    async deptList() {
      let res = await propertyPeopleApi.unitList({
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        this.operationUnit = res.data.list.map((item) => {
          return {
            name: item.companyName,
            ...item,
          };
        });
        if (res.data.list.length > 0) {
          this.propertyUnitPeople(this.operationUnit[0].id);
        }
      }
    },
    async propertyUnitPeople(deptId, type) {
      let res = await propertyPeopleApi.list({
        deptId,
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        this.operationPeopleList = res.data.list;
      }
    },
    showPickerPopup(num) {
      this.pickerType = num;
      switch (num) {
        case 1:
          this.columns = this.operationUnit;
          break;
        case 2:
          this.columns = this.operationPeopleList;
          break;
        case 3:
          this.columns = this.projectList;
          break;
        default:
          this.columns = this.supervise;
          break;
      }
      this.$nextTick(() => {
        this.showPicker = true;
      });
    },
    onConfirm(data, index) {
      console.log(data, index);
      switch (this.pickerType) {
        case 1:
          if (this.ruleForm.responsibleUnit != data.id) {
            this.ruleForm.responsiblePeople = "";
            this.runUnitReviewerName = "";
            this.ruleForm.responsibleUnit = data.id;
            this.runUnitName = data.name;
            this.propertyUnitPeople(this.ruleForm.runUnitId, 2);
          }
          break;
        case 2:
          this.ruleForm.responsiblePeople = data.id;
          this.runUnitReviewerName = data.username;
          break;
        case 3:
          this.ruleForm.supervisionProject = data.project_code;
          this.supervisionProjectName = data.name;
          break;
        default:
          this.ruleForm.supervisionStatus = data.value;
          this.supervisionStatusName = data.name;
          break;
      }
      this.showPicker = false;
    },
    beforeRead(file, detail) {
      return true;
    },
    afterRead(file, detail) {
      console.log(file, detail);
      let formData = new FormData();
      formData.append("uploadFile", file.file);
      fileUploadApi(formData)
        .then((res) => {
          if (res.code == 0) {
            this.fileList.push(res.data)
          } else {
            this.$toast.fail(res.msg);
          }
        })
        .catch((err) => {
          this.$toast(err);
        });
    },
    onConfirmTime(value) {
      console.log(value);
      let mouth =
        value.getMonth() + 1 >= 10
          ? value.getMonth() + 1
          : `0${value.getMonth() + 1}`;
      let day = value.getDate() >= 10 ? value.getDate() : `0${value.getDate()}`;
      let hour =
        value.getHours() >= 10 ? value.getHours() : `0${value.getHours()}`;
      let minute =
        value.getMinutes() >= 10
          ? value.getMinutes()
          : `0${value.getMinutes()}`;
      this.ruleForm.inspectionTime = `${value.getFullYear()}-${mouth}-${day} ${hour}:${minute}:00`;
      this.showTimePicker = false;
    },
    formatter(type, val) {
      if (type === "year") {
        return val + "年";
      }
      if (type === "month") {
        return val + "月";
      }
      if (type === "day") {
        return val + "日";
      }
      if (type === "hour") {
        return val + "时";
      }
      if (type === "minute") {
        return val + "分";
      }
      return val;
    },
    submitForm() {
      this.loading = true;
      if (this.fileList.length > 0) {
        let arr = []
        this.fileList.forEach(item => {
          arr.push(item.id)
        })
        this.ruleForm.problemsPicture = arr.join(',')
      }
      supervisedCheck
        .save(this.ruleForm)
        .then((res) => {
          if (res.code == 0) {
            let _this = this;
            this.$toast.success({
              message: "提交成功~",
              duration: 1000,
              onClose: () => {
                _this.loading = false;
                _this.goBack();
              },
            });
          } else {
            this.loading = false;
            this.$toast.fail(res.message);
          }
        })
        .catch((err) => {
          this.loading = false;
          this.$toast.fail(err);
        });
    },
    goBack() {
      this.$router.replace("/propertySupervision/inspection");
    },
    imagePreview(src) {
      this.$imagePreview([src])
    },
    clearImg(id) {
      let index = this.fileList.findIndex(item => item.id == id)
      this.fileList.splice(index, 1)
    }
  },
};
</script>

<style scoped lang="scss">
.new_task {
  .van-button {
    width: 350px;
    height: 45px;
    margin-left: 12px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    letter-spacing: 2px;
    margin-top: 25px;
  }
}
.default-box {
  display: flex;
  align-items: center;
  height: 100%;
  .img-box {
    width: 20px;
    margin-right: 5px;
  }
  span {
    max-width: calc(100% - 26px);
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}
.upload-preview-box {
  display: flex;
  width: 100%;
  flex-flow: wrap;
  > div {
    margin: 0 9px 9px 0;
    position: relative;
    .clear-btn-icon{
      position: absolute;
      top: -6px;
      right: -6px;
      font-size: 18px;
    }
  }
}
</style>

<style lang="scss">
.upload-box {
  .van-uploader__wrapper {
    width: 70px;
    height: 70px;
    .van-uploader__preview,
    .van-uploader__upload {
      width: 70px;
      height: 70px;
    }
    .van-uploader__upload {
      margin: 0;
    }
    .van-uploader__preview-image {
      width: 100%;
      height: 100%;
    }
  }
}
.upload-preview-box .van-image {
  margin: 0;
}
</style>