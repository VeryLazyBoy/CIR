$(document).ready(function() {

    var multiLine = new $.MultiLine();
    $('#conferenceInput').keyup(multiLine.getKeyupHandler('#conferenceInputContainer .dropdownContainer'));
    $('#confCodeInput1').keyup(multiLine.getKeyupHandler('#ConferenceContainer1 .dropdownContainer'));

    var apiRootUrlOverYears = "http://localhost:8080/json/yeartransitions?";
    var apiRootUrlOverConferences = "http://localhost:8080/json/conftransitions?";

    multiLine.generateAddYearButton("addButton", 3, "ConferenceYearsGroup");
    multiLine.generateRemoveYearButton("removeButton", 3);


    multiLine.generateAddConfButton("addConferenceButton", 1, "ConferenceListContainer", "");
    multiLine.generateRemoveConfButton("removeConferenceButton", 1, "");

    multiLine.generateQuerySelect("");

    $("#generateBtn").click(function() {

        var urlString;

        // Validates conference
        var conference = $("#conferenceInput").val();
        if (!conference) {
            alert("Conference Code is required.");
            return false;
        }

        // Validates conference year list
        var yearList = (multiLine.processInputs("confYearInput", multiLine.yearCounter)).map(element => parseInt(element));
        if (yearList.length == 0) {
            alert("At least 1 conference year is required.");
            return false;
        }
        var conferenceYears = yearList.join('$$');

        if ($('#queryTypeSelect').val() == 0) {
            urlString = apiRootUrlOverYears;

            // Valides start year
            var startYear = $("#startYearInput").val();
            if (!multiLine.isValidYear(startYear)) {
                alert("Start year is invalid");
                return false;
            }

            // Validate end year
            var endYear = $("#endYearInput").val();
            if (!multiLine.isValidYear(endYear)) {
                alert("End year is invalid");
                return false;
            }

            // Validate start year and end year
            if (parseInt(startYear) > parseInt(endYear)) {
                alert("Start year must be before end year.");
                return false;
            }

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

            var conferences = multiLine.validateAndGetConfListFromInput("confCodeInput", multiLine.conferenceCounter);
            if (conferences == null) {
                return false;
            }

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
