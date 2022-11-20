// Class definition
var KTTypeahead = function () {

    // var states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
    //     'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii',
    //     'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana',
    //     'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota',
    //     'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
    //     'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
    //     'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island',
    //     'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
    //     'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
    // ];
    // var states = [{id: "123", name: "locnt", billing: "1/2", note: ""}];

    var demo2 = function () {
        // constructs the suggestion engine
        var bloodhound = new Bloodhound({
            datumTokenizer: function (d) { return Bloodhound.tokenizers.whitespace(d.name) },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            // `states` is an array of state names defined in "The Basics"
            // local: states
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
                // name: 'name',
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
