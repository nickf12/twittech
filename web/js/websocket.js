var ws;
var session = {
    id: '',
    conn: []
};

var pcConfig = {
    'iceServers': [
        {'urls': 'stun:stun.services.mozilla.com'},
        {'urls': 'stun:stun.l.google.com:19302'}
    ]
};

//init websocket connection
$(document).ready(function () {
    var startWs = function() {
        ws = new WebSocket('ws://localhost:8080/signaling');
        ws.onopen = function () {
            console.log('WSconnection ready!');
        };
        ws.onmessage = handleSignal;
        ws.onclose = function () {
            console.log('WSconnection closed!');
            setTimeout(function(){
                startWs();
            }, 500);
        }
    };
    startWs();
});


function handleSignal(message) {
    /*if (!pc) startWS(); // DEPRECATED*/
    try {
        var signal = JSON.parse(message.data);
    } catch (err) {
        console.log("Can parse the JSONstring!");
    }
    if (signal.id) {
        setLocalSession(signal);
    } else if (signal.new) {
        setRemoteSession(signal.new);
    } else if (signal.remove) {
        removeConnection(signal.remove);
    } else if (signal.offer) {
        var conn = getConnById(signal.from);
        conn.pc.setRemoteDescription(new RTCSessionDescription(signal.offer))
            .then(function () {
                return conn.pc.createAnswer();
            })
            .then(function (answer) {
                return conn.pc.setLocalDescription(answer)
            })
            .then(function () {
                ws.send(JSON.stringify({answer: conn.pc.localDescription, from: session.id, to: conn.id}));
            })
            .catch(handleError);
    } else if (signal.answer) {
        var conn = getConnById(signal.from);
        conn.pc.setRemoteDescription(new RTCSessionDescription(signal.answer))
            .catch(handleError);
    } else if (signal.ice) {
        var conn = getConnById(signal.from);
        conn.pc.addIceCandidate(new RTCIceCandidate(signal.ice))
            .catch(handleError);
    } else if (signal.call) {
        if (signal.call == 'offer') {
            alert(signal.from + 'is calling you!');
            acceptCall(signal);
        } else {
            answerCall(signal);
        }
    } else if(signal.is) {
        var callB = document.createElement('button');
        callB.innerText = 'Call';
        callB.disabled = 'true;';
        callB.onclick = function() {
            call(signal.id);
        };
        if(signal.is) {
            callB.disabled = "false"
        }
        $('.follow').prepend(callB);
    } else {
        console.log("UNKOWN SIGNAL:" + signal);
    }
}

/*HANDLE WEBRTC MESSAGES*/
    function handleICE(event) {
        if (event.candidate != null) {
            ws.send(JSON.stringify({'ice': event.candidate}))
        }
    }

    function negotiateSDP(id) {
        try {
            var pc = getConnById(id).pc;
            pc.createOffer()
                .then(function (offer) {
                    pc.setLocalDescription(offer);
                })
                .then(function () {
                    ws.send(JSON.stringify({offer: pc.localDescription, to: id, from: session.id}));
                })
                .catch(handleError);
        } catch(err) {
            console.log(err);
        }

    }
/*¡¡¡¡¡ END !!!!*/


