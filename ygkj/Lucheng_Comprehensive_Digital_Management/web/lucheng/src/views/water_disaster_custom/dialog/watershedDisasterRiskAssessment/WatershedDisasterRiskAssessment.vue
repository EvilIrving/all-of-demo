<template>
  <div class="lyzhfxpg_content">
    <div class="people">
      <div class="peopleNum">影响人数：{{ impactNumber }}</div>
      <!-- <div class="send" @click="visibleDialog = true">发送</div> -->
    </div>
    <div class="personnelInformation">
      <div v-for="item in peopleList" :key="item.id">
        <span class="label">{{ item.chargeType }}</span
        ><span class="value">{{ item.pchargeName }} {{ item.mobile }}</span>
      </div>
      <!-- <div><span class="label">转移责任人</span><span class="value"></span></div>
      <div><span class="label">监测预警员</span><span class="value"></span></div> -->
    </div>
    <div class="personnelList">人员清单</div>
    <div>
      <el-table
        :data="tableData"
        height="245"
        :header-cell-style="{
          background: '#2993b41a',
          color: '#00c1ff'
        }"
        class="customer-table"
        style="width: 100%"
      >
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="defenObj" label="防御对象" align="center"></el-table-column>
        <el-table-column prop="funds" label="类型" align="center"></el-table-column>
        <el-table-column prop="contact" label="影响人员" align="center"> </el-table-column>
        <el-table-column prop="contacttel" label="联系方式" align="center"></el-table-column>
        <el-table-column prop="pcount" label="人口数量" align="center"> </el-table-column>
        <el-table-column prop="chargerName" label="转移责任人" align="center"> </el-table-column>
        <el-table-column prop="chargerPhone" label="责任人电话" align="center"> </el-table-column>
      </el-table>
    </div>
    <PrimaryDialog
      :visible="visibleDialog"
      :appendToBody="true"
      main-title="发送短信"
      top="10vh"
      width="1296px"
      @handleClose="handleClose"
    >
      <div class="new_box">
        <div class="left">
          <div>
            <img src="../../../../assets/images/shortMessage_diamonds.png" alt="" />
            <span style="color: #fff">接收人</span>
          </div>
          <img src="../../../../assets/images/shortMessage_shortLine.png" alt="" />
        </div>
        <div class="right">
          <div class="">
            <img src="../../../../assets/images/shortMessage_diamonds.png" alt="" />
            <span style="color: #fff">短信内容</span>
          </div>
          <img src="../../../../assets/images/shortMessage_longLine.png" alt="" />
          <div class="duty-box flex-box">
          </div>
        </div>
      </div>
      <!-- <el-form ref="form" class="form-box-super" :model="form" label-width="150px">
        <el-form-item label="选择模版：">
          <el-select
            size="small"
            class="select-box"
            v-model="templateTypeValue"
            style="width: 100%"
            filterable
            placeholder="请选择"
            @change="handleTemplateTypeChange"
          >
            <el-option v-for="item in templateType" :key="item.value" :label="item.name" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单位：">
          <el-select
            size="small"
            class="select-box"
            v-model="companyType"
            style="width: 100%"
            placeholder="请选择"
            @change="changeCompany"
          >
            <el-option v-for="item in companyList" :key="item.value" :label="item.name" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接收人：">
          <el-select
            size="small"
            class="select-box"
            v-if="companyType == 'slj'"
            v-model="recInfo"
            style="width: 100%"
            filterable
            multiple
            placeholder="请选择"
            @change="handleChange"
          >
            <el-option
              v-for="item in receiverList"
              :key="item.id"
              :label="item.name + '-' + item.deptName"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <el-select
            size="small"
            class="select-box"
            v-if="companyType == 'boat' || companyType == 'farmer'"
            v-model="recInfo"
            style="width: 100%"
            filterable
            multiple
            placeholder="请选择"
            @change="handleChange"
          >
            <el-option v-for="item in receiverList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
          </el-select>
          <el-select
            size="small"
            class="select-box"
            v-if="companyType == 'torrent'"
            v-model="recInfo"
            style="width: 100%"
            filterable
            multiple
            placeholder="请选择"
            @change="handleChange"
          >
            <el-option
              v-for="item in receiverList"
              :key="item.id"
              :label="item.name + '-' + item.type"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <el-cascader
            v-if="companyType == 'three'"
            :options="receiverList"
            :props="props"
            :show-all-levels="false"
            @change="handleThreeChange"
            clearable
          ></el-cascader>
        </el-form-item>
        <el-form-item v-if="templateTypeValue === 0" label="预期开始时间：">
          <el-date-picker
            v-model="startTime"
            type="datetime"
            format="yyyy-MM-dd HH"
            value-format="yyyy-MM-dd HH"
            placeholder="选择日期"
            @change="selectStartTime"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="templateTypeValue === 0" label="预期结束时间：">
          <el-date-picker
            v-model="endTime"
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            placeholder="选择日期"
            @change="selectEndTime"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="templateTypeValue === 2" label="启动时间：">
          <el-date-picker
            v-model="startTime"
            type="datetime"
            format="yyyy-MM-dd HH"
            value-format="yyyy-MM-dd HH"
            placeholder="选择日期"
            @change="selectStartTime"
          >
          </el-date-picker>
        </el-form-item>
        <div class="training-form-box">
          <el-form-item v-if="templateTypeValue === 0" label="面雨量(范围)：">
            <el-input size="small" v-model="mylMinvalue" @change="inputChange"></el-input>
          </el-form-item>
          <el-form-item v-if="templateTypeValue === 0" label="小于：">
            <el-input size="small" v-model="mylMaxValue" @change="inputChange"></el-input>
          </el-form-item>
        </div>
        <div class="training-form-box">
          <el-form-item v-if="templateTypeValue === 0" label="西部山区(范围)：">
            <el-input size="small" v-model="xbMinvalue" @change="inputChange"></el-input>
          </el-form-item>
          <el-form-item v-if="templateTypeValue === 0" label="小于：">
            <el-input size="small" v-model="xbMaxValue" @change="inputChange"></el-input>
          </el-form-item>
        </div>
        <el-form-item v-if="templateTypeValue === 0" label="局部可达：">
          <el-input size="small" v-model="jbValue" @change="inputChange"></el-input>
        </el-form-item>
        <el-form-item v-if="templateTypeValue === 1" label="应急响应：">
          <el-input size="small" v-model="yjxyValue" @change="inputChange"></el-input>
        </el-form-item>
        <el-form-item v-if="templateTypeValue === 1 || templateTypeValue === 2" label="水旱灾害防御应急响应：">
          <el-input size="small" v-model="shzhValue" @change="inputChange"></el-input>
        </el-form-item>
        <el-form-item label="短信内容：">
          <el-input size="small" type="textarea" readonly v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="btn-box">
            <div class="close-btn" @click="handleClose">取消</div>
            <div class="add-btn" @click="submitForm">确定</div>
          </div>
        </el-form-item>
      </el-form> -->
    </PrimaryDialog>
  </div>
