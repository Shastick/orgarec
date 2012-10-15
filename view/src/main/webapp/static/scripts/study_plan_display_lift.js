/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 26.09.12
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */

var w = 550,
    h = 500

var vis = d3.select("#graph-container").append("svg:svg")
    .attr("width", w)
    .attr("height", h)
    .append('svg:g')
    .call(d3.behavior.zoom().on("zoom", redraw))
    .append('svg:g');

vis.append('svg:rect')
    .attr('width', w)
    .attr('height', h)
    .attr('fill', 'transparent');

function redraw() {
    trans=d3.event.translate;
    scale=d3.event.scale;

    vis.attr("transform",
        "translate(" + trans + ")"
            + " scale(" + scale + ")");
}


function createLinks(json, link){
    var link = vis.selectAll("line.link")
        .data(json.links)
        .enter().insert("svg:line", ".node")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; })

    /* Custumize links */
    // Setting style
    link.attr("stroke", "#9ecae1")
        .attr("stroke-width", 5);

    // Setting title
    link.append("title")
        .text("bonjour!");
    return link;
}

function createNodes(json){
    /* Create nodes */
    var node = vis.selectAll("g.node")
        .data(json.nodes)
        .enter().append("svg:g");
        //.call(node_drag);

    /* Customize circle */
    node.append("svg:circle")
        .attr("cursor","pointer")
        .style("fill",function(d){return "#c6dbef"})
        .attr("r", function(d){return d.credits *5+"px";})
        .attr("stroke", function(d){return "#3182bd";})
        .attr("stroke-width",function(d){return "1.5px";})
        .on("mouseover", function(d){
            d3.json("details/"+ d.order, function(details){
                document.getElementById('info-container').innerHTML= details;
                //details;
            })
        });

    /* Add text in middle of circle */
    node.append("text")
        .attr("class", "nodetext")
        .attr("text-anchor", "middle")
        .attr("dy", ".3em")
        .text(function(d) { return d.alias.substring(0, d.credits*5 / 3); });

    /* Add title */
    node.append("svg:title")
        .text(function(d){ return d.order + ' - ' + d.name});

    return node;
}

var force = d3.layout.force()
    //.nodes(json.nodes)
    //.links(json.links)
    .gravity(.05)
    .distance(300)
    .charge(-100)
    //.linkDistance(function(d){return (100 - d.value*3)})
    .size([w, h])
    //.start();

function showGraph(json){

    /* Configure forces */
    force.nodes(json.nodes)
        .links(json.links)
        .linkDistance(function(d){return (100 - d.value*3)})
        .start();


    var link = createLinks(json);

    /* Functions to drag nodes */
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


    var node = createNodes(json);

    node.call(node_drag);

    function tick() {
        link.attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

        node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    };

    force.on("tick", tick);
}

d3.json("graph", function(json) {showGraph(json);});
