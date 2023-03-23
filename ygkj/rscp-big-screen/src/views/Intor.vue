<!--
 * @Author: hanyu
 * @LastEditTime: 2023-01-08 14:34:50
 * @Description: 进入大屏过渡页面
 * @FilePath: \rscp-big-screen\src\views\Intor.vue
-->
<template>
  <div class="loading_bg">
    <p>正在加载中...</p>
  </div>
</template>

<script>
import { getUserInfoApi, getUserJlzsPermissionApi } from "@/api/api_utils";
import { getQueryString } from '@/utils/utils'
export default {
  name: "Intor",
  props: {},
  components: {},
  data() {
    return {
      phone: ''
    };
  },
  created() {
    this.getUserInfo();
    this.getUserJlzsPermission();
  },
  methods: {
    async getUserInfo() {
      // this.$message.success('intor getUserInfo')
    // 好的
      let res = await getUserInfoApi({
        token: this.$localData("get", "token"),
      });
      if (res.success) {
        // 调整权限 注释
        // res.user.adcd = res.user.adcd.substr(0, 4) + "00000000";
        this.$localData("set", "userInfo", res.user);
        this.$store.commit("SET_LEFT_SELECT_ADNM", res.user.adnm);
        let type = this.$route.query.type
        if (type) {
          let hasTop = getQueryString('hasTop')
          let whiteLeft = getQueryString('whiteLeft')//白色风格是否显示左侧
          //驾驶舱 7个大屏
          switch (type) {
            case 'seawall':
              this.$router.push("/seawallScreen");
              break;
            case 'reservoir':
              this.$router.push("/reservoirScreen");
              break;
            case 'basin':
              this.$router.push("/basinScreen");
              break;
            case 'dike':
              this.$router.push("/dikeScreen");
              break;
            case 'sluice':
              this.$router.push("/sluiceScreen");
              break;
            case 'pump':
              this.$router.push("/pumpScreen");
              break;
            case 'gate':
              this.$router.push("/gateScreen");
              break;


            //拓展大屏
            case 'reservoirSimple':
              this.$router.push("/reservoirScreenSimple");
              break;
            case 'reservoirQtj':
              this.$router.push("/reservoirScreenQtj");
              break;
            // 白色风格 六个工程
            case 'seawallSimpleV2':
            case 'reservoirSimpleV2':
            case 'dikeSimpleV2':
            case 'sluiceSimpleV2':
            case 'pumpSimpleV2':
            case 'gateSimpleV2':
              this.$router.push("/" + type + "?whiteLeft=" + whiteLeft);
              break;
            // 黑色风格 六个工程
            case 'seawallBlack':
            case 'reservoirBlack':
            case 'dikeBlack':
            case 'gateBlack':
            case 'sluiceBlack':
            case 'pumpBlack':
              this.$router.push("/" + type);
              break;
            // 水工智管 第一次4个大屏 2021-12-16
            // 健康在线
            case 'healthyOnline':
            // 蓄供分析
            case 'storageSupply':
            // 防潮研判
            case 'tideAnalysis':
            // 智慧运用
            case 'intelligenceUse':
              this.$router.push("/" + type);
              break;
            // 水工智管 第二次6个大屏 2021-12-23
            // 水工智管驾驶舱
            case 'sgzgDrive':
            case 'zhekuHousehold': // 浙库户籍
            case 'zhekuHealthy': // 浙库健康
            case 'reservoirStorage': // 水库蓄供
            case 'reservoirControl': // 智慧控运
            case 'repairProtect': // 精准维养
            case 'reservoirBenefit': // 浙库效益
              this.$router.push("/" + type + " ?hasTop=" + hasTop);
              break;
            case 'menu': // 默认驾驶舱
            case 'zhMenu': // 智管 工程大屏
            case 'skzgScreen': // 智管驾驶舱
            case 'skzgDikeScreen':
            case 'skzgSluiceScreen':
            case 'skzgPumpScreen':
            case 'skzgGateScreen':
            case 'skzgSeawallScreen':
            case 'zhSeawallScreen': // 省厅海塘防潮汇报 智慧海塘
            case 'zhSeawall': // 省厅海塘防潮汇报 智能模块
            case 'BlueSeawall': // 省厅海塘防潮汇报 废弃
            case 'intelligentModule': // 水库安全风险管控智能模块
              this.$router.push("/" + type);
              break;
            default:
              this.$router.push("/menu");
              break;
          }
        } else {
          this.$router.push("/menu");
        }
      }
    },
    async getUserJlzsPermission() {
      let res = await getUserJlzsPermissionApi()
      if (res.success) {
        this.$localData("set", "permission", res.data)
      } else {
        this.$localData("set", "permission", [])
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.loading_bg {
  width: 100%;
  height: 100%;
  background: url("../assets/images/loading_bg.png") no-repeat center;
  background-size: cover;
  position: relative;
  p {
    position: absolute;
    bottom: 200px;
    width: 100%;
    text-align: center;
    color: #fff;
    font-size: 22px;
  }
}
</style>
