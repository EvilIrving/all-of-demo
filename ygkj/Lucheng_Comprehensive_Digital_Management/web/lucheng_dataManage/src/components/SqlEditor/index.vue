<!--
 * @Author: zg
 * @LastEditTime: 2022-03-24 16:13:52
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: \web-data-center\src\components\SqlEditor\index.vue
-->
<template>
  <div>
    <textarea ref="sqlEditor" v-model="value" class="codesql"></textarea>
  </div>
</template>
<script>
// 引入核心样式
import "codemirror/theme/ambiance.css";
import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";
// 替换主题这里需修改名称
import "codemirror/theme/idea.css";
// 引入自动刷新
import "codemirror/addon/display/autorefresh";
// 引入全局实例
const CodeMirror = require("codemirror/lib/codemirror");
const sqlformatter = require("sql-formatter");
//
require("codemirror/addon/edit/matchbrackets");
require("codemirror/addon/selection/active-line");

// sql语言包
require("codemirror/mode/sql/sql");
// 代码自动提示插件
require("codemirror/addon/hint/show-hint");
require("codemirror/addon/hint/sql-hint");

export default {
  name: "SqlEditor",
  props: {
    // 接收父组件传值
    value: {
      type: String,
      default: "",
    },
    sqlStyle: {
      type: String,
      default: "default",
    },
    editorMode: {
      type: String,
      default: "text/x-sql",
    },
    readOnly: {
      type: [Boolean, String],
    },
    // 父组件将表结构传给编辑器，实现自动提示表名和字段名的功能
    tables: {
      type: Object,
      default: () => {},
    },
    editorHeight: {
      type: Number,
      default: 450,
    },
    tableInfo: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      editor: null,
    };
  },
  computed: {
    // 将编辑器中的值赋给newVal变量
    newVal() {
      if (this.editor) {
        return this.editor.getValue();
      } else {
        return "";
      }
    },
  },
  watch: {
    // 监听newVal值的变化，通过getValue方法获取到值并传递给父组件
    newVal() {
      // console.log(newV, oldV)
      if (this.editor) {
        this.$emit("changeTextarea", this.editor.getValue());
      }
    },
    // 将value赋值给编辑器配置项改变值中的value
    value(newV) {
      //
      // console.log("### 新值" + newV)
      // console.log("### 旧值" + oldV)
      if (this.editor) {
        let cursor = this.editor.doc.getCursor();
        if (newV === "") {
          this.editor.setValue("");
          this.editor.setCursor(cursor);
        } else {
          this.editor.setValue(newV);
          this.editor.setCursor(cursor);
        }
      }
    },
    // tableInfo(val) {
    //   if(val) {
    //     let getCursor = this.editor.doc.getCursor()
    //     let pos = {};
    //     pos.line = getCursor.line;
    //     pos.ch = getCursor.ch;
    //     this.editor.doc.replaceRange(" "+ val +" ",pos)
    //   }
    // },
    // 根据父组件中的高度来给editor动态设置高度
    editorHeight(newV) {
      this.editor.setSize("100%", newV);
    },
  },
  mounted() {
    this.editor = CodeMirror.fromTextArea(this.$refs.sqlEditor, {
      // value: this.value,
      autoRefresh: true,
      mode: this.editorMode,
      indentWithTabs: true,
      smartIndent: true,
      lineNumbers: true,
      matchBrackets: true,
      lineWrapping: true,
      readOnly: this.readOnly,
      theme: "idea",
      cursorHeight: 0.9,
      autofocus: true,
      extraKeys: {
        "'a'": this.completeAfter,
        "'b'": this.completeAfter,
        "'c'": this.completeAfter,
        "'d'": this.completeAfter,
        "'e'": this.completeAfter,
        "'f'": this.completeAfter,
        "'g'": this.completeAfter,
        "'h'": this.completeAfter,
        "'i'": this.completeAfter,
        "'j'": this.completeAfter,
        "'k'": this.completeAfter,
        "'l'": this.completeAfter,
        "'m'": this.completeAfter,
        "'n'": this.completeAfter,
        "'o'": this.completeAfter,
        "'p'": this.completeAfter,
        "'q'": this.completeAfter,
        "'r'": this.completeAfter,
        "'s'": this.completeAfter,
        "'t'": this.completeAfter,
        "'u'": this.completeAfter,
        "'v'": this.completeAfter,
        "'w'": this.completeAfter,
        "'x'": this.completeAfter,
        "'y'": this.completeAfter,
        "'z'": this.completeAfter,
        "'.'": this.completeAfter,
        "Ctrl-Alt-L": this.format,
      },
      //   hintOptions: {
      //     tables: this.dataBaseTips,
      //   },
    });

    this.editor.setSize("100%", this.editorHeight);
    // 监听inputRead事件，实现代码自动提示功能

    // 监听光标事件，获取光标选中的内容
    this.editor.on("cursorActivity", () => {
      // this.editor.showHint();
      let somethingSelected = this.editor.doc.somethingSelected(); //是否有光标选中内容
      let getSelection = this.editor.doc.getSelection(); //光标选中内容
      this.$emit("getSelectContent", somethingSelected, getSelection);
    });

    // this.editor.on("keyup", function (cm ,e) {
    //   var arrows = [37, 38, 39, 40];
    //   if (arrows.indexOf(e) < 0) {
    //     this.editor.execCommand("autocomplete")
    //   }
    // });
  },

  methods: {
    tableInfoChange(val) {
      if (val) {
        let getCursor = this.editor.doc.getCursor();
        let pos = {};
        pos.line = getCursor.line;
        pos.ch = getCursor.ch;
        this.editor.doc.replaceRange(" " + val + " ", pos);
      }
    },
    completeAfter(cm, pred) {
      if (!pred || pred())
        setTimeout(function () {
          if (!cm.state.completionActive)
            cm.showHint({
              completeSingle: false,
            });
        }, 100);
      return CodeMirror.Pass;
    },

    format() {
      let sqlcontent = this.editor.getValue();
      this.editor.setValue(sqlformatter.format(sqlcontent));
      let cursor = this.editor.doc.getCursor();
      this.editor.setCursor(cursor);
    },
    // 取值
    getTextValue() {
      return this.editor.getValue();
    },
    // 赋值
    setTextValue(val) {
      this.editor.setValue(val);
      this.editor.refresh();
    },
    // // 刷新
    // refreshs() {
    //   this.editor.refresh();
    // },
  },
};
</script>

<style scope lang="scss">
.CodeMirror {
  height: auto;
  font-size: 14px;
  font-weight: normal;
  border: 1px solid #dcdfe6;
  /* height: 450px!important; */
  /* width: 900px!important; */
}
.CodeMirror-cursors {
  visibility: visible !important;
  animation: cursorActive 1s infinite steps(1, start);
}
.cm-s-monokai .CodeMirror-cursor {
  border-left: 2px solid #d5690f !important;
}

.json-editor .cm-s-rubyblue span.cm-string {
  color: #f08047;
}
@keyframes cursorActive {
  0%,
  100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}
</style>