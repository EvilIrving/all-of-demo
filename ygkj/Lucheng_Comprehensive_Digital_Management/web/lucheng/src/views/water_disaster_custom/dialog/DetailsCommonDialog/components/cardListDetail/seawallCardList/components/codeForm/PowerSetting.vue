<!--
 * @Date: 2022-01-05 16:13:26
 * @Author:dtb 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-01-05 19:03:27
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\codeForm\PowerSetting.vue
-->
<template>
  <div class="power-setting">
    <el-table ref="multipleTable" :data="tableForm" height="418" stripe class="abc mt16">
      <el-table-column align="center" prop="index" label="序号" width="55">
        <template slot-scope="scope">
          <span class="table-order">{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="类别" prop="category" width="200" />
      <el-table-column align="center" label="权重" width="140">
        <template slot-scope="scope">
          <el-input-number size="mini" v-model="scope.row.score" label="描述文字"></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" label="权重说明" prop="type">
        <template slot-scope="scope">
          <span v-if="scope.row.sort == 1">
            <i class="el-icon-s-operation" @click="childConfig(scope.row,scope.row.sort)"></i>
            一类塘{{ scope.row.subWeight[0].score }}分；二类塘{{
               scope.row.subWeight[1].score
            }}分；三类塘{{  scope.row.subWeight[2].score }}分
          </span>
          <span v-if="scope.row.sort ==2">
            <i class="el-icon-s-operation" @click="childConfig(scope.row,scope.row.sort)"></i>
            <span v-for="v in scope.row.subWeight" :key="v.sort">{{v.category + ''+  v.score + '；'}}</span>
          </span>
          <span v-if="scope.row.sort == 3">
            <i class="el-icon-s-operation" @click="childConfig(scope.row,scope.row.sort)"></i>
            <span v-for="v in scope.row.subWeight" :key="v.sort">{{v.category +   v.score + '；'}}</span>
          </span>
          <span v-if="scope.row.sort == 4">
            <i class="el-icon-s-operation" @click="childConfig(scope.row,scope.row.sort)"></i>
            <span v-for="v in scope.row.subWeight" :key="v.sort">{{v.category +   v.score + '；'}}</span>
          </span>
          <span v-if="
              scope.row.sort == 5 ||
              scope.row.sort == 6 ||
              scope.row.sort == 8 ||
              scope.row.sort == 9 ||
              scope.row.sort == 7
            ">
            否 {{ scope.row.score }} 分；是 0 分
          </span>
        </template>
      </el-table-column>
    </el-table>
    <div class="explain-box">
      <span class="black">权重总得分{{ totalScore }}</span>
    </div>
    <div class="child-btn mt16">
      <span class="btn derive-sty " @click="sure">确定</span>
      <span class="btn" @click="close">取消</span>
    </div>
    <PrimaryDialog :visible.sync="visible" width="500px" :before-close="dialogClose" top="34vh" main-title="提醒" class="list-dialog system-dialog primary-dialog" :append-to-body="true" :close-on-click-modal="false">
      <div class="dialog-content">
        您配置的总分数为{{ totalScore }}分，超过/低于100分，请重新输入
      </div>
      <div class="child-btn mt16">
        <span class="btn" @click="visible = flase">取消</span>
        <span class="btn sure-btn" @click="visible = flase">确定</span>
      </div>
    </PrimaryDialog>

    <PrimaryDialog :visible.sync="childVisible" width="600px" :before-close="childDialogClose" top="25vh" main-title="子项权重配置" class="list-dialog system-dialog primary-dialog" :append-to-body="true" :close-on-click-modal="false">
      <div class="childConfig-content">
        <el-table ref="multipleTable" :data="childTableData" height="260" stripe class="abc mt16">
          <el-table-column prop="index" label="序号" width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="指标子项" prop="category" />
          <el-table-column label="权重" width="160">
            <template slot-scope="scope">
              <el-input-number size="mini" v-model="scope.row.score" label="描述文字"></el-input-number>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="explain-box">
        <span>提示：各项指标子项权重不得大于上级指标项权重</span>
      </div>
      <div class="child-btn mt16">
        <span class="btn" @click="childDialogClose">取消</span>
        <span class="btn sure-btn" @click="childConfigSave">确定</span>
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import { listWeight, saveWeight } from '@/api/seawall.js';
import { cloneObj } from "@/utils/utils";
import { PrimaryDialog } from '@/components';
export default {
  name: "PowerSetting",
  props: {
    info: Object,
  },
  data() {
    return {
      tableForm: {},
      visible: false,
      childVisible: false,
      childType: 1,
      childTableData: [],
    };
  },
  computed: {
    totalScore: function () {
      let total = 0
      this.tableForm.forEach(element => {
        total += element.score
      });
      return total;
    },
  },
  components: { PrimaryDialog },
  created() { },
  mounted() {
    this._getProCodeConfig();
  },
  methods: {
    async _getProCodeConfig() {
      let res = await listWeight(this.info.code);
      if (res.code == 0) {
        if (res.data) {
          this.tableForm = res.data;
        }
      }
    },
    async sure() {
      if (this.totalScore != 100) {
        this.visible = true;
      } else {
        this.tableForm.pjcd = this.info.code;
        let res = await saveWeight(this.tableForm);
        if (res.code == 0) {
          this.$message.success("保存成功！");
        } else {
          this.$message.error(res.message);
        }
      }
    },
    close() {
      this.$emit("close");
    },
    dialogClose() {
      this.visible = false;
    },
    //显示子项配置
    childConfig(row, sort) {
      this.childTableData = row.subWeight;
      this.childType = sort;
      this.childVisible = true;
    },
    //保存子项配置
    childConfigSave() {
      let flag = true;
      let childTotal = 0
      this.childTableData.forEach(element => {
        childTotal += element.score
      });
      if (childTotal > this.tableForm[this.childType - 1].score) {
        flag = false;
      }
      if (flag) {
        this.tableForm = cloneObj(this.childTableData);
        this.childVisible = false;
      } else {
        this.$message.error("各项指标子项权重不得大于上级指标项权重");
      }
    },
    //关闭子项配置
    childDialogClose() {
      this.childTableData = [];
      this.childVisible = false;
    },
  },
};
</script>
<style lang="scss" scoped>
@import './style.scss';
.dialog-content {
  height: 140px;
  padding: 22px 16px;
  color: #333;
}
.icon-evaluate-system-active {
  vertical-align: middle;
  cursor: pointer;
}
.btn {
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  float: right;
  color: #00baff;
  border: 1px solid #00baff;
  background: rgba(35, 157, 239, 0.12);
}
.derive-sty {
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  float: right;
  color: #fff;
  border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
  background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
}
.explain-box {
  width: 1256px;
  height: 44px;
  background: rgba(35, 157, 239, 0.12);
  @include flexbox();
  align-items: center;
  margin: 10px 0px 10px;
  color: #23e3fb;
  .black {
    width: 111px;
    height: 22px;
    font-size: 16px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #23e3fb;
    line-height: 22px;
    margin-left: 10px;
  }
}
</style>
<style>
.el-input-number__decrease {
  background: #082639;
  border-color: #00baff;
  border-left-color: #00baff;
  color: #ffffff;
}
.el-input-number__increase {
  background: #082639;
  border-color: #00baff;
  border-right-color: #00baff;
  color: #ffffff;
}
.el-icon-s-operation {
  font-size: 20px;
  color: #56fefe;
  cursor: pointer;
  /* color: linear-gradient(180deg, #56fefe 0%, #0639ff 100%); */
}
</style>