/*
 * 动态添加 CSS 样式
 * @param selector {string} 选择器
 * @param rules {string} CSS样式规则
 * @param index {number} 插入规则的位置, 靠后的规则会覆盖靠前的，默认在后面插入
 */
var addCssRule = function() {
 // 创建一个 style， 返回其 stylesheet 对象
 function createStyleSheet() {
  var style = document.createElement('style');
  style.type = 'text/css';
  document.head.appendChild(style);
  return style.sheet;
 }
  
 // 创建 stylesheet 对象
 var sheet = createStyleSheet();
  
 // 返回接口函数
 return function(selector, rules, index) {
  index = index || 0;
  sheet.insertRule(selector + "{" + rules + "}", index);
 }
}();


;(function($){
	$.fn.ZoomMark = function(method){
		var settings = {
			'markList':[],
			'markColor': '#d31145',
			'markShape': 'square',
			'showMarkNumber':true,
			'afterMark': function(){},
			'showMarkMsg': function(){}
		};
		var mContainer = this;
		var mImg;
		var mMarks = [];
		var position = {
			'x': 0,
			'y': 0,
			'scale': 1,
			'width':0,
			'height':0,
			'rotate':0
		};
		var mousePosition = {
			'isMouseDown': false,
			x: 0,
			y: 0
		}; 
		this.publicMethods = {
			init:function(options){
				if(mContainer.data('ZoomMarkData'))
					return;
				settings = $.extend(settings,options);

				mImg = mContainer.children('img');
				if(!mImg){
					$.error( 'ZoomMark:Method ' +  method + ' does not exist on e-smartzoom jquery plugin' );
					return;
				}
				mImg.css('position','absolute');

				mContainer.data('ZoomMarkData',{
					'mImg':mImg,
					'mMarks':mMarks,
					'imgPosition':position,
					'mousePosition':mousePosition,
					'settings':settings
				});

				mContainer.css('overflow','hidden');
				mContainer.css('position','relative');
				mContainer.css('cursor','move');
				mContainer.css('text-align','left');
				var markStyle = 'width:25px;height:25px;position:absolute;transform:translate(-50%,-50%);'
					+'color:#FFF;text-align:center;line-height:25px;opacity:0.6;border-radius:12px';
				addCssRule('.mark',markStyle,0);
				addCssRule('.mark:hover','cursor:hand',0);
				// 绑定鼠标事件
				document.oncontextmenu = function(){return false;}
				// mContainer.bind('contextmenu',function(){
				// event.returnValue=false;
				// });
				mContainer.mousewheel(mouseWheelHandler);
				mContainer.dblclick(function(event){
					mContainer.publicMethods.mark(event.pageX- mContainer.offset().left,event.pageY- mContainer.offset().top);
					settings.afterMark(mMarks);
					event.preventDefault();
				});
				mContainer.mousedown(function(event){
					if(event.which == 1){
						mousePosition.isMouseDown = true;
						mousePosition.x = event.pageX- mContainer.offset().left;
						mousePosition.y = event.pageY- mContainer.offset().top;
						
					}
					/*else if(event.which == 3){
						mContainer.publicMethods.mark(event.pageX- mContainer.offset().left,event.pageY- mContainer.offset().top);
						settings.afterMark(mMarks);
					}*/
					event.preventDefault();
				});
				mContainer.mousemove(function(event){
					if(mousePosition.isMouseDown){
						var positionX=event.pageX- mContainer.offset().left; // 获取当前鼠标相对img的X坐标
     					var positionY=event.pageY- mContainer.offset().top; // 获取当前鼠标相对img的Y坐标

     					mContainer.publicMethods.move(positionX - mousePosition.x,positionY - mousePosition.y);
     					mousePosition.x =  positionX;
     					mousePosition.y = positionY;
     					event.preventDefault();
					}
				});
				mContainer.mouseup(function(event){
					mousePosition.isMouseDown = false;
					event.preventDefault();
					/*console.info('Released:up');*/
				});
				mContainer.mouseout(function(event){
					mousePosition.isMouseDown = false;
					event.preventDefault();
					/*console.info('Released:out');*/
				});

				// mImg.css('transition','width 1s,transform 1s');
				resetImg();
			},
			getData:function(){
				var data = mContainer.data('ZoomMarkData');
				return data;
			},
			mark:function(x,y,name,content){
				var data = mContainer.data('ZoomMarkData');
				var lastMarkId = mMarks.length == 0 ? 0:mMarks[mMarks.length-1].id;
				mMarks.push({
					'id':lastMarkId+1,
					'x': x,
					'y': y,
					'name':name,
					'content':content,
					'color':data.settings.markColor,
					'available':true
				});
				var newMark = mMarks[mMarks.length-1];
				mContainer.append($('<div class="mark" id="mark_'+newMark.id+'" style="background-color:'+data.settings.markColor+'"></div>'));
				var object = $('#mark_'+mMarks.length);
				object.css('left',x);
				object.css('top',y);
				document.getElementById("mark_"+newMark.id).addEventListener('click', showMarkMsg, false);
				object.html(mMarks.length);
				updateMarksNumber(mMarks);
				return mMarks;
			},
			zoom:function(scale,x,y){
				var data = mContainer.data('ZoomMarkData');
				var position = data.imgPosition;
				if(!x){
					x = mContainer.width()/2;
					y = mContainer.height()/2;
				}

				if(scale > 1){
					position.x = position.x - (x-position.x)*(scale-1);
					position.y = position.y - (y-position.y)*(scale-1);
				}
				else{
					position.x = position.x + (x-position.x)*(1-scale);
					position.y = position.y + (y-position.y)*(1-scale);
				}
				
				position.scale = position.scale*scale;
				position.width = position.width*scale;
				position.height = position.height*scale;
				updateImgRect();

				var mMarks = data.mMarks;
				for(var i=0;i<mMarks.length;i++){
					mMarks[i].x = x + ( mMarks[i].x - x)*scale;
					mMarks[i].y = y + ( mMarks[i].y - y)*scale;
				}
				updateMarksPosition(mMarks);
			},
			move:function(x,y){
				var data = mContainer.data('ZoomMarkData');
				var position = data.imgPosition;
				if(!x || !y){
					return;
				}
				position.x += x;
				position.y += y;
				updateImgRect();

				var mMarks = data.mMarks;
				for(var i=0;i<mMarks.length;i++){
					mMarks[i].x = x + mMarks[i].x;
					mMarks[i].y = y + mMarks[i].y;
				}
				updateMarksPosition(mMarks);
			},
			changeSettings: function(options){
				var data = mContainer.data('ZoomMarkData');
				data.settings = $.extend(data.settings,options);
				mContainer.data('ZoomMarkData',data);

			},
			// 参数id指的不是当前排序所在的顺序，而是内部id
			deleteMark: function(removeMarkId){
				if(!removeMarkId)
					return;
				var data = mContainer.data('ZoomMarkData');
				// 当removeMarkId为次序id时
				// for(var i=0;i<data.mMarks.length;i++){
				// if(data.mMarks[i].available)
				// t++;
				// if(t == removeMarkId){
				// trueId = data.mMarks[i].id;
				// data.mMarks[i].available = false;
				// break;
				// }
				// }
				for(var i=0;i<data.mMarks.length;i++){
					if(data.mMarks[i].id == removeMarkId){
						data.mMarks[i].available = false;
					}
				}

				$('#mark_'+removeMarkId).remove();
				updateMarksNumber(data.mMarks);
				return data.mMarks;
			},
			rotate:function(angle){
				var data = mContainer.data('ZoomMarkData');
				// 更改图片参数
				var preAngle = data.imgPosition.rotate;
				data.imgPosition.rotate = angle;

				updateImgRect();
				// 更新mark位置
				var marks = data.mMarks;
				var img = data.mImg;
				var centerX = parseFloat(img.css('left')) + img.width()/2;
				var centerY = parseFloat(img.css('top')) + img.height()/2;
				for(var i=0;i<marks.length;i++){
					var newMark = rotateMark({x:centerX,y:centerY},marks[i],angle-preAngle);
					marks[i].x = newMark.x;
					marks[i].y = newMark.y;
				}
				updateMarksPosition(marks);
			},
			reset:function(){
				var data = mContainer.data('ZoomMarkData');
				var position = data.imgPosition;
				var img = data.mImg;
				var x = 0,y = 0;
				mContainer.publicMethods['zoom'](1.0/position.scale);
				if(img.width()/img.height()>mContainer.width()/mContainer.height()){
					y = (mContainer.height()-img.height())/2;
				}
				else{
					x = (mContainer.width()-img.width())/2;
				}
				mContainer.publicMethods['move'](x-position.x,y-position.y);
				mContainer.publicMethods['rotate'](0);
				return  this;
			},
			destroy:function(){

			}
			
		}

		// 参数判断
		if(!method || typeof method == 'object'){
			return this.publicMethods.init(arguments[0]);
		}
		else if(this.publicMethods[method]){
			return this.publicMethods[method].apply(this,Array.prototype.slice.call(arguments,1));
		}
		else{
			$.error( 'ZoomMark:Method ' +  method + ' does not exist on ZoomMark jquery plugin' );
		}

		function mouseWheelHandler(event,delta){
			var scale = delta>0 ? 2:0.5;
			mContainer.publicMethods.zoom(scale);
			event.preventDefault();
		}

		function resetImg(){
			// 调整图片高宽，嵌入container
			if(mImg.width()/mImg.height() > mContainer.width()/mContainer.height()){
				mImg.width(mContainer.width());
				var y = (mContainer.height()-mImg.height())/2;
				mImg.css('top',y+'px');
				position.y = y;

			}
			else{
				mImg.height(mContainer.height());
				var x = (mContainer.width()-mImg.width())/2;
				mImg.css('left',x+'px');
				position.x = x;
			}
			position.width = mImg.width();
			position.height = mImg.height();
		}

 
		function updateImgRect(){
			var data = mContainer.data('ZoomMarkData');
			var img = data.mImg;
			var position = data.imgPosition;
			img.css('left',position.x+'px');
			img.css('top',position.y+'px');
			img.css('transform',' rotate('+position.rotate+'deg)');
			img.width(position.width);
			img.height(position.height);
		}
		// 根据mMark数据更新位置
		function updateMarksPosition(marks){
			for(var i=0;i<marks.length;i++){
				$('#mark_'+marks[i].id).css('left',marks[i].x);
				$('#mark_'+marks[i].id).css('top',marks[i].y);
			}
		}
		// 根据mMark数据更新编号
		function updateMarksNumber(marks){
			var t = 1;
			for(var i=0;i<marks.length;i++){
				if(marks[i].available){
					$('#mark_'+marks[i].id).html(t++);
				}
			}
		}

		function rotateMark(center,mark,angle){
			var r = Math.sqrt((mark.x-center.x)*(mark.x-center.x)+(mark.y-center.y)*(mark.y-center.y));
			if(r === 0)
				return mark;
			var mX = mark.x - center.x;
			var mY = mark.y - center.y;
			var iniAngle =  (Math.asin(mX/r))*180/Math.PI;
			iniAngle = mY>0?(180-iniAngle):iniAngle;

			var x = center.x + r*Math.sin((iniAngle + angle)*Math.PI/180);
			var y = center.y - r*Math.cos((iniAngle + angle)*Math.PI/180);
			return {x:x,y:y};
		}
	}

	
})($);


