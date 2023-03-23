<template>
  <div class="base_form">
    <el-dialog :visible.sync="visible" width="1000px" :before-close="dialogClose" top="15vh" :title="title" class="list-dialog" :append-to-body="true" :close-on-click-modal="false">
      <el-form ref="tableForm" :model="tableForm" :rules="rules" size="mini" label-width="130px" label-position="right">
        <!-- <el-row type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="测站名称 ：" prop="stName">
              <el-input v-model="tableForm.stName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测站类型：" prop="stType">
              <el-input disabled v-model="tableForm.stType"></el-input>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row type="flex" justify="space-between">
          <el-col :span="12">
            <el-form-item label="预报发布时刻：">
              <el-date-picker v-model="releaseTime" type="datetime" placeholder="请选择预报发布时刻" value-format="yyyy-MM-dd HH:mm:ss" format="yyyy-MM-dd HH:mm:ss" clearable>
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <el-button type="primary" @click="addNewOne">增加一条</el-button>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="发布类型：" prop="releaseType">
              <el-select
                size="mini"
                v-model="releaseType"
                placeholder="请选择"
                @change="getTideCharts"
              >
                <el-option
                  v-for="item in releaseTypeOption"
                  :label="item.name"
                  :value="item.value"
                  :key="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
        </el-row>
        <!-- <el-row type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="蓝色警戒水位：" prop="blueLevel">
              <el-input v-model="tableForm.blueLevel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="黄色警戒水位：" prop="yellowLevel">
              <el-input v-model="tableForm.yellowLevel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="橙色警戒水位：" prop="orangeLevel">
              <el-input v-model="tableForm.orangeLevel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="红色警戒水位：" prop="redLevel">
              <el-input v-model="tableForm.redLevel"></el-input>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row v-for="(item, index) in manualValueList" :key="index" type="flex" justify="left">
          <el-col :span="9">
            <el-form-item label="潮位：">
              <el-input placeholder="请填写潮位（数字）：" type="number" v-model="item.tdz"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="时刻：">
              <el-date-picker v-model="item.tm" type="datetime" placeholder="请选择时刻" value-format="yyyy-MM-dd HH:mm:ss" format="yyyy-MM-dd HH:mm:ss" clearable>
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button @click="deleteTide(index)" size="mini" type="danger" plain style="margin-left:20px">删除</el-button>
          </el-col>
        </el-row>

        <!-- <el-row type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="附件：" prop="warningtime">
              <el-input disabled v-model="tableForm.warningtime"></el-input>
            </el-form-item>
          </el-col>
        </el-row> -->
      </el-form>
      <div slot="footer">
        <el-row>
          <div class="form-footer">
            <!-- 增加 -->
            <el-button v-if="isNew" class="form-btn btn-cancel" size="mini" @click="dialogClose">取消</el-button>
            <el-button v-if="isNew" class="form-btn" size="mini" @click="saveData">确定</el-button>
          </div>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import qs from "qs";
import { stormSurgeWarningApi } from "@/api/seawallPrevent";
export default {
  name: "baseForm",
  props: {
    visible: Boolean,
    info: Object,
    isLook: Boolean,
    isNew: Boolean,
  },
  watch: {},
  components: {},
  data() {
    return {
      rules: {
        releaseTime: [
          {
            required: true,
            message: "请选择预报发布时刻",
          },
        ],
      },
      title: "详情",
      tableForm: {
        releaseTime: "",
      },
      releaseTime: "",
      manualValueList: [],
    };
  },
  mounted() {
    this.title = "新增";
    this.addNewOne();
  },
  methods: {
    addNewOne() {
      let temp = { tdz: "", tm: "" };
      this.manualValueList.push(temp);
    },
    deleteTide(index) {
      this.manualValueList.splice(index, 1)
    },
    async saveData() {
      let resVo = {
        releaseTime: this.releaseTime,
        manualValueList: this.manualValueList,
      };

      await stormSurgeWarningApi.addGuoGongShan(resVo).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "新增成功",
            type: "success",
          });
          this.dialogClose();
          this.$emit("getList");
        } else {
          this.$message.error(res.msg || res.message);
        }
      });
    },
    // 关闭
    dialogClose() {
      this.$emit("close");
    },
  },
};
</script>

<style lang="scss" scoped>
.list-dialog {
  ::v-deep .el-dialog__header {
    background: #1492ff;
    padding: 15px 20px;
    .el-dialog__title {
      color: #fff;
    }
    .el-dialog__headerbtn {
      top: 5px;
      right: 5px;
      padding-top: 10px;
      .el-icon-close {
        color: #fff;
        height: 30px;
        width: 35px;
      }
    }
  }
  ::v-deep .el-dialog__body {
    padding: 15px 20px;
    .el-input__inner {
      background-color: #fff;
      border: 0.0625rem solid #dcdfe6;
      color: #606266;
    }
  }
  ::v-deep .el-dialog__footer {
    border-top: 2px solid #dcdfe6;
    padding: 15px;
  }
  .el-select {
    width: 100%;
  }
  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 100%;
  }
}
</style>
