<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-19 11:25:59
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\supervise\components\AKeySupervisory.vue
-->
<template>
  <div class="supervisory-warp">
    <div class="supervisory-top">
      <div class="select-box">
        <div class="label">督查状态：</div>
        <div class="item">
          <el-select size="small" class="select" @change="getList" clearable v-model="value" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
          </el-select>
        </div>
      </div>
      <div class="add-btn" @click="visibleDialog = true">新增</div>
    </div>
    <div class="block">
      <el-timeline v-if="timesData.length > 0">
        <el-timeline-item
          class="timeline-item"
          :timestamp="item.createTime"
          placement="top"
          v-for="item in timesData"
          :key="item.id"
        >
          <el-card class="block-card">
            <div class="seawall-title">
              <span class="img"></span>
              <span>督查信息</span>
            </div>
            <div class="block-box">
              <div class="block-list">
                <div class="label">督查时间：</div>
                <div class="item">{{ item.createTime }}</div>
              </div>
              <div class="block-list">
                <div class="label">截止时间：</div>
                <div class="item">{{ item.deadline }}</div>
              </div>
              <div class="block-list">
                <div class="label">问题描述：</div>
                <div class="item">{{ item.problemDescribe || '-' }}</div>
              </div>
              <div class="block-list">
                <div class="label">问题附件：</div>
                <div class="item">
                  <div class="file-list" v-for="ite in item.problemFileList" :key="ite.id">
                    <div class="file-icon"></div>
                    <div class="file-name">{{ite.fileName}}</div>
                    <div class="file-look" @click="look(ite.fileUrl)">查看</div>
                  </div>
                </div>
              </div>
              <div class="block-list">
                <div class="label">督查人员：</div>
                <div class="item">{{ item.createName || '-' }}</div>
              </div>
              <div class="block-list" v-if="item.status == 2" style="color: #f00">
                整改已逾期，请监督相关整改人员立即处置
              </div>
            </div>
            <div
              class="dispose-icon"
              :class="{
                'dispose-icon-02': item.status == 0,
                'dispose-icon-01': item.status == 1,
                'dispose-icon-03': item.status == -1
              }"
            ></div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <div class="block-no" v-else>暂无数据</div>
    </div>
    <PrimaryDialog
      :visible="visibleDialog"
      :appendToBody="true"
      @analysis="this.$emit('analysis')"
      main-title="新增一键督查"
      top="10vh"
      width="500px"
      @handleClose="handleClose"
    >
      <el-form ref="form" class="form-box-super" :model="form" label-width="100px">
        <el-form-item label="海塘名称：">
          <el-input size="small" disabled v-model.trim="form.projectName"></el-input>
        </el-form-item>
        <el-form-item label="整改人(内)：">
          <el-select
            size="small"
            class="select-box"
            v-model="insideList"
            style="width: 100%"
            filterable
            multiple
            placeholder="请选择"
            @change="handleChange"
          >
            <el-option v-for="item in optionsList" :key="item.id" :label="item.username" :value="item.id"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="整改人(外)：">
          <div v-if="form.outsideList.length > 0">
            <div class="add-people" v-for="(item, index) in form.outsideList" :key="index">
              <div class="people-item">
                <div class="label">姓名</div>
                <el-input class="item-name" size="small" v-model.trim="item.workerName"></el-input>
              </div>
              <div class="people-item">
                <div class="label">手机号码</div>
                <el-input
                  class="item-tel"
                  maxlength="11"
                  onInput="value=value.replace(/[^0-9]/g,'');"
                  size="small"
                  v-model.trim="item.workerPhone"
                ></el-input>
              </div>
              <div class="close-people-btn" @click="clearPeople(index)">删除</div>
            </div>
          </div>
          <span class="add-people-btn" @click="addPeople">+ 添加人员</span>
        </el-form-item>
        <el-form-item label="截止时间：">
          <!-- <el-input size="small" v-model.trim="form.projectName"></el-input> -->
          <el-date-picker
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            style="width: 100%"
            v-model="form.deadline"
            type="datetime"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="问题描述：">
          <el-input size="small" type="textarea" v-model.trim="form.problemDescribe"></el-input>
        </el-form-item>
        <el-form-item label="附件上传：">
          <unpload-file
            @success="handleSuccess"
            @error="handleError"
            @remove="handleRemove"
            accept=".pdf,.png,.jpg,.jpeg"
            :size="15"
            :fileList="fileList"
          ></unpload-file>
        </el-form-item>
        <el-form-item>
          <div class="btn-box">
            <div class="close-btn" @click="handleClose">取消</div>
            <div class="add-btn" @click="submitForm">确定</div>
          </div>
        </el-form-item>
      </el-form>
    </PrimaryDialog>
    <lookFile @close="lookFileType = false" :appendType="true" :visible="lookFileType" :fileSrc="fileSrc" type="png" />
  </div>
