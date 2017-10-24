(function(d3) {
  'use strict';
  //data from API
  // var results = {
  //   "articles": [
  //   {
  //     "id": "id1", 
  //     "title": "paper A", 
  //     "authors":[
  //     "Shawn", "James"
  //     ], 
  //     "level":1
  //   },
  //   {
  //     "id": "id2", 
  //     "title": "paper B", 
  //     "authors":[
  //     "Trevor", "Michael"
  //     ], 
  //     "level":3
  //   },
  //   {
  //     "id": "id3", 
  //     "title": "paper C", 
  //     "authors":[
  //     "Zack", "Brown"
  //     ], 
  //     "level":2
  //   },
  //   {
  //     "id": "id4", 
  //     "title": "paper D",
  //     "authors":[
  //     "Harald", "Jonah"
  //     ], 
  //     "level":1
  //   },
  //   {
  //     "id": "id5", 
  //     "title":"paper M",
  //     "authors":[
  //     "Matt", "Bigsby"
  //     ], 
  //     "level":4
  //   },
  //   {
  //     "id": "id6", 
  //     "title":"paper N", 
  //     "authors":[
  //     "John", "Green"
  //     ], 
  //     "level":3
  //   }
  //   ],
  //   "links": [
  //   {
  //     "source": "id2", 
  //     "target": "id3"
  //     }, //B CITED BY C
  //     {
  //       "source": "id5", 
  //       "target": "id2"
  //     }, //M CITED BY B
  //     {
  //       "source": "id6", 
  //       "target": "id3"
  //     }, //N CITED BY C
  //     {
  //       "source": "id3", 
  //       "target": "id4"
  //     } //C CITED BY D
  //     ]
  //   };

   // $.ajax({
 //        url: "http://rest-service.guides.spring.io/greeting"
 //    }).then(function(data) {
 //       $('.greeting-id').append(data.id);
 //       $('.greeting-content').append(data.content);
 //    });
 d3.json("../json/network2.json", function(results) {

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

    var svg = d3.select("#network").append('svg'),
    width = 2000,
    height = 2000;

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
.attr('class', 'tooltip');
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

// alert("articles: " + results.articles.length + " , links: " + results.links.length);
// text = text
// .data(results.articles)
// .enter().append("text")
// .attr("class", "label")
// .style("fill", function(d) { return color(d.level); })
// .text(function(d) {return d.title;});

})
})(window.d3);