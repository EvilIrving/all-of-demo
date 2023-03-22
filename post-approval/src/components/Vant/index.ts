import { App } from 'vue';
import {
  Button,
  NavBar,
  Tabs,
  Tab,
  DropdownMenu,
  DropdownItem,
  Form,
  CellGroup,
  Cell,
  Field,
  Radio,
  RadioGroup,
  Uploader,
  Icon,
  List,
  Row,
  Col,
  Image as VanImage,
  DatetimePicker,
  Popup,
  Picker
} from 'vant';

// https://vant-contrib.gitee.io/vant/v3/#/zh-CN
const plugins = [
  VanImage,
  Button,
  NavBar,
  Tabs,
  Tab,
  DropdownMenu,
  DropdownItem,
  Form,
  CellGroup,
  Cell,
  Field,
  Radio,
  RadioGroup,
  Uploader,
  Icon,
  List,
  Row,
  Col,
  Picker,
  DatetimePicker,
  Popup,
];

import 'vant/lib/index.css';

export default {
  install: function (vm: App) {
    plugins.forEach(item => {
      vm.component(item.name, item);
    });
  }
};
