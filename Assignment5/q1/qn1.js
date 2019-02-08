$(document).ready(function() {

    var multiLine = new $.MultiLine();
    $('#conferenceInput').keyup(multiLine.getKeyupHandler('#conferenceInputContainer .dropdownContainer'));
    $('#confListInput1').keyup(multiLine.getKeyupHandler('#ConferenceListDiv1 .dropdownContainer'));

    var apiRootUrlOverYears = "http://localhost:8080/json/yeartransitions?";
    var apiRootUrlOverConferences = "http://localhost:8080/json/conftransitions?";

    multiLine.generateAddYearButton("addButton", 3, "ConferenceYearsGroup");
    multiLine.generateRemoveYearButton("removeButton", 3);


    multiLine.generateAddConfButton("addConferenceButton", 1, "ConferenceListGroup", "");
    multiLine.generateRemoveConfButton("removeConferenceButton", 1, "");

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
            var yearList = getConferenceYears(multiLine.yearCounter);
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
            var yearList = getConferenceYears(multiLine.yearCounter);
            if (yearList.length == 0) {
                alert("At least 1 conference year is required.");
                return false;
            }
            var conferenceYears = yearList.join('$$');

            // Validates conference list
            var confList = getConferences(multiLine.conferenceCounter);
            if (confList.length == 0) {
                alert("At least one conference code is required.");
                return false;
            }

            // Validates no same conferences in the conference list
            var confSet = new Set(confList);
            if (confSet.size != multiLine.conferenceCounter - 1) {
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
