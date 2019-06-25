export function createBuildingModel (map, buildingData, showInfo) {

  map.AmbientLight = new AMap.Lights.AmbientLight([1,1,1],1);
  map.DirectionLight = new AMap.Lights.DirectionLight([1,0,-0.5],[1,1,1],1);

  return function () {
    var object3Dlayer = new AMap.Object3DLayer();
    map.add(object3Dlayer);
    //var center = map.getCenter();

    var addBox = function(building,length,width,height){

      var box = new AMap.Object3D.MeshAcceptLights();
      var geometry = box.geometry;
      //box.textures.push(require('assets/texture.bmp'));
      box.textures.push('http://a.amap.com/jsapi_demos/static/demo-center/model/1519/1519.bmp')

      var halfLength = length/2;
      var halfWidth = width/2;
      var path  = [
        [-halfLength,halfWidth],
        [halfLength,halfWidth],
        [halfLength,-halfWidth],
        [-halfLength,-halfWidth]
      ]
      for(var i = 0;i< 4 ;i += 1){//构造4个侧面
        var x1 = path[i][0];
        var y1 = path[i][1];

        var x2 = path[(i+1)%4][0];
        var y2 = path[(i+1)%4][1];

        geometry.vertices.push(x1,y1,0);//每个侧面四个顶点
        geometry.vertices.push(x1,y1,-height);
        geometry.vertices.push(x2,y2,0);
        geometry.vertices.push(x2,y2,-height);


        var v1 = new AMap.Vector3(0, 0, -height)
        var v2 = new AMap.Vector3(x2 - x1, y2 - y1, 0)

        var normal = new AMap.Vector3().crossVectors(v2, v1).normalize().elements;//各面的法向量

        geometry.vertexNormals.push(normal[0] , normal[1], normal[2])
        geometry.vertexNormals.push(normal[0] , normal[1], normal[2])
        geometry.vertexNormals.push(normal[0] , normal[1], normal[2])
        geometry.vertexNormals.push(normal[0] , normal[1], normal[2])

        geometry.vertexUVs.push(0,1)//四个顶点的纹理坐标
        geometry.vertexUVs.push(0,0)//顶部颜色
        geometry.vertexUVs.push(1,1)//底部颜色
        geometry.vertexUVs.push(1,0)//顶部颜色

        var bottomIndex = i * 4;
        var topIndex = bottomIndex + 1;
        var nextBottomIndex = (bottomIndex + 2);
        var nextTopIndex = (bottomIndex + 3);

        geometry.faces.push(topIndex, bottomIndex, nextBottomIndex)//侧面三角形1
        geometry.faces.push(topIndex,  nextBottomIndex,nextTopIndex)//侧面三角形2
      }
      //构造顶面
      for(var i = 0;i< 4 ;i += 1){
        geometry.vertices.push(path[i][0],path[i][1],-height);
        geometry.vertexNormals.push(0 , 0, -1)//顶部颜色
      }
      geometry.vertexUVs.push(0,2)//四个顶点的纹理坐标
      geometry.vertexUVs.push(1,2)//四个顶点的纹理坐标
      geometry.vertexUVs.push(1,0)//四个顶点的纹理坐标
      geometry.vertexUVs.push(0,0)//四个顶点的纹理坐标

      geometry.faces.push(16+0, 16+1, 16+2)//侧面三角形1
      geometry.faces.push(16+0, 16+2, 16+3)//侧面三角形2

      box.position(new AMap.LngLat(building.grapPositX, building.grapPositY))
      box.backOrFront = 'both'
      box.transparent = true;
      //box.rotateZ(-48);
      object3Dlayer.add(box);
    };

    var addModel = function(building){
      var modelName = 'building';
      //var scope = this;
      var objLoader = new THREE.OBJLoader2();
      //var object3Dlayer = new AMap.Object3DLayer();
      var callbackOnLoad = function ( event ) {
        var meshes = event.detail.loaderRootNode.children;
        for(var i=0;i<meshes.length;i++){
        var vecticesF3 = meshes[i].geometry.attributes.position;
        var vecticesNormal3 = meshes[i].geometry.attributes.normal;
        var vecticesUV2 = meshes[i].geometry.attributes.uv;

        var vectexCount =  vecticesF3.count;

          var mesh = new AMap.Object3D.MeshAcceptLights();

          var geometry = mesh.geometry;

          var c,opacity;

          var material = meshes[i].material[0]||meshes[i].material;
          if(material.map)
            mesh.textures.push('http://a.amap.com/jsapi_demos/static/demo-center/model/1519/1519.bmp')
          //mesh.textures.push('http://a.amap.com/jsapi_demos/static/texture3d/shuilifang.png')

          c = material.color;
          opacity = material.opacity

          for(var j=0;j<vectexCount;j+=1){
            var s = j*3;
            geometry.vertices.push(vecticesF3.array[s],vecticesF3.array[s+2],-vecticesF3.array[s+1]);

            if(vecticesNormal3) {
              geometry.vertexNormals.push(vecticesNormal3.array[s],vecticesNormal3.array[s+2],-vecticesNormal3.array[s+1]);
            }
            if(vecticesUV2) {
              geometry.vertexUVs.push(vecticesUV2.array[j*2],1-vecticesUV2.array[j*2+1]);
            }
            geometry.vertexColors.push(c.r,c.g,c.b,opacity)
          }

          mesh.DEPTH_TEST = material.depthTest
          mesh.backOrFront = 'both'
          mesh.transparent = opacity<1;
          mesh.scale(6,6,6)
          //mesh.rotateZ(-48)
          mesh.position(new AMap.LngLat(building.grapPositX, building.grapPositY))
          object3Dlayer.add(mesh)
        }
      };

      var onLoadMtl = function ( materials ) {
        // for(var i=0;i<materials.length;i+=1){
        // 	materials[i].side=2;
        // }
        objLoader.setModelName( modelName );
        objLoader.setMaterials( materials );
        objLoader.load( 'http://a.amap.com/jsapi_demos/static/demo-center/model/1519/1519.obj', callbackOnLoad, null, null, null, false );
      };
      objLoader.loadMtl( 'http://a.amap.com/jsapi_demos/static/demo-center/model/1519/1519.mtl', null, onLoadMtl );
    };

    //比例尺换算
    var getPixelPerRange = function(building, range){
      //debugger
      // "每像素代表" + scale + "米"
      var resolution = map.getResolution(new AMap.LngLat(building.grapPositX,building.grapPositY));
      //var scale = map.getScale(resolution);
      //console.log("dpi:" + window.devicePixelRatio);
      //console.log("grapPosit resolution:" + resolution);
      //var size = map.getSize();
      //console.log("map size:" + size);
      // range（米）的像素数量
      var pixel = Math.round(range / (1 / resolution / 2));
      //console.log(building.buildName + " pixel:" + pixel);
      return pixel;
    };

    buildingData.forEach(building => {
      switch(building.buildType){
          // 大厦
        case '5':
          addModel(building);
          break;
        default:
          addBox(building,getPixelPerRange(building,building.buildLong),getPixelPerRange(building,building.buildWidth),getPixelPerRange(building,building.buildHeight));
          break;
      }
      var height = 0;
      if (building.buildType === 5) {
        height = 650;
      } else {
        height = getPixelPerRange(building, building.buildHeight)
      }
      var A = new AMap.Text({
        text: building.buildName,
        position: [building.grapPositX, building.grapPositY],
        height: height,
        verticalAlign: 'bottom',
        map: map,
        visible: true,
        style: {
          'background-color': '#3399FF',
          /*'border-color': 'white',*/
          'color': 'white',
          'opacity': '.8',
          'font-size': '10px'
        }
      }).on('click', function (e) {
        //console.log(e);
        if (showInfo)
          showInfo(e);
      }).setExtData(building);
      //A.setMap(map);
    });
    var array = [];
    map.getAllOverlays().forEach(item => {
      if(item.CLASS_NAME==='AMap.Text'){
        //console.log(item);
        array.push(item);
      }
    })
    map.setFitView(array);
    //map.clearMap();
  };
}

