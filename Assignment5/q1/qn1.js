$(document).ready(function() {

    var searchRequest = null;
    var getKeyupHandler = function(container) {
        return function() {
            var minLength = 3;
            var inputBar = $(this);
            var input = $(this).get(0).value;
            var url = 'http://localhost:8080/json/places';

            if (input.length < minLength)
                return false;

            if (searchRequest != null)
                searchRequest.abort();

            searchRequest = $.getJSON(url, {
                keyword: input,
                limit: 8
            }, function(data) {
                if (input == inputBar.val()) {
                    var places = '';
                    $('.confOptions').get(0).remove();
                    $.each(data.places, function(index, place) {
                        places += '<a class="conference">' + place + '</a>';
                    });
                    $('<div class="confOptions">' + places + '</div>').appendTo(container);
                    $('.conference').click(function() {
                        inputBar.val($(this).get(0).innerText);
                        $('.confOptions').css('display', 'none');
                    });
                }
            });
        }
    }
    $(document).mouseup(function (e) {
        var divContent= $(".confOptions");
        if(!divContent.is(e.target) && divContent.has(e.target).length === 0) {
            $(".confOptions").hide();
        }
    });
    $('#conferenceInput').keyup(getKeyupHandler('#conferenceInputContainer'));
    $('#confListInput1').keyup(getKeyupHandler('#ConferenceListDiv1'));

    var multiLine = new $.MultiLine();

    // var InitChart = multiLine.InitChart;

    var counter = 2;
    var apiRootUrlOverYears = "http://localhost:8080/json/yeartransitions?";
    var apiRootUrlOverConferences = "http://localhost:8080/json/conftransitions?";

    var conferenceCounter = 2;

    $("#addButton").click(function() {
        if (counter > 10) {
            alert("Max. of 10 years allowed.");
            return false;
        }
        var newTextBoxDiv = $(document.createElement('div'))
            .attr("id", 'ConferenceYearDiv' + counter);
        newTextBoxDiv.after().html('<label>Year ' + counter + ':</label>' +
            '<input class="form-control" type="text" ' + counter +
            '" id="confYearInput' + counter + '" placeholder="e.g. ' + "'1993'" + '">');
        newTextBoxDiv.appendTo("#ConferenceYearsGroup");
        counter++;
    });

    $("#removeButton").click(function() {
        if (counter == 2) {
            alert("At least one year is required.");
            return false;
        }
        counter--;
        $("#ConferenceYearDiv" + counter).remove();
    });

    $("#addConferenceButton").click(function() {
        if (conferenceCounter > 10) {
            alert("Max. of 10 Conferences allowed.");
            return false;
        }
        var newTextBoxDiv = $(document.createElement('div'))
            .attr("id", 'ConferenceListDiv' + conferenceCounter).addClass('dropdown-content');

        newTextBoxDiv.after().html('<label>Conference ' + conferenceCounter + ':</label>' +
            '<input class="form-control" type="text" ' + conferenceCounter +
            '" id="confListInput' + conferenceCounter + '" placeholder="e.g. ' + "'arXiv'" + '">');

        newTextBoxDiv.appendTo("#ConferenceListGroup");

        $('#confListInput' + conferenceCounter).keyup(getKeyupHandler('#ConferenceListDiv' + conferenceCounter));
        conferenceCounter++;
    });

    $("#removeConferenceButton").click(function() {
        if (conferenceCounter == 2) {
            alert("At least one conference is required.");
            return false;
        }
        conferenceCounter--;
        $("#ConferenceListDiv" + conferenceCounter).remove();
    });

    $('#queryTypeSelect').on('change', function() {
        var confYears = false;
        var confList = false;

        var val = this.value;
        // console.log(val);
        switch (val) {
            case '0': //over years
                confYears = true;
                confList = false;
                break;
            case '1':
                confYears = false;
                confList = true;
                break;
        }
        if (confYears) {
            $("#startYearInputContainer").removeClass("hidden");
            $("#endYearInputContainer").removeClass("hidden");
        } else {
            $("#startYearInputContainer").addClass("hidden");
            $("#endYearInputContainer").addClass("hidden");
        }
        if (confList) {
            $("#ConferenceListGroup").removeClass("hidden");
        } else {
            $("#ConferenceListGroup").addClass("hidden");
        }
    });

    var isNumeric = function(num) {
        return !isNaN(num)
    };

    var isValidYear = function(yearString) {
        if (yearString == "") {
            return true;
        }
        if (!isNumeric(yearString)) {
            return false;
        }
        if (parseInt(yearString) < 2017 && parseInt(yearString) > 0) {
            return true;
        }
        return false;
    };

    var getConferenceYears = function(yearCounter) {
        var yearList = [];
        for (var t = 1; t <= yearCounter; t++) {
            var yearString = $('#confYearInput' + t).val();
            if (yearString) {
                //paseInst null or '' => NaN
                yearList.push(parseInt(yearString));
            }
        }
        return yearList;
    }

    var getConferences = function(NumOfConfs) {
        var confList = [];
        for (var t = 1; t <= NumOfConfs; t++) {
            var conf = $('#confListInput' + t).val();
            if (conf) {
                confList.push(conf);
            }
        }
        return confList;
    }

    $("#generateBtn").click(function() {

        var urlString;
        if ($('#queryTypeSelect').val() == 0) {
            urlString = apiRootUrlOverYears;

            // Validates conference
            var conference = $("#conferenceInput").val();
            if (!conference) {
                alert("Conference Code is required.");
                return false;
            }

            // Valides start year
            var startYear = $("#startYearInput").val();
            if (!isValidYear(startYear)) {
                alert("Start year is invalid");
                return false;
            }

            // Validate end year
            var endYear = $("#endYearInput").val();
            if (!isValidYear(endYear)) {
                alert("End year is invalid");
                return false;
            }

            // Validate start year and end year
            if (parseInt(startYear) > parseInt(endYear)) {
                alert("Start year must be before end year.");
                return false;
            }

            // Validates conf year list
            var yearList = getConferenceYears(counter);
            if (yearList.length == 0) {
                alert("At least 1 conference year is required.");
                return false;
            }
            var conferenceYears = yearList.join('$$');

            // End year should be chosen meaningfully to avoid all 0 counts.
            yearList.sort((a, b) => b - a);
            var maxConferenceYear = yearList[0];
            if (maxConferenceYear < endYear) {
                alert("Plear choose end year that are not larger than " + maxConferenceYear + " .");
                return false;
            }

            // Creates the final url
            if (conference && conferenceYears) {
                urlString += "conf=" + conference + "&";
                urlString += "years=" + conferenceYears + "&";
                urlString += "syear=" + startYear + "&";
                urlString += "eyear=" + endYear;
            }
        } else {
            urlString = apiRootUrlOverConferences;

            // Validates conference
            var conference = $("#conferenceInput").val();
            if (!conference) {
                alert("Conference Code is required.");
                return false;
            }

            // Validates conferenece year list
            var yearList = getConferenceYears(counter);
            if (yearList.length == 0) {
                alert("At least 1 conference year is required.");
                return false;
            }
            var conferenceYears = yearList.join('$$');

            // Validates conference list
            var confList = getConferences(conferenceCounter);
            if (confList.length == 0) {
                alert("At least one conference code is required.");
                return false;
            }

            // Validates no same conferences in the conference list
            var confSet = new Set(confList);
            if (confSet.length != conferenceCounter) {
                alert("No conference code should be the same.");
                return false;
            }

            var conferenceList = confList.join('$$');

            // Creates the final url
            if (conference && conferenceYears && conferenceList) {
                urlString += "conf=" + conference + "&";
                urlString += "years=" + conferenceYears + "&";
                urlString += "conflist=" + conferenceList;
            }
        }

        multiLine.InitChart(encodeURI(urlString));

    });

});