</template>

<script>
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import { PrimaryDialog } from '@/components';
export default {
  props: {
    pointData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      peopleList: [],
      impactNumber: '',
      tableData: [],
      startTime: '',
      startDay: '',
      startdate: '',
      endTime: '',
      endDay: '',
      mylMinvalue: '',
      mylMaxValue: '',
      xbMinvalue: '',
      xbMaxValue: '',
      jbValue: '',
      yjxyValue: '',
      shzhValue: '',
      visibleDialog: false,
      templateTypeValue: 0,
      templateType: [
        { name: '模版1', value: 0 },
        { name: '模版2', value: 1 },
        { name: '模版3', value: 2 }
      ],
      companyType: '',
      companyList: [
        { name: '水利局通讯录', value: 'slj' },
        { name: '农机渔船', value: 'boat' },
        { name: '种粮大户', value: 'farmer' },
        { name: '山洪灾害预警责任人', value: 'torrent' },
        { name: '水利工程三个责任人', value: 'three' }
      ],
      receiverList: [],
      recInfo: [],
      form: {
        recInfo: [],
        content: ''
      },
      props: { multiple: true, label: 'name', children: 'charge', value: 'id' }
    };
  },
  components: {
    PrimaryDialog
  },
  mounted() {
    console.log(this.pointData, 'pointData');
    this.getList();
    this.getAffectPopulationList();
    this.form.content =
      '预计我市{f0}日{f1}时到{f2}日有明显降雨，过程面雨量可达{f3}-{f4}毫米，西部山区{f5}-{f6}毫米，局部可达{f7}毫米以上，请注意山洪灾害防范。';
  },
  methods: {
    // send() {
    //   console.log(this.form.content);
    // },
    async getList() {
      let res = await disasterPreventionApi.pvPchargeList({
        naturalAdnm: this.pointData.nvi
      });
      if (res.code == 0) {
        this.peopleList = res.data.list;
        // console.log(this.peopleList);
        // this.pageTotal = res.data.totalRows
      }
    },
    async getAffectPopulationList() {
      let res = await disasterPreventionApi.affectPopulationList({
        baseId: this.pointData.id,
        chargerName: this.pointData.chargerName,
        chargerPhone: this.pointData.chargerPhone
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        // console.log(this.tableData,'this.tableData');
        this.impactNumber = res.data.totalPages;
      }
    },
    async getResponsibleListByType() {
      let res = await disasterPreventionApi.responsibleListByType({
        type: this.companyType
      });
      if (res.code == 0) {
        this.receiverList = res.data;
        // console.log(this.receiverList);
        if (this.companyType == 'three') {
          //   console.log(this.receiverList);
          this.receiverList.map((item) => (item.name = item.projectName));
        }
        // this.pageTotal = res.data.totalRows
      }
    },
    selectStartTime() {
      this.startDay = this.startTime.substring(8, 10);
      this.startdate = this.startTime.substring(this.startTime.length - 2, this.startTime.length);
      this.form.content =
        '预计我市' +
        this.startDay +
        '日' +
        this.startdate +
        '时到{f2}日有明显降雨，过程面雨量可达{f3}-{f4}毫米，西部山区{f5}-{f6}毫米，局部可达{f7}毫米以上，请注意山洪灾害防范。';
    },
    selectEndTime() {
      this.endDay = this.endTime.substring(this.endTime.length - 2, this.endTime.length);
      this.form.content =
        '预计我市' +
        this.startDay +
        '日' +
        this.startdate +
        '时到' +
        this.endDay +
        '日有明显降雨，过程面雨量可达{f3}-{f4}毫米，西部山区{f5}-{f6}毫米，局部可达{f7}毫米以上，请注意山洪灾害防范。';
    },
    inputChange() {
      if (this.templateTypeValue === 0) {
        this.form.content =
          '预计我市' +
          this.startDay +
          '日' +
          this.startdate +
          '时到' +
          this.endDay +
          '日有明显降雨，过程面雨量可达' +
          this.mylMinvalue +
          '-' +
          this.mylMaxValue +
          '毫米，西部山区' +
          this.xbMinvalue +
          '-' +
          this.xbMaxValue +
          '毫米，局部可达' +
          this.jbValue +
          '毫米以上，请注意山洪灾害防范。';
      } else if (this.templateTypeValue === 1) {
        this.form.content =
          '根据区防办启动' +
          this.yjxyValue +
          '级应急响应，我部启动' +
          this.shzhValue +
          '级水旱灾害防御应急响应、做好山塘、水库、沿江水闸开闸预泄和山洪灾害防范工作。';
      } else {
        this.form.content =
          '决定于' +
          this.startDay +
          '日' +
          this.startdate +
          '时启动水旱灾害防御' +
          this.shzhValue +
          '级应急响应，请水利工程和山洪灾害相关责任人根据预案要求做好台风防御工作。';
      }
    },
    handleTemplateTypeChange() {
      this.startTime = '';
      this.endTime = '';
      this.mylMinvalue = '';
      this.mylMaxValue = '';
      this.xbMinvalue = '';
      this.xbMaxValue = '';
      this.jbValue = '';
      this.yjxyValue = '';
      this.shzhValue = '';
      this.recInfo = [];
      //   console.log(this.templateTypeValue);
      if (this.templateTypeValue === 0) {
        this.form.content =
          '预计我市{f0}日{f1}时到{f2}日有明显降雨，过程面雨量可达{f3}-{f4}毫米，西部山区{f5}-{f6}毫米，局部可达{f7}毫米以上，请注意山洪灾害防范。';
      } else if (this.templateTypeValue === 1) {
        this.form.content =
          '根据区防办启动{f0}级应急响应，我部启动{f1}级水旱灾害防御应急响应、做好山塘、水库、沿江水闸开闸预泄和山洪灾害防范工作。';
      } else {
        this.form.content =
          '决定于{f0}日{f1}时启动水旱灾害防御{f2}级应急响应，请水利工程和山洪灾害相关责任人根据预案要求做好台风防御工作。';
      }
      //   console.log(this.form.content);
    },
    changeCompany(val) {
      //   console.log(val);
      this.recInfo = [];
      this.getResponsibleListByType();
    },
    handleChange(val) {
      //   console.log(val);
      this.form.recInfo = [];
      if (val.length > 0) {
        this.form.recInfo = val.map((item) => {
          let data = this.receiverList.find((element) => element.id == item);
          //   console.log(data, '12132132');
          return {
            workerName: data.name,
            workerPhone: data.phone
          };
        });
        // console.log(this.form.recInfo);
      } else {
        this.form.recInfo = [];
      }
    },
    handleThreeChange(val) {
      let newVal = [];
      val.map((item) => {
        newVal.push(item[1]);
      });
      this.form.recInfo = [];
      if (newVal.length > 0) {
        newVal.map((item) => {
          this.receiverList.map((newItem) => {
            newItem.charge.map((element) => {
              if (element.id == item) {
                this.form.recInfo.push({ workerName: element.name, workerPhone: element.phone });
              }
            });
          });
        });
        // console.log(this.form.recInfo);
      } else {
        this.form.recInfo = [];
      }
      //   console.log(newVal);
    },
    handleClose() {
      this.templateTypeValue = 0;
      this.startTime = '';
      this.endTime = '';
      this.mylMinvalue = '';
      this.mylMaxValue = '';
      this.xbMinvalue = '';
      this.xbMaxValue = '';
      this.jbValue = '';
      this.yjxyValue = '';
      this.shzhValue = '';
      this.recInfo = [];
      this.form = {
        recInfo: [],
        content:
          '预计我市{f0}日{f1}时到{f2}日有明显降雨，过程面雨量可达{f3}-{f4}毫米，西部山区{f5}-{f6}毫米，局部可达{f7}毫米以上，请注意山洪灾害防范。'
      };
      this.submitType = false;
      this.visibleDialog = false;
    },
    submitForm() {
      if (this.submitType) {
        return false;
      }
      this.submitType = true;
      disasterPreventionApi
        .sendTfMsg(this.form)
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
.lyzhfxpg_content {
  .people {
    margin-top: 10px;
    height: 30px;
    width: 100%;
    > div {
      float: right;
      line-height: 30px;
      padding: 0 20px;
      border-radius: 5px;
    }
    .peopleNum {
      background-color: #fc9f54;
      font-size: 16px;
      color: #1e2c35;
    }
    .send {
      background-color: aqua;
      margin-right: 15px;
      cursor: pointer;
    }
  }
  .personnelInformation {
    padding: 10px 30px;
    margin: 24px 0;
    background-color: rgba(1, 18, 24, 0.4);
    > div {
      display: inline-block;
      width: 50%;
      margin-top: 16px;
      margin-bottom: 16px;
      padding-left: 8px;
      padding-right: 8px;
      font-size: 18px;
      .label {
        display: inline-block;
        color: #a6becc;
        margin-right: 20px;
        width: 150px;
      }
      .value {
        color: #fff;
      }
    }
  }
  .personnelList {
    color: #fff;
    font-size: 20px;
    font-weight: 700;
    margin-bottom: 20px;
    padding-left: 10px;
  }
}
.training-form-box {
  display: flex;
  width: 100%;
  justify-content: space-between;
  > div {
    width: 50%;
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
.new_box {
  width: 100%;
  height: 100%;
  display: flex;
  & > img {
    width: 90%;
  }
  justify-content: space-between;
  .left > div:nth-child(1),
  .right > div:nth-child(1) {
    margin-top: 20px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    font-size: 18px;
    & > img {
        margin-right: 8px;
    }
  }
  .left {
    width: 325px;
    padding: 10px;
    box-sizing: border-box;
  }
  .right {
    width: calc(100% - 325px);
    padding: 10px;
    box-sizing: border-box;
    > img {
    //   width: 90%;
    }
  }
}
</style>

<style lang="scss">
.customer-table {
  background: transparent;
}
.customer-table tr {
  background: transparent;
}
.customer-table .el-table__body-wrapper {
  &::-webkit-scrollbar {
    display: none;
  }
}
.customer-table .el-table__body {
  width: 100% !important;
}
.customer-table tbody tr:nth-child(odd) > td {
  background-color: rgba(19, 32, 41, 0.6);
  color: #fff;
}
.customer-table tbody tr:nth-child(even) > td {
  background-color: rgba(18, 49, 58, 0.6);
  color: #fff;
}
.customer-table tbody tr:hover > td {
  background-color: rgba(18, 49, 58, 0.6) !important;
}
.customer-table th.el-table__cell {
  background-color: rgba(18, 49, 58, 0.6);
}
// .customer-table tr:nth-child(odd) {
//     background-color: #0082e5;
// }
// .customer-table tr:nth-child(even) {
//     background-color: #0082e5;
// }
.customer-table td.el-table__cell,
.customer-table th.el-table__cell.is-leaf {
  border: 0;
}
.el-table--border::after,
.el-table--group::after,
.customer-table::before {
  content: none;
}
</style>