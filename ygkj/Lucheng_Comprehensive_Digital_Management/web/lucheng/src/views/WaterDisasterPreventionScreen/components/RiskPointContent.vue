<template>
  <div class="risk-point-content map-dialog">
    <div class="base-table">
      <!-- 高风险点 薄弱点-->
      <div
        v-if="pointData.sbSign == '252' || pointData.sbSign == '253'"
        class="risk-point"
      >
        <table>
          <tr>
            <td class="prop-td">名称</td>
            <td>{{ detailInfo.projName }}</td>
            <td class="prop-td">工程类型</td>
            <td>{{ detailInfo.projType }}</td>
          </tr>
          <tr>
            <td class="prop-td">发现渠道</td>
            <td>
              {{
                detailInfo.property == "xjjc"
                  ? "县级检查"
                  : detailInfo.property == "zczc"
                  ? "工程自查"
                  : detailInfo.property == "sjcc"
                  ? "市级抽查"
                  : detailInfo.property == "sjdc"
                  ? "省级督查"
                  : "-"
              }}
            </td>
            <td class="prop-td">流域</td>
            <td>{{ detailInfo.riverBasin }}</td>
          </tr>
          <tr>
            <td class="prop-td">行政区划</td>
            <td colspan="3">{{ detailInfo.adminDivision }}</td>
          </tr>
          <tr>
            <td class="prop-td">位置描述</td>
            <td colspan="3">{{ detailInfo.address }}</td>
          </tr>
          <tr>
            <td class="prop-td">上报人员</td>
            <td>{{ detailInfo.reportPerson }}</td>
            <td class="prop-td">审核人员</td>
            <td>{{ detailInfo.checkPerson }}</td>
          </tr>
          <tr>
            <td class="prop-td">发现时间</td>
            <td>{{ detailInfo.reportTime }}</td>
            <td class="prop-td">风险类型</td>
            <td>{{ detailInfo.sbSign == "252" ? "高风险点" : "风险点" }}</td>
          </tr>
          <tr>
            <td class="prop-td">整改情况</td>
            <td>{{ detailInfo.recCondition }}</td>
            <td class="prop-td">风险标示</td>
            <td>
              {{
                detailInfo.colorGrade == "1"
                  ? "红"
                  : detailInfo.colorGrade == "2"
                  ? "橙"
                  : detailInfo.colorGrade == "3"
                  ? "黄"
                  : "-"
              }}
            </td>
          </tr>
          <tr>
            <td class="prop-td">风险描述</td>
            <td colspan="3">
              {{ detailInfo.dangerDes }}
            </td>
          </tr>
          <tr>
            <td class="prop-td">管控措施</td>
            <td colspan="3">{{ detailInfo.riskPlan }}</td>
          </tr>
          <tr>
            <td class="prop-td">整改责任单位</td>
            <td>{{ detailInfo.dutyUnit }}</td>
            <td class="prop-td">责任人</td>
            <td>{{ detailInfo.dutyPerson + " " + detailInfo.dutyPhone }}</td>
          </tr>
          <tr>
            <td class="prop-td">文件</td>
            <td colspan="3">
              <div
                v-if="
                  detailInfo.fileDetails && detailInfo.fileDetails.length > 0
                "
              >
                <a
                  target="_blank"
                  :href="item.absolutePath"
                  :key="item.fileName"
                  v-for="item in detailInfo.fileDetails"
                  >{{ item.fileName }}</a
                >
              </div>
            </td>
          </tr>
          <tr>
            <td class="prop-td">图片/无图说明</td>
            <td colspan="3">{{ detailInfo.cancelPicText }}</td>
          </tr>
          <tr>
            <td class="prop-td">整改完成时间</td>
            <td>{{ detailInfo.recCompleteDate }}</td>
            <td class="prop-td">成果描述</td>
            <td>{{ detailInfo.recCompleteDescribe }}</td>
          </tr>
          <tr>
            <td class="prop-td">销号上传文件</td>
            <td colspan="3">
              <div
                v-if="
                  detailInfo.numberUploadFiles &&
                  detailInfo.numberUploadFiles.length > 0
                "
              >
                <a
                  target="_blank"
                  :href="item.absolutePath"
                  :key="item.fileName"
                  v-for="item in detailInfo.numberUploadFiles"
                  >{{ item.fileName }}</a
                >
              </div>
            </td>
          </tr>
          <tr>
            <td class="prop-td">销号上传图片</td>
            <td colspan="3">
              <template v-for="(item, index) in detailInfo.numberUploadImages">
                <el-image
                  v-if="detailInfo.numberUploadImages"
                  :key="index"
                  style="width: 100px; height: 100px"
                  :src="item.absolutePath"
                  :preview-src-list="[item.absolutePath]"
                >
                </el-image>
              </template>
            </td>
          </tr>
        </table>
      </div>

      <!-- 隐患点 -->
      <div v-else class="hidden-danger">
        <table style="width: 100%">
          <tr>
            <td class="prop-td">名称</td>
            <td>{{ detailInfo.projName }}</td>
            <td class="prop-td">工程类型</td>
            <td>{{ detailInfo.projType }}</td>
          </tr>
          <tr>
            <td class="prop-td">发现渠道</td>
            <td>
              {{
                detailInfo.property == "xjjc"
                  ? "县级检查"
                  : detailInfo.property == "zczc"
                  ? "工程自查"
                  : detailInfo.property == "sjcc"
                  ? "市级抽查"
                  : detailInfo.property == "sjdc"
                  ? "省级督查"
                  : "-"
              }}
            </td>
            <td class="prop-td">流域</td>
            <td>{{ detailInfo.riverBasin }}</td>
          </tr>
          <tr>
            <td class="prop-td">行政区划</td>
            <td colspan="3">{{ detailInfo.adminDivision }}</td>
          </tr>
          <tr>
            <td class="prop-td">位置描述</td>
            <td colspan="3">{{ detailInfo.address }}</td>
          </tr>
          <tr>
            <td class="prop-td">主要工程量</td>
            <td colspan="3">{{ detailInfo.quantities }}</td>
          </tr>
          <tr>
            <td class="prop-td">上报人员</td>
            <td>{{ detailInfo.reportPerson }}</td>
            <td class="prop-td">审核人员</td>
            <td>{{ detailInfo.checkPerson }}</td>
          </tr>
          <tr>
            <td class="prop-td">发现时间</td>
            <td>{{ detailInfo.reportTime }}</td>
            <td class="prop-td">风险类型</td>
            <td>{{ detailInfo.sbSign == "252" ? "高风险点" : "隐患点" }}</td>
          </tr>
          <tr>
            <td class="prop-td">整改情况</td>
            <td>{{ detailInfo.recCondition }}</td>
            <td class="prop-td">隐患类型</td>
            <td>{{ detailInfo.dangerType }}</td>
          </tr>
          <tr>
            <td class="prop-td">风险描述</td>
            <td colspan="3">
              {{ detailInfo.dangerDes }}
            </td>
          </tr>
          <tr>
            <td class="prop-td">整改责任单位</td>
            <td>{{ detailInfo.dutyUnit }}</td>
            <td class="prop-td">(整改)责任人</td>
            <td>{{ detailInfo.dutyPerson + " " + detailInfo.dutyPhone }}</td>
          </tr>
          <tr>
            <td class="prop-td">主管/监管单位</td>
            <td>{{ detailInfo.regulators }}</td>
            <td class="prop-td">(主管/监管)责任人</td>
            <td>{{ detailInfo.regulPerson + " " + detailInfo.regulPhone }}</td>
          </tr>
          <tr>
            <td class="prop-td">文件</td>
            <td colspan="3">
              <div v-if="detailInfo.fileInfoList.length > 0">
                <a
                  target="_blank"
                  :href="item.absolutePath"
                  :key="item.fileName"
                  v-for="item in detailInfo.fileInfoList"
                  >{{ item.fileName }}</a
                >
              </div>
              <span v-else>无文件</span>
            </td>
          </tr>
          <tr>
            <td class="prop-td">图片/无图说明</td>
            <td colspan="3">
              {{ detailInfo.sbpictext }}
            </td>
          </tr>
        </table>
        <div class="collapse-list">
          <el-collapse v-model="activeModel" @change="handleChange">
            <el-collapse-item name="1" v-if="detailInfo.makeRectificationPlan">
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i>
                  <span>制定整改计划</span>
                </div>
              </template>
              <div>
                <table style="width: 100%">
                  <tr>
                    <td class="prop-td">计划完成期限</td>
                    <td colspan="3">
                      {{ detailInfo.makeRectificationPlan.plannedCompleteDate }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">计划描述</td>
                    <td colspan="3">
                      {{ detailInfo.makeRectificationPlan.recPlanDescribe }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">计划修编预案方案</td>
                    <td>
                      {{ detailInfo.makeRectificationPlan.planRevisionPlan }}
                    </td>
                    <td class="prop-td">整改情况</td>
                    <td>{{ detailInfo.makeRectificationPlan.recCondition }}</td>
                  </tr>
                  <tr>
                    <td class="prop-td">管控措施</td>
                    <td colspan="3">
                      {{
                        detailInfo.makeRectificationPlan.recSuperviseMeasures
                      }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">落实情况</td>
                    <td colspan="3">
                      {{
                        detailInfo.makeRectificationPlan.recImplementSituation
                      }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">相关图片</td>
                    <td colspan="3">
                      <el-image
                        v-for="(item, index) in detailInfo.makeRectificationPlan
                          .imageList"
                        :key="index"
                        style="width: 100px; height: 100px"
                        :src="item.absolutePath"
                        :preview-src-list="[item.absolutePath]"
                      >
                      </el-image>
                    </td>
                  </tr>
                </table>
              </div>
            </el-collapse-item>
            <el-collapse-item name="2" v-if="detailInfo.rectificationNodeResp">
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i>
                  <span>整改节点</span>
                </div>
              </template>
              <div>
                <table style="width: 100%">
                  <tr>
                    <td class="prop-td">节点名称</td>
                    <td colspan="3">
                      {{ detailInfo.rectificationNodeResp.name }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">整改内容</td>
                    <td colspan="3">
                      {{ detailInfo.rectificationNodeResp.content }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">计划完成时间</td>
                    <td>
                      {{ detailInfo.rectificationNodeResp.plannedCompleteDate }}
                    </td>
                    <td class="prop-td">完成时间</td>
                    <td>{{ detailInfo.rectificationNodeResp.completeDate }}</td>
                  </tr>
                  <tr>
                    <td class="prop-td">相关文件</td>
                    <td colspan="3">
                      <div
                        v-if="
                          detailInfo.rectificationNodeResp.fileList.length > 0
                        "
                      >
                        <a
                          target="_blank"
                          :href="item.absolutePath"
                          :key="item.fileName"
                          v-for="item in detailInfo.rectificationNodeResp
                            .fileList"
                          >{{ item.fileName }}</a
                        >
                      </div>
                      <span v-else>无文件</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">相关图片</td>
                    <td colspan="3">
                      <el-image
                        v-for="(item, index) in detailInfo.rectificationNodeResp
                          .imageList"
                        :key="index"
                        style="width: 100px; height: 100px"
                        :src="item.absolutePath"
                        :preview-src-list="[item.absolutePath]"
                      >
                      </el-image>
                    </td>
                  </tr>
                </table>
              </div>
            </el-collapse-item>
            <el-collapse-item
              name="3"
              v-if="detailInfo.rectificationCompletedResp"
            >
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i>
                  <span>整改完成</span>
                </div>
              </template>
              <div>
                <table style="width: 100%">
                  <tr>
                    <td class="prop-td">完成时间</td>
                    <td colspan="3">
                      {{ detailInfo.rectificationCompletedResp.completeDate }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">成果描述</td>
                    <td colspan="3">
                      {{
                        detailInfo.rectificationCompletedResp
                          .recCompleteDescribe
                      }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">首轮审核人</td>
                    <td colspan="3">
                      {{
                        detailInfo.rectificationCompletedResp.firstCheckPerson
                      }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">最终审核人</td>
                    <td colspan="3">
                      {{
                        detailInfo.rectificationCompletedResp.finalCheckPerson
                      }}
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">相关图片</td>
                    <td colspan="3">
                      <el-image
                        v-for="(item, index) in detailInfo
                          .rectificationCompletedResp.imageList"
                        :key="index"
                        style="width: 100px; height: 100px"
                        :src="item.absolutePath"
                        :preview-src-list="[item.absolutePath]"
                      >
                      </el-image>
                    </td>
                  </tr>
                </table>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { disasterPreventionApi } from "@/api/RightSildePanel/index";
import $ from "jquery";
export default {
  name: "RiskPointContent",
  props: {
    pointData: {
      type: Object,
    },
  },
  data() {
    return {
      activeModel: ["1"],
      detailInfo: {},
    };
  },
  computed: {
    ...mapGetters([]),
  },
  created() {
    if (this.pointData.sbSign == "251") {
      this.findDetailsHiddenDangerBySbId();
    } else {
      this.findDetailsHighriskBySbId();
    }
  },
  components: {},
  watch: {},
  methods: {
    init() {
      if (this.pointData.sbSign == "251") {
        this.findDetailsHiddenDangerBySbId();
      } else {
        this.findDetailsHighriskBySbId();
      }
    },
    handleChange(val) {
    },
    /**
     * 根据sbId查询隐患点详情
     */
    async findDetailsHiddenDangerBySbId() {
      let opt = {
        sbId: this.pointData.code,
      };
      let res = await disasterPreventionApi.findDetailsHiddenDangerBySbId(opt);
      if (res.code == 0) {
        this.detailInfo = res.data;
      }
    },
    /**
     * 根据sbId查询高风险点/薄弱点详情
     */
    async findDetailsHighriskBySbId() {
      // console.log(this.pointData);
      let opt = {
        sbId: this.pointData.code,
      };
      let res = await disasterPreventionApi.findDetailsHighriskBySbId(opt);
      if (res.code == 0) {
        this.detailInfo = res.data;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.risk-point-content {
  height: 100%;
  width: 100%;
  height: 600px;
  .base-table {
    height: 100%;
    overflow-y: auto;
    table {
      tr {
        td {
          font-size: 16px;
          padding: 12px;
          color: #fff;
          line-height: 20px;
          border: 1px solid rgba(0, 193, 255, 0.4);
          &.prop-td {
            text-align: center;
            width: 160px;
            color: #56fefe;
            background-image: linear-gradient(
              90deg,
              rgba(44, 158, 191, 0.3) 3%,
              rgba(41, 147, 180, 0.1) 100%
            );
            vertical-align: middle;
          }
          a {
            display: inline-block;
            vertical-align: middle;
            font-size: 16px;
            color: #56fefe;
            margin-right: 10px;
          }
        }
      }
    }
  }
  .collapse-list {
    margin-top: 20px;
    .collapse-list-title {
      // background: rgba(7,157,254,0.10);
      // border-radius: 2px;
      width: 100%;
      i {
        width: 26px;
        height: 26px;
        vertical-align: middle;
      }
      span {
        display: inline-block;
        margin-left: 8px;
        vertical-align: middle;
      }
    }
    ::v-deep .el-collapse {
      border-top: none;
      border-bottom: none;
      .el-collapse-item__header {
        background: rgba(0, 0, 0, 0);
        color: #fff;
        font-size: 18px;
        letter-spacing: 0;
        line-height: 24px;
        border-bottom: none;
        background: rgba(7, 157, 254, 0.1);
        border-radius: 2px;
        margin-bottom: 12px;
      }
      .el-collapse-item__wrap {
        background: rgba(0, 0, 0, 0);
        border-bottom: none;
      }
    }
  }
}
</style>
