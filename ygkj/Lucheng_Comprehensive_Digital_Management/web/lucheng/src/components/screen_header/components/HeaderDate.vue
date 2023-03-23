<script>
import moment from 'moment';
moment.locale('zh-CN');

export default {
  name: 'FooterDate',
  data() {
    return {
      currentTimer: null,
      currentTime: moment().format('HH:mm:ss'),
      dateTime: moment().format('YYYY.MM.DD'),
      weekTime: moment().format('dddd')
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
        this.currentTime = moment().format('HH:mm:ss');
      }, 1000);
    }
  },
  render() {
    return (
      <div class="map-footer__date">
        <span class="data-time">{this.dateTime}</span>
        <span class="current-time">{this.currentTime}</span>
        <span class="week-time">{this.weekTime}</span>
      </div>
    );
  }
};
</script>
<style lang="scss" scoped>
.map-footer__date {
  font-size: 16px;
  background: linear-gradient(180deg, #5de6fb 0%, #02a4ed 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  .data-time {
    margin-right: 8px;
  }
  .current-time {
    margin-right: 12px;
  }
}
</style>