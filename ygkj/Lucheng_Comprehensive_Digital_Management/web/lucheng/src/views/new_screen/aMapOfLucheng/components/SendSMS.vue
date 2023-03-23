<!--
 * @Author: wqn
 * @Date: 2022-09-23 16:13:11
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-23 16:13:11
 * @Description: 发送短信弹窗内容
-->
<template>
  <div class="new_box">
    <div class="left">
      <div>
        <img src="@/assets/images/shortMessage_diamonds.png" alt="" />
        <span style="color: #fff">接收人</span>
      </div>
      <img src="@/assets/images/shortMessage_shortLine.png" alt="" />
      <el-input class="filterText" placeholder="输入关键字进行过滤" v-model="filterText"> </el-input>
      <el-tree class="filter-tree treeStyle" :data="personData" show-checkbox @check="handleCheckChange" node-key="id" :props="defaultProps" :filter-node-method="filterNode" ref="tree"></el-tree>
    </div>
    <div class="right">
      <div class="">
        <img src="@/assets/images/shortMessage_diamonds.png" alt="" />
        <span style="color: #fff">短信内容</span>
      </div>
      <img src="@/assets/images/shortMessage_longLine.png" alt="" />
      <div class="table-list-box">
        <el-form ref="form" :model="form" label-width="0">
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">选择模版</div>
              <div class="value-item">
                <el-form-item>
                  <el-select size="small" class="select-box" v-model="templateTypeValue" style="width: 100%" placeholder="请选择模版" @change="handleTemplateTypeChange">
                    <el-option v-for="item in templateType" :key="item.value" :label="item.name" :value="item.value"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 0">
            <div class="label-value-item">
              <div class="lable-item">选择时间</div>
              <div class="value-item">
                <el-form-item>
                  <el-date-picker v-model="selectTime" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间" @change="inputChange"></el-date-picker>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 1">
            <div class="label-value-item">
              <div class="lable-item">预期开始时间</div>
              <div class="value-item">
                <el-form-item>
                  <el-date-picker v-model="startTime" type="datetime" format="yyyy-MM-dd HH" value-format="yyyy-MM-dd HH" placeholder="选择开始时间" @change="inputChange">
                  </el-date-picker>
                </el-form-item>
              </div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">预期结束时间</div>
              <div class="value-item">
                <el-form-item>
                  <el-date-picker v-model="endTime" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择结束时间" @change="inputChange">
                  </el-date-picker>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 1">
            <div class="label-value-item">
              <div class="lable-item">面雨量（范围）</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="mylMinvalue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">小于</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="mylMaxValue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 1">
            <div class="label-value-item">
              <div class="lable-item">西部山区（范围）</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="xbMinvalue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">小于</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="xbMaxValue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 1">
            <div class="label-value-item">
              <div class="lable-item">局部可达</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="jbValue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 2">
            <div class="label-value-item">
              <div class="lable-item">应急响应</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="yjxyValue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">水旱灾害防御应急响应</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="shzhValue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item" v-if="templateTypeValue === 3">
            <div class="label-value-item">
              <div class="lable-item">启动时间</div>
              <div class="value-item">
                <div class="value-item">
                  <el-form-item>
                    <el-date-picker v-model="startTime" type="datetime" format="yyyy-MM-dd HH" value-format="yyyy-MM-dd HH" placeholder="选择开始时间" @change="inputChange">
                    </el-date-picker>
                  </el-form-item>
                </div>
              </div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">水旱灾害防御应急响应</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" v-model="shzhValue" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item" v-if="templateTypeValue === 0">
              <div class="lable-item lable-item-base">编辑内容</div>
              <div class="value-item">
                <el-form-item>
                  <el-input size="small" type="textarea" :autosize="{ minRows: 8 }" v-model="content" @change="inputChange"></el-input>
                </el-form-item>
              </div>
            </div>
            <div class="label-value-item" v-if="templateTypeValue != 0">
              <div class="lable-item lable-item-base">短信内容</div>
              <div class="value-item value-item-base">
                <el-form-item>
                  <p>{{ form.content }}</p>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">手机号(逗号隔开)</div>
              <div class="value-item">
                <el-input size="small" type="textarea" v-model="tel"></el-input>
              </div>
            </div>
          </div>
        </el-form>
      </div>
      <div class="sendMessage" @click="sendMessage">发送短信</div>
    </div>
  </div>
</template>

