(function($) {
    var undefined;
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

    $.MultiLine = function(options) {
        var self = this;
        var cfg = $.extend(true, {}, this.defaults, options);

        // ********************
        // start:private
        // ********************
        function _init() {

        };

        // ********************
        // start:public
        // ********************
        this.methodName = function() {

        };

        _init();
    };

    $.MultiLine.prototype.defaults = {};

    $.MultiLine.prototype.unnestDataGroup = function(data, children) {
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
    };

    //Referenced From: https://code.tutsplus.com/tutorials/building-a-multi-line-chart-using-d3js-part-2--cms-22973
    $.MultiLine.prototype.InitChart = function(urlString) {
        var self = this;
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
            var data = self.unnestDataGroup(dataGroup, dataGroupKeys[1]);
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

    $.MultiLine.prototype.searchRequest = null;
    $.MultiLine.prototype.getKeyupHandler = function(container) {
        var self = this;
        return function() {
            var minLength = 3;
            var inputBar = $(this);
            var input = $(this).get(0).value;
            var url = 'http://localhost:8080/json/places';

            if (input.length < minLength)
                return false;

            if (self.searchRequest != null)
                self.searchRequest.abort();

            self.searchRequest = $.getJSON(url, {
                keyword: input,
                limit: 8
            }, function(data) {
                if (input == inputBar.val()) {
                    var places = '';
                    var confOptions = $(container).find('.confOptions');
                    if (confOptions.length > 0)
                        confOptions.remove();
                    $.each(data.places, function(index, place) {
                        places += '<a class="conference">' + place + '</a>';
                    });
                    $('<div class="confOptions">' + places + '</div>').appendTo(container);
                    $(container).find('.conference').click(function() {
                        inputBar.val($(this).get(0).innerText);
                        $(container).css('display', 'none');
                    });
                }
            });
        }
    };

    $.MultiLine.prototype.conferenceCounter = 2;
    $.MultiLine.prototype.conferenceCounter2 = 2;
    $.MultiLine.prototype.yearCounter = 2;
    $.MultiLine.prototype.getCounter = function(index) {
        switch(index) {
            case 1:
                return this.conferenceCounter;
            case 2:
                return this.conferenceCounter2;
            case 3:
                return this.yearCounter;
            default:
                return this.yearCounter;
        }
    }

    $.MultiLine.prototype.setCounter = function(index, value) {
        if (index == 1) {
            this.conferenceCounter = value;
        } else if (index == 2) {
            this.conferenceCounter2 = value;
        } else {
            this.yearCounter = value;
        }
    }

    $.MultiLine.prototype.generateAddYearButton = function(buttonId, counterIndex, groupId) {
        var self = this;
        $('#' + buttonId).click(function() {
            var counter = self.getCounter(counterIndex);
            var confDivId = String.format('ConferenceYearDiv{0}', counter);
            var inputId = String.format('confYearInput{0}', counter);
            if (counter > 10) {
                alert("Max. of 10 years allowed.");
                return false;
            }
            var newTextBoxDiv = $(document.createElement('div'))
                .attr("id", confDivId);
            newTextBoxDiv.after().html('<label>Year ' + counter + ':</label>' +
                '<input class="form-control" type="text" ' +
                'id="' + inputId + '" placeholder="e.g. ' + "'1993'" + '">');
            newTextBoxDiv.appendTo('#' + groupId);
            counter++;
            self.setCounter(counterIndex, counter);
        });
    }

    $.MultiLine.prototype.generateRemoveYearButton = function(buttonId, counterIndex) {
        var self = this;
        $("#" + buttonId).click(function() {
            var counter = self.getCounter(counterIndex);
            if (counter == 2) {
                alert("At least one year is required.");
                return false;
            }
            counter--;
            var confDivId = String.format('ConferenceYearDiv{0}', counter);
            $("#" + confDivId).remove();
            self.setCounter(counterIndex, counter);
        });
    }

    $.MultiLine.prototype.generateAddConfButton = function(buttonId, counterIndex, groupId, groupLabel) {
        var self = this;

        $('#' + buttonId).click(function() {
            var counter = self.getCounter(counterIndex);
            var confDivId = String.format('ConferenceList{0}Div{1}', groupLabel, counter);
            var inputId = String.format('confList{0}Input{1}', groupLabel, counter);

            if (counter > 10) {
                alert("Max. of 10 Conferences allowed.");
                return false;
            }

            var newTextBoxDiv = $(document.createElement('div'))
                .attr("id", confDivId).addClass('dropdown-content');

            newTextBoxDiv.after().html('<label>Conference ' + counter + ':</label>' +
                '<input class="form-control" type="text" ' +
                'id="' + inputId + '" placeholder="e.g. ' + "'arXiv'" + '">'
                + '<div class="dropdownContainer"></div>');

            newTextBoxDiv.appendTo('#' + groupId);

            $('#' + inputId).keyup(self.getKeyupHandler('#' + confDivId +  ' .dropdownContainer'));
            counter++;
            self.setCounter(counterIndex, counter);
        });
    }

    $.MultiLine.prototype.generateRemoveConfButton = function(buttonId, counterIndex, groupLabel) {
        var self = this;
        $("#" + buttonId).click(function() {
            var counter = self.getCounter(counterIndex);
            if (counter == 2) {
                alert("At least one conference is required.");
                return false;
            }
            counter--;
            var confDivId = String.format('ConferenceList{0}Div{1}', groupLabel, counter);
            $("#" + confDivId).remove();
            self.setCounter(counterIndex, counter);
        });
    }

    $.MultiLine.prototype.processInputs = function(inputLabel, numOfInputs) {
        var inputList = [];
        for (var t = 1; t <= numOfInputs; t++) {
            var input = $('#' + inputLabel + t).val();
            if (input) {
                inputList.push(input);
            }
        }
        return inputList;
    }

    $(document).mouseup(function (e) {
        var divContent= $(".dropdownContainer");
        var input = $("input[id^=confList]");
        var anotherInput = $("[id^=conferenceInput]");


        // if somewhere except dropdown is clicked, hide all dropdown
        if (!divContent.is(e.target) && divContent.has(e.target).length === 0) {
            $(".dropdownContainer").hide();
        }

        // if the input for conference is clicked, show the dropdown only after this input
        if (input.is(e.target) || anotherInput.is(e.target)) {
            $(e.target).nextAll(".dropdownContainer").show();
        }
    });

})(jQuery);