export function createBuildingMarker (map, buildingData, showInfo, icon) {

  return function () {
    var getPixelPerRange = function(building, range){
      //debugger
      // "每像素代表" + scale + "米"
      var resolution = map.getResolution(new AMap.LngLat(building.grapPositX,building.grapPositY));
      //var scale = map.getScale(resolution);
      //console.log("dpi:" + window.devicePixelRatio);
      //console.log("grapPosit resolution:" + resolution);
      //var size = map.getSize();
      //console.log("map size:" + size);
      // range（米）的像素数量
      var pixel = Math.round(range / (1 / resolution / 2));
      //console.log(building.buildName + " pixel:" + pixel);
      return pixel;
    };

    map.remove(map.getAllOverlays("marker"));
    map.setFitView();//地图自适应

    var height = 0;

    buildingData.forEach(building => {
      if (building.buildType === 5) {
        height = 650;
      } else {
        height = getPixelPerRange(building, building.buildHeight)
      }
      if (icon) {
        var marker = new AMap.Marker({
          position: new AMap.LngLat(building.grapPositX, building.grapPositY),
          icon: require('assets/' + icon),
          map: map
        }).on('click', function (e) {
          //console.log(e);
          if (showInfo)
            showInfo(e);
        }).setExtData(building);
      } else {
        var marker = new AMap.Marker({
          position: new AMap.LngLat(building.grapPositX, building.grapPositY),
          map: map
        }).on('click', function (e) {
          //console.log(e);
          if (showInfo)
            showInfo(e);
        }).setExtData(building);
      }
    });
    map.setFitView(map.getAllOverlays("marker"));
  }

}

