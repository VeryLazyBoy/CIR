$(document).ready(function() {

    // var dataGroup = [
    //   {
    //     "confId": "arXiv2014",
    //     "yearline": [
    //       {
    //         "year": 1993,
    //         "citations": 1
    //       },
    //       {
    //         "year": 1994,
    //         "citations": 4
    //       },
    //      {
    //         "year": 1995,
    //         "citations": 8
    //       }
    //     ]
    //   },
    //   {
    //     "confId": "arXiv2015",
    //     "yearline": [
    //       {
    //         "year": 1993,
    //         "citations": 8
    //       },
    //       {
    //         "year": 1994,
    //         "citations": 7
    //       },
    //       {
    //         "year": 1995,
    //         "citations": 4
    //       }
    //      ]
    //   }
    // ];

    // var dataGroup = [
    //   {
    //     "confId": "arXiv2014",
    //     "venueline": [
    //       {
    //         "conference": "abc",
    //         "citations": 1
    //       },
    //       {
    //         "conference": "edf",
    //         "citations": 4
    //       },
    //      {
    //         "conference": "ggg",
    //         "citations": 7
    //       }
    //     ]
    //   },
    //   {
    //     "confId": "arXiv2015",
    //     "venueline": [
    //       {
    //         "conference": "abc",
    //         "citations": 8
    //       },
    //       {
    //         "conference": "edf",
    //         "citations": 7
    //       },
    //      {
    //         "conference": "ggg",
    //         "citations": 7
    //       }
    //     ]
    //   },
    //   {
    //     "confId": "Shawn",
    //     "venueline": [
    //       {
    //         "conference": "abc",
    //         "citations": 10
    //       },
    //       {
    //         "conference": "edf",
    //         "citations": 3
    //       },
    //      {
    //         "conference": "ggg",
    //         "citations": 1
    //       }
    //     ]
    //   }
    // ];


    // var dataGroup = [
    //   {
    //     "confId": "arXiv2016",
    //     "yearline": [
    //       {
    //         "year": 1993,
    //         "citations": 1
    //       },
    //       {
    //         "year": 1994,
    //         "citations": 4
    //       },
    //      {
    //         "year": 1995,
    //         "citations": 8
    //       }
    //     ]
    //   },
    //   {
    //     "confId": "bat2016",
    //     "yearline": [
    //       {
    //         "year": 1993,
    //         "citations": 8
    //       },
    //       {
    //         "year": 1994,
    //         "citations": 7
    //       },
    //       {
    //         "year": 1995,
    //         "citations": 4
    //       }
    //      ]
    //   }
    // ];

    var multiLine = new $.MultiLine();
    $('#confListInput1').keyup(multiLine.getKeyupHandler('#ConferenceListDiv1 .dropdownContainer'));
    $('#confList2Input1').keyup(multiLine.getKeyupHandler('#ConferenceList2Div1 .dropdownContainer'));

    var apiRootUrlOverYears = "http://localhost:8080/json/yearcontemporaries?";
    var apiRootUrlOverConferences = "http://localhost:8080/json/confcontemporaries?";

    multiLine.generateAddConfButton("addConferenceButton", 1, "ConferenceListGroup", "");
    multiLine.generateRemoveConfButton("removeConferenceButton", 1, "");

    multiLine.generateAddConfButton("addConferenceButton2", 2, "ConferenceListGroup2", "2");
    multiLine.generateRemoveConfButton("removeConferenceButton2", 2, "2");

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
            $("#ConferenceListGroup2").removeClass("hidden");
        } else {
            $("#ConferenceListGroup2").addClass("hidden");
        }
    });

    $("#generateBtn").click(function() {

        var urlString;

        // Validates conference year
        var conferenceYear = $("#conferenceYearInput").val();
        if (!conferenceYear) {
            alert("Conference year is required");
            return false;
        }
        if (!multiLine.isValidYear(conferenceYear)) {
            alert("Conference year is invalid");
            return false;
        }

        // Validates conference list
        var confList = multiLine.processInputs("confListInput", multiLine.conferenceCounter);
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

        var conferences = confList.join('$$');

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
            if (parseInt(conferenceYear) < parseInt(endYear)) {
                alert("Plear choose end year that are not larger than " + parseInt(conferenceYear) + " .");
                return false;
            }

            if (conferenceYear && conferences) {
                urlString += "year=" + conferenceYear + "&";
                urlString += "confs=" + conferences + "&";
                urlString += "syear=" + startYear + "&";
                urlString += "eyear=" + endYear;
            }
        } else {
            urlString = apiRootUrlOverConferences;

            // Validates conference list
            confList = multiLine.processInputs("confList2Input", multiLine.conferenceCounter2);
            if (confList.length == 0) {
                alert("At least one conference code is required.");
                return false;
            }

            // Validates no same conferences in the conference list
            confSet = new Set(confList);
            if (confSet.size != multiLine.conferenceCounter2 - 1) {
                alert("No conference code should be the same.");
                return false;
            }

            var conferences2 = confList.join('$$');

            if (conferenceYear && conferences && conferences2) {
                urlString += "year=" + conferenceYear + "&";
                urlString += "confs=" + conferences + "&";
                urlString += "conflist=" + conferences2;
            }
        }

        multiLine.InitChart(encodeURI(urlString));

    });

});
