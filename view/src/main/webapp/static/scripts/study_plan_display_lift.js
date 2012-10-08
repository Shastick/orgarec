/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 26.09.12
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */


var w = 960,
    h = 500

var vis = d3.select("#graph-container").append("svg:svg")
    .attr("width", w)
    .attr("height", h);

d3.json(document.getElementById('jsonData'), function(json) {
    var force = self.force = d3.layout.force()
        .nodes(json.nodes)
        .links(json.links)
        .gravity(.05)
        .distance(300)
        .charge(-100)
        .linkDistance(function(d){return (100 - d.value*3)})
        .size([w, h])
        .start();

    var link = vis.selectAll("line.link")
        .data(json.links)
        .enter().insert("svg:line", ".node")
        .attr("class", "link")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    var node_drag = d3.behavior.drag()
        .on("dragstart", dragstart)
        .on("drag", dragmove)
        .on("dragend", dragend);

    function dragstart(d, i) {
        force.stop() // stops the force auto positioning before you start dragging
    }

    function dragmove(d, i) {
        d.px += d3.event.dx;
        d.py += d3.event.dy;
        d.x += d3.event.dx;
        d.y += d3.event.dy;
        tick(); // this is the key to make it work together with updating both px,py,x,y on d !
    }

    function dragend(d, i) {
        d.fixed = true; // of course set the node to fixed so the force doesn't include the node in its auto positioning stuff
        tick();
        force.resume();
    }


    var node = vis.selectAll("g.node")
        .data(json.nodes)
        .enter().append("svg:g")
        .call(node_drag);


    node.append("svg:circle")
        .attr("class", "node")
        .style("fill","#c6dbef")
        .attr("r", function(d){return d.credits *5+"px";}) ;

    node.append("text")
        .attr("class", "nodetext")
        .attr("text-anchor", "middle")
        .attr("dy", ".3em")
        .text(function(d) { return d.alias.substring(0, d.credits*5 / 3); });

    $('svg circle').tipsy({
        gravity: 'w',
        html: true,
        title: function() {
            var d = this.__data__,
                name = d.name,
                order = d.order;
            return order +' - ' +name;
        }
    });

    force.on("tick", tick);

    function tick() {
        link.attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

        node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    };
});
