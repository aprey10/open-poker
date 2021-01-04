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