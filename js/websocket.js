

var name="";
            if ("WebSocket" in window)
            {
               
               // Let us open a web socket
                var adres="ws://tidermondb.herokuapp.com:9696/";
               var ws = new WebSocket(adres);
				
               ws.onopen = function()
               {
                  // Web Socket is connected, send data using send()
									
	
                };
				
               ws.onmessage = function (evt) 
               { 
                  var received_msg = evt.data;
								//			alert(evt.data);
							
									var obj = JSON.parse(received_msg);
									if(obj.cmd == "hallo")
                  alert(obj.msg);
               };
				
               ws.onclose = function()
               { 
                  // websocket is closed.
                  alert("Connection is closed..."); 
               };
					
               window.onbeforeunload = function(event) {
                  socket.close();
               };
            }
            
            else
            {
               // The browser doesn't support WebSocket
               alert("WebSocket NOT supported by your Browser!");
            }
						
			

         
				
	
