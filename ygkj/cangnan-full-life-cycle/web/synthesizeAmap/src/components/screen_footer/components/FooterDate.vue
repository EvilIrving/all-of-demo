<script>
import moment from "moment";
moment.locale("zh-CN");

export default {
  name: "FooterDate",
  data() {
    return {
      currentTimer: null,
      currentTime: moment().format("HH:mm:ss"),
      dateTime: moment().format("YYYY[年] MM[月] DD[日] dddd"),
    };
  },
  mounted() {
    this.setTimer();
  },
  beforeDestroy() {
    clearInterval(this.currentTimer);
  },
  methods: {
    /**
     * 定时器显示时间
     */
    setTimer() {
      this.currentTimer = setInterval(() => {
        this.currentTime = moment().format("HH:mm:ss");
      }, 1000);
    },
  },
  render() {
    return (
      <div class="map-footer__date">
        <div class="map-footer__date--wrap">
          <span class="current-time">{this.currentTime}</span>
          {this.dateTime}
        </div>
      </div>
    );
  },
};
</script>
<style lang="scss" scoped>
.map-footer__date {
  user-select: none;
  flex: 1;
  height: 50px;
  display: flex;
  align-items: center;
  flex-direction: row-reverse;
  font-size: 20px;
  color: #fff;
  margin-right: 32px;
  margin-top: 36px;
  z-index: 1;
  .current-time {
    font-size: 24px;
    margin-right: 18px;
  }
}
</style>