<script>
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
export default {
  name: '',
  props: {},
  components: {},
  data() {
    return {
      filterText: '',
      personData: [],
      defaultProps: {
        children: 'list',
        label: 'name'
      },
      form: {
        phones: '',
        content: ''
      },
      templateTypeValue: 0,
      templateType: [
        { name: '模版1', value: 0 },
        { name: '模版2', value: 1 },
        { name: '模版3', value: 2 },
        { name: '模版4', value: 3 }
      ],
      selectTime: '',
      startTime: '',
      endTime: '',
      mylMinvalue: '',
      mylMaxValue: '',
      xbMinvalue: '',
      xbMaxValue: '',
      jbValue: '',
      yjxyValue: '',
      shzhValue: '',
      content: '',
      tel: '',
      phoneData: [],
      phone: '',
      startDay: '',
      startdate: '',
      endDay: ''
    };
  },
  computed: {},
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getResponsibleListByType();
  },
  mounted() {},
  destroyed() {},
  methods: {
    getResponsibleListByType() {
      disasterPreventionApi.responsibleListByType().then((res) => {
        if (res.code == 0) {
          this.personData = res.data;
        }
      });
    },
    handleCheckChange(data, node) {
      this.phoneData = [];
      node.checkedNodes.map((item) => {
        this.phoneData.push(item.phone);
      });
      this.phone = this.phoneData.join(',');
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleTemplateTypeChange() {
      this.selectTime = '';
      this.content = '';
      this.startTime = '';
      this.startDay = '';
      this.startdate = '';
      this.endTime = '';
      this.endDay = '';
      this.mylMinvalue = '';
      this.mylMaxValue = '';
      this.xbMinvalue = '';
      this.xbMaxValue = '';
      this.jbValue = '';
      this.yjxyValue = '';
      this.shzhValue = '';
      this.tel = '';
      this.phone = '';
      this.form.phones = '';
      this.$refs.tree.setCheckedKeys([]);
      if (this.templateTypeValue === 0) {
        this.form.content = '';
      } else if (this.templateTypeValue === 1) {
        this.form.content =
          '预计我市{f0}日{f1}时到{f2}日有明显降雨，过程面雨量可达{f3}-{f4}毫米，西部山区{f5}-{f6}毫米，局部可达{f7}毫米以上，请注意山洪灾害防范。';
      } else if (this.templateTypeValue === 2) {
        this.form.content =
          '根据区防办启动{f0}级应急响应，我部启动{f1}级水旱灾害防御应急响应、做好山塘、水库、沿江水闸开闸预泄和山洪灾害防范工作。';
      } else {
        this.form.content =
          '决定于{f0}日{f1}时启动水旱灾害防御{f2}级应急响应，请水利工程和山洪灾害相关责任人根据预案要求做好台风防御工作。';
      }
    },
    inputChange() {
      if (this.startTime) {
        this.startDay = this.startTime.substring(8, 10);
        this.startdate = this.startTime.substring(this.startTime.length - 2, this.startTime.length);
      }
      if (this.endTime) {
        this.endDay = this.endTime.substring(this.endTime.length - 2, this.endTime.length);
      }
      if (this.templateTypeValue === 0) {
        let testing = this.content.substring(this.content.length - 1, this.content.length);
        if (testing !== '。') {
          this.content = this.content + '。';
        }
        this.form.content = `时间${this.selectTime ? this.selectTime : new Date().format('yyyy-MM-dd')}，${
          this.content ? this.content : ''
        }`;
      } else if (this.templateTypeValue === 1) {
        this.form.content = `预计我市${this.startDay ? this.startDay : '{f0}'}日${
          this.startdate ? this.startdate : '{f1}'
        }时到${this.endDay ? this.endDay : '{f2}'}日有明显降雨，过程面雨量可达${
          this.mylMinvalue ? this.mylMinvalue : '{f3}'
        }-${this.mylMaxValue ? this.mylMaxValue : '{f4}'}毫米，西部山区${this.xbMinvalue ? this.xbMinvalue : '{f5}'}-${
          this.xbMaxValue ? this.xbMaxValue : '{f6}'
        }毫米，局部可达${this.jbValue ? this.jbValue : '{f7}'}毫米以上，请注意山洪灾害防范。`;
      } else if (this.templateTypeValue === 2) {
        this.form.content = `根据区防办启动${this.yjxyValue ? this.yjxyValue : '{f0}'}级应急响应，我部启动${
          this.shzhValue ? this.shzhValue : '{f1}'
        }级水旱灾害防御应急响应、做好山塘、水库、沿江水闸开闸预泄和山洪灾害防范工作。`;
      } else {
        this.form.content = `决定于${this.startDay ? this.startDay : '{f0}'}日${
          this.startdate ? this.startdate : '{f1}'
        }时启动水旱灾害防御${
          this.shzhValue ? this.shzhValue : '{f0}'
        }级应急响应，请水利工程和山洪灾害相关责任人根据预案要求做好台风防御工作。`;
      }
    },
    sendMessage() {
      if (this.tel) {
        this.form.phones = this.phone + ',' + this.tel;
      } else {
        this.form.phones = this.phone;
      }
      // if (this.submitType) {
      //   return false;
      // }
      // this.submitType = true;
      disasterPreventionApi
        .sendTfMsg(this.form)
        .then((res) => {
          if (res.code == 0) {
            this.$message.success('发送成功');
            // this.submitType = false;
            // this.handleClose();
            this.$emit('handleClose');
            // this.getList();
          } else {
            this.$message.error(res.msg);
            // this.submitType = false;
          }
        })
        .catch((err) => {
          this.$message.error(err.msg);
          // this.submitType = false;
        });
    }
    // handleClose() {
    //   this.selectTime = '';
    //   this.content = '';
    //   this.startTime = '';
    //   this.startDay = '';
    //   this.startdate = '';
    //   this.endTime = '';
    //   this.endDay = '';
    //   this.mylMinvalue = '';
    //   this.mylMaxValue = '';
    //   this.xbMinvalue = '';
    //   this.xbMaxValue = '';
    //   this.jbValue = '';
    //   this.yjxyValue = '';
    //   this.shzhValue = '';
    //   this.tel = '';
    //   this.phone = '';
    //   this.form.phones = '';
    //   this.templateTypeValue = 0;
    //   this.form.content = '';
    // }
  }
};
</script>
<style lang='scss' scoped>
.new_box {
  width: 100%;
  height: 600px;
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
  }
  .filterText {
    margin: 22px 0 16px 0;
  }
  .treeStyle {
    height: calc(100% - 140px);
    overflow: auto;
    background: transparent;
    color: #ffffff;
    ::v-deep .el-tree-node__content:hover {
      background-image: linear-gradient(90deg, rgba(0, 196, 255, 0.3), rgba(41, 147, 180, 0.1));
      color: #00baff;
    }
    ::v-deep .el-tree-node:focus > .el-tree-node__content {
      background-image: linear-gradient(90deg, rgba(0, 196, 255, 0.3), rgba(41, 147, 180, 0.1));
      color: #00baff;
    }
  }
  ::v-deep .el-form-item {
    margin-bottom: 0;
    p {
      width: 769px;
    }
  }
  ::v-deep .el-textarea__inner {
    border: 0;
    border-radius: 0;
    background-color: transparent;
    color: #fff;
  }
  .table-list-box {
    border: 1px solid rgba(0, 186, 255, 0.6);
    margin-top: 22px;
    margin-bottom: 25px;
    .table-list-item {
      display: flex;
      border-bottom: 1px solid rgba(0, 186, 255, 0.6);
      .label-value-item {
        flex: 1;
        display: flex;
        border-right: 1px solid rgba(0, 186, 255, 0.6);
        .lable-item {
          width: 160px;
          color: #00baff;
          background: linear-gradient(90deg, rgba(0, 122, 255, 0.3) 0%, rgba(0, 122, 255, 0) 100%);
          text-align: center;
          padding: 14px 0;
          border-right: 1px solid rgba(0, 186, 255, 0.6);
        }
        .lable-item-base {
          height: 176px;
          line-height: 148px;
        }
        .value-item {
          width: calc(100% - 161px);
          // padding: 14px 16px;
          box-sizing: border-box;
          color: #fff;
          &.color_2 {
            color: #fcee27;
          }
          &.color_3 {
            color: orange;
          }
          &.color_4 {
            color: #ff0000;
          }
        }
        .value-item-base {
          padding: 14px 16px;
        }
        &:last-child {
          border-right: none;
        }
      }
      &:last-child {
        border-bottom: none;
      }
    }
  }
  .sendMessage {
    width: 122px;
    height: 40px;
    background: linear-gradient(180deg, #23e3fb 0%, #239def 100%);
    border-radius: 4px;
    border: 1px solid #00baff;
    line-height: 40px;
    font-weight: 400;
    font-size: 16px;
    text-align: center;
    color: #ffffff;
    float: right;
  }
}
</style>