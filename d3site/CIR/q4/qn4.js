$( document ).ready(function() {
  var apiRoot = "http://localhost:8080/json/networks?";

  var generateD3 = function(urlString) {
    console.log(urlString);
    'use strict';
  //data from API
  var resultsParent = [
  {
    "articles": [
    {
      "id": "id1",
      "title": "CS3219",
      "authors": [
      "Shawn",
      "James"
      ],
      "level": 1
    },
    {
      "id": "id2",
      "title": "paper B",
      "authors": [
      "Trevor",
      "Michael"
      ],
      "level": 3
    },
    {
      "id": "id3",
      "title": "paper C",
      "authors": [
      "Zack",
      "Brown"
      ],
      "level": 2
    },
    {
      "id": "id4",
      "title": "paper D",
      "authors": [
      "Harald",
      "Jonah"
      ],
      "level": 1
    },
    {
      "id": "id5",
      "title": "paper M",
      "authors": [
      "Matt",
      "Bigsby"
      ],
      "level": 4
    },
    {
      "id": "id6",
      "title": "paper N",
      "authors": [
      "John",
      "Green"
      ],
      "level": 3
    }
    ],
    "links": [
    {
      "source": "id2",
      "target": "id3"
    },
    {
      "source": "id5",
      "target": "id2"
    },
    {
      "source": "id6",
      "target": "id3"
    },
    {
      "source": "id3",
      "target": "id4"
    }
    ]
  },
  {
    "articles": [
    {
      "id": "id11",
      "title": "machinelearning",
      "authors": [
      "1Shawn",
      "1James"
      ],
      "level": 1
    },
    {
      "id": "id12",
      "title": "paper 1B",
      "authors": [
      "1Trevor",
      "1Michael"
      ],
      "level": 3
    },
    {
      "id": "id13",
      "title": "paper 1C",
      "authors": [
      "1Zack",
      "1Brown"
      ],
      "level": 2
    },
    {
      "id": "id14",
      "title": "paper 1D",
      "authors": [
      "1Harald",
      "1Jonah"
      ],
      "level": 1
    },
    {
      "id": "id15",
      "title": "paper 1M",
      "authors": [
      "1Matt",
      "1Bigsby"
      ],
      "level": 4
    },
    {
      "id": "id16",
      "title": "paper 1N",
      "authors": [
      "1John",
      "1Green"
      ],
      "level": 3
    }
    ],
    "links": [
    {
      "source": "id12",
      "target": "id13"
    },
    {
      "source": "id15",
      "target": "id12"
    },
    {
      "source": "id16",
      "target": "id13"
    },
    {
      "source": "id13",
      "target": "id14"
    }
    ]
  }
  ];

// $.ajax({
//         url: urlString
//     }).then(function(results) {
//        alert("RESULTS LOADED!");

 // d3.json("../json/network.json", function(results) {
  if(resultsParent){
    for(var v = 0; v < resultsParent.length; v++){
      var results = resultsParent[0];
      var uniqueIds = [];
      var uniqueArticles = [];
      for (var i = 0; i < results.articles.length; i++) {
        var exists = false;
        var id = results.articles[i].id;
        for (var j = 0; j < uniqueIds.length; j++) {
          if(uniqueIds[j] == id){
            exists = true;
          }
        }
        if(!exists){
          uniqueIds.push(id);
          uniqueArticles.push(results.articles[i]);
        }
      }

      // console.log(results[0]);
      //clear existing svg
      $("#networkSVG").html("");
      var svg = d3.select("#networkSVG");
      var width = 200;
      var height = 200;

      svg.attr('width', width).attr('height', height);

      var color = d3.scaleOrdinal(["#000000", "#86DDB2", "#F8C58C", "#E57661"]);

      var simulation = d3.forceSimulation()
      .force("charge_force", d3.forceManyBody().strength(-200))
      .force("link", d3.forceLink().id(function(d) { return d.id; }))
      .force("x", d3.forceX(width / 2))
      .force("y", d3.forceY(height / 2))
      .on("tick", ticked);

      var link = svg.append("g").selectAll(".link"),
      node = svg.append("g").selectAll(".node");
    // text = svg.append("g").selectAll("circle.node")
    simulation.nodes(uniqueArticles);
    simulation.force("link").links(results.links).distance(50);

    var authorsToString = function(authors){
      var combinedString = "";
      for (var i = 0; i < authors.length; i++) {
        combinedString += authors[i];
        if(i<authors.length-1){
          combinedString += ", ";
        }
      }
      return combinedString;
    };
  }

  var ticked = function() {
    link.attr("x1", function(d) { return d.source.x; })
    .attr("y1", function(d) { return d.source.y; })
    .attr("x2", function(d) { return d.target.x; })
    .attr("y2", function(d) { return d.target.y; });

    node.attr("cx", function(d) { return d.x; })
    .attr("cy", function(d) { return d.y; });

      // text.attr("x", function(d) { return d.x-10; })
      // .attr("y", function(d) { return d.y+20; });
    };
    simulation.on("tick", ticked);


    //Tooltip
    var tooltip = d3.select('#network')
    .append('div')
    .attr('class', 'custom-tooltip');
    tooltip.append('div')
    .attr('class', 'title');
    tooltip.append('div')
    .attr('class', 'author');

    link = link
    .data(results.links)
    .enter().append("line")
    .attr("class", "link")
    .attr("marker-end", "url(#arrow)"); //add arrow

    node = node
    .data(uniqueArticles)
    .enter().append("circle")
    .attr("class", "node")
    .attr("r", 8) //radius of the circle
    .style("fill", function(d) { return color(d.level); })
    .on('mouseover', function(d) {
      var currentTitle = d.title;
      var currentAuthors = authorsToString(d.authors);
      tooltip.select('.title').html("Title: "+currentTitle);
      tooltip.select('.author').html("Author: "+currentAuthors);
      tooltip.style('display','block');
      
    })
    .on('mouseout', function(data) {
      tooltip.style('display', 'none')

    })
    .on('mousemove', function(data){
      tooltip.style('top', (d3.event.layerY + 10) + 'px')
      .style('left', (d3.event.layerX + 10)+'px')
    });

  };
  //});
}

var generateAPIUrl = function(){
  var urlString = apiRoot;
  //add values if they exist
  if($('#paper1Value').val() && $('#paper2Value').val()){
      urlString += "paper1="+$('#paper1Value').val() + "&";
      urlString += "paper2="+$('#paper2Value').val();
      return urlString;
  }else{
    alert("Both paper 1 and paper 2 titles are required");
    return false;
  }
  if(urlString == apiRoot){
    alert("Both paper 1 and paper 2 titles are required");
    return false;
  }
}

$("#generateBtn").click(function(){
    var urlString = generateAPIUrl();
    // console.log(urlString);
    if(urlString){
      generateD3(encodeURI(urlString));
    }
});


});

