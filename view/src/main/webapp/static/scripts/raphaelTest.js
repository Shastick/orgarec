/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 24.09.12
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */

window.onload = function() {
    // Creates canvas 320 × 200 at 10, 50
    var paper = new Raphael(document.getElementById('canvas_container'), 500, 500);

    // Creates circle at x = 50, y = 40, with radius 10
    var circle = paper.circle(50, 40, 10);

    // Sets the fill attribute of the circle to red (#f00)
    circle.attr("fill", "#f00");

    // Sets the stroke attribute of the circle to white
    circle.attr("stroke", "#fff");
}