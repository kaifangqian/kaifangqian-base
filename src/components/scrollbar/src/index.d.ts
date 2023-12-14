export interface ElementObj extends Node {
  [key: string]: any;
  resizeListners: Function[];
  observer: MutationObserver;
}

export interface BarMapItem {
  key: string;
  size: string; // 长度
  axis: string;
  client: string;
  scrollSize: string;
  scroll: string;
  offset: string;
  direction: string;
  wide: string; // 宽度
}

export interface BarMap {
  [key: string]: BarMapItem;
}
