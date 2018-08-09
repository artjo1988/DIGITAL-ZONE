function fixation(url) {
    $.ajax({
        url: 'http://localhost:8080/fix',
        type: 'GET',
        data:
            {
              url: url
            },
        success: function(){
            alert('Спасибо, что посители наш сайт!)')
        }
    });
}

function results() {
    $.ajax({
        url: "http://localhost:8080/results",
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            document.getElementById('totalCount').value = response["totalCountUsers"];
            document.getElementById('uniqueCount').value = response["uniqueCountUsers"];

        }
    })
}

function resultsForPeriod(dateBegin, timeBegin, dateEnd, timeEnd) {
    $.ajax({
        url: "http://localhost:8080/resultsForPeriod",
        type: 'GET',
        data:
            {
                dateBegin : dateBegin,
                timeBegin : timeBegin,
                dateEnd : dateEnd,
                timeEnd : timeEnd,
            },
        dataType: 'json',
        success: function (response) {
            document.getElementById('totalCountForPeriod').value = response["totalCountUsersForPeriod"];
            document.getElementById('uniqueCountForPeriod').value = response["uniqueCountUsersForPeriod"];
            document.getElementById('regularCountForPeriod').value = response["regularCountUsersForPeriod"];

        }
    })
}

