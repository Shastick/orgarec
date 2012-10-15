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
        //var n = findNode(id);
        while (i < links.length) {
            if ((links[i].source == id)||(links[i].target == id))
            {
                links.splice(i,1);
            }
            else i++;
        }
        nodes.splice(findNodeIndex(id),1);
        update();
    };

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

    this.removeallLinks = function(){
        links.splice(0,links.length);
        update();
    };

    this.removeAllNodes = function(){
        nodes.splice(0,links.length);
        update();
    };

    this.addLink = function (link) {
        links.push({"source":findNode(link.source),"target":findNode(link.target),"value":link.value});
        update();
    };

    var findNode = function(id) {
        for (var i=0;i<nodes.length;i++) {
            if (nodes[i].id == id) {
                return nodes[i];
            }
        };
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
        .append('svg:g');

    var force = d3.layout.force();

    var nodes = force.nodes(),
        links = force.links();

    var update = function () {

        var link = vis.selectAll("line.link")
            .data(links)

        link.enter().insert("svg:line", ".node")
            .attr("id",function(d){return d.source + "-" + d.target;})
            //.attr("class","link")
            .attr("stroke", "#9ecae1")
            .attr("stroke-width", 5);

        link.append("title")
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
            .data(nodes);


        var nodeEnter = node.enter().append("g")
            .attr("class", "node")
            .call(node_drag);


        /* Customize circle */
        nodeEnter.append("svg:circle")
            .attr("cursor","pointer")
            .style("fill",function(d){return "#c6dbef"})
            .attr("r", function(d){return d.credits *5+"px";})
            .attr("stroke", function(d){return "#3182bd";})
            .attr("stroke-width",function(d){return "1.5px";})
            .on("mouseover", function(d){
                d3.json("details/"+ d.id, function(details){
                    document.getElementById('info-container').innerHTML= details;
                    //details;
                })
            });

        /* Add text in middle of circle */
        nodeEnter.append("text")
            .attr("class", "nodetext")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .text(function(d) { return d.alias.substring(0, d.credits*5 / 3); });

        /* Add title */
        nodeEnter.append("svg:title")
            .text(function(d){ return d.id + ' - ' + d.name});

        node.exit().remove();

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
            graph.addLink(links[i])
        }
        //graph.removeNode(100);

    });


    /*graph.addNode('A');
    graph.addNode('B');
    graph.addNode('C');
    graph.addLink('A','B','10');
    graph.addLink('A','C','8');
    graph.addLink('B','C','15');*/
}
drawGraph();