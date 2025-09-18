import { defineComponent, unref } from 'vue';
import { BasicDrawer } from '/@/components/Drawer/index';
import { Divider } from 'ant-design-vue';
import {
  ThemeColorPicker,
  TypePicker,
} from './components';


import {
  HandlerEnum,
  menuTypeList,
} from './enum';

import { useRootSetting } from '/@/hooks/setting/useRootSetting';
import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';


import { baseHandler } from './handler';

import {
  HEADER_PRESET_BG_COLOR_LIST,
  SIDE_BAR_BG_COLOR_LIST,
  APP_PRESET_COLOR_LIST

} from '/@/settings/designSetting';

export default defineComponent({
  name: 'SettingDrawer',
  setup(_, { attrs }) {
    const {
      getThemeColor
    } = useRootSetting();

    const {
      getMenuBgColor,
      getIsHorizontal,
      getMenuType,
    } = useMenuSetting();

    const {
      getHeaderBgColor,
    } = useHeaderSetting();

    function renderSidebar() {
      return (
        <>
          <TypePicker
            menuTypeList={menuTypeList}
            handler={(item: typeof menuTypeList[1]) => {
              baseHandler(HandlerEnum.CHANGE_LAYOUT, {
                mode: item.mode,
                type: item.type,
                split: unref(getIsHorizontal) ? false : undefined,
              });
            }}
            def={unref(getMenuType)}
          />
        </>
      );
    }


    function renderHeaderTheme() {
      return (
        <ThemeColorPicker
          colorList={HEADER_PRESET_BG_COLOR_LIST}
          def={unref(getHeaderBgColor)}
          event={HandlerEnum.HEADER_THEME}
        />
      );
    }

    function renderSiderTheme() {
      return (
        <ThemeColorPicker
          colorList={SIDE_BAR_BG_COLOR_LIST}
          def={unref(getMenuBgColor)}
          event={HandlerEnum.MENU_THEME}
        />
      );
    }

    function renderMainTheme() {
      return (
        <ThemeColorPicker
          colorList={APP_PRESET_COLOR_LIST}
          def={unref(getThemeColor)}
          event={HandlerEnum.CHANGE_THEME_COLOR}
        />
      );
    }

    return () => (
      <BasicDrawer
        {...attrs}
        title='项目配置'
        width={330}
        class="setting-drawer"
      >

        <Divider>导航栏模式</Divider>
        {renderSidebar()}
        <Divider>头部主题</Divider>
        {renderHeaderTheme()}
        <Divider>菜单主题</Divider>
        {renderSiderTheme()}
        <Divider>系统颜色</Divider>
        {renderMainTheme()}
      </BasicDrawer>
    );
  },
});
