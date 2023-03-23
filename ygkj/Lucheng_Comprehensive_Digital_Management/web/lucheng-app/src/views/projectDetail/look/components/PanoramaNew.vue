<!--
 * @Author: dyn
 * @Date: 2021-11-18 13:53:17
 * @LastEditTime: 2021-11-18 14:06:46
 * @LastEditors: Please set LastEditors
 * @Description: 全景图
 * @FilePath: \cangnan-app\src\views\projectDetail\look\components\PanoramaNew.vue
-->
<template>
  <div class="panorama">
    <iframe
      v-if="panoramaSrc && panoramaVisible"
      :src="panoramaSrc"
      class="panoramaIframe"
    ></iframe>
    <div
      v-if="!panoramaSrc && panoramaVisible"
      style="width: 100%; line-height: 310px; text-align: center; color: #333"
    >
      暂无全景图
    </div>
  </div>
</template>

<script>
import { panoramaUrlApi } from "@/api/projectDetail";
export default {
  name: "Panorama",
  props: {
    info: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      panoramaSrc: "",
      panoramaVisible: false,
    };
  },
  mounted() {
    this.getPanoramaUrls();
  },
  methods: {
    //获取全景图url
    async getPanoramaUrls() {
      let res = await panoramaUrlApi.getPanoramaUrl({
        pjcd: this.$localData("get", "projectCode"),
      });
      this.panoramaVisible = true;
      if (res.code == 0) {
        if (res.data.list && res.data.list.length > 0) {
          this.panoramaSrc = res.data.list[0].panoramaAddress;
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.panorama {
  height: 100%;
  width: 100%;
  .panoramaIframe {
    width: 100%;
    height: 100%;
  }
}
</style>