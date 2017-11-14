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

var dataGroup = [
  {
    "confId": "arXiv2014",
    "venueline": [
      {
        "conference": "abc",
        "citations": 1
      },
      {
        "conference": "edf",
        "citations": 4
      },
     {
        "conference": "ggg",
        "citations": 7
      }
    ]
  },
  {
    "confId": "arXiv2015",
    "venueline": [
      {
        "conference": "abc",
        "citations": 8
      },
      {
        "conference": "edf",
        "citations": 7
      },
     {
        "conference": "ggg",
        "citations": 7
      }
    ]
  }
];

var unnestDataGroup = function(data, children){
                var out = [];
        data.forEach(function(d, i){
          // console.log(i, d);
          d_keys = Object.keys(d);
          // console.log(i, d_keys)
          values = d[children];
          
          values.forEach(function(v){
            d_keys.forEach(function(k){
              if (k != children) { v[k] = d[k]}
            })
            out.push(v);
          })
          
        })
        return out;
      }

var dataGroupKeys = Object.keys(dataGroup[0]);
console.log(dataGroupKeys[1]);

var data = unnestDataGroup(dataGroup, dataGroupKeys[1]);

var dataKeys = Object.keys(data[0]);
var xAxisFormat = d3.format("");

var InitChart = function(){
    var color = d3.scale.category10();
    var vis = d3.select("#custom-chart"),
        WIDTH = 1000,
        HEIGHT = 500,
        MARGINS = {
            top: 50,
            right: 20,
            bottom: 50,
            left: 50
        },
        lSpace = WIDTH/dataGroup.length;
        xScale = d3.scale.ordinal()
        .rangePoints([MARGINS.left, WIDTH - MARGINS.right])
        .domain(data.map(function(d) { 
            console.log(d[dataKeys[0]]);
            return d[dataKeys[0]]; 
        }));
        // .domain([d3.min(data, function(d) {
        //     // console.log("data of xscale (data) "+JSON.stringify(data));
        //     // console.log("data of xscale (dataGroup) "+JSON.stringify(dataGroup));
        //     console.log("X: " + d[dataKeys[0]])
        //     return d[dataKeys[0]];
        // }), d3.max(data, function(d) {
        //     return d[dataKeys[0]];
        // })]),
        yScale = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([d3.min(data, function(d) {
            return d[dataKeys[1]];
        }), d3.max(data, function(d) {
            return d[dataKeys[1]];
        })]),
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
        .interpolate("basis");
    dataGroup.forEach(function(d,i) {
        // console.log("d is " + Object.keys(d));
        vis.append('svg:path')
        .attr('d', lineGen(d[dataGroupKeys[1]]))
        .attr('stroke', function(d,j) { 
                return "hsl(" + Math.random() * 360 + ",100%,50%)";
        })
        .attr('stroke-width', 2)
        .attr('id', 'line_'+d[dataGroupKeys[0]])
        .attr('fill', 'none');
        vis.append("text")
            .attr("x", (lSpace/2)+i*lSpace)
            .attr("y", HEIGHT)
            .style("fill", d3.select("#line_" + d[dataGroupKeys[0]]).attr('stroke'))
            .attr("class","legend")
            .on('click',function(){
                var active   = d.active ? false : true;
                var opacity = active ? 0 : 1;
                d3.select("#line_" + d[dataGroupKeys[0]]).style("opacity", opacity);
                d.active = active;
            })
            .text(d[dataGroupKeys[0]]);
    });
    };
    InitChart();
});