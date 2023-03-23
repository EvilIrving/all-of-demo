<script>
import PersonnelListDialog from "../personnelListDailog";
import { exportDetail } from "@/utils/utils";
import config from "@/config/config";
import { projectSupervisionApi } from "@/api/projectSupervision";
const MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
export default {
  props: {
    /** @type {import('vue/types/options').PropValidator<{id:string}>} */
    inspection: Object,
  },
  components: { PersonnelListDialog },
  data() {
    return {
      personnelDialogShow: false,
      /** @type {{userName:string}} */
      inspectorsData: {},
      /** @type {{entryName:string}[]} */
      issueList: [],
      form: {
        /** 右侧新增复核问题用 */
        entryName: "",
        /** 左侧下发整改名称 */
        rectifyName: "",
        /** 左侧整改开始时间 */
        rectifyBeginTime: "",
        /** 左侧整改截止日期 */
        rectifyEndTime: "",
        reviewer: "",
        reviewerId: "",
        rectifyNoticeTime: null,
      },
      /** @type {number[]} */
      rectifyNoticeTimeExtra: [],
    };
  },
  computed: {
    rectifyBeginTimeOption() {
      return {
        disabledDate(time) {
          return time.getTime() < Date.now() - MILLISECONDS_PER_DAY;
        },
      };
    },
    rectifyEndTimeOption() {
      const beginTime =
        new Date(this.rectifyBeginTime).getTime() - MILLISECONDS_PER_DAY;
      const thirtyDayAfter = Date.now() + 3 * 30 * MILLISECONDS_PER_DAY;
      return {
        disabledDate(time) {
          return time.getTime() < beginTime || time.getTime() > thirtyDayAfter;
        },
      };
    },
  },
  mounted() {
    this.getIssueList();
  },
  methods: {
    saveIssue() {
      if (!this.form.entryName) {
        this.$message.warning("请输入复核问题");
        return;
      }
      const opt = {
        inspectionId: this.inspection.id,
        problemDescrip: this.form.entryName,
        entryName: this.form.entryName,
        status: 2,
        rectify: 1,
      };
      projectSupervisionApi.addProblem(opt).then((res) => {
        if (res.code == 0) {
          this.$message.success("保存成功");
          this.form.entryName = "";
          this.getIssueList();
        }
      });
    },
    async getIssueList() {
      const opt = {
        inspectionId: this.inspection.id,
        rectify: 1,
        status: 2,
      };
      projectSupervisionApi.getList(opt).then((res) => {
        if (res.code == 0) {
          this.issueList = res.data.list;
        }
      });
    },
    generateRectificationOrder() {
      const url = `${config.baseUrl}/river-service/blueInspect/generateRectificationOrder`;
      exportDetail(url, { inspectionId: this.inspection.id });
    },
    onDispatch() {
      let rectifyNoticeTime = this.form.rectifyNoticeTime + "";
      if (this.rectifyNoticeTimeExtra.length > 0) {
        this.rectifyNoticeTimeExtra.forEach((item) => {
          rectifyNoticeTime += `,${item}`;
        });
      }
      const opt = {
        inspectionId: this.inspection.id,
        rectifyName: this.form.rectifyName,
        rectifyBeginTime: this.form.rectifyBeginTime,
        rectifyEndTime: this.form.rectifyEndTime,
        reviewer: this.form.reviewer,
        reviewerId: this.form.reviewerId,
        rectifyNoticeTime,
      };
      projectSupervisionApi.review(opt).then((res) => {
        if (res.code == 0) {
          this.$emit("detail");
          this.$message.success("派发成功!");
        }
      });
    },
    onSaveInspector(val) {
      this.inspectorsData = val[0];
      this.personnelDialogShow = false;
      this.form.reviewer = this.inspectorsData.userName;
      this.form.reviewerId = this.inspectorsData.id;
    },
  },
};
</script>
<template>
  <div class="supervise-configuration">
    <div class="supervise-configuration__content">
      <el-form>
        <div class="supervise-configuration__base">
          <el-form-item label="下发整改名称">
            <el-input v-model="form.rectifyName" placeholder="" type="text" />
          </el-form-item>
          <el-form-item label="整改开始时间">
            <el-date-picker
              v-model="form.rectifyBeginTime"
              :default-value="Date.now()"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期"
              :picker-options="rectifyBeginTimeOption"
            />
          </el-form-item>
          <el-form-item class="part-item" label="整改截止时间">
            <el-date-picker
              v-model="form.rectifyEndTime"
              value-format="yyyy-MM-dd"
              type="date"
              :disabled="!form.rectifyBeginTime"
              :picker-options="rectifyEndTimeOption"
              :placeholder="
                !form.rectifyBeginTime ? '请先选择开始日期' : '选择日期'
              "
            />
          </el-form-item>
          <div class="el-form--inline" style="margin-top: 5px 0">
            <el-form-item label="指定巡查人员">
              <span v-if="inspectorsData.userName" class="inspector-sty">
                {{ inspectorsData.userName }}
              </span>
              <el-button
                icon="el-icon-plus"
                size="small"
                circle
                @click="personnelDialogShow = true"
              />
            </el-form-item>
          </div>
          <el-form-item label="通知时间">
            <span>
              距离整改截止日期前
              <el-input-number
                size="mini"
                v-model="form.rectifyNoticeTime"
                :min="1"
                :max="1000"
              />
              天通知
              <el-button size="mini" @click="rectifyNoticeTimeExtra.push(0)">
                新增
              </el-button>
              <el-button size="mini" @click="rectifyNoticeTimeExtra = []">
                清除
              </el-button>
            </span>
            <ul
              style="max-height: 80px; overflow-y: scroll; padding-left: 67px"
            >
              <li v-for="(time, index) in rectifyNoticeTimeExtra" :key="index">
                距离整改截止日期前
                <el-input-number
                  size="mini"
                  v-model="rectifyNoticeTimeExtra[index]"
                  :min="1"
                  :max="1000"
                />
                天通知
              </li>
            </ul>
          </el-form-item>
          <el-form-item>
            <!-- <el-button
              type="primary"
              size="small"
              @click="generateRectificationOrder"
            >
              生成整改单
            </el-button> -->
          </el-form-item>
        </div>
        <div class="supervise-configuration__addition">
          <div class="form-title">复核内容配置</div>
          <div>
            <ul>
              <li v-for="(item, i) in issueList" :key="i">
                {{ i + 1 }}、{{ item.entryName }}
              </li>
            </ul>
            <el-form-item label="新增复核问题:">
              <el-input
                v-model="form.entryName"
                type="textarea"
                :rows="4"
                resize="none"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="saveIssue">
                保存
              </el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div class="supervise-configuration__footer">
      <el-button size="small">取消</el-button>
      <el-button type="primary" size="small" @click="onDispatch">
        派发
      </el-button>
    </div>
    <PersonnelListDialog
      v-if="personnelDialogShow"
      PersonnelType="巡查人员"
      :visible.sync="personnelDialogShow"
      :selectPersonList="[inspectorsData]"
      @saveEmit="onSaveInspector"
    />
  </div>
</template>
<style lang="scss" scoped>
.supervise-configuration {
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid #eee;
  padding: 16px 0;
}
.supervise-configuration__content {
  width: 100%;
  flex: 1;
  .el-form {
    display: flex;
    height: 100%;
  }
}
.supervise-configuration__base {
  padding: 0 8px;
  height: 100%;
  width: 40%;
}
.supervise-configuration__addition {
  padding: 0 8px;
  height: 100%;
  width: 60%;
}
.supervise-configuration__footer {
  padding: 0 16px;
  margin-left: auto;
}
</style> 