/*
 * ! jQuery Mousewheel 3.1.13
 * 
 * Copyright 2015 jQuery Foundation and other contributors Released under the
 * MIT license. http://jquery.org/license
 */
!function(a){"function"==typeof define&&define.amd?define(["jquery"],a):"object"==typeof exports?module.exports=a:a(jQuery)}(function(a){function b(b){var g=b||window.event,h=i.call(arguments,1),j=0,l=0,m=0,n=0,o=0,p=0;if(b=a.event.fix(g),b.type="mousewheel","detail"in g&&(m=-1*g.detail),"wheelDelta"in g&&(m=g.wheelDelta),"wheelDeltaY"in g&&(m=g.wheelDeltaY),"wheelDeltaX"in g&&(l=-1*g.wheelDeltaX),"axis"in g&&g.axis===g.HORIZONTAL_AXIS&&(l=-1*m,m=0),j=0===m?l:m,"deltaY"in g&&(m=-1*g.deltaY,j=m),"deltaX"in g&&(l=g.deltaX,0===m&&(j=-1*l)),0!==m||0!==l){if(1===g.deltaMode){var q=a.data(this,"mousewheel-line-height");j*=q,m*=q,l*=q}else if(2===g.deltaMode){var r=a.data(this,"mousewheel-page-height");j*=r,m*=r,l*=r}if(n=Math.max(Math.abs(m),Math.abs(l)),(!f||f>n)&&(f=n,d(g,n)&&(f/=40)),d(g,n)&&(j/=40,l/=40,m/=40),j=Math[j>=1?"floor":"ceil"](j/f),l=Math[l>=1?"floor":"ceil"](l/f),m=Math[m>=1?"floor":"ceil"](m/f),k.settings.normalizeOffset&&this.getBoundingClientRect){var s=this.getBoundingClientRect();o=b.clientX-s.left,p=b.clientY-s.top}return b.deltaX=l,b.deltaY=m,b.deltaFactor=f,b.offsetX=o,b.offsetY=p,b.deltaMode=0,h.unshift(b,j,l,m),e&&clearTimeout(e),e=setTimeout(c,200),(a.event.dispatch||a.event.handle).apply(this,h)}}function c(){f=null}function d(a,b){return k.settings.adjustOldDeltas&&"mousewheel"===a.type&&b%120===0}var e,f,g=["wheel","mousewheel","DOMMouseScroll","MozMousePixelScroll"],h="onwheel"in document||document.documentMode>=9?["wheel"]:["mousewheel","DomMouseScroll","MozMousePixelScroll"],i=Array.prototype.slice;if(a.event.fixHooks)for(var j=g.length;j;)a.event.fixHooks[g[--j]]=a.event.mouseHooks;var k=a.event.special.mousewheel={version:"3.1.12",setup:function(){if(this.addEventListener)for(var c=h.length;c;)this.addEventListener(h[--c],b,!1);else this.onmousewheel=b;a.data(this,"mousewheel-line-height",k.getLineHeight(this)),a.data(this,"mousewheel-page-height",k.getPageHeight(this))},teardown:function(){if(this.removeEventListener)for(var c=h.length;c;)this.removeEventListener(h[--c],b,!1);else this.onmousewheel=null;a.removeData(this,"mousewheel-line-height"),a.removeData(this,"mousewheel-page-height")},getLineHeight:function(b){var c=a(b),d=c["offsetParent"in a.fn?"offsetParent":"parent"]();return d.length||(d=a("body")),parseInt(d.css("fontSize"),10)||parseInt(c.css("fontSize"),10)||16},getPageHeight:function(b){return a(b).height()},settings:{adjustOldDeltas:!0,normalizeOffset:!0}};a.fn.extend({mousewheel:function(a){return a?this.bind("mousewheel",a):this.trigger("mousewheel")},unmousewheel:function(a){return this.unbind("mousewheel",a)}})});