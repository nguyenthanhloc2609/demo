var Pagination = function() {
	var _this = this;

	var extend = function(options) {
		_this.viewTotalRecords = options['viewTotalRecords']||false;
		_this.offset = options['offset']||0;
		_this.limit = options['limit']||10;
		_this.count = options['count']||0;
		// _this.currentPage = 1+(_this.offset/_this.limit);
		_this.currentPage = 1+_this.offset;
		_this.totalRecords = options['total']||0;
		_this.step = 5;
		_this.totalPages = parseInt(_this.totalRecords/_this.limit);
		_this.totalPages = (_this.totalRecords%_this.limit == 0) ? _this.totalPages : _this.totalPages+1;
		_this.first = _this.currentPage == 1;
		_this.last = _this.currentPage == _this.totalPages;
		if(_this.currentPage - _this.step <= 0) {
			_this.from = 1;
		} else {
			_this.from = _this.currentPage - _this.step;
		}
		if(_this.from + _this.step > _this.totalPages) {
			_this.to = _this.totalPages;
		} else {
			_this.to = _this.from+_this.step;
		}
	};

	var render = function(id, func, options) {
		_this.id = id||'page';
		_this.func = func;
		extend(options);
		create();
		registerEvents();
	};

	var create = function() {
		if(_this.totalPages == 0) {
			$('#'+_this.id).html('');
			$('#'+_this.id).hide();
			return;
		}
		$('#'+_this.id).show();
		var html = '<div class="row ml-1">'; 
		html += '<div class="pull-left form-inline"><span style="margin: 5px;">Hiển thị</span><lable class="select"><select id="'+_this.id+'_cblimit" class="form-control" style="border: 1px solid #ccc;vertical-align:top;padding: 1px 1px 1px 9px;height: 30px;">';
		var limits = [5, 10, 15, 20];
		for(i = 0; i < limits.length; i++) {
			if(limits[i] == _this.limit) {
				html += '<option value="'+limits[i]+'" selected>'+limits[i]+'</option>';
			} else {
				html += '<option value="'+limits[i]+'">'+limits[i]+'</option>';
			}
		}
		html += '</select></lable><span style="margin: 5px;">bản ghi</span></div>';
		
		html += '<div class="ml-auto mr-3" style="dataTables_paginate paging_simple_numbers" >'
				   + '<ul class="pagination pull-right">';
		if(_this.viewTotalRecords) {
			html += '<li style="margin-right: 10px;"><span>Tổng số bản ghi '+_this.totalRecords+'</span><li>';
		}
		if(_this.first === false) {
			html += '<li id="'+_this.id+'-go-first" class="paginate_button page-item previous"><a class="page-link" ><i class="fa fa-angle-double-left" aria-hidden="true"></i></a></li>';
		}else {
			html += '<li id="'+_this.id+'-go-first" class="paginate_button page-item previous disabled"><a class="page-link"><i class="fa fa-angle-double-left" aria-hidden="true"></i></a></li>';
		}
		if(_this.currentPage > 1) {
			html += '<li id="'+_this.id+'-go-prev" class="paginate_button page-item"><a class="page-link" ><i class="fa fa-angle-left" aria-hidden="true"></i></a></li>';
		}
		for(var i = _this.from; i <= _this.to; i++) {
			var page = i;
			if(i == _this.currentPage) {
				html += '<li class="paginate_button page-item active"><a class="page-link">'+page+'</a></li>';
			} else {
				html += '<li class="'+_this.id+'-go-page class="paginate_button page-item"" data-page="'+page+'"><a  class="page-link">'+page+'</a></li>';
			}
		}
		if(_this.currentPage >= 1 && _this.currentPage < _this.totalPages) {
			html += '<li id="'+_this.id+'-go-next" class="paginate_button page-item"><a class="page-link" ><i class="fa fa-angle-right" aria-hidden="true"></i></a></li>';
		}
		if(_this.last === false) {
			html += '<li id="'+_this.id+'-go-last" class="paginate_button page-item"><a class="page-link" ><i class="fa fa-angle-double-right" aria-hidden="true"></i></a></li>';
		}else {
			html += '<li id="'+_this.id+'-go-last" class="paginate_button page-item disabled"><a class="page-link"><i class="fa fa-angle-double-right" aria-hidden="true"></i></a></li>';
		}
		html += '</ul></div></div>';
		$('#'+_this.id).html(html);
	};

	var registerEvents = function() {
		$('#'+_this.id+'-go-prev').on('click', goPrev);
		$('#'+_this.id+'-go-next').on('click', goNext);
		$('.'+_this.id+'-go-page').on('click', goPage);
		$('#'+_this.id+'-go-first').on('click', goFirst);
		$('#'+_this.id+'-go-last').on('click', goLast);
		$('#'+_this.id+'_cblimit').on('change', changeNumberLimit);
	};

	var goPrev = function() {
		if(_this.currentPage > 1) {
			_this.currentPage--;
			buildOffset();
		}
		_this.func();
	};

	var goNext = function() {
		_this.currentPage++;
		buildOffset();
		_this.func();
	};

	var goPage = function() {
		_this.currentPage = $(this).attr('data-page')||1;
		buildOffset();
		_this.func();
	};

	var goFirst = function() {
		_this.currentPage = 1;
		buildOffset();
		_this.func();
	};

	var goLast = function() {
		_this.currentPage = _this.totalPages;
		buildOffset();
		_this.func();
	};

	var buildOffset = function() {
	   // _this.offset = (_this.currentPage-1)*_this.limit;
		_this.offset = _this.currentPage-1;
	}
	
	var changeNumberLimit = function() {
		_this.limit = $('#'+_this.id+'_cblimit').val()||20;
		_this.currentPage = 1;
		_this.offset = 0;
		_this.func();
	};

	var reset = function() {
		_this.offset = 0;
		_this.limit = 10;
		_this.totalPages = 0;
		_this.count = 0;
		_this.currentPage = 1;
		_this.totalRecords = 0;
		_this.first = true;
		_this.last = true;
		_this.step = 5;
		_this.to = 0;
		_this.from = 0;
		$('#'+_this.id).html('');
		$('#'+_this.id).hide();
	};

	return {
		count: function() {
			return _this.count;
		},
		totalPages: function() {
			return _this.totalPages;
		},
		currentPage: function() {
			return _this.currentPage;
		},
		offset: function() {
			return _this.offset;
		},
		limit: function() {
			return _this.limit;
		},
		render: render,
		reset: reset
	}
};
