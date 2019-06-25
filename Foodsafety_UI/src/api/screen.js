import store from '../store';

export function loadGridHeight (tabName) {
  // debugger
  var height = store.getters.screenHeight;
  //console.log(height);
  //console.log($("fieldset.fieldset").height());
  //console.log($(".head-box").height());
  //console.log($(".pagination-container").height());
  //debugger
  // 查询条件 tab为标签页的ID
  if (tabName) {
    // debugger
    // alert($("#"+tabName+" .fieldset").actual('height'))
    // alert($("#"+tabName+" .fieldset").actual('innerHeight'))
    // alert($("#"+tabName+" .fieldset").actual('outerHeight'))
    height -= 55;
    height -= $("#"+tabName+" .fieldset").actual('height');
  } else {
    if($(".fieldset").length>0)
      height -= $(".fieldset").height();
    // 标签页标签高度
    if ($(".el-tabs").length>0)
      height -= 55;
  }
  // console.log($(".fieldset").height());
  //alert($("#pane-a2 .fieldset").height());
  // 表格工具栏
  if ($(".head-box").length>0)
    height -= $(".head-box").height();
  // 表格分页栏
  if ($(".pagination-container").length>0)
    height -= $(".pagination-container").height();
  // padding
  height -= 40;
  // header加上页面标签栏的高度
  height -= 100;
  //console.log(height);
  return height;
  /*if (height === undefined)
    return 520;
  switch(height){
    case 768:
      return 370;
      break;
    case 1080:
      return 670;
      break;
    default:
      return 520;
  }*/
}

export function loadDashboardHeight () {
  switch(screen.height){
    case 768:
      return 'dashboard768';
      break;
    case 1080:
      return 'dashboard1080';
      break;
    default:
      return 'dashboard';
  }
}

export function loadMapHeight (fullScreen) {
  var height = store.getters.screenHeight;
  if (height === undefined)
    return fullScreen ? 'mapAreaFull' : 'mapArea';
  switch(height){
    case 768:
      return fullScreen ? 'mapAreaFull768' : 'mapArea768';
      break;
    case 1080:
      return fullScreen ? 'mapAreaFull1080' : 'mapArea1080';
      break;
    default:
      return fullScreen ? 'mapAreaFull' : 'mapArea';
  }
}

export function loadHouseRightTreeHeight(fullScreen) {
  var height = store.getters.screenHeight;
  if (height === undefined)
    return fullScreen?768*0.69: '59vh';
  switch(height){
    case 768:
      return fullScreen?768*0.69: '59vh';
      break;
    case 1080:
      return fullScreen?1080*0.76:'69vh';
      break;
    default:
      return  fullScreen?768*0.69: '59vh';
  }
}
