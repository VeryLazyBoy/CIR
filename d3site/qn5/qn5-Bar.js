(function(d3) {
  'use strict';
  //data from API
  // var results = [
  // {
  //   'venue': 'Earth',
  //   'publications':2134
  // },
  // {
  //   'venue': 'Mars',
  //   'publications':4352
  // },
  // {
  //   'venue': 'Jupiter',
  //   'publications':45645
  // },
  // {
  //   'venue': 'Saturn',
  //   'publications':3242
  // },
  // {
  //   'venue': 'Venus',
  //   'publications':6576
  // }
  // ];
  
  $.ajax({
        url: "http://localhost:8080/json/venues?year=2016"
    }).then(function(results) {
       alert(results);
// d3.json("../json/venue_sector.json", function(results) {
results.splice(0,1);
//Margins to accommodate X and Y axis labels
var margin = {top: 30, right: 10, bottom: 30, left: 100}
var verticalMargin = margin.top + margin.bottom;
var horizontalMargin = margin.left + margin.right;

//Sizes and Offsets
var height = 2000 - verticalMargin;
var width = 100000 - horizontalMargin;
var barWidth = 40;
var barOffset = 20;

//Colors
var barColor = '#8CBEB2';
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
.attr('class', 'tooltip');
tooltip.append('div')
.attr('class', 'label');
tooltip.append('div')
.attr('class', 'count');

//Build graph
d3.select('#bar-chart').append('svg') // append SVG to id=bar-chart
.attr('width', width + horizontalMargin) // width + horizontal margins
.attr('height', height + verticalMargin) // height + vertical margins
.style('background', backgroundColor) // background color
.append('g') // for translating the entire graph to accommodate axes
.attr('transform', 'translate('+margin.left+','+margin.top+')') // transform and translate
.selectAll('rect').data(results) // create rectangles for the graph
.enter().append('rect') // placeholder append rectangle
.style({'fill': barColor, 'stroke': strokeColor, 'stroke-width': '2'}) // styling for each bar
// .attr('width', barWidth)
.attr('width', xScale.rangeBand()) //use x axis rangeBand to determine width of each bar
.attr('height', function (data) {
  return yScale(data.publications); //use y axis range to determine height of each bar. The tallest value takes up all the height
})
.attr('x', function (data, i) {
  // return i * (barWidth + barOffset);
  return xScale(i); //use x axis rangeband to determine position of the respective bar
})
.attr('y', function (data) {
  return height - yScale(data.publications); // use y axis range to determine height of each bar. 
})
.on('mouseover', function(data) { // hover changes the color of the bars
  barColor = this.style.fill;
  d3.select(this)
  .style('fill', hoverColor);
  var currentvenue = data.venue;
  var currentpublications = data.publications;
  tooltip.select('.label').html("Venue: "+currentvenue);
  tooltip.select('.count').html("Publications: "+currentpublications);
  tooltip.style('display','block');
  
})
.on('mouseout', function(data) { // mouse out changes the color back to the original color
  d3.select(this)
  .style('fill', barColor)

  tooltip.style('display', 'none')

})
.on('mousemove', function(data){
  tooltip.style('top', (d3.event.layerY - 50) + 'px')
  .style('left', (d3.event.layerX + 10)+'px')
});

//Y axis range breakpoints.
var verticalGuideScale = d3.scale.linear()
.domain([0, maxY(results)])
.range([height, 0]); //different from yScale because it arranges the values 0 at the bottom and larger values at the top

var vAxis = d3.svg.axis()
.scale(verticalGuideScale)
.orient('left')
.ticks(30);

var verticalGuide = d3.select('svg').append('g')
vAxis(verticalGuide)
verticalGuide.attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')')
verticalGuide.selectAll('path')
.style({fill: 'none', stroke: barColor})
verticalGuide.selectAll('line')
.style({stroke: barColor});

//X axis range breakpoints
var hAxis = d3.svg.axis()
.scale(xScale)
.orient('bottom')
.tickFormat(function(i){ //X axis venue names
  // return results[i].venue;
  return "";
});

var horizontalGuide = d3.select('svg').append('g')
hAxis(horizontalGuide)
horizontalGuide.attr('transform', 'translate(' + margin.left + ', ' + (height + margin.top) + ')')
horizontalGuide.selectAll('path')
.style({fill: 'none', stroke: barColor})
horizontalGuide.selectAll('line')
.style({stroke: barColor});
})
})(window.d3);