<script>
import { isNativeComponent, isPagePaneItem, getTitle, genLinkProp } from './utils.js';

export default {
  name: 'PagePane',
  model: { prop: 'activeName', event: 'input' },
  props: {
    activeName: String,
    title: {
      type: [Object, String],
      default: () => {
        return { text: '标题' };
      }
    },
    to: { type: [String, Object] },
    inSimulation: { type: Boolean, default: false }
  },
  provide() {
    return {
      page: this
    };
  },
  data() {
    return {
      panes: [],
      navList: []
    };
  },
  methods: {
    calcPaneInstances(isForceUpdate = false) {
      if (this.$slots.default) {
        const paneSlots = this.$slots.default.filter(
          (vnode) => vnode.tag && vnode.componentOptions && isPagePaneItem(vnode)
        );
        // 需要更新时
        const panes = paneSlots.map(({ componentInstance }) => componentInstance);
        const navList = panes.map((item) => {
          if (isNativeComponent(item._vnode))
            return {
              name: item._vnode.componentInstance.name,
              label: item._vnode.componentInstance.label
            };
          return {
            name: item.name,
            label: item.label
          };
        });
        const panesChanged = !(
          panes.length === this.panes.length && panes.every((pane, index) => pane === this.panes[index])
        );
        if (isForceUpdate || panesChanged) {
          this.panes = panes;
          this.navList = navList;
        }
      } else if (this.panes.length !== 0) {
        this.panes = [];
      }
    },
    renderNavList() {
      return (
        <div class="pane-title__nav" role="tablist">
          {this.navList.map((item) => {
            return (
              <div
                class={['pane-title__nav-item', this.activeName == item.name && 'active']}
                id={`tab-${item.name}`}
                role="tab"
                onClick={() => {
                  this.$emit('change', item.name);
                  this.$emit('input', item.name);
                }}
              >
                {item.label}
              </div>
            );
          })}
        </div>
      );
    },
    renderTitle() {
      const { title, to, inSimulation, navList } = this;
      const slotTitle = this.$slots.title;
      const slotAppend = (
        <div class="pane-title__append">
          {this.$slots.append}
        </div>
      );
      const iconTitle = title.icon && (
        <span v-if="title.icon" class="panel-item__title--icon">
          <i class={title.icon} />
        </span>
      );
      const defaultProps = { class: 'pane-title__label' };
      if (this.$listeners.onTitle != undefined) {
        defaultProps.on = {
          click: () => {
            this.$emit('onTitle');
          }
        };
        defaultProps.style = { cursor: 'pointer' };
      }
      const props = { class: 'pane-title__label', ...genLinkProp(to) };

      // eslint-disable-next-line no-undef
      const titleRender = h(to ? 'a' : 'span', to ? props : defaultProps, getTitle(title));
      const append = this.$slots.append;
      return (
        <div class={['page-pane__title', inSimulation && 'in-simulation']}>
          {navList.length == 0 ? (slotTitle ? slotTitle : [iconTitle, titleRender]) : this.renderNavList()}
          <div style="display: flex">
            {this.$slots.append && slotAppend}
            <i
              style="cursor: pointer;margin-left: 5px"
              class="icon-chose"
              onClick={() => {
                this.$parent.$parent.closeCard();
              }}
            ></i>
          </div>
        </div>
      );
    },
    renderHeader() {
      return (
        <div class="page-pane__header drag">
          <div class="page-pane__header--light" />
          {this.renderTitle()}
        </div>
      );
    },
    closeCard() {
      console.log(this, 'dasdasdas');
      // console.log(this.$parent.$parent, '1213');
    }
  },
  mounted() {
    this.calcPaneInstances(true);
  },
  updated() {
    this.calcPaneInstances();
  },
  render() {
    const panes = <div class={['page-pane__content', this.panes.length > 0 && 'has-pane']}>{this.$slots.default}</div>;
    return (
      <div class="page-pane">
        {this.renderHeader()}
        {panes}
      </div>
    );
  }
};
</script>
<style lang="scss">
.page-pane {
  padding: 8px 0;
  height: 100%;
  width: 100%;
  .page-pane__header {
    width: 100%;
    height: 32px;
    background: url(~assets/images/ScreenAsset/title-icon.png) no-repeat;
    background-size: 100% 100%;
    display: flex;
    // align-items: center;
    color: #fff;
    // padding: 8px 0;
    // .page-pane__header--light {
    //   height: 32px;
    //   width: 40px;
    //   background: url(~assets/images/WaterDisastersImage/title-bg_light.gif) no-repeat;
    //   background-size: 100% 100%;
    //   margin-right: 18px;
    // }
    .pane-title__label,
    .pane-title__nav-item {
      user-select: none;
      font-size: 20px;
      margin-left: 38px;
      margin-right: 10px;
      color: #fff;
    }
    .page-pane__title {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      position: relative;
      .pane-title__append {
        display: flex;
        // align-items: center;
        > [class^='icon-'] {
          cursor: pointer;
          &:not(:last-child) {
            margin-right: 8px;
          }
        }
      }
      &.in-simulation {
        > div:first-child {
          &::after {
            content: '演练中';
            margin-left: 6px;
            font-size: 20px;
            color: #1cfff5;
            background-color: #08516c;
            border-radius: 2px;
          }
        }
      }
    }
    .pane-title__nav {
      display: flex;
      padding-top: 4px;
      flex-direction: row;
      .pane-title__nav-item {
        margin-right: 12px;
        cursor: pointer;
        &.active {
          color: #00baff;
        }
      }
    }
  }
  .page-pane__content {
    height: calc(100% - 35px);
  }
}
</style>
