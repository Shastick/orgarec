/*
 * Inspired by: http://stackoverflow.com/questions/11400241/updating-links-on-a-force-directed-graph-from-dynamic-json-data
 */

jQuery.noConflict();

var graph;

function myGraph(el) {


   /* Add and remove elements on the graph object
    * Constraint: if a node with same id is already in the graph, the node is not added.
    */
    this.addNode = function (node) {
        if(findNode(node.id) ==null){
            node.radius = 2*node.radius;
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

    //var zoomFactor = 4;
    var zoom = d3.behavior.zoom();

    var vis = d3.select(el)
        .append("svg:svg")
        .attr("width", "100%")
        .attr("height", "100%")
        .attr("id","svg")
        .attr("pointer-events", "all")
        .attr("viewBox","0 0 "+w+" "+h)
        .attr("perserveAspectRatio","xMinYMid")
        .append('svg:g')
        .call(zoom.on("zoom", redraw))
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
                if (selectedNode !== null && (d.source == selectedNode || d.target == selectedNode) )
                    return "rgba(255,0,0, 0.7)"
                if (d.showLink)
                    return "rgba(200, 50, 255, 0.2)";
                else return "transparent";
            })
            .attr("stroke-width", function(d){return 20 - d.distance/5});


        // TODO use following source for tooltips: http://bl.ocks.org/1212215
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
            .attr("fill-opacity", "80%")
            .attr("stroke-opacity", "80%")
            .on("mouseover", actionsOnMouseOver)
            //.on("mouseout", );

        nodeEnter.append("foreignObject")
            .attr("id", function(d){return "text-node-"+ d.id})
            .attr("x", function(d){return -d.radius+"px";})
            .attr("y", function(d){return -d.radius+"px";})
            .attr("width", function(d){return 2*d.radius+"px";})
            .attr("height", function(d){return 2*d.radius+"px";})
            .append("xhtml:div")
            .attr("class", "textContainer")
            .style("line-height", function(d){return 2*d.radius+"px";} )
            .append("xhtml:div")
            //.attr("dx", "-10em")
            .attr("class", "textCentered")
            .html(function(d) {return d.name;});

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
            .gravity(.5)
            .distance(100)
            .charge(function(d){return - d.radius*300; })
            //.friction(0.01)
            .linkDistance(function(d){return (110 - d.distance*3)})
            .size([w, h])
            .start();
    };

    var selectedNode = null;

    function actionsOnMouseOver(node){
        if(selectedNode !== null){
            unselectNode(selectedNode)
        }
        selectedNode = node

        vis.select("#circle-node-"+ node.id)
            .transition()
            .attr("r", 2*node.radius + "px")
            .attr("fill", "steelBlue");

        vis.select("#text-node-"+ node.id)
            .attr("x", function(d){return -2*node.radius+"px";})
            .attr("y", function(d){return -2*node.radius+"px";})
            .attr("width", function(d){return 4*node.radius+"px";})
            .attr("height", function(d){return 4*node.radius+"px";})
            .select(".textContainer")
            .style("line-height", function(d){return 4*d.radius+"px";} )
        jQuery( "#tags" ).val(node.name);

        vis.selectAll('[class=link][id*='+ node.id+']')
           .attr("stroke", "rgba(255, 0, 0, 0.7)")

        getDetails(node.id);
    }

    function unselectNode(node){
        vis.selectAll('[class=link][id*='+ node.id+']')
            .attr("stroke", function(d){
                if (d.showLink)
                    return "rgba(200, 50, 255, 0.2)";
                else return "transparent";
            })

        vis.select("#text-node-"+ node.id)
            .attr("x", function(d){return -node.radius+"px";})
            .attr("y", function(d){return -node.radius+"px";})
            .attr("width", function(d){return 2*node.radius+"px";})
            .attr("height", function(d){return 2*node.radius+"px";})
            .select(".textContainer")
            .style("line-height", function(d){return 2*d.radius+"px";} )

        vis.select("#circle-node-"+ node.id)
            .transition()
            .attr("r", node.radius + "px")
            .attr("fill", node.fill);

        selectedNode = null;
    }

    function zoomOnNode(node){
        var zoomFactor = parseInt(vis.select("#circle-node-"+node.id).attr("r")) *0.25
        var transx = (-node.x)*zoomFactor + w/2,
            transy = (-node.y)*zoomFactor + h/2;
        vis.transition().attr("transform", "translate(" + transx + "," + transy + ") scale(" + zoomFactor + ")");
        zoom.scale(zoomFactor);
        zoom.translate([transx, transy]);


    }

    jQuery(function() {
        jQuery( "#tags" ).autocomplete({
            minLength: 0,
            source: function(request, resolve) {
                var availableTags1 = jQuery.map(nodes, function(node){return {value: node.id, label: node.name}})
                resolve(availableTags1.filter(function(node){return node.label.toLowerCase().indexOf(request.term.toLowerCase())!==-1}));
            },
            focus: function( event, ui ) {
                jQuery( "#tags" ).val(ui.item.label );
                return false;
            },
            select: function( event, ui){
                jQuery( "#tags" ).val(ui.item.label );
                actionsOnMouseOver(findNode(ui.item.value))
                zoomOnNode(findNode(ui.item.value))
                return false;
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

jQuery(document).ready( function(){
    getGraph("makeGraph");
    }
)