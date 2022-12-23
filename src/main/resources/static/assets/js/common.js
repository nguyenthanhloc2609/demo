function postRequest(options) {
    var header = 'X-CSRF-TOKEN';
    var token = $("meta[name='_csrf']").attr('content');
    $.ajax({
        type: options['type'] || "POST",
        contentType: "application/json",
        url: options['url'],
        data: JSON.stringify(options['data']),
        dataType: 'json',
        timeout: options['timeout'] || 60000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
            if (options['start']) {
                options['start']();
            }
        },
        success: options['success'],
        error: function (e) {
            var msg = e.responseJSON || {message: e.statusText, description: ''};
            msg['httpStatus'] = e.status;
            options['error'](msg);
        },
        complete: options['end'],
        async: options['async']
    });
}

function putRequest(options) {
    var header = 'X-CSRF-TOKEN';
    var token = $("meta[name='_csrf']").attr('content');
    $.ajax({
        type: options['type'] || "PUT",
        contentType: "application/json",
        url: options['url'],
        data: JSON.stringify(options['data']),
        timeout: options['timeout'] || 60000,
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
            if (options['start']) {
                options['start']();
            }
        },
        success: options['success'],
        error: function (e) {
            var msg = e.responseJSON || {message: e.statusText, description: ''};
            msg['httpStatus'] = e.status;
            options['error'](msg);
        },
        complete: options['end'],
        async: options['async']
    });
}

function getRequest(options) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: options['url'],
        timeout: options['timeout'] || 60000,
        beforeSend: options['start'],
        success: options['success'],
        error: function (e) {
            var msg = e.responseJSON || {message: e.statusText, description: ''};
            msg['httpStatus'] = e.status;
            options['error'](msg);
        },
        complete: options['end'],
        async: options['async']
    });
}

