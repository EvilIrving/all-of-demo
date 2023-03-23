<template>
  <div class="form_wrap">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="断面名称：" prop="name">
            <!-- <el-select v-model="form.name" placeholder="请选择" style="width: 300px;">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.label">
              </el-option>
            </el-select> -->
            <el-input
              v-model="form.name"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="风区长度：" prop="fetchLength">
            <el-input
              v-model="form.fetchLength"
              placeholder="请输入"
              style="width: 300px"
              :step="0.1"
            >
              <template slot="append">m</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="堤前平均水深：" prop="averageDepth">
            <el-input
              v-model="form.averageDepth"
              placeholder="请输入"
              style="width: 300px"
            >
              <template slot="append">m</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="风向：" prop="windDirection">
            <el-input
              v-model="form.windDirection"
              placeholder="请输入"
              style="width: 300px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="风速：" prop="rate">
            <el-input
              v-model="speed"
              placeholder="请输入"
              style="width: 300px"
              disabled
            >
              <template slot="append">m</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="天文潮数值：" prop="projectLeader">
            <el-input
              v-model="astrotd"
              placeholder="请输入"
              style="width: 300px"
              disabled
            >
              <template slot="append">m</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="堤顶高程：" prop="crestElevatio">
            <el-input
              v-model="form.crestElevatio"
              placeholder="请输入"
              style="width: 300px"
            >
              <template slot="append">m</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="工程名称：" prop="prcd">
            <el-select
              v-model="form.prcd"
              placeholder="请选择"
              style="width: 300px"
            >
              <el-option
                v-for="item in tideOption"
                :key="item.stCode"
                :label="item.label"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="经度：" prop="longitude">
            <el-input
              type="number"
              v-model="form.longitude"
              placeholder="请输入经度"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="纬度：" prop="latitude">
            <el-input
              type="number"
              v-model="form.latitude"
              placeholder="请输入纬度"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="22">
          <el-form-item label="地图选点：" prop="phone">
            <img
              src="../../../../assets/images/mapsearch.png"
              style="cursor: pointer"
              @click="mapSelectPoint()"
              alt=""
            />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeEditDialog');
            }
          "
          >取消</el-button
        >
        <el-button type="primary" @click="submitForm('form')">保存</el-button>
      </div>
    </el-form>

    <el-dialog
      title="地图选点"
      :visible.sync="showMapDialog"
      :close-on-click-modal="false"
      width="800px"
      append-to-body
    >
      <map-select-point
        v-if="showMapSelect"
        :pointLng="form.longitude"
        :pointLat="form.latitude"
        @closeMapSelectDialog="closeMapSelectDialog"
        @selectPoint="selectPoint"
      ></map-select-point>
    </el-dialog>
  </div>
</template>

<script>
import {
  stormSurgeWarningApi,
  getTideTree,
  seawallListApi,
} from "@/api/seawallPrevent";
import mapSelectPoint from "@/components/mapSelectPoint";
export default {
  name: "configurationDetail",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: {
    mapSelectPoint,
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
    this.getSpeed();
    this.getTideList();
  },
  data() {
    return {
      form: {
        name: "",
        fetchLength: "",
        averageDepth: "",
        windDirection: "",
        crestElevatio: "",
        prcd: "",
      },
      rules: {
        prcd: [
          { required: true, message: "请选择工程", trigger: "change" },
        ],
        
      },
      speed: "",
      astrotd: "",
      showMapDialog: false,
      showMapSelect: true,
      submitFlag: true,
      options: [
        {
          value: "1",
          label: "北堤J0+146",
        },
        {
          value: "2",
          label: "北堤J0+598",
        },
        {
          value: "3",
          label: "东堤J0+950",
        },
        {
          value: "4",
          label: "东堤J0+950",
        },
        {
          value: "5",
          label: "东堤J0+950",
        },
        {
          value: "6",
          label: "南堤J0+042",
        },
        {
          value: "7",
          label: "南堤J0+242",
        },
      ],
      stationName: "",
      tideOption: [],
    };
  },
  methods: {
    // 获取潮位站列表
    async getTideList() {
      let res = await seawallListApi.list({});
      console.log(res, 111111);
      for (var [index, item] of res.data.list.entries()) {
        this.tideOption.push({
          label: item.seawallName,
          value: index,
          id: item.seawallCode,
        });
      }
      console.log(this.tideOption);
    },

    mapSelectPoint() {
      this.showMapDialog = true;
      this.showMapSelect = false;
      this.$nextTick(() => {
        this.showMapSelect = true;
      });
    },
    // 地图选点 获取经纬度
    selectPoint(lng, lat) {
      this.showMapDialog = false;
      this.showMapSelect = false;
      this.form.longitude = lng;
      this.form.latitude = lat;
    },
    // 关闭地图弹框
    closeMapSelectDialog() {
      this.showMapDialog = false;
      this.showMapSelect = false;
    },
    async getDetail() {
      let res = await stormSurgeWarningApi.getSectionList({
        id: this.id,
      });
      Object.assign(this.form, res.data.list[0]);
    },
    async getSpeed() {
      let res = await stormSurgeWarningApi.getAcquisition({});
      console.log(res);
      this.speed = res.data.speed;
      this.astrotd = res.data.astrotd;
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.creationTime;
          delete this.form.paged;
          // this.form['prcd'] = this.projectInfo.prcd
          stormSurgeWarningApi
            .editSectionList(this.form)
            .then((res) => {
              console.log(res, 11111);
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                //this.$message.error(res.msg);
                console.log(res.msg);
                this.$message.error({ message: res.msg || "新增失败" });
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
  },
};
</script>

<style scoped lang="scss">
.form_wrap {
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
    margin-right: 50px;
  }
}
</style>