</template>

<script>
import lookFile from '@/components/look_pdf/lookFile.vue';
import { PrimaryDialog } from '@/components';
import unploadFile from '@/components/uploadFile.vue';
import { superviseList, saveOrUpdate, usersList } from '@/api/seawall.js';
export default {
  props: {
    code: {
      type: String,
      default: ''
    },
    detail: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      optionsList: [
        {
          value: '选项1',
          label: '黄金糕'
        },
        {
          value: '选项2',
          label: '双皮奶'
        },
        {
          value: '选项3',
          label: '蚵仔煎'
        },
        {
          value: '选项4',
          label: '龙须面'
        },
        {
          value: '选项5',
          label: '北京烤鸭'
        }
      ],
      timesData: [],
      visibleDialog: false,
      options: [
        {
          value: '0',
          label: '未处置'
        },
        {
          value: '1',
          label: '已处置'
        },
        {
          value: '-1',
          label: '逾期未处置'
        }
      ],
      value: '',
      insideList: [],
      submitType: false,
      form: {
        projectName: '',
        insideList: [],
        outsideList: [],
        problemDescribe: '',
        problemFile: '',
        deadline: '',
        projectCode: ''
      },
      fileList: [],
      lookFileType: false,
      fileSrc: ''
    };
  },
  components: {
    PrimaryDialog,
    unploadFile,
    lookFile
  },
  mounted() {
    this.getList();
    this.getUsersList();
    this.form.projectName = this.detail.seawallName;
    this.form.projectCode = this.detail.seawallCode;
  },
  methods: {
    look(url) {
      this.fileSrc = url;
      this.lookFileType = true;
    },
    async getList() {
      let res = await superviseList({ projectCode: this.code, status: this.value });
      if (res.code == 0) {
        this.timesData = res.data.list;
      }
    },
    handleSuccess(res) {
      if (res.file.code == 0) {
        let file = res.file.data;
        this.form.problemFile = file.id;
      }
    },
    handleError(res) {},
    handleRemove() {
      this.form.problemFile = '';
    },
    async getUsersList() {
      let res = await usersList();
      if (res.code == 0) {
        this.optionsList = res.data;
      }
    },
    addPeople() {
      let obj = {
        workerName: '',
        type: 2,
        workerPhone: ''
      };
      this.form.outsideList.push(obj);
    },
    clearPeople(index) {
      this.form.outsideList.splice(index, 1);
    },
    handleChange(val) {
      if (val.length > 0) {
        this.form.insideList = val.map((item) => {
          let data = this.optionsList.find((element) => element.id == item);
          console.log(data, '12132132');
          return {
            type: 1,
            workerId: item,
            workerName: data.username,
            workerPhone: data.phone
          };
        });
      } else {
        this.form.insideList = [];
      }
    },
    handleClose() {
      this.insideList = [];
      this.form = {
        deadline: '',
        insideList: [],
        outsideList: [],
        problemDescribe: '',
        problemFile: ''
      };
      this.fileList = [];
      this.submitType = false;
      this.visibleDialog = false;
    },
    submitForm() {
      if (this.form.insideList.length == 0 &&  this.form.outsideList.length == 0) {
        this.$message.error('请选择整改人或者填写整改人');
        return false;
      }
      if (!this.form.deadline) {
        this.$message.error('请x选择截止时间');
        return false;
      }
      if (!this.form.problemDescribe) {
        this.$message.error('请填写问题描述');
        return false;
      }
      if (!this.form.projectName) {
        this.$message.error('请上传问题附件');
        return false;
      }
      if (this.submitType) {
        return false;
      }
      this.submitType = true;
      saveOrUpdate(this.form)
        .then((res) => {
          if (res.code == 0) {
            this.$message.success('新增成功');
            this.submitType = false;
            this.handleClose();
            this.getList();
          } else {
            this.$message.error(res.msg);
            this.submitType = false;
          }
        })
        .catch((err) => {
          this.$message.error(err.msg);
          this.submitType = false;
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.seawall-title {
  display: flex;
  align-items: center;
  padding-bottom: 14px;
  position: relative;
  color: #ffffff;
  font-size: 18px;
  .img {
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
    background-size: 100% 100%;
    width: 20px;
    height: 20px;
    margin-right: 6px;
  }
  &::after {
    content: '';
    position: absolute;
    width: 100%;
    height: 4px;
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview.png) no-repeat;
    background-size: 100% 100%;
    bottom: 0;
  }
}
.add-btn {
  width: 64px;
  height: 35px;
  line-height: 35px;
  text-align: center;
  color: #fff;
  background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
  border-radius: 4px;
  cursor: pointer;
}
.close-btn {
  width: 64px;
  height: 35px;
  line-height: 35px;
  text-align: center;
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid #00baff;
  margin-right: 20px;
  color: #00baff;
}
.btn-box {
  display: flex;
  justify-content: flex-end;
}
.supervisory-warp {
  .supervisory-top {
    display: flex;
    justify-content: space-between;
    .select-box {
      display: flex;
      align-items: center;
      .label {
        color: #fff;
        font-size: 14px;
      }
      .item {
        .select {
          border: 1px solid #ffffff;
          border-radius: 4px;
        }
      }
    }
  }
  .block {
    margin-top: 20px;
    height: 600px;
    overflow: auto;
    .block-card {
      position: relative;
      .block-list {
        display: flex;
        font-size: 14px;
        padding-top: 16px;
        align-items: center;
        .label {
          width: 75px;
          color: #ffffff;
        }
        .item {
          width: calc(100% - 75px);
          color: #ffffff;
        }
      }
      .dispose-icon {
        position: absolute;
        right: 50px;
        top: 12px;
        width: 106px;
        height: 93px;
        &.dispose-icon-01 {
          background: url(~assets/images/ScreenAsset/dispose-icon-01.png) no-repeat;
          background-size: 100% 100%;
        }
        &.dispose-icon-02 {
          background: url(~assets/images/ScreenAsset/dispose-icon-02.png) no-repeat;
          background-size: 100% 100%;
        }
        &.dispose-icon-03 {
          background: url(~assets/images/ScreenAsset/dispose-icon-03.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }
    .block-no {
      text-align: center;
      padding-top: 100px;
    }
    .file-list {
      display: flex;
      align-items: center;
      width: 360px;
      height: 30px;
      background: linear-gradient(270deg, rgba(0, 99, 125, 0) 0%, #003447 100%);
      box-sizing: border-box;
      padding: 0 14px;
      .file-icon {
        width: 13px;
        height: 15px;
        background: url(~assets/images/file-icon.png) no-repeat;
        background-size: 100% 100%;
        margin-right: 10px;
      }
      .file-name {
        width: 80%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .file-look {
        color: #23e3fb;
        cursor: pointer;
      }
    }
  }
}
.form-box-super {
  .add-people {
    display: flex;
    .people-item {
      // display: flex;
      margin-right: 15px;
      .label {
        color: #fff;
        height: 20px;
        line-height: 20px;
      }
      .item-name {
        width: 100px;
      }
      .item-tel {
        width: 200px;
      }
    }
    .close-people-btn {
      color: #f00;
      cursor: pointer;
      position: relative;
      bottom: -20px;
    }
  }
  .add-people-btn {
    color: #00baff;
    cursor: pointer;
  }
}
</style>

<style lang="scss">
.block {
  .timeline-item {
    .el-timeline-item__timestamp {
      color: #fff;
    }
    .block-card {
      background: linear-gradient(270deg, rgba(0, 123, 209, 0) 0%, rgba(0, 123, 209, 0.2) 100%);
      border-radius: 6px;
      border: none;
      .el-card__body {
        padding: 16px 20px;
      }
    }
  }
}
.form-box-super {
  padding-top: 20px;
  .el-form-item {
    .el-form-item__label {
      color: #fff;
    }
    .el-form-item__content {
      .el-input {
        .el-input__inner {
          border-radius: 4px;
          border: 1px solid #ffffff;
        }
      }
      .el-textarea {
        .el-textarea__inner {
          border-radius: 4px;
          border: 1px solid #ffffff;
          background-color: transparent;
          color: #fff;
        }
      }
      .select-box {
        .el-tag {
          color: #fff;
          background-color: transparent;
        }
      }
    }
  }
}
</style>