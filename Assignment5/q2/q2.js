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
    $('#confCodeInput1').keyup(multiLine.getKeyupHandler('#ConferenceContainer1 .dropdownContainer'));
    $('#confCode2Input1').keyup(multiLine.getKeyupHandler('#Conference2Container1 .dropdownContainer'));

    var apiRootUrlOverYears = "http://localhost:8080/json/yearcontemporaries?";
    var apiRootUrlOverConferences = "http://localhost:8080/json/confcontemporaries?";

    multiLine.generateAddConfButton("addConferenceButton", 1, "ConferenceListContainer", "");
    multiLine.generateRemoveConfButton("removeConferenceButton", 1, "");

    multiLine.generateAddConfButton("addConferenceButton2", 2, "ConferenceListContainer2", "2");
    multiLine.generateRemoveConfButton("removeConferenceButton2", 2, "2");

    multiLine.generateQuerySelect("2");

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

        var conferences = multiLine.validateAndGetConfListFromInput("confCodeInput", multiLine.conferenceCounter);
        if (conferences == null) {
                return false;
        }

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

            var conferences2 = multiLine.validateAndGetConfListFromInput("confList2Input", multiLine.conferenceCounter2);
            if (conferences2 == null) {
                return false;
            }

            if (conferenceYear && conferences && conferences2) {
                urlString += "year=" + conferenceYear + "&";
                urlString += "confs=" + conferences + "&";
                urlString += "conflist=" + conferences2;
            }
        }

        multiLine.InitChart(encodeURI(urlString));

    });

});
