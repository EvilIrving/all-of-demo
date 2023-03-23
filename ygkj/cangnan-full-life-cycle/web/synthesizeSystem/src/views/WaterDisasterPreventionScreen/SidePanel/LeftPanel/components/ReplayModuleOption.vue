<script>
import { PrimaryDialog } from "@/components/";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention/index";
import { mapActions, mapGetters } from "vuex";
import moment from "moment";
export default {
  name: "ReplayModuleOption",
  data() {
    return {
      /** @type {String} ["typhoon","custom"] */
      lastMethod: "",
      method: "",
      dialog: false,
      startTime: "",
      endTime: "",
      form: { startTime: "", endTime: "" },
      rules: {
        startTime: { required: true, message: "请选择开始时间", trigger: "blur" },
        endTime: { required: true, message: "请选择结束时间", trigger: "blur" },
      },
      typhoonList: [
        { a: "typhoonListTodo" },
        { a: "typhoonListTodo" },
        { a: "typhoonListTodo" },
      ],
      typhoonRadio: 0,
      collectList:[
        { id:'123',type: "台风", code:"201909"},
        { id:'222',type: "自定义" },
      ],
      collectRadio:0,
      collectType:'',
      isCollected:false,
      collectedId:"",
      TyphoonOptions: [
        {
          label: '全部',
          value: null,
        },{
          label: '是',
          value: true,
        },{
          label: '否',
          value: false,
        }
      ],
      year: '',
      affectedArea: null,
      historyTyphoonData: []
    };
  },
  mounted() {
    let d = new Date;
    this.year = d.getFullYear().toString();
    this.typhoons();
    this.getHistroyTyphoonList();
    this.getDuplicateCollectionList();
  },
  computed: { ...mapGetters(["historyTyphoonList","typhoonDataList"]) },
  methods: {
    ...mapActions(["getHistroyTyphoonList"]),
    yearSelect (e) {
      this.year = e;
      this.typhoons();
    },
     setSelect (e) {
      this.affectedArea = e;
      this.typhoons();
    },
    async typhoons() {
      let str = {
        year: this.year,
        affectedArea: this.affectedArea
      }
      let res = await disasterPreventionApi.typhoons(str);
      if(res.code == 0){
        this.historyTyphoonData = res.data;
      }
    },
    renderTyphoonDialog() {
      return (
        <div class="content-typhoon">
          <el-row style="margin-bottom: 15px;">
            <el-col span="6">
              <span style="color:#fff;">影响温州：</span>
              <el-select value={this.affectedArea} onChange={this.setSelect}>
                {this.TyphoonOptions.map(item => {
                  return ( <el-option key={item.value} label={item.label} value={item.value} /> )
                })}
              </el-select>
            </el-col>
            <el-col span="6">
              <span style="color:#fff;">年份：</span>
              <el-date-picker v-model={this.year} type="year" placeholder="选择年" value-format="yyyy" onChange={this.yearSelect} />
            </el-col>
          </el-row>
          <el-table data={this.historyTyphoonData} style="height:500px;overflow-y: auto;">
            <el-table-column width={60} label="单选">
              {({ $index }) => (
                <el-radio
                  value={this.typhoonRadio}
                  onInput={(value) => {
                    this.typhoonRadio = value;
                  }}
                  label={$index}
                />
              )}
            </el-table-column>
            <el-table-column type="index" label="序号" />
            <el-table-column prop="code" label="编号" />
            <el-table-column prop="name" label="中文名称" />
            <el-table-column prop="englishName" label="英文名称" />
          </el-table>
        </div>
      );
    },
    renderCustomDialog() {
      let formProps = { props: { model: this.form, rules: this.rules } };
      return (
        <el-form ref="form" inline {...formProps}>
          <el-row class="content-custom">
            <el-col span={12}>
              <el-form-item prop="startTime">
                * 开始时间：
                <el-date-picker
                  value={this.form.startTime}
                  prefixIcon="el-icon-date"
                  type="datetime"
                  format="yyyy-MM-dd HH时"
                  valueFormat="timestamp"
                  placeholder="选择时间"
                  clearable={false}
                  onInput={(value) => {
                    this.form.startTime = value;
                  }}
                />
              </el-form-item>
            </el-col>
            <el-col span={12}>
              <el-form-item prop="endTime">
                * 结束时间：
                <el-date-picker
                  value={this.form.endTime}
                  prefixIcon="el-icon-date"
                  type="datetime"
                  format="yyyy-MM-dd HH时"
                  valueFormat="timestamp"
                  placeholder="选择时间"
                  clearable={false}
                  onInput={(value) => {
                    this.form.endTime = value;
                  }}
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      );
    },
    renderCollectDialog(){
      return (
        <div class="content-typhoon">
          <el-table data={this.collectList}>
            <el-table-column width={60} label="单选">
              {({ $index }) => (
                <el-radio
                  value={this.collectRadio}
                  onInput={(value) => {
                    this.collectRadio = value;
                  }}
                  label={$index}
                />
              )}
            </el-table-column>
            <el-table-column type="index" label="序号" />
            <el-table-column prop="type" label="复盘类型">
              {({ row }) => (
                <span>{row.type == '1' ? '台风':'自定义'}</span>
              )}
            </el-table-column>
            <el-table-column prop="typhoonName" label="名称" />
            <el-table-column prop="startTime" label="开始时间" sortable />
            <el-table-column prop="endTime" label="结束时间" sortable />
            <el-table-column prop="timeInterval" label="影响时间(h)" sortable />
            <el-table-column prop="unkonw" label="复盘报告">
            {({row})=>(
              <div class='table-operate'>
                <i class="el-icon-document" onClick={()=>{
                  this.previewPort(row)
                }}></i>
                <i class="el-icon-download" onClick={()=>{
                  this.downPort(row)
                }}></i>
              </div>
            )}
            </el-table-column>
            <el-table-column prop="unkonw" label="操作">
              {({ row }) =>(
                <span onClick={()=>{
                  this.doUnCollect(row.id);
                }}>取消收藏</span>
              )}
            </el-table-column>
          </el-table>
        </div>
      );
    },
    renderDialog() {
      let dialogTitle = this.method == "typhoon" ? "台风复盘" : this.method == "custom" ? "自定义复盘" : "已收藏复盘";
      const dialogContent =
        this.method == "typhoon" ? this.renderTyphoonDialog() : this.method == "custom" ? this.renderCustomDialog() :this.renderCollectDialog();
      const dialogWidth = this.method == "typhoon" || this.method == "collect"  ? "1300px" : "530px";
      return (
        <PrimaryDialog
          mainTitle={dialogTitle}
          visible={this.dialog}
          hasAnalysis={false}
          onHandleClose={() => {
            this.dialog = false;
          }}
          width={dialogWidth}
          class="option-dialog"
          appendToBody
        >
          {dialogContent}
          <div slot="footer">
            <el-button class="cancel" onClick={this.close}>
              取消
            </el-button>
            <el-button class="confirm" onClick={this.confirm}>
              确定
            </el-button>
          </div>
        </PrimaryDialog>
      );
    },
    handleTyphoonReplayClick() {
      this.lastMethod = this.method;
      this.method = "typhoon";
      this.dialog = true;
    },
    handleCustomReplayClick() {
      this.lastMethod = this.method;
      this.method = "custom";
      this.dialog = true;
    },
    handleCollectReplayClick(){
      this.lastMethod = this.method;
      this.method = "collect";
      this.dialog = true;
    },
    close() {
      this.dialog = false;
      if (this.method == "custom") this.$refs.form.resetFields();
      setTimeout(() => {
        this.method = this.lastMethod;
      }, 200);
    },
    add0(m) {
      return m < 10 ? "0" + m : m;
    },
    format(timer) {
      var time = new Date(timer);
      var y = time.getFullYear();
      var m = time.getMonth() + 1;
      var d = time.getDate();
      var h = time.getHours();
      var mm = time.getMinutes();
      var s = time.getSeconds();
      return (
        y +
        "-" +
        this.add0(m) +
        "-" +
        this.add0(d) +
        " " +
        this.add0(h) +
        ":" +
        this.add0(mm) +
        ":" +
        this.add0(s)
      );
    },
    initCollect(){
      this.isCollected = false;
      this.collectedId = "";
    },
    confirm() {
      const close = () => {
        this.dialog = false;
        this.initCollect();
        if (this.method == "custom") this.$refs.form.resetFields();
      };
      //自定义复盘确定事件
      const doCustomAction = (startTime,endTime) => {
        this.startTime = startTime;
        this.endTime = endTime;
        this.$emit("selectRange", [this.startTime, this.endTime]);
        this.$store.commit("typhoonTime", [
          this.format(this.startTime),
          this.format(this.endTime),
        ]);
        close();
      }
      //台风复盘确定事件
      const doTyphoonAction = (typhoonRadio) => {
        // console.log(typhoonRadio,2233)
        let { beginTime, endTime } = this.historyTyphoonList[typhoonRadio];
        beginTime = moment(beginTime).valueOf();
        endTime = moment(endTime).valueOf();
        this.$store.commit("typhoonDataList", this.historyTyphoonList[typhoonRadio]);
        this.$store.commit("typhoonTime", [this.format(beginTime), this.format(endTime)]);
        this.$emit("selectRange", [beginTime, endTime]);
        close();
      }
      if (this.method == "custom")
        this.$refs.form.validate((valid) => {
          if (valid) {
            doCustomAction(this.form.startTime,this.form.endTime);
            this.$store.commit("analyse",this.method);
          } else return false;
        });
      else if(this.method == 'typhoon'){
        this.$store.commit("analyse",this.method);
        doTyphoonAction(this.typhoonRadio);
      }else{
        //已收藏复盘
        const collectItem = this.collectList[this.collectRadio];
        // console.log(collectItem,111)
        if(collectItem.type == '1'){
          let idx = this.historyTyphoonList.findIndex((item)=>{
            return item.code == collectItem.typhoonId
          })
          this.$store.commit("analyse",'typhoon');
          doTyphoonAction(idx);
          this.method = "typhoon";
        }else{
          if(collectItem.startTime && collectItem.endTime)
            doCustomAction(collectItem.startTime,collectItem.endTime)
            this.$store.commit("analyse",'custom');
            this.method = "custom";
        }
        this.isCollected = true;
        this.collectedId = collectItem.id;
      }
    },
    previewPort(data){
      console.log(data,1);
    },
    downPort(data){
      console.log(data,2);
    },
    async doUnCollect(collectedId,flag){
      this.$confirm('是否执行取消收藏此复盘操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          let res = await disasterPreventionApi.delDuplicateCollection({id:collectedId});
          if(res.code == 0){
            this.$message({
              type: 'success',
              message: '取消收藏成功'
            });
            this.getDuplicateCollectionList();
            //flag为true时 大屏侧边栏的取消收藏  不传时 弹框里的取消收藏
            if(flag){
              this.collectedId = "";
              this.isCollected = false;
            }
          }
        });
    },
    async doCollect(){
      if(this.method == '') return
      if(this.isCollected){
        //取消收藏
        this.doUnCollect(this.collectedId,true);
      }else{
        //收藏
        let opt;
        if(this.method == 'custom'){
          //自定义复盘
          opt = {
            type:2,
            startTime:this.format(this.startTime),
            endTime:this.format(this.endTime),
          }
        }else if(this.method == 'typhoon'){
          //台风复盘
          opt = {
            type:1,
            startTime:this.typhoonDataList.beginTime,
            endTime:this.typhoonDataList.endTime,
            typhoonId:this.typhoonDataList.code,
            typhoonName:this.typhoonDataList.name
          }
        }
        if(this.collectedId)
          opt = {
            ...opt,
            id:this.collectedId
          }
        let res = await disasterPreventionApi.saveOrUpdateDuplicate(opt);
        if(res.code == 0){
          this.$message({
            type:"success",
            message:"收藏成功"
          });
          this.isCollected = true;
          this.collectedId = res.data;
          this.getDuplicateCollectionList();
        }
      }
    },
    async getDuplicateCollectionList(){
      let opt = {
        type:this.collectType
      }
      let res = await disasterPreventionApi.getDuplicateCollectionList(opt);
      if(res.code == 0){
        this.collectList = res.data;
      }
    },
    async saveOrUpdateDuplicate(opt){
      let res = await disasterPreventionApi.saveOrUpdateDuplicate(opt);
      if(res.code == 0){
        this.$message({
          type: "success",
          message:"收藏成功"
        })
      }else{
        this.$message.error(res.message);
      }
    },
  },
  render() {
    let dialog = this.renderDialog();
    return (
      <div class="replay-option">
        <ul class="replay-option__selection">
          <li
            class={["selection", this.method == "typhoon" && "active"]}
            onClick={this.handleTyphoonReplayClick}
          >
            <span>台风复盘</span>
            <i class="el-icon-setting" />
          </li>
          <li
            class={["selection", this.method == "custom" && "active"]}
            onClick={this.handleCustomReplayClick}
          >
            <span>自定义复盘</span>
            <i class="el-icon-setting" />
          </li>
          <li
            class={["selection", this.method == "collect" && "active"]}
            onClick={this.handleCollectReplayClick}
          >
            <span>已收藏复盘</span>
            <i class="el-icon-setting" />
          </li>
          {dialog}
        </ul>
        <div class="replay-option__result">
         <div class="replay-option__title" onClick={this.doCollect}>
            <i class={this.isCollected ? "el-icon-star-on" : "el-icon-star-off"} />
            <span >收藏此复盘</span>
          </div>
          <div class={['replay-option__title',this.isCollected && 'active']}>
            <i class='el-icon-document'/>
            <span>复盘报告</span>
          </div>
        </div>
      </div>
    );
    
  },
};
</script>
<style lang="scss">
.replay-option {
  color: #fff;
  // display: flex;
  // align-items: center;
  // justify-content: space-between;
  margin-bottom: 12px;
  .replay-option__selection {
    display: flex;
    li {
      user-select: none;
      cursor: pointer;
      height: 34px;
      width: 120px;
      line-height: 34px;
      text-align: center;
      border-radius: 17px;
      > i {
        margin-left: 3px;
        vertical-align: text-top;
      }
      &.active {
        background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
      }
    }
  }
  .replay-option__result{
    border-top:1px solid rgba(0,193,255,0.25);
    padding: 12px 0 0;
    @include flexbox;
    @include flexflow(row,nowrap);
    @include flexJC(space-around);
  }
  .replay-option__title {
    user-select: none;
    font-size: 20px;
      color: #fff;
    &.active{
      color: #1ef8f2;
    }
    > i {
      margin-right: 6px;
    }
  }
}
.table-operate{
  i{
    margin-right: 8px;
    cursor: pointer;
  }
}
</style>
<style lang="scss">
.content-custom {
  color: #fff;
  padding-top: 24px;
  .el-date-editor {
    width: 150px;
    .el-input__inner {
      height: 32px;
      border: 1px solid #fff;
      border-radius: 4px;
      padding: 0 9px;
    }
    .el-input__prefix {
      left: unset;
      right: 3px;
      > i {
        line-height: 32px;
      }
    }
  }
  .el-form-item__error {
    left: 83px;
  }
}
</style>
<style lang="scss">
.content-typhoon {
  .el-radio__input.is-checked {
    .el-radio__inner {
      background: transparent;
      &:after {
        background-color: #1ef8f2;
        border-color: #1ef8f2;
        transform: translate(-50%, -50%) scale(1.8);
      }
    }
  }

  .el-radio__label {
    display: none;
  }
}
</style>
