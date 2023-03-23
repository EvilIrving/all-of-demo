<template>
  <div class="base_form">
    <el-dialog :visible.sync="visible" width="1000px" :before-close="dialogClose" top="15vh" :title="title" class="list-dialog" :append-to-body="true" :close-on-click-modal="false">
      <el-form ref="tableForm" :model="tableForm" :rules="rules" size="mini" label-width="125px" label-position="right">
        <el-row type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="规则配置" prop="ruletype">
              <el-radio-group v-model="tableForm.ruletype">
                <el-radio :label="1">报警规则</el-radio>
                <el-radio :label="2">提醒规则</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="tableForm.ruletype === 1" key="warningrule-item" type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="报警规则" prop="warningrule">
              <el-select v-model="tableForm.warningrule" @change="ruleTypeChange">
                <el-option v-for="item in ruleTypeOptions" :key="item.value" :value="item.value" :label="item.label"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="tableForm.ruletype === 2" key="reminderRule-item" type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="提醒规则" prop="reminderRule">
              <el-select v-model="tableForm.reminderRule" @change="tableForm.frequency = ''">
                <el-option v-for="item in reminderRuleOptions" :key="item.value" :value="item.value" :label="item.label"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="tableForm.ruletype === 2 && tableForm.reminderRule !== ''" key="frequency-item" type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="提醒频率" prop="frequency">
              <el-select v-model="tableForm.frequency">
                <template v-if="tableForm.reminderRule === 15">
                  <el-option v-for="item in reminderFrequencyOtherOptions" :key="item.value" :value="item.value" :label="item.label"></el-option>
                </template>
                <template v-else>
                  <el-option v-for="item in reminderFrequencyOptions" :key="item.value" :value="item.value" :label="item.label"></el-option>
                </template>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left" v-if="tableForm.ruletype == 1 && tableForm.warningrule == 8">
          <el-col :span="12">
            <el-form-item key="1" label="预警规则" prop="rainhour">
              <el-input v-model="tableForm.rainhour" placeholder="请输入内容"><i slot="suffix" class="dw">h</i></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item key="2" label="累计降雨量超过" prop="cumulativerain">
              <el-input v-model="tableForm.cumulativerain" placeholder="请输入内容"><i slot="suffix" class="dw">mm</i></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="early_warning_rules" justify="left" v-if="tableForm.ruletype == 1 && tableForm.warningrule == 9">
          <el-col :span="24">
            <el-form-item key="3" prop="overwarninglevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 60px;">预警规则:当前水位超过警戒水位</p>
              <el-input style="width:61%;" placeholder="请输入内容" v-model="tableForm.overwarninglevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成警报</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top:15px;">
            <el-form-item key="4" prop="overguaranteelevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 124px;">或当前水位超过保证水位</p>
              <el-input style="width:59%;" placeholder="请输入内容" v-model="tableForm.overguaranteelevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成警报</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="early_warning_rules" justify="left" v-if="tableForm.ruletype == 1 && tableForm.warningrule == 10">
          <el-col :span="24">
            <el-form-item key="5" prop="overlimitlevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 60px;">预警规则:当前水位超过汛限水位</p>
              <el-input style="width:61%;" placeholder="请输入内容" v-model="tableForm.overlimitlevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成警报</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top:15px;">
            <el-form-item key="6" prop="overfloodlevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 110px;">或当前水位超过防洪高水位</p>
              <el-input style="width:59%;" placeholder="请输入内容" v-model="tableForm.overfloodlevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成警报</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="early_warning_rules" justify="left" v-if="tableForm.ruletype == 1 && tableForm.warningrule == 11">
          <el-col :span="24">
            <el-form-item key="7" prop="overbluelevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 60px">预警规则:当前潮位超过</p>
              <el-input style="width:59%; margin-left:14px;" placeholder="请输入内容" v-model="tableForm.overbluelevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成蓝色警报</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top:15px;">
            <el-form-item key="8" prop="overyellowlevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 120px;">或当前潮位超过</p>
              <el-input style="width:59%;" placeholder="请输入内容" v-model="tableForm.overyellowlevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成黄色警报</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top:15px;">
            <el-form-item key="9" prop="overorangelevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 120px;">或当前潮位超过</p>
              <el-input style="width:59%;" placeholder="请输入内容" v-model="tableForm.overorangelevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成橙色警报</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top:15px;">
            <el-form-item key="10" prop="overredlevel">
              <p style="float:left;line-height: 40px;height: 40px;  margin:0 15px 0 120px;">或当前潮位超过</p>
              <el-input style="width:59%;" placeholder="请输入内容" v-model="tableForm.overredlevel">
                <i slot="suffix" class="dw">mm</i>
              </el-input>
              <span style="line-height: 40px;height: 40px; margin-left: 15px;">生成红色警报</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left" v-if="tableForm.ruletype == 1 && (tableForm.warningrule == 12 || tableForm.warningrule == 13)">
          <el-col :span="24">
            <el-form-item label="测站类型" prop="stType">
              <el-select v-model="tableForm.stType" placeholder="请选择" @change="stTypeChange">
                <el-option value="1" label="水位站"></el-option>
                <el-option value="2" label="雨量站"></el-option>
                <el-option value="3" label="流量站"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left">
          <el-col :span="24">
            <el-form-item :label="(tableForm.ruletype == 2 || formlabelType) ? '关联工程' : '关联测站'" prop="projectName">
              <el-input type="textarea" :disabled="tableForm.ruletype != 2 && (tableForm.warningrule && (tableForm.warningrule == 12 || tableForm.warningrule == 13) && !tableForm.stType)" v-model="tableForm.projectName" placeholder="请选择关联工程" @focus="thresholdSet"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left" v-if="showRespons" :key="tableForm.warningrule + '-person-liable'">
          <el-col :span="24">
            <el-form-item label="责任人" prop="personLiable">
              <el-select v-model="tableForm.personLiable" placeholder="请选择" multiple>
                <el-option value="1" label="政府负责人"></el-option>
                <el-option value="2" label="水行政部门负责人"></el-option>
                <el-option value="3" label="主管部门负责人"></el-option>
                <el-option value="4" label="管理单位责任人"></el-option>
                <el-option value="5" label="技术责任人"></el-option>
                <el-option value="6" label="巡查责任人"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left" v-if="tableForm.ruletype == 1 && tableForm.warningrule == 6" key="haitang-person-liable">
          <el-col :span="24">
            <el-form-item label="海塘责任人" prop="htzrn">
              <el-select v-model="tableForm.htzrn" placeholder="请选择" multiple>
                <el-option value="1" label="政府负责人"></el-option>
                <el-option value="2" label="水行政部门负责人"></el-option>
                <el-option value="3" label="主管部门负责人"></el-option>
                <el-option value="4" label="管理单位责任人"></el-option>
                <el-option value="5" label="技术责任人"></el-option>
                <el-option value="6" label="巡查责任人"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left" v-if="tableForm.ruletype == 1 && (tableForm.warningrule == 12 || tableForm.warningrule == 13)">
          <el-col :span="24">
            <el-form-item :label="tableForm.warningrule == 12 ? '差额限值' : '上限'" prop="maximum">
              <el-input type="text" v-model="tableForm.maximum" placeholder="必填项"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left" v-if="tableForm.ruletype == 1 && tableForm.warningrule == 13">
          <el-col :span="24">
            <el-form-item label="下限" prop="minimum">
              <el-input type="number" v-model="tableForm.minimum" placeholder="必填项"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="接收人" prop="usersName">
              <el-input type="textarea" v-model="tableForm.usersName" placeholder="接收人" @focus="personSet"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left">
          <el-col :span="12">
            <el-form-item label="启闭状态" prop="isopen">
              <el-select v-model="tableForm.isopen" placeholder="请选择启闭状态">
                <el-option v-for="item in isCloseOptions" :key="item.value" :value="item.value" :label="item.label"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-row>
          <div class="form-footer">
            <el-button class="form-btn btn-cancel" @click="dialogClose" size="mini">取消</el-button>
            <el-button class="form-btn btn-save" @click="saveData" size="mini">保存</el-button>
          </div>
        </el-row>
      </div>

      <!-- 关联工程 -->
      <threshold-dialog v-if="thresholdShow" ref="thresholdDialog" :visible="thresholdShow" :type="optionType" :stType="stType" :checkedStations="tableForm.bindcode" :isSingle="tableForm.ruletype == 1 && (tableForm.warningrule == 13)" @close="thresholdClose" @setThresholdId="setThresholdId" />
      <!-- 关联人员 -->
      <person-dialog ref="personDialog" v-if="personShow" :visible="personShow" :checkedPersons="tableForm.sendcode" @setPersonIds="setPersonIds" @saveEmit="setPersonIds" @close="personClose" />
    </el-dialog>
  </div>
