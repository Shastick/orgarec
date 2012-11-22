/*
$(function updateValues(event, ui) {
    var offset1 = $('.ui-slider-handle:first').css('left');
    var offset2 = $('.ui-slider-handle:last').css('left');
    var tickerWidth = $('.sliderLabel').css('width');
    var value1 = ui.values[ 0 ];
    var value2 = ui.values[ 1 ];
    var yearDiff = value2 - value1;
    var specialOffsetMin = 0, specialOffsetMax = 0;
    if (yearDiff > 0 && yearDiff < 3) {
        if (value1 < 1980) {
            specialOffsetMax = 10;
        } else {
            specialOffsetMin = 10;
        }
    }
    var offset1Int = offset1.substring(0, offset1.length - 2);
    var offset2Int = offset2.substring(0, offset2.length - 2);
    var widthInt = tickerWidth.substring(0, tickerWidth.length - 2);
    var correctedOffset1 = offset1Int - 8 - specialOffsetMin;
    var correctedOffset2 = offset2Int - widthInt - 8 + specialOffsetMax;
    $('#value1').text(value1).css({'left':correctedOffset1 +'px'});
    $('#value2').text(value2).css({'left':correctedOffset2 +'px'});
})
*/
/*
function updateValue(event, ui, valueID) {
     var offset1 = $('.ui-slider-handle:first').css('left');
     var value = ui.value;
     var offset1Int = offset1.substring(0, offset1.length - 2);
     var correctedOffset1 = offset1Int - 8;
     $(valueID).text(value).css({'left':correctedOffset1 +'px'});
} )*/