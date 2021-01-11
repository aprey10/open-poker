//TODO: consider replacing it with some lightweight single page JS framework.
(function ($) {
    $(document).ready(function () {
        opSyncInit();
        JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, context, reason) {
            if (reason === 'issueTableRowRefreshed' || reason === 'pageLoad') {
                setTimeout(opAUIInit, 500);
            }
        });
    });

})(AJS.$ || jQuery);

function opSyncInit() {
    var url = AJS.contextPath() + "/rest/open-poker/1.0/session";
    var sessionStatus = $("#open-poker-js-session-status").val();
    if (sessionStatus === "IN_PROGRESS") {
        sync(url);
    }
}

function opAUIInit() {
    $(".op-estimator-tooltip").tooltip();
    $(".op-aui-select").auiSelect2();
    renderChart();
}

function renderChart() {
    if ($("#open-poker-js-session-status").val() !== "FINISHED") {
        return;
    }

    var estimatesMap = {};
    $.each($(".open-poker-estimate-grade"), function (i, e) {
        var grade = $.trim($(e).html());
        if (estimatesMap[grade]) {
            estimatesMap[grade]++;
        } else {
            estimatesMap[grade] = 1;
        }
    })

    var chartData = {
        data: [], labels: [], colors: []
    }
    var index = 0;
    $.each(estimatesMap, function (k, v) {
        chartData.data.push(v);
        chartData.labels.push(k);
        chartData.colors.push(getColor(index))
        index++;
    });

    initiateChart(chartData);
}

function initiateChart(chartData) {
    var data = {
        datasets: [{
            data: chartData.data,
            backgroundColor: chartData.colors
        }],
        labels: chartData.labels
    };

    new Chart($("#open-poker-results-chart"), {
        type: 'doughnut',
        data: data,
        options: {
            legend: {
                display: false
            },
            tooltips: {
                displayColors: false,
                callbacks: {
                    title: function (tooltipItems, data) {
                        return '';
                    },
                    label: function (tooltipItem, data) {
                        return data.labels[tooltipItem.index];
                    }
                }
            }
        }
    });
}

function sync(url) {
    var issueId = $("#open-poker-js-issueId").val()
    var userId = $("#open-poker-js-userId").val()
    $.ajax({
        url: url,
        contentType: 'application/json; charset=utf-8',
        dataType: "json",
        data: {issueId: issueId, userId: userId}
    }).done(function (data) {
        if (data.status === "IN_PROGRESS") {
            syncEstimators(data.estimators);
            setTimeout(function () {
                sync(url);
            }, 7000);
        }
    });
}

function syncEstimators(estimatorsFromServer) {
    var displayedEstimators = getEstimatorsFromView();

    if (estimatorsFromServer.length > 0 && displayedEstimators.length === 0) {
        // Removes a word placeholder which indicates that no one has voted yet
        $('#open-poker-js-estimators').empty();
    }

    $.each(estimatorsFromServer, function (i, el) {
        if (doesNotContain(displayedEstimators, el.displayName)) {
            addEstimator(el.avatarUrl, el.displayName);
        }
    })
}

function doesNotContain(list, item) {
    return list.indexOf(item) === -1;
}

function getEstimatorsFromView() {
    var estimators = []
    $.each($(".op-estimator-tooltip"), function (i, e) {
        estimators.push(e.getAttribute('data-title'));
    })

    return estimators;
}

function addEstimator(avatarUrl, displayName) {
    var img = $('<img />', {'src': avatarUrl, 'alt': displayName});
    var span = $('<span />', {
        'class': 'aui-avatar-inner op-estimator-tooltip',
        'title': displayName,
        'data-title': displayName
    })
        .append(img);
    var outerSpan = $('<span />', {'class': 'aui-avatar aui-avatar-small estimator-avatar'}).append(span);

    $('#open-poker-js-estimators').append(outerSpan);
}

function getColor(index) {
    var colorArray = [
        '#4C9AFF',
        '#C1C7D0',
        '#79E2F2',
        '#8777D9',
        '#79F2C0',
        '#FFAB00',
        '#00B8D9',
        '#0052CC',
        '#6554C0',
        '#344563',
        '#C1C7D0',
        '#403294',

    ];

    if (index > colorArray.length) {
        return '#C1C7D0';
    }

    return colorArray[index];
}