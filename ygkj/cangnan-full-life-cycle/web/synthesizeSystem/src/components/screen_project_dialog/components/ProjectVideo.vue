<script>
import { PrimaryVideo } from "@/components/";
import { precipitation } from "@/api/dataScreen/floodJudge/index";

const CameraItem = {
  name: "CameraItem",
  props: { name: String, code: String },
  methods: {
    handleClick(e) {
      this.$emit("change", this.code);
    },
  },
  render() {
    return (
      <div class="video-camera-item" onClick={this.handleClick}>
        {this.name}
      </div>
    );
  },
};
export default {
  name: "ProjectVideo",
  props: { code: String },
  data() {
    return {
      cameraList: [],
      currentIndex: 0,
      currentCamera: "",
      currentCameraReload: false,
    };
  },
  mounted() {
    this.getVideoList();
  },
  methods: {
    // 视频列表
    async getVideoList() {
      await precipitation.getVideoList(this.code).then((res) => {
        if (res.code == 0) {
          this.cameraList = res.data;
          this.currentCamera = res.data[0].cameraIndexCode;
        }
      });
    },
  },
  render() {
    return (
      <div class="project-video">
        <div class="video-camera-list">
          {this.cameraList.map((item, index) => (
            <CameraItem
              class={this.currentIndex == index && "active"}
              name={item.cameraName}
              code={item.cameraIndexCode}
              onChange={(val) => {
                this.currentCamera = val;
                this.currentCameraReload = true;
                this.currentIndex = index;
                setTimeout(() => {
                  this.currentCameraReload = false;
                }, 200);
              }}
            />
          ))}
        </div>
        <div class="video-content">
          {!this.currentCameraReload && <PrimaryVideo code={this.currentCamera} />}
        </div>
      </div>
    );
  },
};
</script>
<style lang="scss">
.project-video {
  display: flex;
  height: 450px;
  .video-camera-list {
    width: 30%;
    overflow-y: scroll;
    margin-right: 8px;
  }
  .video-content {
    width: 70%;
  }
}
.video-camera-item {
  user-select: none;
  color: #fff;
  cursor: pointer;
  margin: 8px 0;
  margin-right: 6px;
  border: 1px #285c9e solid;
  padding: 6px;
  &:hover,
  &.active {
    border: 1px solid #168aa7;
    box-shadow: 0 0 20px #168aa746 inset;
  }
}
</style>
