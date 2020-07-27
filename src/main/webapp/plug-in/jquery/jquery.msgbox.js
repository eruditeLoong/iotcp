/**
 *
 */
(function($) {
	// 蒙板div私有属性
	var div_mask = {
		id : '_j_div_mask_',
		css : {
			"clear" : "both",
			"float" : "left",
			"z-index" : "10000",
			"position" : "fixed",
			"top" : "0px",
			"left" : "0px",
			"cursor" : "not-allowed",
			'border' : 'none'
		}
	};
	// 消息框div私有属性
	var div_msg_box = {
		id : '_j_div_msg_box_',
		css : {
			"z-index" : "10000",
			"position" : "fixed",
			'border' : '2px solid #2484c2',
			'background-color' : 'white',
			'cursor' : 'default',
			'border-radius' : '8px',
			'-moz-border-radius' : '8px',
			'-webkit-border-radius' : '8px'
		}
	};
	// 消息框标题头div私有属性
	var div_msg_box_header = {
		id : '_j_div_msg_box_header_',
		css : {
			'padding' : '1px 5px',
			'color' : 'white',
			'background-color' : '#2484c2',
			'line-height' : '26px',
			'font-weight' : 'bold',
			'border' : 'none'
		}

	};
	// 消息框消息正文div私有属性
	var div_msg_box_message = {
		id : '_j_div_msg_box_message_id',
		css : {
			'overflow' : 'hidden',
			'padding-left' : '10px',
			'padding-top' : '10px',
			"border-width" : '0px'
		}
	};
	// 消息框底部(操作按钮)div私有属性
	var div_msg_box_bottom = {
		id : '_j_div_msg_box_bottom_',
		css : {
			'text-align' : 'center',
			"border-width" : '0px'
		}
	};
	// 消息框按钮私有属性
	var div_msg_box_btn = {
		css : {
			'cursor' : 'pointer',
			'width' : '60px',
			'line-height' : '24px',
			'margin-top' : '5px',
			'margin-right' : '10px',
			'color' : '#fff',
			'background' : '#1b78b4',
			'display' : 'inline-block',
			'font-size' : '12px'
		}
	};
	/**
	 * 创建div元素对象
	 *
	 * @param {string}
	 *            id 元素id属性
	 * @param {string}
	 *            text 元素text属性
	 * @param {object}
	 *            元素样式对象
	 * @return
	 *
	 * <pre><code>
	 * jQuery('&lt;div id=&quot;&quot; style='css'&gt;text&lt;/div&gt;')
	 * </code></pre>
	 */
	function creatediv(id, text, css) {
		return $('<div id="' + id + '">' + text + '</div>').css(css);
	}

	// 蒙板div
	function mask_div() {
		return creatediv(div_mask.id, '', div_mask.css);
	}

	// 消息框标题div
	function msgBox_header(title) {
		return creatediv(div_msg_box_header.id, title, div_msg_box_header.css);
	}

	// 消息框消息正文div
	function msgBox_message(message) {
		return creatediv(div_msg_box_message.id, message, div_msg_box_message.css);
	}

	// 消息框底部div
	function msgBox_bottom() {
		return $("<div id='" + div_msg_box_bottom.id + "'>").css(div_msg_box_bottom.css);
	}

	function mask(opts) {
		var bwh = browser_width_high();
		$mask = mask_div();
		$mask.css({
			"width" : Math.max(bwh.b_c_w, bwh.b_s_w),
			"height" : Math.max(bwh.b_c_h, bwh.b_s_h),
			"background-color" : opts.bgcolor,
			"opacity" : opts.opacity
		});
		return $mask;
	}

	/**
	 * 消息框基本框架
	 *
	 * @param {string}
	 *            title 消息标题
	 * @param {mixed}
	 *            msg 消息正文
	 * @param {int}
	 *            width 消息框宽
	 * @param {int}
	 *            height 消息框高
	 * @result
	 *
	 * <pre><code>
	 * jQuery('
	 * 	&lt;div id=&quot;msgbox&quot;&gt;
	 * 	&lt;div id=&quot;header&quot;&gt;&lt;/div&gt;
	 * 		&lt;div id=&quot;message&quot;&gt;&lt;/div&gt;
	 * 		&lt;div id=&quot;bottom&quot;&gt;&lt;/div&gt;
	 * &lt;/div&gt;
	 * ')对象
	 * </code></pre>
	 */
	function msgBox(title, msg, width, height) {
		var bwh = browser_width_high();
		// 滚动条滚动过的宽+(可视窗口宽-消息框宽)/2
		var x = (bwh.b_c_w - width) / 2;
		var y = (bwh.b_c_h - height) / 2;
		var $box_core = creatediv(div_msg_box.id, '', div_msg_box.css).css({
			'left' : x,
			'top' : y,
			'width' : width,
			'height' : height
		});
		$box_core.append(msgBox_header(title));
		$box_core.append(msgBox_message(msg).css({
			'height' : height - 74
		}));
		$box_core.append(msgBox_bottom());
		return $box_core;
	}

	/**
	 * 消息框按钮.生成<a>name</a>代码，并绑定onclick事件
	 *
	 * @param {string}
	 *            按钮显示名称
	 * @param {function}
	 *            按钮触发事件
	 * @return 返回jQuery('<a>name</a>')对象
	 */
	function msgBox_btn(name, fn) {
		return $('<a>' + name + '</a>').css(div_msg_box_btn.css).bind("click", fn);
	}

	// 浏览器宽、高 ,滚动条宽、高,滚动条滚动过的高、宽
	function browser_width_high() {
		return {
			b_c_w : document.documentElement.clientWidth,
			b_c_h : document.documentElement.clientHeight,
			b_s_w : document.documentElement.scrollWidth,
			b_s_h : document.documentElement.scrollHeight,
			b_s_t : document.documentElement.scrollTop || document.body.scrollTop,
			b_s_l : document.documentElement.scrollLeft || document.body.scrollLeft

		};
	}

	/**
	 * 获取遮罩层或者消息框div对象.这里使用原始方法获取指定元素id是为了方便返回。<br/>
	 * 如果使用jQuery的$(#id)获取，则无法使用||操作.因为$(#id)，当id不存在时返回[]而非null
	 */
	function getMaskOrMsgBoxDiv() {
		return $(document.getElementById(div_mask.id) || document.getElementById(div_msg_box.id));
	}

	// 遮罩层
	$.fn.jmask = function(options) {
		var opts = $.extend({}, $.fn.jmask.defaults, options);
		var bwh = browser_width_high();
		this.css({
			"z-index" : "100000",
			"position" : "fixed",
			"display" : "block",
			'left' : (bwh.b_c_w - this.width()) / 2,
			'top' : (bwh.b_c_h - this.height()) / 2
		});
		this.after(mask(opts));
	};
	// 遮罩层属性
	$.fn.jmask.defaults = {
		bgcolor : '#eee',
		opacity : 0.8
	};

	// 取消遮罩层
	$.fn.junmask = function() {
		this.slideUp("slow", function() {
			$("#" + div_mask.id).hide(0, function() {
				$(this).detach();
			});
		});
	};

	/**
	 * 消息div
	 *
	 * @param {string}
	 *            msg 消息内容
	 * @param {object}
	 *            options 属性对象
	 */
	$.fn.jalert = function(msg, options) {

		var opts = $.extend({}, $.fn.jalert.defaults, options);

		var $msg_box = msgBox(opts.title, msg, opts.width, opts.height);

		var $btn_ok = msgBox_btn("确定", function() {
			getMaskOrMsgBoxDiv().hide("slow", function() {
				$(this).detach();
			});
		});

		$msg_box.find("#" + div_msg_box_bottom.id).append($btn_ok);

		if (opts.mask) {
			$msg_box = mask({
				bgcolor : opts.maskcolor,
				opacity : opts.maskopacity
			}).append($msg_box);
		}
		this.after($msg_box);
	};
	// 消息div默认属性
	$.fn.jalert.defaults = {
		title : '消息框',
		width : 320,
		height : 240,
		mask : true,
		maskcolor : '#eee',
		maskopacity : 0.8
	};

	/**
	 * 确认消息div
	 *
	 * @param {string}
	 *            msg 消息内容
	 * @param {string}
	 *            url 确认消息跳转地址
	 * @param {object}
	 *            属性对象
	 */
	$.fn.jconfirm = function(msg, url, options) {

		var opts = $.extend({}, $.fn.jconfirm.defaults, options);

		var $msg_box = msgBox(opts.title, msg, opts.width, opts.height);

		var $btn_ok = msgBox_btn("确定", function() {
			getMaskOrMsgBoxDiv().hide("slow", function() {
				$(this).detach();
				if (url) {
					window.location.href = url;
				}
			});
		});
		var $btn_cancel = msgBox_btn("取消", function() {
			getMaskOrMsgBoxDiv().hide("slow", function() {
				$(this).detach();
			});
		});
		$msg_box.find("#" + div_msg_box_bottom.id).append($btn_ok).append($btn_cancel);

		if (opts.mask) {
			$msg_box = mask({
				bgcolor : opts.maskcolor,
				opacity : opts.maskopacity
			}).append($msg_box);
		}
		this.after($msg_box);
	};
	// 确认消息div默认属性
	$.fn.jconfirm.defaults = {
		title : '确认消息框',
		width : 320,
		height : 240,
		mask : true,
		maskcolor : '#eee',
		maskopacity : 0.8
	};
})(jQuery); 