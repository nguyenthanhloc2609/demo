<script type="text/javascript" th:inline="javascript">
    $(function () {

        initDateRange();

        $("#searchData").click(function () {
            loadTransactionsList();
        });

        function getMaxDate() {
            var maxDate = new Date();
            maxDate.setDate(maxDate.getDate() - 1);
            return maxDate;
        }

        function initDateRange() {
            var $startDate = $("#input-search-start-date");
            var $endDate = $("#input-search-end-date");
            // Init 1 week range
            var maxDate = getMaxDate();
            $endDate.val(formatDate(maxDate));
            maxDate.setDate(maxDate.getDate() - 29);
            $startDate.val(formatDate(maxDate));
            // Init date-range picker
            $("#search-daterange").datepicker({
                locale: 'vi',
                format: "dd/mm/yyyy",
                endDate: getMaxDate(),
                zIndexOffset: 1000
            });
            // In 30 days range
            $startDate.datepicker().on('changeDate', function (ev) {
                var maxDate = getMaxDate();
                var date = $startDate.datepicker('getDate');
                date = new Date(date.setDate(date.getDate() + 29));
                if (date < maxDate) {
                    $endDate.val(formatDate(date));
                    $endDate.datepicker('update', date);
                }
            });
            $endDate.datepicker().on('changeDate', function (ev) {
                var maxDate = $endDate.datepicker('getDate');
                var date = $startDate.datepicker('getDate');
                maxDate = new Date(maxDate.setDate(maxDate.getDate() - 29));
                if (date < maxDate) {
                    $startDate.val(formatDate(maxDate));
                    $startDate.datepicker('update', maxDate);
                }
            });
        }

        function getStartDate() {
            var date = $("#input-search-start-date").datepicker('getDate');
            return date.getTime();
        }

        function getEndDate() {
            var date = $("#input-search-end-date").datepicker('getDate');
            return date.getTime();
        }

        const pagination = new Pagination();

        initTransactionsList();

        function initTransactionsList() {
            pagination.reset();
            loadTransactionsList();
        }

        function loadTransactionsList() {
            var projectId = localStorage.getItem('project_id');
            var offset = pagination.offset() || 0;
            var limit = pagination.limit() || 20;
            var startDate = getStartDate();
            var endDate = getEndDate();
            var filterDate = startDate + '|' + endDate;
            var searchParams = encodeURIComponent(filterDate);
            getRequest({
                url: "/statistics/" + projectId + "/transactions_paging?searchParams=" + searchParams + "&limit=" + limit + "&offset=" + offset,
                success: function (data) {
                    onLoadedTransactionsList(data);
                },
                error: function (e) {
                }
            });
        }

        function onLoadedTransactionsList(data) {
            var paginData = data['pagination'] || {};
            pagination.render('list-pagination', loadTransactionsList, {
                count: data['count'],
                limit: paginData['limit'],
                total: paginData['total'],
                offset: paginData['offset']
            });
            var list = data['list'] || [];
            var html = '';
            for (var i = 1; i <= list.length; i++) {
                var item = list[i - 1];
                var time = new Date(Date.parse(item['createdAt']));
                var convertedDateTime = formatDateTime(time);
                var countNumber = Number.parseInt(paginData['offset']) + i;
                html += '<tr>'
                        + '<td class="text-left">' + countNumber + '</td>'
                        + '<td>' + encodeHtmlEntity(item['action']) + '</td>'
                        + '<td>' + convertedDateTime + '</td>'
                        + '<td>' + encodeHtmlEntity(item['method']) + '</td>'
                        + '<td>' + encodeHtmlEntity(item['url']) + '</td>'
                        + '<td>' + encodeHtmlEntity(item['result']) + '</td>'
                        + '</tr>';
            }
            $('#tbody-list').html(html);
        }

    });
</script>