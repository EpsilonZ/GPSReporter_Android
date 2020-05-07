# Test

__Important note: Enable WiFi for better results__

__Note about setting up the app: When executing this app you'll retrieve gps data with a lot of updates so battery may drain really fast. Enable maximum performance on your Android phone for better results__

Set up a server with this code (enter the IP on the app) and you'll be able to receive GPS data:

```
import socket

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
