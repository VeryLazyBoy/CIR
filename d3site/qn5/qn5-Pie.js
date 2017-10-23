 (function(d3) {
  'use strict';

  // var dataset = [
  // {
  //   'venue': 'Earth',
  //   'publications':2134
  // },
  // {
  //   'venue': 'Mars',
  //   'publications':4352
  // },
  // {
  //   'venue': 'Venus',
  //   'publications':45645
  // },
  // {
  //   'venue': 'Jupiter',
  //   'publications':3242
  // },
  // {
  //   'venue': 'Saturn',
  //   'publications':6576
  // }
  // ];
  
  // $.ajax({
 //        url: "http://rest-service.guides.spring.io/greeting"
 //    }).then(function(data) {
 //       $('.greeting-id').append(data.id);
 //       $('.greeting-content').append(data.content);
 //    });
  d3.json("../json/venue_sector.json", function(dataset) {

  var width = 16000;
  var height = 16000;
  var radius = Math.min(width, height) / 2;

  var color = d3.scaleOrdinal(d3.schemeCategory20);

  var donutWidth = 15000; 
  var legendRectSize = 18;
  var legendSpacing = 4;

  var svg = d3.select('#chart')
  .append('svg')
  .attr('width', width)
  .attr('height', height)
  .append('g')
  .attr('transform', 'translate(' + (width / 2) +
    ',' + (height / 2) + ')');

  var arc = d3.arc()
  .innerRadius(radius-donutWidth)
  .outerRadius(radius);

  var pie = d3.pie()
  .value(function(d) { return d.publications; })
  .sort(null);

  var tooltip = d3.select('#chart')
  .append('div')
  .attr('class', 'tooltip');

  tooltip.append('div')
  .attr('class', 'venue');

  tooltip.append('div')
  .attr('class', 'publications');

  tooltip.append('div')
  .attr('class', 'percent');

  dataset.forEach(function(d){
    d.publications = +d.publications;
    d.enabled = true;
  });

  var path = svg.selectAll('path')
  .data(pie(dataset))
  .enter()
  .append('path')
  .attr('d', arc)
  .attr('fill', function(d) {
    return color(d.data.venue);
  })
  .each(function(d){this._current=d;});

  path.on('mouseover', function(d){
    var total=d3.sum(dataset.map(function(d){
      return (d.enabled)?d.publications:0;
    }));

    var percent = Math.round(1000*d.data.publications/total)/10;
    tooltip.select('.venue').html("venue: "+d.data.venue);
    tooltip.select('.publications').html("publications: "+d.data.publications);
    tooltip.select('.percent').html(percent+'%');
    tooltip.style('display', 'block');
  });

  path.on('mouseout', function(d){
    tooltip.style('display','none');
  });

  path.on('mousemove', function(d){
    tooltip.style('top', (d3.event.layerY + 10) + 'px')
    .style('left', (d3.event.layerX+10)+'px');
  });

  var legend = svg.selectAll('.legend')
  .data(color.domain())
  .enter()
  .append('g')
  .attr('class', 'legend')
  .attr('transform', function(d,i){
    var height = legendRectSize + legendSpacing;
    var offset = height * color.domain().length/2;
    var horz = -2 * legendRectSize;
    var vert = i*height - offset;
    return 'translate(' + horz + ',' + vert + ')';
  });

  // legend.append('rect')
  // .attr('width', legendRectSize)
  // .attr('height', legendRectSize)
  // .style('fill', color)
  // .style('stroke', color)
  // .on('click', function(venue){
  //   var rect=d3.select(this);
  //   var enabled=true;
  //   var totalEnabled = d3.sum(dataset.map(function(d){
  //     return (d.enabled)?1:0;
  //   }));

  //   if(rect.attr('class')==='disabled'){
  //     rect.attr('class','');
  //   }else{
  //     if(totalEnabled < 2) return;
  //     rect.attr('class', 'disabled');
  //     enabled = false;
  //   }

  //   pie.value(function(d){
  //     if(d.venue===venue)d.enabled=enabled;
  //     return(d.enabled)?d.publications:0;
  //   })

  //   path = path.data(pie(dataset));

  //   path.transition()
  //   .duration(750)
  //   .attrTween('d', function(d){
  //     var interpolate = d3.interpolate(this._current,d);
  //     this._current = interpolate(0);
  //     return function(t){
  //       return arc(interpolate(t));
  //     };
  //   });
  // });

  // legend.append('text')
  // .attr('x', legendRectSize + legendSpacing)
  // .attr('y', legendRectSize - legendSpacing)
  // .text(function(d) { return d; });
})
})(window.d3);