"use strict";

var KTCalendarBasic = function () {

    return {
        //main function to initiate the module
        init: function () {
            var todayDate = moment().startOf('day');
            var YM = todayDate.format('YYYY-MM');
            var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
            var TODAY = todayDate.format('YYYY-MM-DD');
            var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');

            var calendarEl = document.getElementById('kt_calendar');
            var calendar = new FullCalendar.Calendar(calendarEl, {
                plugins: ['interaction', 'dayGrid'],

                isRTL: KTUtil.isRTL(),
                customButtons: {
                    btnExcel: {
                        text: 'Xuất excel',
                        click: function () {
                            var date = '01 '+ document.getElementsByClassName('fc-center')[0].innerText;
                            console.log(formatDate(date));
                            window.location = "/dashboard/excel?date="+date;
                        }
                    }
                },
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'btnExcel'
                },

                height: 800,
                contentHeight: 780,
                aspectRatio: 3,  // see: https://fullcalendar.io/docs/aspectRatio

                nowIndicator: true,
                now: TODAY + 'T09:25:00', // just for demo

                // views: {
                //     dayGridMonth: { buttonText: 'month' },
                //     timeGridWeek: { buttonText: 'week' },
                //     timeGridDay: { buttonText: 'day' }
                // },

                defaultView: 'dayGridMonth',
                defaultDate: TODAY,
                editable: true,
                selectable: true,
                selectHelper: true,
                eventLimit: true, // allow "more" link when too many events
                navLinks: true,
                events: [
                    // {
                    //     title: 'Click for Google',
                    //     url: '/clinic',
                    //     start: YM + '-01',
                    //     className: "fc-event-solid-info fc-event-light",
                    //     description: 'go to home'
                    // }
                ],

                eventRender: function (info) {
                    var element = $(info.el);

                    if (info.event.extendedProps && info.event.extendedProps.description) {
                        if (element.hasClass('fc-day-grid-event')) {
                            element.data('content', info.event.extendedProps.description);
                            element.data('placement', 'top');
                            KTApp.initPopover(element);
                        } else if (element.hasClass('fc-time-grid-event')) {
                            element.find('.fc-title').append('<div class="fc-description">' + info.event.extendedProps.description + '</div>');
                        } else if (element.find('.fc-list-item-title').lenght !== 0) {
                            element.find('.fc-list-item-title').append('<div class="fc-description">' + info.event.extendedProps.description + '</div>');
                        }
                    }
                },
                select: function (dates) {
                    console.log(dates);
                    var diff = (dates.end - dates.start) / 86400000;
                    if (diff === 1) {
                        window.location = '/tran-a-day'
                        window.localStorage.setItem("date", formatDate(dates.start));
                   }
                }
            });

            calendar.render();
        }
    };
}();

jQuery(document).ready(function () {
    KTCalendarBasic.init();
});

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [day, month, year].join('/');
}
function getMonth(){
    var date = $("#calendar").fullCalendar('getDate');
    var month_int = date.getMonth();
    console.log(month_int);
    //you now have the visible month as an integer from 0-11
  }