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

        //nv.utils.windowResize(chart.update);

        return chart;
    });
}