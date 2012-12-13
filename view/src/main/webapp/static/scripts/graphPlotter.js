/*
 * Inspired by: http://stackoverflow.com/questions/11400241/updating-links-on-a-force-directed-graph-from-dynamic-json-data
 */


var graph;

function myGraph(el) {

   /* Add and remove elements on the graph object
    * Constraint: if a node with same id is already in the graph, the node is not added.
    */
    this.addNode = function (node) {
        if(findNode(node.id) ==null){
            nodes.push(node);
            update();
        }
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
        initialNode["radius"] = node.radius;
        initialNode["fill"] = node.fill;
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

    this.addLink = function (link) {
        if(findNode(link.source)!= null && findNode(link.target)!= null)  {
            links.push({"source":findNode(link.source),"target":findNode(link.target),"distance":link.distance, "showLink": link.showLink, "coStudents":link.coStudents});
            update();
        }
    };

    var findNode = function(id) {
        for (var i in nodes) {
            if (nodes[i]["id"] == id) return nodes[i]
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
    var w = 4000,
        h = 3000;
    var vis = d3.select(el)
        .append("svg:svg")
        .attr("width", "100%")
        .attr("height", "100%")
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
            .attr("stroke", function(d){
                if (d.showLink)
                    return "rgba(200, 50, 255, 0.2)";
                else return "transparent";
            })
            .attr("stroke-width", function(d){return 20 - d.distance/5});

        linkEnter.append("title")
            .text(function(d){
                return d.coStudents + " students followed both courses";
            });

        link.exit().remove();

        /* Functions to drag nodes */
        var node_drag = d3.behavior.drag()
            .on("dragstart", dragstart)
            .on("drag", dragmove)
            .on("dragend", dragend);

        function dragstart(d, i) {
            d.fixed =true;
        }

        function dragmove(d, i) {
            d.px += d3.event.dx;
            d.py += d3.event.dy;
            d.x += d3.event.dx;
            d.y += d3.event.dy;
            tick();
        }

        function dragend(d, i) {
            force.resume();
        }


        var node = vis.selectAll("g.node")
            .data(nodes, function(d) {return d.id;});


        var nodeEnter = node.enter().append("g")
            .attr("class", "node")
            .call(node_drag)

        /* append circle */
        var circle = nodeEnter.append("circle")
            .attr("id", function(d){return "circle-node-"+ d.id})
            .attr("class", "circle-node")
            .attr("cursor","pointer")
            .attr("fill",function(d){return d.fill})
            .attr("r", function(d){return d.radius+"px";})
            .attr("stroke", function(d){return "black";})
            .attr("stroke-width",function(d){return 1+"px";})
            .on("mouseover", actionsOnMouseOver)
            .on("mouseout", actionsOnMouseOut);

        nodeEnter.append("foreignObject")
            .attr("id", function(d){return "text-node-"+ d.id})
            .attr("x", function(d){return -d.radius+"px";})
            .attr("y", function(d){return -0.3*d.radius+"px";})
            .attr("width", function(d){return 2*d.radius+"px";})
            .attr("height", function(d){return 2*d.radius+"px";})
            .append("xhtml:div")
            .attr("dx", "-10em")
            .attr("class", "textContainer")
            .html(function(d) {return d.name;});

        /* Add title */
        nodeEnter.append("title")
            .text(function(d){ return d.id + ' - ' + d.name});

        node.exit().transition().remove();

        function actionsOnMouseOver(node){
            $("#circle-node-"+ node.id).attr("r", 2*node.radius + "px");
            $("#text-node-"+ node.id)
                .attr("x", function(d){return -2*node.radius+"px";})
                .attr("y", function(d){return -0.6*node.radius+"px";})
                .attr("width", function(d){return 4*node.radius+"px";})
                .attr("height", function(d){return 4*node.radius+"px";})
            getDetails(node.id);
        }

        function actionsOnMouseOut(node){
            $("#text-node-"+ node.id)
                .attr("x", function(d){return -node.radius+"px";})
                .attr("y", function(d){return -0.3*node.radius+"px";})
                .attr("width", function(d){return 2*node.radius+"px";})
                .attr("height", function(d){return 2*node.radius+"px";})
            $("#circle-node-"+ node.id).attr("r", node.radius + "px");
        }

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
            .gravity(.5)
            .distance(100)
            .charge(function(d){return - d.radius*300; })
            //.friction(0.01)
            .linkDistance(function(d){return (110 - d.distance*3)})
            .size([w, h])
            .start();
    };

    $(function() {
        $( "#tags" ).autocomplete({
            minLength: 0,
            source: function(request, resolve) {
                var availableTags1 = $.map(nodes, function(node){return {value: node.id, label: node.name}})
                resolve(availableTags1.filter(function(node){return node.label.toLowerCase().indexOf(request.term.toLowerCase())!==-1}));
            },
            focus: function( event, ui ) {
                $( "#tags" ).val( ui.item.label );
                return false;
            },
            select: function( event, ui){
                $( "#tags" ).val( ui.item.label );
                vis.selectAll("#circle-node-"+ui.item.value)
                    .transition()
                    .attr("r", function(n){console.log("plop"); return "200px"})
                    .attr("fill", "rgba(255,0,0,1)")
            }

        })
    });

    // Make it all go
    update();
}

function makeGraph(json){
    graph = new myGraph("#graph-container");

    var nodes = json.nodes;
    var links = json.links;
    for (var i = 0; i < nodes.length; i++) {
        graph.addNode(nodes[i]);
    }
    for (var i = 0; i < links.length; i++)  {
        graph.addLink(links[i])
    }
}

$(document).ready( function(){
    getGraph("makeGraph");
    }
)