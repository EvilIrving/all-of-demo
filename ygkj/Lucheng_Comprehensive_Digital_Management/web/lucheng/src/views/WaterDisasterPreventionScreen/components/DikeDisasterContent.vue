<template>
  <div class="dike-disaster-content map-dialog">
    <div class="base-table">
      <!-- 堤防保险点弹框内容 -->
      <div class="hidden-danger">
        <div class="collapse-list">
          <el-collapse v-model="activeModel" @change="handleChange">
            <el-collapse-item name="1">
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i> 
                  <span>发起(2019-10-25 11:31:18)</span>
                </div>
              </template>
              <div>
                <table style="width:100%">
                  <tr>
                    <td class="prop-td">合同名称</td>
                    <td>泰顺堤防灾害保险服务协议</td>
                  </tr>
                  <tr>
                    <td class="prop-td">位置</td>
                    <td>27.6,120.08</td>
                  </tr>
                  <tr>
                    <td class="prop-td">堤防名称</td>
                    <td>大壤坑防洪堤</td>
                  </tr>
                  <tr>
                    <td class="prop-td">文件</td>
                    <td>文件名称.docx</td>
                  </tr>
                  <tr>
                    <td class="prop-td">理赔描述</td>
                    <td>理赔描述理赔描述理赔描述理赔描述理赔描述理赔描述理赔描述理赔描述理赔描述</td>
                  </tr>
                  <tr>
                    <td class="prop-td">图片</td>
                    <td>
                      <el-image 
                        style="width: 100px; height: 100px"
                        src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg" 
                        :preview-src-list="['https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg']">
                      </el-image>
                    </td>
                  </tr>
                </table>
              </div>
            </el-collapse-item>
            <el-collapse-item name="2">
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i> 
                  <span>堪核(-)</span>
                </div>
              </template>
              <div>
                <table style="width:100%">
                  <tr>
                    <td class="prop-td">图片</td>
                    <td>
                      <el-image 
                        style="width: 100px; height: 100px"
                        src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg" 
                        :preview-src-list="['https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg']">
                      </el-image>
                    </td>
                  </tr>
                  <tr>
                    <td class="prop-td">视频</td>
                    <td></td>
                  </tr>
                  <tr>
                    <td class="prop-td">音频</td>
                    <td></td>
                  </tr>
                  <tr>
                    <td class="prop-td">堪核意见</td>
                    <td>经现场查勘，司前镇台边村防洪堤4.9米损毁需要修复。</td>
                  </tr>
                </table>
              </div>
            </el-collapse-item>
            <el-collapse-item name="3">
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i> 
                  <span>主管部门确认(-)</span>
                </div>
              </template>
              <div>
                <table style="width:100%">
                  <tr>
                    <td class="prop-td">确认意见</td>
                    <td></td>
                  </tr>
                </table>
              </div>
            </el-collapse-item>
            <el-collapse-item name="4">
              <template #title>
                <div class="collapse-list-title">
                  <i class="icon-collapse-title"></i> 
                  <span>已理赔(-)</span>
                </div>
              </template>
              <div>
                <table style="width:100%">
                  <tr>
                    <td class="prop-td">确认意见</td>
                    <td></td>
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
export default {
  name: "RiskPointContent",
  props: {

  },
  data() {
    return {
      activeModel:['1']
    };
  },
  computed: {
    ...mapGetters([

    ])
  },
  components: {
  },
  watch: {},
  methods: {
    handleChange(val){
      console.log(val);
    }
  },
};
</script>

<style lang="scss" scoped>
.dike-disaster-content {
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
        }
      }
    }
  }
  .collapse-list{
    margin-top: 20px;
    .collapse-list-title{
      // background: rgba(7,157,254,0.10);
      // border-radius: 2px;
      width: 100%;
      i{
        width: 26px;
        height: 26px;
        vertical-align: middle;
      }
      span{
        display: inline-block;
        margin-left: 8px;
        vertical-align: middle;
      }
    }
    ::v-deep .el-collapse{
      border-top: none;
      border-bottom: none;
      .el-collapse-item__header{
        background: rgba(0,0,0,0);
        color: #fff;
        font-size: 18px;
        letter-spacing: 0;
        line-height: 24px;
        border-bottom: none;
        background: rgba(7,157,254,0.10);
        border-radius: 2px;
        margin-bottom: 12px;
      }
      .el-collapse-item__wrap{
        background: rgba(0,0,0,0);
        border-bottom: none;
      }
    }
  }
}
</style>
