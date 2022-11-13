// Class definition
var KTTypeahead = function() {

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

    var demo2 = function() {
        // constructs the suggestion engine
        var bloodhound = new Bloodhound({
            datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.name)},
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            // `states` is an array of state names defined in "The Basics"
            // local: states
            remote: {
                url: "customer/name?name=%QUERY",
                filter: function(data) {
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

        $('#cusName').bind('typeahead:select', function(e, datum) {
            $('#cusName').typeahead('val', datum.name);
            $('#pay').val(datum.billing);
            $('#note1').val(datum.note);
            // console.log($('#hidden-input').val());
        });
    }

    return {
        // public functions
        init: function() {
            demo2();
        }
    };
}();

jQuery(document).ready(function() {
    KTTypeahead.init();
});