/*MANAGE CONNECTIONS*/
    //create a peerconnection with each current peer.
    function setLocalSession(signal) {
        session.id = signal.id;
        if (signal.connections) {
            signal.connections.forEach(function (id, i) {
                    createRemoteConn(id);
                    var pc = session.conn[i].pc;
                    pc.createOffer()
                        .then(function (offer) {
                            return pc.setLocalDescription(offer);
                        })
                        .then(function () {
                            ws.send(JSON.stringify({offer: pc.localDescription, to: id, from: session.id}));
                        })
                        .catch(handleError);
                    //show the peer in a peerList
                    $('#leftcolumn').append('<p id="' + id + '"><a onclick="call(this.innerText)">' + id + '</a></p>');
                }
            );
        }
    }

    //Add a new connection
    function setRemoteSession(id) {
        var pc, dc;
        pc = new RTCPeerConnection(pcConfig);
        pc.onicecandidate = function (event) {
            if (event.candidate != null) {
                ws.send(JSON.stringify({ice: event.candidate, to: id, from: session.id}));
            }
        };
        pc.onnegotiationneeded = function (event) {
            negotiateSDP(id);
        };
        pc.ondatachannel = function (event) {
            getConnById(id).dc = event.channel;
            event.channel.send(JSON.stringify({}));
        };
        pc.onaddstream = function (event) {
            var remote = $('#remote').length > 0 ? $('#remote') : document.createElement('video');
            remote.id = 'remote';
            remote.src = window.URL.createObjectURL(event.stream);
            $('#content').append(remote);
        };

        session.conn.push({
            id: id,
            pc: pc,
            dc: dc
        });
        /*createRemoteConn(id);*/
        $('#leftcolumn').append('<p id="' + id + '"><a onclick="call(this.innerText)">' + id + '</a></p>');
    }

    //init a peerconnection with peer(id)
    function createRemoteConn(id) {
        var pc, dc;
        pc = new RTCPeerConnection(pcConfig);
        pc.onicecandidate = function (event) {
            if (event.candidate != null) {
                ws.send(JSON.stringify({ice: event.candidate, to: id, from: session.id}));
            }
        };
        pc.onnegotiationneeded = function () {
            negotiateSDP(id);
        };
        pc.ondatachannel = function (event) {
            dc.send(JSON.stringify({}));
        };
        pc.onaddstream = function (event) {
            var remote = $('#remote').length > 0 ? $('#remote') : document.createElement('video');
            remote.id = 'remote';
            remote.src = window.URL.createObjectURL(event.stream);
            $('#content').append(remote);
        };
        /*dc = pc.createDataChannel('DC-' + id);
         dc.onmessage = function (event) {
         var result = document.createElement('p');
         result.innerText = event.data;
         $(document).prepend(result);
         };
         dc.onopen = function () {
         console.log('datachannel ready')
         };*/
        session.conn.push({
            pc: pc,
            dc: dc,
            id: id
        });
    }

    //initialize a peerConnection
    function createPeerConnection() {
        var pc = new RTCPeerConnection(pcConfig);
        pc.onicecandidate = function (event) {
            if (event.candidate != null) {
                ws.send(JSON.stringify({ice: event.candidate, to: id, from: session.id}));
            }
        };
        pc.onnegotiationneeded = function () {
            negotiateSDP(id);
        };
        pc.ondatachannel = function (event) {
            dc.send(JSON.stringify({}));
        };
        pc.onaddstream = function (event) {
            var remote = $('#remote').length > 0 ? $('#remote') : document.createElement('video');
            remote.id = 'remote';
            remote.src = window.URL.createObjectURL(event.stream);
            $('#content').append(remote);
        };
        return pc;
    }

    //remove peer(id)
    function removeConnection(id) {
        session.conn.forEach(function (conn, i) {
            if (conn.id == id) {
                /*conn.dc.close();*/
                conn.pc.close();
                session.conn.splice(i, 1);
            }
        });
        $('#leftcolumn #' + id).remove();
    }

    //return peer(id)
    function getConnById(id) {
        var res = null;
        session.conn.forEach(function (conn, i) {
            if (conn.id == id) {
                res = conn;
            }
        });

        return res;
    }
/*¡¡¡¡¡END!!!!!*/


/*HANDLE CALLS*/
    function call(id) {
        ws.send(JSON.stringify({call: 'offer', to: id, from: session.id}));
    }

    function acceptCall(offer) {
        var pc = getConnById(offer.from).pc;
        if (navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({video: true, audio: true})
                .then(function (stream) {
                    var local = $('#local').length > 0 ? $('#local') : document.createElement('video');
                    local.id = 'local';
                    local.src = window.URL.createObjectURL(stream);
                    $('#content').append(local);
                    ws.send(JSON.stringify({call: 'answer', to: offer.from, from: session.id}));
                    pc.addStream(stream);
                })
                .catch(handleError);
        } else {
            alert('Your browser does not support getUserMedia API');
        }
    }

    function answerCall(answer) {
        var pc = getConnById(answer.from).pc;
        if (navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({video: true, audio: true})
                .then(function (stream) {
                    var local = $('#local').length > 0 ? $('#local') : document.createElement('video');
                    local.id = 'local';
                    local.src = window.URL.createObjectURL(stream);
                    $('#content').append(local);
                    pc.addStream(stream);
                })
                .catch(handleError);
        } else {
            alert('Your browser does not support getUserMedia API');
        }
    }
/*¡¡¡¡END!!!!*/

const sendMe = function(userId) {
    ws.send(JSON.stringify({me: userId}));
};

function handleError(error) {
    console.log(error);
}