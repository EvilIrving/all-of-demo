<template>
  <div class="new_task">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="'新增验收'" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <van-cell-group>
      <van-field
        v-model="ruleForm.contractName"
        label="合同名称"
        placeholder="请输入合同名称"
      />
      <van-field
        v-model="ruleForm.propertyUnitName"
        readonly
        label="物业管理单位"
        placeholder="请选择物业管理单位"
      />
      <van-field
        v-model="userInfo.username"
        readonly
        label="审核人"
        placeholder="请选择审核人"
      />
      <van-field
        v-model="runUnitName"
        is-link
        readonly
        label="运行管理单位"
        placeholder="请选择运行管理单位"
        @click="showPickerPopup(1)"
      />
      <van-field
        v-model="runUnitReviewerName"
        is-link
        readonly
        label="审核人"
        placeholder="请选择审核人"
        @click="showPickerPopup(2)"
      />
      <van-field
        v-model="waterAdministrationUnitName"
        is-link
        readonly
        label="水行政主管部门"
        placeholder="请选择水行政主管部门"
        @click="showPickerPopup(3)"
      />
      <van-field
        v-model="waterAdministrationUnitReviewerName"
        is-link
        readonly
        label="审核人"
        placeholder="请选择审核人"
        @click="showPickerPopup(4)"
      />
      <van-field
        v-model="ruleForm.serviceStartTime"
        is-link
        readonly
        label="服务期限开始"
        placeholder="请选择服务期限"
        @click="showPickerTimes('serviceStartTime')"
      />
      <van-field
        v-model="ruleForm.serviceEndTime"
        is-link
        readonly
        label="服务期限结束"
        placeholder="请选择服务期限"
        @click="showPickerTimes('serviceEndTime')"
      />
      <van-field is-link readonly label="合同附件" placeholder="请上传合同附件">
        <template #input>
          <van-uploader
            name="contractFile"
            style="width: 100%"
            accept=".pdf"
            max-count="1"
            :before-read="beforeRead"
            :after-read="afterRead"
          >
            <div v-if="ruleForm.contractFile" class="text-space-overflow">{{ contractFileName }}</div>
            <div v-else class="gray-color">请上传PDF附件</div>
          </van-uploader>
        </template>
      </van-field>
      <van-field is-link readonly label="验收附件" placeholder="请上传验收附件">
        <template #input>
          <van-uploader
            name="acceptanceFile"
            style="width: 100%"
            accept=".pdf"
            max-count="1"
            :before-read="beforeRead"
            :after-read="afterRead"
          >
            <div v-if="ruleForm.acceptanceFile" class="text-space-overflow">{{ acceptanceFileName }}</div>
            <div v-else class="gray-color">请上传PDF附件</div>
          </van-uploader>
        </template>
      </van-field>
      <van-field
        v-model="ruleForm.problem"
        label="问题描述"
        placeholder="请输入问题描述"
        type="textarea"
        rows="2"
        autosize
      />
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
        type="date"
        @confirm="onConfirmTime"
        @cancel="showTimePicker = false"
      />
    </van-popup>
  </div>
</template>

