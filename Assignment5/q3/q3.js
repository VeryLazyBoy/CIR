$(document).ready(function() {
    var apiRoot = "http://localhost:8080/json/";

    var generateD3 = function(urlString) {
        console.log(urlString);

        //Clear the bar-chart
        $("#bar-chart").html(function() {
            return "";
        });

        //generate the bar-chart
        'use strict';
        //data from API
        // var results = [
        // {
        //   "author": "Trump",
        //   "publications": 1000
        // },
        // {
        //   "author": "Xi Jinping",
        //   "publications": 100
        // },
        // {
        //   "author":"Frank",
        //   "publications":10
        // }
        // ];

        $.ajax({
            url: urlString
        }).then(function(results) {
            // alert("RESULTS LOADED!");
            if (results.length == 0) {
                alert("No Results!");
                return false;
            }
            // d3.json("../json/article_bar.json", function(results) {

            if (results.length > 0) {
                //get keys in JSON
                var keys = Object.keys(results[0]);
                // console.log(keys);

                //Margins to accommodate X and Y axis labels
                var margin = {
                    top: 30,
                    right: 10,
                    bottom: 30,
                    left: 100
                }
                var verticalMargin = margin.top + margin.bottom;
                var horizontalMargin = margin.left + margin.right;

                //Sizes and Offsets
                var height = 500 - verticalMargin;
                var width = 800 - horizontalMargin;
                var barWidth = 40;
                var barOffset = 20;

                //Colors
                var barColor = '#8CBEB2';
                var hoverColor = '#ffffff';
                var strokeColor = '#F06060';
                var backgroundColor = '#F2EBBF'

                //Y axis values
                var maxY = function(results) {
                    var highest = -1;
                    for (var i = 0; i < results.length; i++) {
                        if (results[i][keys[1]] > highest) {
                            highest = results[i][keys[1]];
                        }
                    }
                    return highest;
                }

                var yScale = d3.scale.linear()
                    .domain([0, maxY(results)])
                    .range([0, height])

                //X axis values
                var xScale = d3.scale.ordinal()
                    .domain(d3.range(0, results.length))
                    .rangeBands([0, width])

                //Tooltip
                var tooltip = d3.select('#bar-chart')
                    .append('div')
                    .attr('class', 'bar-tooltip');
                tooltip.append('div')
                    .attr('class', 'custom-tooltip-label');
                tooltip.append('div')
                    .attr('class', 'count');

                //Build graph
                d3.select('#bar-chart').append('svg') // append SVG to id=bar-chart
                    .attr('width', width + horizontalMargin) // width + horizontal margins
                    .attr('height', height + verticalMargin) // height + vertical margins
                    .style('background', backgroundColor) // background color
                    .append('g') // for translating the entire graph to accommodate axes
                    .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')') // transform and translate
                    .selectAll('rect').data(results) // create rectangles for the graph
                    .enter().append('rect') // placeholder append rectangle
                    .style({
                        'fill': barColor,
                        'stroke': strokeColor,
                        'stroke-width': '2'
                    }) // styling for each bar
                    // .attr('width', barWidth)
                    .attr('width', xScale.rangeBand()) //use x axis rangeBand to determine width of each bar
                    .attr('height', function(data) {
                        // console.log("DATA: " + data[keys[1]]);
                        return yScale(data[keys[1]]); //use y axis range to determine height of each bar. The tallest value takes up all the height
                    })
                    .attr('x', function(data, i) {
                        // return i * (barWidth + barOffset);
                        return xScale(i); //use x axis rangeband to determine position of the respective bar
                    })
                    .attr('y', function(data) {
                        return height - yScale(data[keys[1]]); // use y axis range to determine height of each bar. 
                    })
                    .on('mouseover', function(data) { // hover changes the color of the bars
                        barColor = this.style.fill;
                        d3.select(this)
                            .style('fill', hoverColor);

                        var currentX = data[keys[0]];
                        var currentY = data[keys[1]];
                        var xLabelToUpper = keys[0].toUpperCase();
                        var yLabelToUpper = keys[1].toUpperCase();

                        tooltip.select('.custom-tooltip-label').html(xLabelToUpper + ": " + currentX);
                        tooltip.select('.count').html(yLabelToUpper + ": " + currentY);
                        tooltip.style('display', 'block');
                    })
                    .on('mouseout', function(data) { // mouse out changes the color back to the original color
                        d3.select(this)
                            .style('fill', barColor)

                        tooltip.style('display', 'none')

                    })
                    .on('mousemove', function(data) {
                        tooltip.style('top', (d3.event.layerY + 10) + 'px')
                            .style('left', (d3.event.layerX + 10) + 'px')
                    });

                //Y axis range breakpoints.
                var verticalGuideScale = d3.scale.linear()
                    .domain([0, maxY(results)])
                    .range([height, 0]); //different from yScale because it arranges the values 0 at the bottom and larger values at the top

                var vAxis = d3.svg.axis()
                    .scale(verticalGuideScale)
                    .orient('left')
                    .ticks(10);

                var verticalGuide = d3.select('svg').append('g')
                vAxis(verticalGuide)
                verticalGuide.attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')')
                verticalGuide.selectAll('path')
                    .style({
                        fill: 'none',
                        stroke: barColor
                    })
                verticalGuide.selectAll('line')
                    .style({
                        stroke: barColor
                    });

                //X axis range breakpoints
                var hAxis = d3.svg.axis()
                    .scale(xScale)
                    .orient('bottom')
                    .tickFormat(function(i) { //X axis title names
                        // return results[i].title;
                        return "";
                    });

                var horizontalGuide = d3.select('svg').append('g')
                hAxis(horizontalGuide)
                horizontalGuide.attr('transform', 'translate(' + margin.left + ', ' + (height + margin.top) + ')')
                horizontalGuide.selectAll('path')
                    .style({
                        fill: 'none',
                        stroke: barColor
                    })
                horizontalGuide.selectAll('line')
                    .style({
                        stroke: barColor
                    });

            } else {
                alert("No Results Found!");
            };
        });
    }

    var isNumeric = function(num) {
        return !isNaN(num)
    }

    multiLine = new MultiLine();

    var generateAPIUrl = function() {
        var urlString = apiRoot;
        //get value of select
        var val = $('#queryTypeSelect').val();
        //add first filter
        switch (val) {
            case '0':
            case '1':
            case '2':
                urlString += "authors?"
                break;
            case '3':
            case '4':
            case '5':
                urlString += "citedauthors?"
                break;
            case '6':
            case '7':
            case '8':
                urlString += "citations?"
                break;
            case '9':
                urlString += "authors/citations?"
                break;
        }
        //add values if they exist
        if ($('#topInputContainer').is(":visible")) {
            if ($('#topValue').val()) {
                urlString += "top=" + $('#topValue').val() + "&";
            } else {
                alert("Number of results is required");
                return false;
            }
        }
        if ($('#yearInputContainer').is(":visible")) {
            if (!multiLine.isValidYear($('#yearValue').val())) {
                alert("Year is invalid");
                return false;
            }
            if ($('#yearValue').val()) {
                urlString += "year=" + $('#yearValue').val() + "&";
            } else {
                alert("Year is required");
                return false;
            }
        }
        if ($('#confInputContainer').is(":visible")) {
            if ($('#confValue').val()) {
                urlString += "conf=" + $('#confValue').val() + "&";
            } else {
                alert("Conference is required");
                return false;
            }
        }
        if ($('#authorInputContainer').is(":visible")) {
            if ($('#authorValue').val()) {
                urlString += "author=" + $('#authorValue').val() + "&";
            } else {
                alert("Author is required");
                return false;
            }
        }
        if (urlString == apiRoot) {
            alert("Please select a query");
            return false;
        }
        urlString = urlString.slice(0, -1); //removes the last '&' from the string

        return urlString;
    }

    $('#queryTypeSelect').on('change', function() {
        var top = false;
        var year = false;
        var conf = false;
        var author = false;

        var val = this.value;
        // console.log(val);
        switch (val) {
            case '0':
            case '3':
            case '6':
                top = true;
                year = true;
                conf = true;
                break;
            case '1':
            case '4':
            case '7':
                top = true;
                conf = true;
                break;
            case '2':
            case '5':
            case '8':
                top = true;
                year = true;
                break;
            case '9':
                top = true;
                author = true;
                break;
        }
        // console.log(top);
        // console.log(year);
        // console.log(conf);
        // console.log(author);
        if (top) {
            $("#topInputContainer").removeClass("hidden");
        } else {
            $("#topInputContainer").addClass("hidden");
        }
        if (year) {
            $("#yearInputContainer").removeClass("hidden");
        } else {
            $("#yearInputContainer").addClass("hidden");
        }
        if (conf) {
            $("#confInputContainer").removeClass("hidden");
        } else {
            $("#confInputContainer").addClass("hidden");
        }
        if (author) {
            $("#authorInputContainer").removeClass("hidden");
        } else {
            $("#authorInputContainer").addClass("hidden");
        }
    });

    $("#generateBtn").click(function() {
        var urlString = generateAPIUrl();
        // console.log(urlString);
        if (urlString) {
            generateD3(encodeURI(urlString));
        }
    });

});