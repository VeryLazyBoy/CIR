$(document).ready(function() {

    // Defines format function
    if (!String.format) {
        String.format = function(format) {
            var args = Array.prototype.slice.call(arguments, 1);
            return format.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] != 'undefined' ?
                    args[number] :
                    match;
            });
        };
    }

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
                    })
                }
            });
        }
    }

    $('#conferenceInput').keyup(getKeyupHandler('#conferenceInputContainer'));
    $('#confListInput1').keyup(getKeyupHandler('#ConferenceListDiv1'));

    var unnestDataGroup = function(data, children) {
        var out = [];
        data.forEach(function(d, i) {
            // console.log(i, d);
            d_keys = Object.keys(d);
            // console.log(i, d_keys)
            values = d[children];

            values.forEach(function(v) {
                d_keys.forEach(function(k) {
                    if (k != children) {
                        v[k] = d[k]
                    }
                })
                out.push(v);
            })

        })
        return out;
    }

    //Referenced From: https://code.tutsplus.com/tutorials/building-a-multi-line-chart-using-d3js-part-2--cms-22973
    var InitChart = function(urlString) {
        //clear chart
        $('#custom-chart').html("");
        // console.log("url string = " + urlString);

        $.ajax({
            url: urlString
        }).then(function(dataGroup) {
            // alert("RESULTS LOADED!");
            if (dataGroup.length == 0) {
                alert("No Results!");
                return false;
            }

            var dataGroupKeys = Object.keys(dataGroup[0]);
            // console.log(dataGroupKeys[1]);
            var data = unnestDataGroup(dataGroup, dataGroupKeys[1]);
            var dataKeys = Object.keys(data[0]);
            var xAxisFormat = d3.format("");

            var color = d3.scale.category10();
            var vis = d3.select("#custom-chart");

            var WIDTH = 1000,
                HEIGHT = 500,
                MARGINS = {
                    top: 50,
                    right: 20,
                    bottom: 50,
                    left: 50
                };

            lSpace = WIDTH / dataGroup.length;
            xScale = d3.scale.ordinal()
                .rangePoints([MARGINS.left, WIDTH - MARGINS.right])
                .domain(data.sort(function(a, b) {
                    return (a[dataKeys[0]] > b[dataKeys[0]]) ? 1 : ((b[dataKeys[0]] > a[dataKeys[0]]) ? -1 : 0);
                }).map(function(d) {
                    // console.log(d[dataKeys[0]]);
                    return d[dataKeys[0]];
                }));

            var minCount = d3.min(data, function(d) {
                return d[dataKeys[1]];
            })

            var maxCount = d3.max(data, function(d) {
                return d[dataKeys[1]];
            })

            if (minCount == maxCount) {
                yScale = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([0, Math.max(1, data[0][dataKeys[1]] * 2)]);
            } else {
                yScale = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([minCount, maxCount]);
            }

            xAxis = d3.svg.axis()
                .scale(xScale),
                // .tickFormat(xAxisFormat),
                yAxis = d3.svg.axis()
                .scale(yScale)
                .orient("left");

            vis.append("svg:g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + (HEIGHT - MARGINS.bottom) + ")")
                .call(xAxis);
            vis.append("svg:g")
                .attr("class", "y axis")
                .attr("transform", "translate(" + (MARGINS.left) + ",0)")
                .call(yAxis);

            var lineGen = d3.svg.line()
                .x(function(d) {
                    // console.log("Line data: "+d);
                    return xScale(d[dataKeys[0]]);
                })
                .y(function(d) {
                    return yScale(d[dataKeys[1]]);
                })
                .interpolate("basis"); // basis is for trend, the scale might not be accurate

            var pathToCircle = function(d) {
                moveToLeft = 'm -1, 0';
                firstHalfCircle = 'a 1, 1 0 1, 1 2, 0';
                secondHalfCircle = 'a 1, 1 0 1, 1 -2, 0';
                return d + moveToLeft + firstHalfCircle + secondHalfCircle;
            }

            dataGroup.forEach(function(d, i) {
                // console.log("d is " + Object.keys(d));
                if (d[dataGroupKeys[1]].length == 1) {
                    path = pathToCircle(lineGen(d[dataGroupKeys[1]]));
                } else {
                    path = lineGen(d[dataGroupKeys[1]]);
                }
                vis.append('svg:path')
                    .attr('d', path)
                    .attr('stroke', function(d, j) {
                        return "hsl(" + Math.random() * 360 + ",100%,50%)";
                    })
                    .attr('stroke-width', 2)
                    .attr('id', 'line_' + d[dataGroupKeys[0]])
                    .attr('fill', 'none');
                vis.append("text")
                    .attr("x", (lSpace / 3))
                    .attr("y", 50 + i * 50)
                    .style("fill", d3.select("#line_" + d[dataGroupKeys[0]]).attr('stroke'))
                    .attr("class", "legend")
                    .on('click', function() {
                        var active = d.active ? false : true;
                        var opacity = active ? 0 : 1;
                        d3.select("#line_" + d[dataGroupKeys[0]]).style("opacity", opacity);
                        d.active = active;
                    })
                    .text(d[dataGroupKeys[0]]);
            });
        });
    };

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
            var conf = $('#confYearInput' + t).val();
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

        InitChart(encodeURI(urlString));

    });

});