// function postFormRequest(options, formData) {
//     var header = 'X-CSRF-TOKEN';
//     var token = $("meta[name='_csrf']").attr('content');
//     $.ajax({
//         type: "POST",
//         contentType: "application/json",
//         url: options['url'],
//         data: formData,
//         timeout: options['timeout'] || 60000,
//         dataType: 'json',
//         processData: false,
//         beforeSend: function (xhr) {
//             xhr.setRequestHeader(header, token);
//             if (options['start']) {
//                 options['start']();
//             }
//         },
//         success: options['success'],
//         error: function (e) {
//             var msg = e.responseJSON || {message: e.statusText, description: ''};
//             msg['httpStatus'] = e.status;
//             options['error'](msg);
//         },
//         complete: options['end'],
//         async: options['async']
//     });
// }
//
function deleteRequest(options) {
    var header = 'X-CSRF-TOKEN';
    var token = $("meta[name='_csrf']").attr('content');
    $.ajax({
        type: "DELETE",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
            if (options['start']) {
                options['start']();
            }
        },
        async: options['async'],
        url: options['url'],
        timeout: options['timeout'] || 60000,
        success: options['success'],
        error: function (e) {
            var msg = e.responseJSON || {message: e.statusText, description: ''};
            msg['httpStatus'] = e.status;
            options['error'](msg);
        },
        complete: options['end'],
        statusCode: {
            401: function () {
                window.location.reload();
            }
        }
    });
}
//
// function showSuccessToast(msg) {
//     notification.raise("Thông báo", msg || '', "success", 1000);
// }
//
// function showErrorToast(title, msg) {
//     title = title || 'Lỗi';
//     notification.raise(title, msg || '', "error", 1000);
// }
//
// function resetToastPosition() {
//     $('.jq-toast-wrap').removeClass('bottom-left bottom-right top-left top-right mid-center');
//     $(".jq-toast-wrap").css({"top": "", "left": "", "bottom": "", "right": ""});
// }
//
// function sendGlobalEvent(action, data) {
//     $(document).trigger(action, data);
// }
//
// function listenGlobalEvent(action, callback) {
//     $(document).on(action, callback);
// }
//
// function md2html(text) {
//     return globalMd.render(text || '');
// }
//
function numberWithCommas(x) {
    return x.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ".");
}
//
// function formatDate(date) {
//     var dd = date.getDate();
//     var mm = date.getMonth() + 1; //January is 0!
//     var yyyy = date.getFullYear();
//     if (dd < 10) {
//         dd = '0' + dd;
//     }
//     if (mm < 10) {
//         mm = '0' + mm;
//     }
//     return dd + '/' + mm + '/' + yyyy;
// }
//
// function showOverlay(options) {
//     var options = options || {title: "Đang tải dữ liệu, vui lòng đợi..."};
//     $('.overlay #overlay-title').text(options.title);
//     $('.overlay').show();
// }
//
// function hideOverlay() {
//     $('.overlay #overlay-title').text('');
//     $('.overlay').hide();
// }
//
// function disableButton(disable, $target) {
//     var text = $target.text();
//     var html = text;
//     $target.prop('disabled', disable);
//     if (disable) {
//         html = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ' + text;
//     }
//     $target.html(html);
// }
//
// function matchSearchComboText(params, data) {
//     // If there are no search terms, return all of the data
//     if ($.trim(params.term) === '') {
//         return data;
//     }
//
//     // Do not display the item if there is no 'text' property
//     if (typeof data.text === 'undefined') {
//         return null;
//     }
//
//     // `params.term` should be the term that is used for searching
//     // `data.text` is the text that is displayed for the data object
//     var dataText = data.text || '';
//     dataText = dataText.toLowerCase();
//     var searchText = params.term || '';
//     searchText = searchText.toLowerCase();
//     if (dataText.indexOf(searchText) > -1) {
//         var modifiedData = $.extend({}, data, true);
//
//         // You can return modified objects from here
//         // This includes matching the `children` how you want in nested data sets
//         return modifiedData;
//     }
//
//     // Return `null` if the term should not be displayed
//     return null;
// }
//
// var decodeHtmlEntity = function (str) {
//     if (!str) {
//         return '';
//     }
//     return str.replace(/&#(\d+);/g, function (match, dec) {
//         return String.fromCharCode(dec);
//     });
// }
//
// var encodeHtmlEntity = function (str) {
//     if (!str) {
//         return '';
//     }
//     var buf = [];
//     for (var i = str.length - 1; i >= 0; i--) {
//         buf.unshift(['&#', str[i].charCodeAt(), ';'].join(''));
//     }
//     return buf.join('');
// }
//
// function encodeHtmlEntityUtf8(str) {
//     return str.replace(/[\u00A0-\u9999\<\>\&\'\"\\\/]/gim, function (c) {
//         return '&#' + c.charCodeAt(0) + ';';
//     });
// }
//
// function formatDateTime(datetime) {
//     if (!datetime) {
//         return '';
//     }
//     var month = datetime.getMonth() + 1;
//     month = (month >= 10) ? month : '0' + month;
//     var date = datetime.getDate() >= 10 ? datetime.getDate() : '0' + datetime.getDate();
//     var hour = datetime.getHours() >= 10 ? datetime.getHours() : '0' + datetime.getHours();
//     var min = datetime.getMinutes() >= 10 ? datetime.getMinutes() : '0' + datetime.getMinutes();
//     var sec = datetime.getSeconds() >= 10 ? datetime.getSeconds() : '0' + datetime.getSeconds();
//     return date + '/' + month + '/' + datetime.getFullYear()
//         + " " + hour + ":" + min + ":" + sec;
// }
//
// function removeItemOnce(arr, value) {
//     var index = arr.indexOf(value);
//     if (index > -1) {
//         arr.splice(index, 1);
//     }
//     return arr;
// }
//
// function parseError(e) {
//     var msg = e['message'] || 'Lỗi thực hiện';
//     var json = {};
//     try {
//         json = JSON.parse(e['error'] || '');
//     } catch (e) {
//         json = {};
//     }
//     return {
//         message: msg,
//         detail: json['message'] || '',
//         type: json['type'] || ''
//     };
// }
