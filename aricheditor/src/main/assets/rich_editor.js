/**
 * Copyright (C) 2017 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var RE = {
    init: function init() {
		//初始化内部变量
		var _self = this;
		_self.initSetting();
	},
	setting: {
    		screenWidth: 0,
    		margin: 20
    	},
	initSetting: function initSetting() {
    		var _self = this;
    		_self.setting.screenWidth = window.innerWidth - 20;
    	},
	execformatBlock: function exec(command) {
    		//执行指  zan令
    		var _self = this;
    		var value = '<' + command + '>';
    		document.execCommand('formatBlock', false, value);
    	}

};
RE.init();


RE.currentSelection = {
    "startContainer": 0,
    "startOffset": 0,
    "endContainer": 0,
    "endOffset": 0};

RE.editor = document.getElementById('editor');
RE.title = document.getElementById('title');

document.addEventListener("selectionchange", function() { RE.backuprange(); });

// Initializations
RE.textcheck = function() {
    AndroidInterface.textChange(RE.getHtml());
    RE.staticWords();
}
RE.titlecheck = function() {
    AndroidInterface.titleChange(RE.getTitle());
}
RE.setHtml = function(contents) {
    RE.editor.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
}

RE.getHtml = function() {
  var ranges = [
          '\ud83c[\udf00-\udfff]',
          '\ud83d[\udc00-\ude4f]',
          '\ud83d[\ude80-\udeff]'
      ];
   return RE.editor.innerHTML.replace(new RegExp(ranges.join('|'), 'g'), '');
}
RE.getTitle = function() {
    return RE.title.innerText;
}

RE.setBaseTextColor = function(color) {
    RE.editor.style.color  = color;
}

RE.setBaseFontSize = function(size) {
    RE.editor.style.fontSize = size;
}

RE.setPadding = function(left, top, right, bottom) {
  RE.editor.style.paddingLeft = left;
  RE.editor.style.paddingTop = top;
  RE.editor.style.paddingRight = right;
  RE.editor.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function(color) {
    document.body.style.backgroundColor = color;
}

RE.setBackgroundImage = function(image) {
    RE.editor.style.backgroundImage = image;
}

RE.setWidth = function(size) {
    RE.editor.style.minWidth = size;
}
RE.setHeight = function(size) {

    RE.editor.style.height = size;
}

RE.setTextAlign = function(align) {
    RE.editor.style.textAlign = align;
}

RE.setVerticalAlign = function(align) {
    RE.editor.style.verticalAlign = align;
}

RE.setPlaceholder = function(placeholder) {
    RE.editor.setAttribute("placeholder", placeholder);
}

RE.setInputEnabled = function(inputEnabled) {
    RE.editor.contentEditable = String(inputEnabled);
}

RE.undo = function() {
    document.execCommand('undo', false, null);
}

RE.redo = function() {
    document.execCommand('redo', false, null);
}

RE.setBold = function() {
    document.execCommand('bold', false, null);
}

RE.setItalic = function() {
    document.execCommand('italic', false, null);
}

RE.setSubscript = function() {
    document.execCommand('subscript', false, null);
}

RE.setSuperscript = function() {
    document.execCommand('superscript', false, null);
}

RE.setStrikeThrough = function() {
    document.execCommand('strikeThrough', false, null);
}

RE.setUnderline = function() {
    document.execCommand('underline', false, null);
}

RE.setBullets = function() {
    document.execCommand('insertUnorderedList', false, null);
}

RE.setNumbers = function() {
    document.execCommand('insertOrderedList', false, null);
}

RE.setTextColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('foreColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setTextBackgroundColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('hiliteColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setFontSize = function(fontSize){
    document.execCommand("fontSize", false, fontSize);
}

RE.setHeading = function(heading,b) {
        if(b)
            document.execCommand('formatBlock', false, '<h'+heading+'>');
        else
            document.execCommand('formatBlock', false, '<p>');

}

RE.setIndent = function() {
    document.execCommand('indent', false, null);
}

RE.setOutdent = function() {
    document.execCommand('outdent', false, null);
}

RE.setJustifyLeft = function() {
    document.execCommand('justifyLeft', false, null);
}

RE.setJustifyCenter = function() {
    document.execCommand('justifyCenter', false, null);
}

RE.setJustifyRight = function() {
    document.execCommand('justifyRight', false, null);
}

RE.setBlockquote = function(b) {
    document.execCommand('formatBlock', false, '<blockquote>');
        if(b)
            document.execCommand('formatBlock', false, '<blockquote>');
        else
            document.execCommand('formatBlock', false, '<p>');
}

RE.insertImage = function(url,width, height) {
  		var newWidth = 0,
  		    newHeight = 0;
  		var screenWidth = RE.setting.screenWidth;
  		if (width > screenWidth) {
  			newWidth = screenWidth;
  			newHeight = height * newWidth / width;
  		} else {
  			newWidth = width;
  			newHeight = height;
  		}
  		var image = '<div><br></div>\n\t\t\t\t<div class="img-block"> \n\t\t\t\t <img class="images" style="width: ' + newWidth + 'px; height: ' + newHeight + 'px;" src="' + url + '"/>\n\t\t\t</div><div><br></div>';
  		RE.insertHTML(image);
}

RE.insertHTML = function(html) {
    RE.restorerange();
    document.execCommand('insertHTML', false, html);
}

RE.insertLine = function(){
	const html = '<hr><div><br></div><div><br></div>';
	RE.insertHTML(html);
}



RE.setTodo = function(text) {
    var html = '<input type="checkbox" name="'+ text +'" value="'+ text +'"/> &nbsp;';
    document.execCommand('insertHTML', false, html);
}

RE.prepareInsert = function() {
    RE.backuprange();
}

RE.backuprange = function(){
    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
      var range = selection.getRangeAt(0);
      RE.currentSelection = {
          "startContainer": range.startContainer,
          "startOffset": range.startOffset,
          "endContainer": range.endContainer,
          "endOffset": range.endOffset};
    }
}

RE.restorerange = function(){
    var selection = window.getSelection();
    selection.removeAllRanges();
    var range = document.createRange();
    range.setStart(RE.currentSelection.startContainer, RE.currentSelection.startOffset);
    range.setEnd(RE.currentSelection.endContainer, RE.currentSelection.endOffset);
    selection.addRange(range);
}

RE.enabledEditingItems = function(e) {
    var items = [];
    if (document.queryCommandState('bold')) {
        items.push('bold');
    }
    if (document.queryCommandState('italic')) {
        items.push('italic');
    }
    if (document.queryCommandState('strikeThrough')) {
        items.push('strikeThrough');
    }
    if (document.queryCommandState('underline')) {
        items.push('underline');
    }
    if (document.queryCommandState('h1')) {
        items.push('h1');
    }
    if (document.queryCommandState('insertOrderedList')) {
        items.push('orderedList');
    }
    if (document.queryCommandState('insertUnorderedList')) {
        items.push('unorderedList');
    }
    var formatBlock = document.queryCommandValue('formatBlock');//h1 h2 这些内容检测
    if (formatBlock.length > 0) {
        items.push(formatBlock);
    }

    window.location.href = "re-state://" + encodeURI(items.join(','));
}
RE.editor.style.minHeight = window.innerHeight - 69 + 'px';

RE.focus = function() {
    var range = document.createRange();
    range.selectNodeContents(RE.editor);
    range.collapse(false);
    var selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    RE.editor.focus();
}


RE.blurFocus = function() {
    RE.editor.blur();
}

RE.staticWords=function() {
		var content = RE.editor.innerHTML.replace(/<div\sclass="tips">.*<\/div>|<\/?[^>]*>/g, '').replace(/\s+/, '').trim();
		AndroidInterface.contentLength(content.length)
}

RE.removeFormat = function() {
    document.execCommand('removeFormat', false, null);
}
// Event Listeners
RE.editor.addEventListener("input", RE.textcheck);
RE.title.addEventListener("input", RE.titlecheck);

RE.editor.addEventListener("keyup", function(e) {
    var KEY_LEFT = 37, KEY_RIGHT = 39;
    if (e.which == 8) {
       RE.textcheck();
    }
    if (e.which == KEY_LEFT || e.which == KEY_RIGHT || e.which ==8 || e.which == 13) {//监听键盘换行移动回车操作重置底部按钮
       RE.enabledEditingItems(e);
       }
    }
);



RE.editor.addEventListener("click", RE.enabledEditingItems);


RE.title.addEventListener("keypress",function(evt){
if (evt.which == 13 ) {
 evt.preventDefault();//拦截掉回车键 防止过多行
		}
	});

RE.title.addEventListener("focus",function(evt){
    			AndroidInterface.titlefocuse();
    	});
RE.editor.addEventListener("focus",function(evt){
    			AndroidInterface.editorfocuse();
    	});
RE.editor.addEventListener("blur",function(evt){
    			AndroidInterface.editorblur();
    	});

RE.title.addEventListener("keyup",function(evt){  //监听title回退
if (evt.which == 13 ) {
 evt.preventDefault();
    RE.editor.focus();
			AndroidInterface.titleKeyUpBack;
		}
	});

