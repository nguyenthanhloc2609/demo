var notification = {
    'confirmLabel': '',
    'cancelLabel': '',
    'init': function (confirmLabel, cancelLabel) {
        this.cancelLabel = cancelLabel,
            this.confirmLabel = confirmLabel
    },
    'hide': function () {
        bootbox.hideAll();
    },
    'loading': function (title, processMessage, doneMessage, erroMessage, closeButton, delay) {
        var dialog = bootbox.dialog({
            size: "small",
            message: '<center><div class="cp-spinner-block"><div class="cp-spinner cp-skeleton"></div></div></center>',
            closeButton: false
        });
    },
    'confirm': function (title, message, callback) {
        var confirmation = bootbox.confirm({
            title: title,
            closeButton: false,
            message: message,
            buttons: {
                confirm: {
                    label: "<i class='icon icon-send'></i> Đồng ý",
                    className: 'btn-primary'
                },
                cancel: {
                    label: "<i class='icon icon-remove'></i> Hủy",
                    className: 'btn-danger'
                }
            },
            callback: callback
        });

        return confirmation;
    },
    'raise': function (title, message, type, delay) {
        this.hide();
        new PNotify({
            title: title,
            text: message,
            type: type
        });
    },
    'notice': function (title, message, type, isSticky) {
        PNotify.desktop.permission();
        (new PNotify({
            title: title,
            text: message,
            type: type,
            hide: isSticky,
            desktop: {
                desktop: true,
                icon: '/Content/Images/logo.png'
            }
        })).get().click(function (e) {
            if ($('.ui-pnotify-closer, .ui-pnotify-sticker, .ui-pnotify-closer *, .ui-pnotify-sticker *').is(e.target)) return;
        });
    },
    'prompt': function (title, type, callback) {
        if (type == 'select') {
            bootbox.prompt({
                title: title,
                inputType: type,
                inputOptions: [
                    {
                        text: 'Chậm gia hạn',
                        value: 'CHAMGIAHAN',
                    },
                    {
                        text: 'Vi phạm điều khoản',
                        value: 'VIPHAMDIEUKHOAN',
                    }
                ],
                callback: callback
            });
        } else {
            bootbox.prompt({
                title: title,
                callback: callback
            });
        }
    },
    'innerLoading': function (element){
        $(element).html('<br/><div class="spinner-border text-primary" role="status"></div><span class="sr-only">đang tải dữ liệu...</span>');
    }
}

new ClipboardJS('.btn-clipboard');