export function createDistrictPolygon(DistrictName, map) {
     //加载行政区划插件
    AMap.service('AMap.DistrictSearch', function () {
      var opts = {
        subdistrict: 1,   //返回下一级行政区
        extensions: 'all',  //返回行政区边界坐标组等具体信息
        level: 'district'  //查询行政级别为区/县
      };
      //实例化DistrictSearch
      let district = new AMap.DistrictSearch(opts);
      district.setLevel('district');
      //行政区查询
      district.search(DistrictName, function (status, result) {

        var bounds = result.districtList[0].boundaries;
        var polygons = [];
        if (bounds) {
          for (var i = 0, l = bounds.length; i < l; i++) {
            for(var j=0;j<bounds[i].length;j++){
              polygons.push([bounds[i][j].lng,bounds[i][j].lat]);
            }
          }
          var polyline = new AMap.Polyline({
            path: polygons,
            isOutline: true,
            outlineColor: '#ffeeff',
            borderWeight: 1,
            strokeColor: "#CC66CC",
            strokeOpacity: 1,
            strokeWeight: 2,
            // 折线样式还支持 'dashed'
            strokeStyle: "solid",
            // strokeStyle是dashed时有效
            strokeDasharray: [10, 5],
            lineJoin: 'round',
            lineCap: 'round',
            zIndex: 50,
          })
          polyline.setMap(map);

          // for (var i = 0, l = bounds.length; i < l; i++) {
          //   //生成行政区划polygon
          //   var polygon = new AMap.Polygon({
          //     map: map,
          //     strokeWeight: 1,
          //     path: bounds[i],
          //     fillOpacity: 0.3,
          //     fillColor: '#CCF3FF',
          //     strokeColor: '#CC66CC',
          //     zIndex: -1
          //   });
          //   polygons.push(polygon);
          // }
       //   map.setFitView();//地图自适应
        }
      });
    });

}

export function createLandHot (map, landData) {
  var heatmapOpts = {
    //3d 相关的参数
    '3d': {
      //热度转高度的曲线控制参数，可以利用左侧的控制面板获取
      heightBezier: [0.4, 0.2, 0.4, 0.8],
      //取样精度，值越小，曲面效果越精细，但同时性能消耗越大
      gridSize: 2,
      heightScale: 1
    }
  };
  var heatmap = new AMap.Heatmap(map, heatmapOpts);

  heatmap.setDataSet({
    data: landData
  });

}

export function createMarkerCluster(map, tag, enterpriseInfo, markerEvents) {
  //debugger
  var cluster, marker, markers=[], bgcolor, bdcolor, sdcolor;
  enterpriseInfo.forEach(item => {
    if (item.alarmCount > 0) {
      bgcolor = "hsla(0, 100%, 50%, 0.7)";
      bdcolor = "hsl(0, 100%, 40%)";
      sdcolor = "hsl(0, 100%, 50%)";
    }
    else {
      bgcolor = "hsla(180, 100%, 50%, 0.7)";
      bdcolor = "hsl(180, 100%, 40%)";
      sdcolor = "hsl(180, 100%, 50%)";
    }
    marker = new AMap.Marker({
      position: [item.enterpriseInfo.longitude, item.enterpriseInfo.latitude],
      content: '<div style="line-height:24px; font-size: 12px; text-align: center; background-color:'+bgcolor+' ; height: 24px; width: 24px; border: 1px solid '+bdcolor+'; border-radius: 12px; box-shadow: '+sdcolor+' 0px 0px 1px;">'+item.alarmCount+'</div>',
      offset: new AMap.Pixel(-15, -15)
    });
    markers.push(marker);
    marker.on('click', function (e) {
      //console.log(e);
      if (markerEvents.click)
        markerEvents.click(e);
    }).on('mouseout', function (e) {
      //console.log(e);
      if (markerEvents.mouseout)
        markerEvents.mouseout(e);
    }).setExtData(item.enterpriseInfo);
  });

  var count = markers.length;
  var _renderCluserMarker = function (context) {
    var factor = Math.pow(context.count / count, 1 / 18);
    var div = document.createElement('div');
    var Hue = 180 - factor * 180;
    var bgColor = 'hsla(' + Hue + ',100%,50%,0.7)';
    var fontColor = 'hsla(' + Hue + ',100%,20%,1)';
    var borderColor = 'hsla(' + Hue + ',100%,40%,1)';
    var shadowColor = 'hsla(' + Hue + ',100%,50%,1)';
    div.style.backgroundColor = bgColor;
    var size = Math.round(30 + Math.pow(context.count / count, 1 / 5) * 20);
    div.style.width = div.style.height = size + 'px';
    div.style.border = 'solid 1px ' + borderColor;
    div.style.borderRadius = size / 2 + 'px';
    div.style.boxShadow = '0 0 1px ' + shadowColor;
    div.innerHTML = context.count;
    div.style.lineHeight = size + 'px';
    div.style.color = fontColor;
    div.style.fontSize = '14px';
    div.style.textAlign = 'center';
    context.marker.setOffset(new AMap.Pixel(-size / 2, -size / 2));
    context.marker.setContent(div)
  };

  if (cluster) {
    cluster.setMap(null);
  }
  if (tag == 1) {//完全自定义
    cluster = new AMap.MarkerClusterer(map, markers, {
      gridSize: 80,
      renderCluserMarker: _renderCluserMarker
    });
  } else {//默认样式
    cluster = new AMap.MarkerClusterer(map, markers, {gridSize: 80});
  }
  map.setFitView(markers);
}
