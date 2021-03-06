var margin = {top: 1, right: 1, bottom: 6, left: 1},
    width = 800 - margin.left - margin.right,
    height = 600 - margin.top - margin.bottom;

var formatNumber = d3.format(",.0f"),
    format = function(d) { return formatNumber(d) + " students"; },
    color = d3.scale.category20();

var sankey = d3.sankey()
    .nodeWidth(15)
    .nodePadding(10)
    .size([width, height]);

var path = sankey.link();

function drawSankeyPlot(flowgraph,selector){
  sankey
      .nodes(flowgraph.nodes)
      .links(flowgraph.links)
      .layout(32);

  var svg = d3.select(selector).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  var link = svg.append("g").selectAll(".link")
      .data(flowgraph.links)
    .enter().append("path")
      .attr("class", function(d){return "link " + d.source.id + " " + d.target.id })
      .attr("d", path)
      .style("stroke-width", function(d) { return Math.max(1, d.dy); })
      .sort(function(a, b) { return b.dy - a.dy; });

  link.append("title")
      .text(function(d) { return d.source.name + " → " + d.target.name + "\n" + format(d.value); });

  var node = svg.append("g").selectAll(".node")
      .data(flowgraph.nodes)
    .enter().append("g")
      .attr("class", "node")
      .attr("id", function(n){ return n.id })
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; })
      .on("mouseover", function(d) {
    	  d3.selectAll(".link." + d.id).style("stroke-opacity",0.7)
    	  	.style("stroke", "red")
    	  	.style("z-index", 1)
    	  })
      .on("mouseout", function(d) {
    	  d3.selectAll(".link." + d.id).style("stroke-opacity",0.2)
    	  	.style("stroke", "#000000")
    	  	.style("z-index",0)
      	  })
    .call(d3.behavior.drag()
      .origin(function(d) { return d; })
      .on("dragstart", function() { this.parentNode.appendChild(this); })
      .on("drag", dragmove));

  node.append("rect")
      .attr("height", function(d) { return d.dy; })
      .attr("width", sankey.nodeWidth())
      .style("fill", function(d) { return d.color = color(d.name.replace(/ .*/, "")); })
      .style("stroke", function(d) { return d3.rgb(d.color).darker(2); })
    .append("title")
      .text(function(d) { return d.name + "\n" + format(d.value); });

  node.append("text")
      .attr("x", -6)
      .attr("y", function(d) { return d.dy / 2; })
      .attr("dy", ".35em")
      .attr("text-anchor", "end")
      .attr("transform", null)
      .text(function(d) { return d.name; })
    .filter(function(d) { return d.x < width / 2; })
      .attr("x", 6 + sankey.nodeWidth())
      .attr("text-anchor", "start");

  function dragmove(d) {
    d3.select(this).attr("transform", "translate(" + d.x + "," + (d.y = Math.max(0, Math.min(height - d.dy, d3.event.y))) + ")");
    sankey.relayout();
    link.attr("d", path);
  }
};
