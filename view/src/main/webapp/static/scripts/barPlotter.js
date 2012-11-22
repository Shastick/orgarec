function drawBarPlot(csvStr,selector,w,h) {
  var margin = {top: 20, right: 20, bottom: 30, left: 50},
    width = w - margin.left - margin.right,
    height = h - margin.top - margin.bottom;

  var formatPercent = d3.format(".0%");

  var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .1);

  var y = d3.scale.linear()
    .range([height, 0]);

  var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .tickFormat(function(d){
          return d.substring(0, 9);
    });

  var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(formatPercent);

  var data = d3.csv.parse(csvStr);
  data.forEach(function(d) {
    d.ratio = +d.ratio;
  });

  var svg = d3.select(selector).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  x.domain(data.map(function(d) { return d.name;}));
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
      .style("text-anchor", "end");
      /*.text("Proportion");*/

  svg.selectAll(".bar")
      .data(data)
      .enter().append("rect")
      .attr("class", "bar")
      .attr("x", function(d) { return x(d.name); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.ratio); })
      .attr("height", function(d) { return height - y(d.ratio); });

};

function drawBarPlot2(csvStr,selector,w,h){

    var data = [{key: "Cumulative Return",values: d3.csv.parse(csvStr)}];

    nv.addGraph(function() {
        var chart = nv.models.discreteBarChart()
            .x(function(d) { return d.name })
            .y(function(d) { return d.ratio })
            .forceY([0,1])
            .staggerLabels(true)
            .tooltips(true)
            .showValues(true)

        d3.select(selector).append("svg")
            .datum(data)
            .transition().duration(500)
            .call(chart);

        nv.utils.windowResize(chart.update);

        return chart;
    });
}