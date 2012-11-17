var margin = {top: 20, right: 20, bottom: 30, left: 40},
    width = 660 - margin.left - margin.right,
    height = 200 - margin.top - margin.bottom;

var formatPercent = d3.format(".0%");

var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .1);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(formatPercent);


function drawBarPlot(csvStr,selector) {
  var data = d3.csv.parse(csvStr);
  data.forEach(function(d) {
    d.ratio = +d.ratio;
  });

  var svg = d3.select(selector).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  x.domain(data.map(function(d) { return d.course; }));
  //y.domain([0, d3.max(data, function(d) { return d.ratio; })]);
  y.domain([0,1]);

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Proportion");

  svg.selectAll(".bar")
      .data(data)
    .enter().append("rect")
      .attr("class", "bar")
      .attr("x", function(d) { return x(d.course); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.ratio); })
      .attr("height", function(d) { return height - y(d.ratio); });

};