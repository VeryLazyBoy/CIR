(function(d3) {
  'use strict';
  //data from API
  var results = [
  {
    'year': 2000,
    'publications':2134
  },
  {
    'year': 2001,
    'publications':4352
  },
  {
    'year': 2002,
    'publications':45645
  },
  {
    'year': 2003,
    'publications':3242
  },
  {
    'year': 2004,
    'publications':6576
  },
  {
    'year': 2005,
    'publications':13414
  },
  {
    'year': 2006,
    'publications':43577
  },
  {
    'year': 2007,
    'publications':4422
  },
  {
    'year': 2008,
    'publications':65234
  },
  {
    'year': 2009,
    'publications':12348
  }
  ];

 // $.ajax({
 //        url: "http://rest-service.guides.spring.io/greeting"
 //    }).then(function(data) {
 //       $('.greeting-id').append(data.id);
 //       $('.greeting-content').append(data.content);
 //    });
//Margins to accommodate X and Y axis labels
var margin = {top: 30, right: 100, bottom: 30, left: 100}
var verticalMargin = margin.top + margin.bottom;
var horizontalMargin = margin.left + margin.right;

//Sizes and Offsets
var height = 400 - verticalMargin;
var width = 800 - horizontalMargin;
var lineWidth = 40;
var lineOffset = 20;

//Colors
var lineColor = '#8CBEB2';
var hoverColor = '#ffffff';
var strokeColor = '#F06060';
var backgroundColor = '#F2EBBF'

//Y axis values
var maxY = function(results){
  var highest = -1;
  for(var i=0; i<results.length; i++){
    if(results[i].publications > highest){
      highest = results[i].publications;
    }
  }
  return highest;
}

//X axis values
var maxX = function(results){
  var highest = -1;
  for(var i=0; i<results.length; i++){
    if(results[i].year > highest){
      highest = results[i].year;
    }
  }
  return highest;
}

var y = d3.scaleLinear()
.domain([0, maxY(results)])
.range([height, 0]);

//X axis values
var x = d3.scaleLinear()
.domain(d3.extent(results, function(d){return d.year;}))
.range([0, width]);

var trendline = d3.line()
.x(function(d){return x(d.year);})
.y(function(d){return y(d.publications);});

//Tooltip
var tooltip = d3.select('#line-chart')
.append('div')
.attr('class', 'tooltip');
tooltip.append('div')
.attr('class', 'label');
tooltip.append('div')
.attr('class', 'count');

//Build graph
var svg = d3.select('#line-chart').append('svg') // append SVG to id=line-chart
.attr('width', width + horizontalMargin) // width + horizontal margins
.attr('height', height + verticalMargin) // height + vertical margins
.style('background', backgroundColor) // background color
.append('g') // for translating the entire graph to accommodate axes
.attr('transform', 'translate('+margin.left+','+margin.top+')') // transform and translate
.append("path")
.data([results])
.attr("class", "line")
.attr("d", trendline)
.style("stroke", lineColor)

//Y axis range breakpoints.
var verticalGuideScale = d3.scaleLinear()
.domain([0, maxY(results)])
.range([height, 0]); //different from yScale because it arranges the values 0 at the bottom and larger values at the top

var vAxis = d3.axisLeft()
.scale(verticalGuideScale)
.ticks(10);

var verticalGuide = d3.select('svg').append('g')
vAxis(verticalGuide)
verticalGuide.attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')')
verticalGuide.selectAll('path')
.style({stroke: 'none', stroke: lineColor})
verticalGuide.selectAll('line')
.style({stroke: lineColor});

//X axis range breakpoints
var hAxis = d3.axisBottom()
.scale(x)
.ticks(10)
.tickFormat(d3.format("d"));

var horizontalGuide = d3.select('svg').append('g')
hAxis(horizontalGuide)
horizontalGuide.attr('transform', 'translate(' + margin.left + ', ' + (height + margin.top) + ')')
horizontalGuide.selectAll('path')
.style({stroke: 'none', stroke: lineColor})
horizontalGuide.selectAll('line')
.style({stroke: lineColor});
})(window.d3);