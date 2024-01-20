import { ElementObj } from './index.d';

const isServer = typeof window === 'undefined';

export const resizeHandler = (mutationsList: MutationRecord[], observer: any, element: ElementObj) => {
  const listeners = element.resizeListners || [];
  if (listeners.length) {
    listeners.forEach((fn: Function) => fn());
  }
};

export const addResizeListener = (element: ElementObj, fn: any) => {
  if (isServer) return;
  if (!element.resizeListners) {
    element.resizeListners = [];
    window.addEventListener('resize', fn);
    const mutationObserverSupported = typeof MutationObserver !== 'undefined';
    if (mutationObserverSupported) {
      element.observer = new MutationObserver((mutationsList, observer) => resizeHandler(mutationsList, observer, element));
      const config = {
        attributes: true,
        childList: true,
        subtree: true,
        characterData: true,
      };

      element.observer.observe(element as Node, config);
    }
  }
  element.resizeListners.push(fn);
};

export const removeResizeListener = (element: ElementObj, fn: Function) => {
  if (!element || !element.resizeListners) return;
  element.resizeListners.splice(element.resizeListners.indexOf(fn), 1);
  if (!element.resizeListners.length) {
    element.observer.disconnect();
  }
};
