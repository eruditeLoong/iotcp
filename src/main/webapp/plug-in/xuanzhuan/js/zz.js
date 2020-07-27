// JavaScript Document

$(document).ready(function(){

$('#img1').rotate({angle:45});

$("#img2").rotate({ 
   bind: 
     { 
        mouseover : function() { 
            $(this).rotate({animateTo:180});
        },
        mouseout : function() { 
            $(this).rotate({animateTo:0});
        }
     } 
   
});


var angle = 0;
setInterval(function(){
      angle+=3;
     $("#img3").rotate(angle);
},50);


var rotation = function (){
   $("#img4").rotate({
      angle:0, 
      animateTo:360, 
      callback: rotation
   });
}
rotation();



var rotation2 = function (){
   $("#img5").rotate({
      angle:0, 
      animateTo:360, 
      callback: rotation2,
      easing: function (x,t,b,c,d){        // t: current time, b: begInnIng value, c: change In value, d: duration
          return c*(t/d)+b;
      }
   });
}
rotation2();


$("#img6").rotate({ 
   bind: 
     { 
        click: function(){
            $(this).rotate({ angle:0,animateTo:180,easing: $.easing.easeInOutExpo })
        }
     } 
   
});


var value2 = 0
$("#img7").rotate({ 
   bind: 
     { 
        click: function(){
            value2 +=90;
            $(this).rotate({ animateTo:value2})
        }
     } 
   
});

});