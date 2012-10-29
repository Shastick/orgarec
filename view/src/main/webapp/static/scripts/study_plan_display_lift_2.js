/*
 * Inspired by: http://stackoverflow.com/questions/11400241/updating-links-on-a-force-directed-graph-from-dynamic-json-data
 */


var graph;

function myGraph(el) {

// Add and remove elements on the graph object
    this.addNode = function (node) {
        nodes.push(node);
        update();
    };

    this.removeNode = function (id) {
        var i = 0;
        var n = findNode(id);
        if(n != null){
            while (i < links.length) {
                if ((links[i]['source'] == n)||(links[i]['target'] == n))
                    links.splice(i,1);
                else i++;
            }
            nodes.splice(findNodeIndex(id),1);
            update();
        }
    };

    this.editNode = function(node) {
        var n = findNodeIndex(node.id);
        var initialNode = nodes[n];
        initialNode["credits"] = node.credits;
        nodes.splice(n, 1, initialNode);
        update();
    }

    this.removeLink = function (source,target){
        for(var i=0;i<links.length;i++)
        {
            if(links[i].source.id == source && links[i].target.id == target)
            {
                links.splice(i,1);
                break;
            }
        }
        update();
    };


    this.removeAllLinks = function(){
        links.splice(0,links.length);
        update();
    };

    this.removeAllNodes = function(){
        nodes.splice(0,links.length);
        update();
    };

    this.addLink = function (source, target, value) {
        links.push({"source":findNode(source),"target":findNode(target),"value":value});
        update();
    };

    var findNode = function(id) {
        for (var i in nodes) {
            if (nodes[i]["id"] === id) return nodes[i]
        ;};
        return null;
    };

    var findNodeIndex = function(id) {
        for (var i=0;i<nodes.length;i++) {
            if (nodes[i].id==id){
                return i;
            }
        };
    };

// set up the D3 visualisation in the specified element
    var w = 550,
        h = 500;
    var vis = d3.select(el)
        .append("svg:svg")
        .attr("width", w)
        .attr("height", h)
        .attr("id","svg")
        .attr("pointer-events", "all")
        .attr("viewBox","0 0 "+w+" "+h)
        .attr("perserveAspectRatio","xMinYMid")
        .append('svg:g')
        .call(d3.behavior.zoom().on("zoom", redraw))
        .append('svg:g');

    /* For zoom in non-occupied places */
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


    var force = d3.layout.force();

    var nodes = force.nodes(),
        links = force.links();

    var update = function () {

        var link = vis.selectAll("line")
            .data(links, function(d) {
                return d.source.id + "-" + d.target.id;
            });

        var linkEnter = link.enter().insert("svg:line", ".node")
            .attr("id",function(d){return d.source.id + "-" + d.target.id;})
            .attr("class","link")
            .attr("stroke", "#9ecae1")
            .attr("stroke-width", 5);

        linkEnter.append("title")
            .text(function(d){
                return d.value;
            });

        link.exit().remove();

        /* Functions to drag nodes */
        var node_drag = d3.behavior.drag()
            .on("dragstart", dragstart)
            .on("drag", dragmove)
            .on("dragend", dragend);

        function dragstart(d, i) {
            d.fixed =true;
            //force.stop() //stops the force auto positioning before you start dragging
        }

        function dragmove(d, i) {
            d.px += d3.event.dx;
            d.py += d3.event.dy;
            d.x += d3.event.dx;
            d.y += d3.event.dy;
            //tick();
        }

        function dragend(d, i) {
            force.resume();
        }


        var node = vis.selectAll("g.node")
            .data(nodes, function(d) {return d.id;});

        node.selectAll("circle")
            .transition()
            .attr("r", function(d){return d.credits *5+"px";});

        var nodeEnter = node.enter().append("g")
            .attr("class", "node")
            .call(node_drag)

        /* append circle */
        nodeEnter.append("circle")
            .attr("id", function(d){return "circle-node-"+ d.id})
            .attr("class", "circle-node")
            .attr("cursor","pointer")
            .style("fill",function(d){return "#c6dbef"})
            .attr("r", function(d){return d.credits *5+"px";})
            .attr("stroke", function(d){return "#3182bd";})
            .attr("stroke-width",function(d){return "1.5px";})
            .on("contextmenu", function(data, index) {
                d3.select('#my_custom_menu')
                    .style('position', 'absolute')
                    //.style('left', d3.event.x)
                    .style('left', d3.event.x + "px")
                    .style('top', d3.event.y + "px")
                    .style('display', 'block');
                    //.style()
                d3.event.preventDefault();
                alert(d3.event.x);
            })
            .on("mouseover", function(d){
                $.ajax({
                    url: "node_mouseover/"+ d.id,
                    type: "GET",
                    dataType: "script"
                });
            });
        /* Add text in middle of circle */
        nodeEnter.append("text")
            .attr("class", "nodetext")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .text(function(d) { return d.alias.substring(0, d.credits*5 / 3); });
        /* Add title */
        nodeEnter.append("title")
            .text(function(d){ return d.id + ' - ' + d.name});



        node.exit().transition().remove();

        function tick() {
            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });
            node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y  + ")"; });
        }

        force.on("tick", tick);

        // Restart the force layout.
        force
            .gravity(.05)
            .distance(300)
            .charge(-100)
            .linkDistance(function(d){return (100 - d.value*3)})
            .size([w, h])
            .start();
    };


// Make it all go
    update();
}

function drawGraph()
{
    graph = new myGraph("#graph-container");
    d3.json("graph", function(json) {
        var nodes = json.nodes;
        var links = json.links;
        for (var i = 0; i < nodes.length; i++) {
            graph.addNode(nodes[i]);
        }
        for (var i = 0; i < links.length; i++)  {
            graph.addLink(links[i].source, links[i].target, links[i].value)
        }
    });
}
drawGraph();