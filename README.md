# Test

__Important note: When executing this app make sure you'll retrieve gps data very quickly so battery may drain really fast. Also, if you enable maximum performance on your Android phone it'll run better__

Set up a server with this code (enter the IP on the app) and you'll be able to receive GPS data:

```import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(('0.0.0.0',15000))
sock.listen(1)

while(True):

        connection, client_addr = sock.accept()

        print ("accepted conn")
        data = recvall(connection)
        connection.close()

        print(data)
        lat,lon,speed = data.split(",")
        ```

