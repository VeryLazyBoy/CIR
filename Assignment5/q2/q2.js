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
    var isNumeric = function(num) {
        return !isNaN(num)
    }
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
    }
    $("#generateBtn").click(function() {

        var urlString;
        if ($('#queryTypeSelect').val() == 0) {
            urlString = apiRootUrlOverYears;

            var conferenceYear = $("#conferenceYearInput").val();
            if (!conferenceYear) {
                alert("Conference year is required");
                return false;
            }
            if (!isValidYear(conferenceYear)) {
                alert("Conference year is invalid");
                return false;
            }
            var startYear = $("#startYearInput").val();
            if (!isValidYear(startYear)) {
                alert("Start year is invalid");
                return false;
            }
            var endYear = $("#endYearInput").val();
            if (!isValidYear(endYear)) {
                alert("End year is invalid");
                return false;
            }

            if (parseInt(startYear) > parseInt(endYear)) {
                alert("Start year must be before end year.");
                return false;
            }
            var conferences = "";
            for (t = 1; t <= multiLine.conferenceCounter; t++) {
                if ($('#confListInput' + t).val()) {
                    conferences += $('#confListInput' + t).val() + '$$';
                }
            }
            // console.log("Before slicing: " + conferences);
            if (conferences) {
                // console.log("slicing ");
                conferences = conferences.slice(0, -1); //removes the last '$' from the string
                conferences = conferences.slice(0, -1); //removes the 2nd last '$' from the string
                // console.log("After slicing: " + conferences);
            } else {
                alert("At least 1 conference code is required.");
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
            var conferenceYear = $("#conferenceYearInput").val();
            var conferences = "";
            if (!conferenceYear) {
                alert("Conference year is required");
                return false;
            }
            for (t = 1; t <= multiLine.conferenceCounter; t++) {
                if ($('#confListInput' + t).val()) {
                    conferences += $('#confListInput' + t).val() + '$$';
                }
            }
            if (conferences) {
                // console.log("slicing ");
                conferences = conferences.slice(0, -1); //removes the last '$' from the string
                conferences = conferences.slice(0, -1); //removes the 2nd last '$' from the string
                // console.log("After slicing: " + conferences);
            } else {
                alert("At least 1 conference code is required.");
                return false;
            }
            var conferences2 = "";
            for (t = 1; t <= multiLine.conferenceCounter2; t++) {
                if ($('#confList2Input' + t).val()) {
                    conferences2 += $('#confList2Input' + t).val() + '$$';
                }
            }
            if (conferences2) {
                // console.log("slicing ");
                conferences2 = conferences2.slice(0, -1); //removes the last '$' from the string
                conferences2 = conferences2.slice(0, -1); //removes the 2nd last '$' from the string
                // console.log("After slicing: " + conferences2);
            } else {
                alert("At least 1 conference code is required.");
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
