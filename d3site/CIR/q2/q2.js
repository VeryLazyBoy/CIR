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
  },
  {
    "confId": "Shawn",
    "venueline": [
      {
        "conference": "abc",
        "citations": 10
      },
      {
        "conference": "edf",
        "citations": 3
      },
     {
        "conference": "ggg",
        "citations": 1
      }
    ]
  }
];


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

//Referenced From: https://code.tutsplus.com/tutorials/building-a-multi-line-chart-using-d3js-part-2--cms-22973
//
var InitChart = function(urlString){
console.log("url string = " + urlString);

// $.ajax({
//         url: urlString
//     }).then(function(results) {
//        alert("RESULTS LOADED!");

var dataGroupKeys = Object.keys(dataGroup[0]);
// console.log(dataGroupKeys[1]);
var data = unnestDataGroup(dataGroup, dataGroupKeys[1]);
var dataKeys = Object.keys(data[0]);
var xAxisFormat = d3.format("");


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
            // console.log(d[dataKeys[0]]);
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
    //});
};

var apiRootUrlOverYears = "http://localhost:8080/json/yearcontemporaries?";
var apiRootUrlOverConferences = "http://localhost:8080/json/confcontemporaries?";

var conferenceCounter = 2;
var conferenceCounter2 = 2;

$("#addConferenceButton").click(function () {
    if(conferenceCounter>10){
        alert("Max. of 10 Conferences allowed.");
        return false;
    }
    var newTextBoxDiv = $(document.createElement('div'))
    .attr("id", 'ConferenceListDiv' + conferenceCounter);
    newTextBoxDiv.after().html('<label>Conference '+ conferenceCounter + ':</label>' +
     '<input class="form-control" type="text" ' + conferenceCounter +
     '" id="confListInput' + conferenceCounter + '" placeholder="e.g. '+"'arXiv'" +'">');     
    newTextBoxDiv.appendTo("#ConferenceListGroup");     
    conferenceCounter++;
});

$("#removeConferenceButton").click(function () {
    if(conferenceCounter==2){
        alert("At least one conference is required.");
        return false;
    }     
    conferenceCounter--;     
    $("#ConferenceListDiv" + conferenceCounter).remove();     
});

$("#addConferenceButton2").click(function () {
    if(conferenceCounter2>10){
        alert("Max. of 10 Conferences allowed.");
        return false;
    }
    var newTextBoxDiv = $(document.createElement('div'))
    .attr("id", 'ConferenceList2Div' + conferenceCounter2);
    newTextBoxDiv.after().html('<label>Conference '+ conferenceCounter2 + ':</label>' +
     '<input class="form-control" type="text" ' + conferenceCounter2 +
     '" id="confList2Input' + conferenceCounter2 + '" placeholder="e.g. '+"'arXiv'" +'">');     
    newTextBoxDiv.appendTo("#ConferenceListGroup2");     
    conferenceCounter2++;
});

$("#removeConferenceButton2").click(function () {
    if(conferenceCounter2==2){
        alert("At least one conference is required.");
        return false;
    }     
    conferenceCounter2--;     
    $("#ConferenceList2Div" + conferenceCounter2).remove();     
});

$('#queryTypeSelect').on('change', function() {
  var confYears = false;
  var confList = false;

  var val = this.value;
  // console.log(val);
  switch(val){
    case '0': //over years
    confYears = true;
    confList = false;
    break;
    case '1':
    confYears = false;
    confList = true;
    break;
  }
  if(confYears){
    $("#startYearInputContainer").removeClass("hidden");
    $("#endYearInputContainer").removeClass("hidden");
  }else{
    $("#startYearInputContainer").addClass("hidden");
    $("#endYearInputContainer").addClass("hidden");
  }
  if(confList){
    $("#ConferenceListGroup2").removeClass("hidden");
  }else{
    $("#ConferenceListGroup2").addClass("hidden");
  }
});
var isNumeric = function(num){
    return !isNaN(num)
}
var isValidYear = function(yearString){
    if(yearString == ""){
        return true;
    }
    if(!isNumeric(yearString)){
        return false;
    }
    if(parseInt(yearString) < 2017 && parseInt(yearString) > 0){
        return true;
    }
    return false;
}
$("#generateBtn").click(function () {

    var urlString;
    if($('#queryTypeSelect').val() == 0){
        urlString = apiRootUrlOverYears;

        var conferenceYear = $("#conferenceYearInput").val();
         if(!conferenceYear){
            alert("Conference year is required");
            return false;
        }
        if(!isValidYear(conferenceYear)){
            alert("Conference year is invalid");
            return false;
        }
        var startYear = $("#startYearInput").val();
         if(!isValidYear(startYear)){
            alert("Start year is invalid");
            return false;
        }
        var endYear = $("#endYearInput").val();
        if(!isValidYear(endYear)){
            alert("End year is invalid");
            return false;
        }
       
        if(parseInt(startYear) > parseInt(endYear)){
            alert("Start year must be before end year.");
            return false;
        }
        var conferences = "";
        for(t = 1; t<=conferenceCounter; t++){
            if($('#confListInput'+t).val()){
                conferences += $('#confListInput' + t).val() + '$$';
            }
        }
        // console.log("Before slicing: " + conferences);
        if(conferences){
            // console.log("slicing ");
            conferences = conferences.slice(0,-1); //removes the last '$' from the string
            conferences = conferences.slice(0,-1); //removes the 2nd last '$' from the string
            // console.log("After slicing: " + conferences);
        }else{
            alert("At least 1 conference code is required.");
            return false;
        }

        if(conferenceYear && conferences){
            urlString += "year=" + conferenceYear + "&";
            urlString += "confs=" + conferences + "&";
            urlString += "syear=" + startYear + "&";
            urlString += "eyear=" + endYear;
        }
    }else{
        urlString = apiRootUrlOverConferences;
        var conferenceYear = $("#conferenceYearInput").val();
        var conferences = "";
        if(!conferenceYear){
            alert("Conference year is required");
            return false;
        }
        for(t = 1; t<=conferenceCounter; t++){
            if($('#confListInput'+t).val()){
                conferences += $('#confListInput' + t).val() + '$$';
            }
        }
        if(conferences){
            // console.log("slicing ");
            conferences = conferences.slice(0,-1); //removes the last '$' from the string
            conferences = conferences.slice(0,-1); //removes the 2nd last '$' from the string
            // console.log("After slicing: " + conferences);
        }else{
            alert("At least 1 conference code is required.");
            return false;
        }
        var conferences2 = "";
        for(t = 1; t<=conferenceCounter2; t++){
            if($('#confList2Input'+t).val()){
                conferences2 += $('#confList2Input' + t).val() + '$$';
            }
        }
        if(conferences2){
            // console.log("slicing ");
            conferences2 = conferences2.slice(0,-1); //removes the last '$' from the string
            conferences2 = conferences2.slice(0,-1); //removes the 2nd last '$' from the string
            // console.log("After slicing: " + conferences2);
        }else{
            alert("At least 1 conference code is required.");
            return false;
        }
        if(conferenceYear && conferences && conferences2){
            urlString += "year=" + conferenceYear + "&";
            urlString += "confs=" + conferences + "&";
            urlString += "conflist=" + conferences2;
        }
    }


    InitChart(encodeURI(urlString));
    
});

// InitChart();

});