</template>
<script>
import thresholdDialog from "./thresholdDialog.vue";
import personDialog from "./personDialog.vue";
import { ruleConfiguration } from "@/api/seawallPrevent";
import { cloneObj } from "@/utils/utils.js";
export default {
  name: "baseForm",
  props: {
    visible: Boolean,
    isLook: Boolean,
    isNew: Boolean,
    info: Object
  },
  watch: {
    visible(val) {
      this.show = val;
    },
    "tableForm.ruletype"(val) {
      if (val === 2) {
        this.optionType = "engineeringAll";
      }
    }
  },
  components: {
    thresholdDialog,
    personDialog
  },
  data() {
    return {
      optionType: "seawall", //option选择报警规则 做不同请求
      show: false,
      mapVisible: false,
      pointArr: [],
      rules: {
        ruletype: [{ required: true, message: "请选择配置规则" }],
        warningrule: [{ required: true, message: "请选择报警规则" }],
        reminderRule: [{ required: true, message: "请选择提醒规则" }],
        frequency: [{ required: true, message: "请选择提醒频率" }],
        rainhour: [{ required: true, message: "请输入时段雨量报警统计时间" }],
        cumulativerain: [
          { required: true, message: "请输入时段雨量报警累计降雨量值" }
        ],
        overwarninglevel: [
          { required: true, message: "请输入河道水情报警超过警戒水位值" }
        ],
        overguaranteelevel: [
          { required: true, message: "请输入河道水情报警超过保证水位值" }
        ],
        overlimitlevel: [
          { required: true, message: "请输入水库水情报警超过汛限水位值" }
        ],
        overfloodlevel: [
          { required: true, message: "请输入水库水情报警超过防洪高水位值" }
        ],
        overbluelevel: [
          { required: true, message: "请输入潮位报警超过蓝色警报水位值" }
        ],
        overyellowlevel: [
          { required: true, message: "请输入潮位报警超过黄色警报水位值" }
        ],
        overorangelevel: [
          { required: true, message: "请输入潮位报警超过橙色警报水位值" }
        ],
        overredlevel: [
          { required: true, message: "请输入潮位报警超过红色警报水位值" }
        ],
        projectName: [{ required: true, message: "请选择关联工程" }],
        // usersName: [{ required: true, message: "请选择接收人" }],
        personLiable: [{ required: true, message: "请选择责任人" }],
        htzrn: [{ required: true, message: "请选择海塘负责人" }],
        stType: [{ required: true, message: "请选择测站类型" }],
        maximum: [{ required: true, message: "必填项" }],
        minimum: [{ required: true, message: "必填项" }]
      },
      title: "新增",
      tableForm: {
        bindcode: "",
        isopen: "",
        sendcode: "",
        ruletype: "",
        warningrule: "",
        reminderRule: "",
        frequency: "",
        rainhour: "",
        cumulativerain: "",
        overwarninglevel: "",
        overguaranteelevel: "",
        overlimitlevel: "",
        overfloodlevel: "",
        overbluelevel: "",
        overyellowlevel: "",
        overorangelevel: "",
        overredlevel: "",
        id: "",
        usersName: "",
        projectName: "",
        type: "",
        personLiable: [],
        htzrn: [],
        stType: "",
        maximum: "",
        minimum: ""
      },
      thresholdShow: false,
      personShow: false,
      isCloseOptions: [
        {
          label: "开启",
          value: 1
        },
        {
          label: "关闭",
          value: 2
        }
      ],
      messageTypeOptions: [
        {
          label: "报警",
          value: "1"
        }
      ],
      ruleTypeOptions: [
        //   <=7 绑定海塘  >7 测站
        {
          value: 1,
          label: "安全鉴定超期"
        },
        {
          value: 2,
          label: "汛前检查超期"
        },
        {
          value: 3,
          label: "年度总结超期"
        },
        {
          value: 4,
          label: "巡查信息异常"
        },
        {
          value: 5,
          label: "工程隐患"
        },
        {
          value: 6,
          label: "海塘实时报警"
        },
        {
          value: 7,
          label: "海塘预报预警"
        },
        {
          value: 8,
          label: "时段雨量报警"
        },
        {
          value: 9,
          label: "河道水情报警"
        },
        {
          value: 10,
          label: "水库水情报警"
        },
        {
          value: 11,
          label: "潮位报警"
        },
        {
          value: 12,
          label: "测站关联报警"
        },
        {
          value: 13,
          label: "测站限值报警"
        }
      ],
      reminderRuleOptions: [
        {
          value: 15,
          label: "日常检查提醒"
        },
        {
          value: 18,
          label: "汛前检查提醒"
        },
        {
          value: 16,
          label: "汛后检查提醒"
        },
        {
          value: 17,
          label: "年度检查提醒"
        },
        {
          value: 14,
          label: "预案资料提醒"
        }
      ],
      reminderFrequencyOptions: [
        {
          value: 1,
          label: "1天/次"
        },
        {
          value: 2,
          label: "1周/次"
        },
        {
          value: 3,
          label: "半月/次"
        },
        {
          value: 4,
          label: "1月/次"
        }
      ],
      reminderFrequencyOtherOptions: [
        {
          value: 5,
          label: "1小时/次"
        },
        {
          value: 6,
          label: "2小时/次"
        },
        {
          value: 7,
          label: "3小时/次"
        }
      ],
      formlabelType: true,
      stType: ""
    };
  },
  computed: {
    showRespons() {
      if (this.tableForm.ruletype == 1 && [1, 2, 3, 4, 5].includes(this.tableForm.warningrule)) return true
      if (this.tableForm.ruletype == 2) return true
      return false
    },
  },
  mounted() {
    this.$nextTick(() => {
      //console.log(this.info);
      if (this.isNew) {
        this.title = "新增";
      } else {
        if (this.isLook) {
          this.title = "查看";
        } else {
          this.title = "编辑";
        }
        //调用获取详情数据方法
        this.getListById();
      }
    });
  },
  methods: {
    //获取详情
    async getListById() {
      await ruleConfiguration.getById({ id: this.info.id }).then(res => {
        if (res.code == 0) {
          this.tableForm = res.data;
          const {
            bindcode,
            isopen,
            sendcode,
            warningrule,
            rainhour,
            cumulativerain,
            overwarninglevel,
            overguaranteelevel,
            overlimitlevel,
            overfloodlevel,
            overbluelevel,
            overyellowlevel,
            overorangelevel,
            overredlevel,
            id,
            projectName,
            usersName,
            sendcount,
            bindcount,
            maximum,
            minimum,
            ruletype,
            frequency
          } = res.data;
          const htzrn = res.data.htzrn ? res.data.htzrn.split(",") : [];
          const stType = res.data.stType ? res.data.stType.toString() : "";
          let personLiable = [];
          if ([1, 2, 3, 4, 5].includes(warningrule)) {
            personLiable = htzrn;
          }
          let reminderRule = "";
          if (ruletype === 2) {
            reminderRule = warningrule;
          }
          this.tableForm = {
            bindcode,
            isopen,
            sendcode,
            warningrule,
            reminderRule,
            frequency,
            rainhour,
            cumulativerain,
            overwarninglevel,
            overguaranteelevel,
            overlimitlevel,
            overfloodlevel,
            overbluelevel,
            overyellowlevel,
            overorangelevel,
            overredlevel,
            id,
            projectName,
            usersName,
            sendcount,
            bindcount,
            personLiable,
            htzrn,
            stType,
            maximum,
            minimum,
            ruletype
          };
          // this.tableForm.bindcode = res.data.bindcode;
          // this.tableForm.sendcode = res.data.sendcode;
          if (res.data.bindcount && res.data.bindcount > 0) {
            let tempArr = [];
            res.data.bindList.map(i => {
              tempArr.push(i.seawall_name || i.st_name || i.project_name);
            });
            this.tableForm.projectName = tempArr.join(",");
          }
          if (res.data.sendcount && res.data.sendcount > 0) {
            let tempArr = [];
            res.data.users.map(i => {
              tempArr.push(i.name);
            });
            this.tableForm.usersName = tempArr.join(",");
          }
          switch (this.tableForm.warningrule) {
            // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
            case 8:
              this.optionType = "PP";
              break;
            case 9:
              this.optionType = "ZZ";
              break;
            case 10:
              this.optionType = "RR";
              break;
            case 11:
              this.optionType = "TT";
              break;
            default:
              this.optionType = "seawall";
              break;
          }
          if (res.data.stType) {
            this.stType = res.data.stType;
            this.formlabelType = false;
            this.optionType = "CZ"; // CZ测站统一标识
          }
        }
      });
    },
    // 验证表单
    saveData() {
      this.$refs.tableForm.validate(valid => {
        if (valid) {
          this.saveForm();
        } else {
          this.$message.error("请填写必填内容！");
          return false;
        }
      });
    },
    async saveForm() {
      let data = cloneObj(this.tableForm);
      if (data.warningrule == 6) {
        data.htzrn = data.htzrn.join(",");
      } else if ([1, 2, 3, 4, 5].includes(this.tableForm.warningrule)) {
        data.htzrn = data.personLiable.join(",");
      } else {
        data.htzrn = "";
      }
      if (data.ruletype == 2) {
        data.warningrule = data.reminderRule;
      }
      if (!this.isNew) {
        await ruleConfiguration.update(data).then(res => {
          if (res.code == 0) {
            this.$message({
              message: "编辑成功",
              type: "success"
            });
            this.dialogClose();
            this.$emit("getList");
          } else {
            this.$message.error(res.message);
          }
        });
      } else {
        await ruleConfiguration.save(data).then(res => {
          if (res.code == 0) {
            this.$message({
              message: "保存成功",
              type: "success"
            });
            this.dialogClose();
            this.$emit("getList");
          } else {
            this.$message.error(res.message);
          }
        });
      }
    },
    setFormWarn() {
      this.tableForm.bindcode = "";
      this.tableForm.bindcount = 0;
      this.tableForm.projectName = "";
    },
    stTypeChange(val) {
      this.setFormWarn();
      this.formlabelType = false;
      this.optionType = "CZ"; // CZ测站统一标识
      this.stType = val;
    },
    ruleTypeChange(val) {
      this.setFormWarn();
      switch (val) {
        // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
        case 8:
          this.formlabelType = false;
          this.optionType = "PP";
          break;
        case 9:
          this.formlabelType = false;
          this.optionType = "ZZ";
          break;
        case 10:
          this.formlabelType = false;
          this.optionType = "RR";
          break;
        case 11:
          this.formlabelType = false;
          this.optionType = "TT";
          break;
        case 12:
          this.formlabelType = false;
          break;
        case 13:
          this.formlabelType = false;
          break;
        default:
          this.formlabelType = true;
          this.optionType = "seawall";
          break;
      }
    },
    // 显示关联工程
    thresholdSet() {
      this.thresholdShow = true;
    },
    // 关闭关联工程
    thresholdClose() {
      this.thresholdShow = false;
    },
    // 关联工程返回数据
    setThresholdId(val) {
      let arr = [];
      let str = [];
      console.log(val);
      // if (this.optionType == "seawall") {
      //   val.forEach(item => {
      //     arr.push(item.seawallCode);
      //     str.push(item.seawallName);
      //   });
      //   // this.tableForm.stationNames = str.join(",");
      // } else
      switch (this.optionType) {
        case 'engineeringAll':
        case 'seawall':
          val.forEach(item => {
            arr.push(item.project_code);
            str.push(item.project_name);
          });
          break;
        case 'PP':
          val.forEach(item => {
            arr.push(item.stcd);
            str.push(item.stationName);
          });
          break;
        default:
          val.forEach(item => {
            arr.push(item.stCode);
            str.push(item.stName);
          });
          break;
      }
      this.tableForm.bindcode = arr.join(",");
      this.tableForm.bindcount = arr.length;
      this.tableForm.projectName = str.join(",");
    },
    // 显示接收人
    personSet() {
      this.personShow = true;
    },
    // 关闭接收人
    personClose() {
      this.personShow = false;
    },
    // 接收人返回数据
    setPersonIds(checkArr) {
      let arr = [];
      let str = [];
      checkArr.forEach(item => {
        arr.push(item.id);
        str.push(item.name);
      });
      this.tableForm.sendcode = arr.join(",");
      this.tableForm.sendcount = arr.length;
      this.tableForm.usersName = str.join(",");
    },
    // 重置
    resetForm() {
      this.$refs.tableForm.resetFields();
    },
    // 关闭
    dialogClose() {
      this.$emit("close");
    }
  }
};
</script>

<style lang="scss" scoped>
.dw {
  color: rgb(255, 0, 0);
}
.early_warning_rules {
  ::v-deep .el-form-item__content {
    margin-left: 0 !important;
  }
  p {
    &:before {
      content: "*";
      color: #f56c6c;
      margin-right: 4px;
    }
  }
  ::v-deep .el-form-item__error {
    right: 400px;
    left: auto;
  }
}
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
