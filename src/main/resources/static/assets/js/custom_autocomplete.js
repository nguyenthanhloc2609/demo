// Class definition
var KTTypeahead = function () {

    var demo2 = function () {
        // constructs the suggestion engine
        var bloodhound = new Bloodhound({
            datumTokenizer: function (d) { return Bloodhound.tokenizers.whitespace(d.name) },
            queryTokenizer: Bloodhound.tokenizers.whitespace,

            remote: {
                url: "customers/name?name=%QUERY",
                filter: function (data) {
                    // console.log(data)
                    return data;
                },
                wildcard: "%QUERY"
            }
        });

        $('#cusName').typeahead({
            hint: true,
            highlight: true,
            minLength: 3
        },
            {
                displayKey: 'name',
                source: bloodhound
            });

        $('#cusName').bind('typeahead:select', function (e, datum) {
            $('#cusName').typeahead('val', datum.name);
            var bill = datum.billing;
            var num1, num2;
            if (bill.includes('N')) {
                num2 = Number(bill.split('N')[1]);
                $('#pay').val('N' + (num2 + 1));
            } else if (bill.includes('/')) {
                var arr = bill.split('/');
                num1 = Number(arr[0]);
                num2 = Number(arr[1]);
                if (num1 === num2) {
                    num1++;
                    num2++;
                } else {
                    num1++;
                }
                $('#pay').val(num1 + "/" + num2);
            } else {
                $('#pay').val(datum.billing);
            }

            $('#note1').val(datum.note);
            $('#diagnostic').val(datum.diag);
            var money = datum.money;
            if (!datum.money)
                money = 0;
            if (money < 0)
                $('#money-info').attr('data-original-title', 'Bệnh nhân còn đang nợ ' + money);
            else
                $('#money-info').attr('data-original-title', 'Số tiền còn lại của bệnh nhân: ' + money);

            console.log(datum);
        });
    }

    return {
        // public functions
        init: function () {
            demo2();
        }
    };
}();

jQuery(document).ready(function () {
    KTTypeahead.init();
});
