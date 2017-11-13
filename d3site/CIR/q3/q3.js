$( document ).ready(function() {
  var apiString = "";
  var generateD3 = function(urlString) {

    //Clear the bar-chart
    $("#bar-chart").html(function(){
      return "";
    });

    //generate the bar-chart
    'use strict';
  //data from API
  var results = [
  {
    'title': 'Paper1',
    'shawn':2134
  },
  {
    'title': 'Paper2',
    'shawn':4352
  },
  {
    'title': 'Paper3',
    'shawn':45645
  },
  {
    'title': 'Paper4',
    'shawn':3242
  },
  {
    'title': 'Paper5',
    'shawn':6576
  },
  {
    'title': 'Paper6',
    'shawn':13414
  },
  {
    'title': 'Paper7',
    'shawn':43577
  },
  {
    'title': 'Paper8',
    'shawn':4422
  },
  {
    'title': 'Paper9',
    'shawn':65234
  },
  {
    'title': 'Paper10',
    'shawn':12348
  }
  ];
  // $.ajax({
  //       url: "http://localhost:8080/json/articles?venue=arXiv&top=5"
  //   }).then(function(results) {
  //      alert("RESULTS LOADED!");

// d3.json("../json/article_bar.json", function(results) {

//get keys in JSON
var keys = Object.keys(results[0]);
console.log(keys);

//Margins to accommodate X and Y axis labels
var margin = {top: 30, right: 10, bottom: 30, left: 100}
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
var maxY = function(results){
  var highest = -1;
  for(var i=0; i<results.length; i++){
    if(results[i][keys[1]] > highest){
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
  console.log("DATA: " + data[keys[1]]);
  return yScale(data[keys[1]]); //use y axis range to determine height of each bar. The tallest value takes up all the height
})
.attr('x', function (data, i) {
  // return i * (barWidth + barOffset);
  return xScale(i); //use x axis rangeband to determine position of the respective bar
})
.attr('y', function (data) {
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

  tooltip.select('.label').html(xLabelToUpper+": "+currentX);
  tooltip.select('.count').html(yLabelToUpper+": "+currentY);
  tooltip.style('display','block');
})
.on('mouseout', function(data) { // mouse out changes the color back to the original color
  d3.select(this)
  .style('fill', barColor)

  tooltip.style('display', 'none')

})
.on('mousemove', function(data){
  tooltip.style('top', (d3.event.layerY + 10) + 'px')
  .style('left', (d3.event.layerX + 10)+'px')
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
.style({fill: 'none', stroke: barColor})
verticalGuide.selectAll('line')
.style({stroke: barColor});

//X axis range breakpoints
var hAxis = d3.svg.axis()
.scale(xScale)
.orient('bottom')
.tickFormat(function(i){ //X axis title names
  // return results[i].title;
  return "";
});

var horizontalGuide = d3.select('svg').append('g')
hAxis(horizontalGuide)
horizontalGuide.attr('transform', 'translate(' + margin.left + ', ' + (height + margin.top) + ')')
horizontalGuide.selectAll('path')
.style({fill: 'none', stroke: barColor})
horizontalGuide.selectAll('line')
.style({stroke: barColor});
// })

};
var generateAPIUrl = function(){
  var urlString;
   Top = document.getElementById("topvalue").value;
   TypeX= document.getElementById("TypeX").value;   
   TypeY= document.getElementById("TypeY").value;
   TypeYval= document.getElementById("TypeYvalue").value;
   TypeY2 = document.getElementById("TypeY2").value;
   TypY2val = document.getElementById("TypeY2value").value;
   location.href = "T3.js?Top="+ Top + "&TypeX=" + TypeX + "&TypeY="+ TypeY + "&TypeYVal=" + TypeYval + "&TypeY2=" + TypeY2 + 
   "&TypeY2Val="+ TypY2val;

return urlString;
}

$('#queryTypeSelect').on('change', function() {
  alert( this.value );
})

$("#generateBtn").click(function(){

var urlString = generateAPIUrl();
 generateD3(urlString);
});
});