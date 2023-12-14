interface BarMapItem {
  key: string;
  size: string;
  axis: string;
  client: string;
  scroll: string;
  scrollSize: string;
  offset: string;
  direction: string;
  wide: string;
}

interface BarMap {
  [key: string]: BarMapItem;
}

export const BAR_MAP: BarMap = {
  vertical: {
    offset: 'offsetHeight',
    key: 'vertical',
    size: 'height',
    wide: 'width',
    axis: 'Y',
    client: 'clientY',
    scroll: 'scrollTop',
    scrollSize: 'scrollHeight',
    direction: 'top',
  },
  horizontal: {
    offset: 'offsetWidth',
    key: 'horizontal',
    size: 'width',
    wide: 'height',
    axis: 'X',
    client: 'clientX',
    scroll: 'scrollLeft',
    scrollSize: 'scrollWidth',
    direction: 'left',
  },
};
