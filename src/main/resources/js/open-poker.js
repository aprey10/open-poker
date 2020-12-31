//TODO: consider replacing it with some lightweight single page JS framework.
(function ($) {
    var url = AJS.contextPath() + "/rest/open-poker/1.0/session";

    $(document).ready(function () {
        $(".op-estimator-tooltip").tooltip();
        var sessionStatus = $("#open-poker-js-session-status").val();
        if (sessionStatus === "IN_PROGRESS") {
            ping($("#open-poker-js-issueId").val(), url);
        }
    });

})(AJS.$ || jQuery);

function ping(issueId, url) {
    $.ajax({
        url: url,
        contentType: 'application/json; charset=utf-8',
        dataType: "json",
        data: {issueId: issueId}
    }).done(function (data) {
        if (data.status === "IN_PROGRESS") {
            syncEstimators(data.estimators);
            setTimeout(function () {
                ping(issueId, url);
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
            addEstimator(el.username, el.displayName);
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

function addEstimator(username, displayName) {
    var url = AJS.contextPath() + "/secure/useravatar?size=small&ownerId=" + username;
    var img = $('<img />', {'src': url, 'alt': displayName});
    var span = $('<span />', {
        'class': 'aui-avatar-inner op-estimator-tooltip',
        'title': displayName,
        'data-title': displayName
    })
        .append(img);
    var outerSpan = $('<span />', {'class': 'aui-avatar aui-avatar-small estimator-avatar'}).append(span);

    $('#open-poker-js-estimators').append(outerSpan);
}