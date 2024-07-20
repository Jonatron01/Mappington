var color = "#000000"
var key = "key"
document.addEventListener("DOMContentLoaded", function() {
    var socket = new SockJS('https://server.mappington.org:9009/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = null
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/update/pixels", function (pixel) {
            if (pixel.body != null) {
                var px = JSON.parse(pixel.body);
                var clientPx = document.getElementById(px.id);
                clientPx.style.backgroundColor = px.color;
            }
            else {
                console.log("trolled")
            }
        });
        var socketUrl = socket._transport.url;
        var sessionId = /([^\/]+)\/websocket/.exec(socketUrl)[1];
        console.log(sessionId);
        stompClient.subscribe("/update/reload/" + sessionId, function (pixels) {
            console.log("class");
            var pxs = JSON.parse(pixels.body);
            pxs.forEach(function(px) {
                var clientPx = document.getElementById(px.id);
                clientPx.style.backgroundColor = px.color;
            });
        });
        stompClient.subscribe("/update/key/" + sessionId, function (key) {
            console.log("ive got the key")
            console.log(key)
            key = JSON.parse(key.body);
        });
        stompClient.send("/app/reload")
    });
    const container = document.getElementById('container');
    const squares = createSquares(128); // Create 10x10 grid of squares

    squares.forEach(square => {
        container.appendChild(square);

        square.addEventListener('click', function() {
            // Toggle color on click
            square.style.backgroundColor = color;
            console.log(key)
            console.log(stompClient.send("/app/pixel", key, JSON.stringify({
                'id': square.getAttribute("id"),
                'author': '',
                'color': color
            })))
        });
    });

    function createSquares(num) {
        const squares = [];
        for (let i = 1; i <= num * num; i++) {
            const square = document.createElement('div');
            square.classList.add("square");
            square.setAttribute('id',i);
            squares.push(square);
        }
        return squares;
    }

    const hex = document.getElementById('hexColor');
    function updateSelectedColor() {
        color = hex.value;

    }



    hex.addEventListener('input', updateSelectedColor);

});