<script>
import {
  supervisedCheck,
  contractApi,
  propertyPeopleApi,
  fileUploadApi,
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
        propertyUnitId: "",
        propertyUnitReviewerId: "",
        propertyUnitName: "",
        runUnitId: "67074514",
        runUnitReviewerId: "",
        waterAdministrationUnitId: "",
        waterAdministrationUnitReviewerId: "",
        problem: "",
        serviceStartTime: "",
        serviceEndTime: "",
        contractName: "",
        acceptanceFile: "",
        contractFile: "",
      },
      runUnitName: "",
      runUnitReviewerName: "",
      waterAdministrationUnitName: "",
      waterAdministrationUnitReviewerName: "",
      contractFileName: "",
      acceptanceFileName: "",
      showTimePicker: false,
      timePickerType: "",
      showPicker: false,
      columns: [],
      operationUnit: [],
      administrativeUnit: [],
      operationPeopleList: [],
      administrativePeopleList: [],
      pickerType: 0,
      loading: false,
      currentDate: new Date(),
    };
  },
  mounted() {
    this.addZwlog(this)
    this.ruleForm.propertyUnitReviewerId = this.userInfo.id;
    this.ruleForm.propertyUnitId = this.userInfo.deptId;
    this.ruleForm.propertyUnitName = this.userInfo.department;
    this.deptList("67011533");
  },
  methods: {
    async deptList(deptIds) {
      let res = await supervisedCheck.deptList({
        deptIds,
      });
      if (res.code == 0) {
        this.operationUnit = res.data;
        this.administrativeUnit = res.data.filter(
          (item) => item.id == "67004483"
        );
        this.runUnitName = res.data.find((item) => item.id == "67074514").name;
        this.propertyUnitPeople(this.ruleForm.runUnitId, 2);
      }
    },
    async propertyUnitPeople(deptId, type) {
      let res = await propertyPeopleApi.list({
        deptId,
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        if (type == 2) {
          this.operationPeopleList = res.data.list;
        } else {
          this.administrativePeopleList = res.data.list;
        }
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
          this.columns = this.administrativeUnit;
          break;
        default:
          this.columns = this.administrativePeopleList;
          break;
      }
      this.$nextTick(() => {
        this.showPicker = true;
      });
    },
    onConfirm(data, index) {
      switch (this.pickerType) {
        case 1:
          if (this.ruleForm.runUnitId != data.id) {
            this.ruleForm.runUnitReviewerId = "";
            this.runUnitReviewerName = "";
            this.ruleForm.runUnitId = data.id;
            this.runUnitName = data.name;
            this.propertyUnitPeople(this.ruleForm.runUnitId, 2);
          }
          break;
        case 2:
          this.ruleForm.runUnitReviewerId = data.id;
          this.runUnitReviewerName = data.username;
          break;
        case 3:
          if (this.ruleForm.waterAdministrationUnitId != data.id) {
            this.ruleForm.waterAdministrationUnitReviewerId = "";
            this.waterAdministrationUnitReviewerName = "";
            this.ruleForm.waterAdministrationUnitId = data.id;
            this.waterAdministrationUnitName = data.name;
            console.log(data.id, "dasdasdas");
            this.propertyUnitPeople(this.ruleForm.waterAdministrationUnitId, 3);
          }
          break;
        default:
          this.ruleForm.waterAdministrationUnitReviewerId = data.id;
          this.waterAdministrationUnitReviewerName = data.username;
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
          console.log(res);
          if (res.code == 0) {
            this.ruleForm[detail.name] = res.data.id;
            this[`${detail.name}Name`] = res.data.originalName;
          } else {
            this.$toast(res.msg);
          }
        })
        .catch((err) => {
          this.$toast(err);
        });
    },
    showPickerTimes(type) {
      this.timePickerType = type;
      this.showTimePicker = true;
    },
    onConfirmTime(value) {
      let mouth =
        value.getMonth() + 1 >= 10
          ? value.getMonth() + 1
          : `0${value.getMonth() + 1}`;
      let day = value.getDate() >= 10 ? value.getDate() : `0${value.getDate()}`;
      this.ruleForm[
        this.timePickerType
      ] = `${value.getFullYear()}-${mouth}-${day}`;
      this.showTimePicker = false;
    },
    submitForm() {
      if (this.ruleForm.serviceStartTime && this.ruleForm.serviceEndTime) {
        let startTimes = Date.parse(
          this.ruleForm.serviceStartTime.replace(/-/g, "/")
        );
        let endTimes = Date.parse(
          this.ruleForm.serviceEndTime.replace(/-/g, "/")
        );
        if (endTimes <= startTimes) {
          return this.$toast("请选择正确的服务时间");
        }
      }
      this.loading = true;
      contractApi
        .save(this.ruleForm)
        .then((res) => {
          if (res.code == 0) {
            let _this = this
            this.$toast.success({
              message: "提交成功~",
              duration: 1000,
              onClose: () => {
                _this.loading = false;
                _this.goBack()
              }
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
      this.$router.replace("/acceptance?type=1");
    },
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
</style>

<style lang="scss">
.new_task .van-uploader__input-wrapper {
  width: 100%;
}
</style>