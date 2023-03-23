<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px" :rules="rules">
      <el-row>
        <el-col :span="11">
          <el-form-item label="工程名称：" prop="pjnm">
            <el-input v-model="form.pjnm" placeholder="请输入" maxlength="50" style="width: 300px" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="值班日期：" prop="dutyDate" required>
            <el-date-picker type="date" placeholder="请选择" v-model="form.dutyDate" style="width: 300px;" value-format="yyyy-MM-dd"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="值班负责人：" prop="leaderName">
            <!-- <el-input v-model="form.leaderName" placeholder="请输入" maxlength="50" style="width: 300px"></el-input> -->
            <el-select v-model="form.leader" placeholder="请选择值班负责人：">
              <el-option v-for="item in options" :key="item.uid" :label="item.realName" :value="item.uid">
              </el-option>
            </el-select>
          </el-form-item>

        </el-col>
        <el-col :span="10">
          <el-form-item label="值班员：" prop="mgtDayName">
            <!-- <el-input v-model="form.mgtDayName" placeholder="请输入" maxlength="50" style="width: 300px"></el-input> -->
            <el-select v-model="form.mgtDay" placeholder="请选择值班员：">
              <el-option v-for="item in options" :key="item.uid" :label="item.realName" :value="item.uid">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="11">
          <el-form-item label="水工巡查人员：" prop="mgtNightName">
            <!-- <el-input v-model="form.mgtNightName" placeholder="请输入" maxlength="50" style="width: 300px"></el-input> -->
            <el-select v-model="form.mgtNightId" placeholder="请选择水工巡查人员：">
              <el-option v-for="item in patrolOptions" :key="item.uid" :label="item.realName" :value="item.uid">
              </el-option>
            </el-select>
          </el-form-item>

        </el-col>
        <el-col :span="10">
          <el-form-item label="设备巡查人员：" prop="devicePatrolName">
            <!-- <el-input v-model="form.devicePatrolName" placeholder="请输入" maxlength="50" style="width: 300px"></el-input> -->
            <el-select v-model="form.devicePatrolUid" placeholder="请选择设备巡查人员：">
              <el-option v-for="item in patrolOptions" :key="item.uid" :label="item.realName" :value="item.uid">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-row>
        <el-col :span="11">
          <el-form-item label="水工巡查手机：" prop="mgtNightPhone">
            <el-input type="number" v-model="form.mgtNightPhone" placeholder="请输入" maxlength="15" style="width: 300px"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="设备巡查手机：" prop="devicePatrolPhone">
            <el-input type="number" v-model="form.devicePatrolPhone" placeholder="请输入" maxlength="15" style="width: 300px"></el-input>
          </el-form-item>
        </el-col>
      </el-row> -->
      <el-form-item label="值班电话：" prop="dutyPhone">
        <el-input type="number" v-model="form.dutyPhone" placeholder="请输入" maxlength="15" style="width: 300px"></el-input>
      </el-form-item>

      <el-form-item label="巡查类型：" prop="patrolType" required>
        <el-radio-group v-model="form.patrolType">
          <el-radio label="日常巡查"></el-radio>
          <el-radio label="汛前检查"></el-radio>
          <el-radio label="年度检查"></el-radio>
          <el-radio label="特别检查"></el-radio>
        </el-radio-group>
      </el-form-item>

      <div class="submit-box">
        <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
        <el-button type="primary" @click="submitForm('form')">提交</el-button>
      </div>
    </el-form>

  </div>
</template>

<script>


import { beOnDuty } from "@/api/projectUnitDetail";

export default {
  name: "newDutyForm",
  props: {
    id: {
      type: String,
      default: '',
    }
  },
  components: {},
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo
    }
  },
  data() {
    return {
      form: {
        pjnm: '',
        devicePatrolName: '',
        dutyDate: '',
        dutyPhone: '',
        leaderName: '',
        mgtDayName: '',
        mgtNightName: '',
        patrolType: '',
        mgtNightPhone: '',//水工巡查人员电话
        devicePatrolPhone: '',//设备巡查人员电话
        leader: '',//值班负责人id
        mgtDay: '',//值班员id
        devicePatrolUid: '',//设备巡查人员id
        mgtNightId:''//水工id
      },
      submitFlag: true,
      rules: {},
      options: [],
      patrolOptions: [],
    }
  },
  mounted() {
    this.getUserList();
    this.getPatrolList();
    // this.form.pjnm = this.projectInfo.name
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime
          this.form['pid'] = this.projectInfo.prcd
          beOnDuty
            .saveOnDuty(this.form)
            .then((res) => {
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                // this.$message.error(res.msg);
                this.$message.error({ message: res.message || "添加失败" });
              }
              this.submitFlag = true;
            })
            .catch((err) => {
              this.submitFlag = true;
              this.$message.error(err);
            });
        } else {
          return false;
        }
      });
    },
    async getDetail() {
      let res = await beOnDuty.getOnDutyList({
        id: this.id,
        pid: this.projectInfo.prcd
      });
      if (res.code == 0) {
        Object.assign(this.form, res.data.list[0])
        this.form.pjnm = this.projectInfo.name
        this.form.leader = this.form.leader - 0
        this.form.mgtDay = this.form.mgtDay - 0
        this.form.devicePatrolUid = this.form.devicePatrolUid - 0
        this.form.mgtNightId = this.form.mgtNightId - 0
      }
    },
    async getUserList() {
      let res = await beOnDuty.getBhUser();
      if (res.code == 0) {
        this.options = res.data
      }
    },
    async getPatrolList() {
      let res = await beOnDuty.getBhUser({roleId:14});
      if (res.code == 0) {
        this.patrolOptions = res.data
      }
    },

  }
}
</script>

<style scoped lang="scss">
.wrap-form-box {
  .training-form-box {
    display: flex;
    width: 100%;
    justify-content: space-between;
    > div {
      width: 48%;
    }
  }
  .submit-box {
    text-align: right;
  }
}
</style>