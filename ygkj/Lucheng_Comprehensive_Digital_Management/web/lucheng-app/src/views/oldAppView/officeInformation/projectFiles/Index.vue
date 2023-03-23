<template>
  <!--  工程档案-->
  <view-page>
    <div class="project_wrap">
      <div class="top_bar">
        <van-field
          v-model="input"
          placeholder="请输入关键字"
          @change="$forceUpdate()"
        />
      </div>
      <div class="list_content">
        <van-list
          v-model="loading"
          :finished="finished"
          @load="getProList"
          v-if="filteredList.length !== 0"
        >
          <van-cell v-for="(item, index) in filteredList" :key="index">
            <div class="item" @click="onItemClick(item)">
              <img
                src="@/assets/images/officeInformation/file.png"
                v-if="item.type == 'folder'"
              />
              <img
                :class="fileClassName(item)"
                :src="fileIcon(item)"
                v-if="item.type !== 'folder'"
              />
              <div class="item_box">
                <div class="title">
                  <span>{{ item.name || item.fileName }}</span>
                </div>
                <span class="num">{{ item.fileSize }} MB</span>
                <span class="time">{{ item.createTime }}</span>
              </div>
            </div>
          </van-cell>
        </van-list>

        <div class="no-data" v-else-if="id === '-1'">
          <img src="@/assets/images/officeInformation/no_data.png" />
          <div>暂无数据</div>
        </div>
      </div>
      <ArchivesShortcut :directory="id" @change="getProList" />
      <Dialog title="下载" v-model="preview" show-cancel-button>
        <van-cell
          center
          :label="`${file.size}MB`"
          :title="file.name"
          is-link
          :url="file.url"
        />
      </Dialog>
    </div>
  </view-page>
</template>

<script>
import { ImagePreview } from "vant";
import { appFiles } from "@/api/appView";
import ArchivesShortcut from "./components/ArchivesShortcut.vue";
import { Dialog } from "vant";
import { mapActions } from "vuex";
/**
 * @typedef {import('./components/type').AchievesFolder} AchievesFolder
 * @typedef {import('./components/type').AchievesFile} AchievesFile
 */

export default {
  name: "Index",
  props: {
    id: {
      type: String,
      default: "-1",
    },
  },
  components: { ArchivesShortcut, Dialog: Dialog.Component },
  data() {
    return {
      title: "",
      input: "",
      loading: false,
      finished: true,
      list: /** @type {(AchievesFolder|AchievesFile)[]}*/ ([]),
      type: "",
      preview: false,
      file: {
        name: "",
        url: "",
        ext: "",
        size: 0,
      },
    };
  },
  watch: {
    $route(to, from) {
      if (to.params.id != from.params.id) {
        this.getProList();
      }
    },
  },
  computed: {
    filteredList() {
      return this.list.filter((item) => {
        if (item.type === "file") {
          return item.fileName.includes(this.input);
        }
        return item.name.includes(this.input);
      });
    },
  },
  mounted() {
    this.addZwlog(this)
    this.type = this.$route.query.type;
    console.log(this.type,'type')
    this.title = this.type === "工程" ? "工程档案" : "物业档案";
    console.log(this.title)
    this.getProList();
  },
  methods: {
    ...mapActions(["setFilesSrc"]),
    fileClassName(item) {
      if (item.fileType === "rar") {
        return "file-RAR-img";
      } else {
        return "file-img";
      }
    },
    fileIcon(item) {
      let fileType = item.fileType.toLowerCase();
      if (fileType === "pdf") {
        return require("@/assets/images/fileIcon/pdf-icon.png");
      } else if (fileType === "rar" || fileType === "zip") {
        return require("@/assets/images/fileIcon/rar-icon.png");
      } else if (fileType === "doc" || fileType === "docx") {
        return require("@/assets/images/fileIcon/word-icon.png");
      } else if (
        fileType === "jpg" ||
        fileType === "png" ||
        fileType === "jpeg"
      ) {
        return require("@/assets/images/fileIcon/png-icon.png");
      } else if (fileType === "ppt" || fileType === "pptx") {
        return require("@/assets/images/fileIcon/ppt-icon.png");
      } else if (fileType === "xls" || fileType === "xlsx") {
        return require("@/assets/images/fileIcon/excel-icon.png");
      } else if (item.fileType === "txt") {
        return require("@/assets/images/fileIcon/txt-icon.png");
      } else {
        return require("@/assets/images/fileIcon/other-icon.png");
      }
    },
    async getProList() {
      this.loading = true;
      const res = await appFiles.getAppAchievement({
        achievementType: this.type === "工程" ? 1 : 2,
        parentId: this.id ?? -1,
      });
      this.loading = false;
      if (res.code === 0) {
        this.list = res.data;
        this.loading = false;
        this.finished = true;
      }
    },
    /**
     * @param {AchievesFolder|AchievesFile} item
     */
    onItemClick(item) {
      const { type, id } = item;
      if (type === "folder")
        this.$router.push({ name: "projectFiles", params: { id } });
      else {
        let fileType = item.fileType.toLowerCase();
        if (fileType === "jpg" || fileType === "png" || fileType === "jpeg") {
          ImagePreview([item.fileUrl]);
        } else if (fileType === "pdf") {
          this.setFilesSrc(item.fileUrl);
          this.$router.push("/lookFile");
        } else {
          this.file = {
            name: item.fileName,
            url: item.fileUrl,
            ext: item.fileType,
            size: item.fileSize,
          };
          this.preview = true;
        }
      }
    },
    onBackClick() {
      this.loading = true;
      this.$router.go(-1);
      this.$nextTick(() => {
        this.loading = false;
      });
    },
  },
};
</script>

<style scoped lang="scss">
.project_wrap {
  width: 100%;
  height: 100%;
  .top_bar {
    background: #fbfbfb;
    height: 44px;
    display: flex;
    flex-direction: row;
  }
  .item {
    display: flex;
    flex-direction: row;
    font-size: 18px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 200;
    color: #666666;
    line-height: 16px;
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: 21px;
    img {
      width: 45px;
      height: 39px;
    }
    .file-img {
      width: 34px;
      height: 39px;
    }
    .file-RAR-img {
      width: 40px;
      height: 33px;
    }
    .title {
      font-size: 20px;
      font-family: SourceHanSansSC-Medium, SourceHanSansSC;
      font-weight: 250;
      color: #000000;
      margin-left: 12px;
      margin-bottom: 5px;
      word-wrap: break-word;
      word-break: break-all;
    }
    .num {
      margin-left: 12px;
      margin-right: 14px;
    }
  }
  .list_content {
    height: calc(100% - 120px);
    overflow: auto;
    position: relative;
    .no-data {
      margin-top: 60px;
      margin-left: 129px;
      img {
        width: 117px;
        height: 114px;
        margin-bottom: 28px;
      }
      > div {
        width: 78px;
        height: 22px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 200;
        color: #999999;
        line-height: 22px;
        margin-left: 32px;
      }
    }
  }
}
</style>
<style lang="scss">
.project_wrap {
  .van-dropdown-menu__bar {
    height: 44px;
    box-shadow: none;
  }
}
</style>
