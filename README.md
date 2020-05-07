# Test

__Important note: Enable WiFi for better results__

__Note about setting up the app: When executing this app you'll retrieve gps data with a lot of updates so battery may drain really fast. Enable maximum performance on your Android phone for better results__

Set up a server with this code (enter the IP on the app) and you'll be able to receive GPS data:

```
import socket

def recvall(sock):
        BUFF_SIZE = 1 # 4 KiB
        data = b''
        while True:
                part = sock.recv(BUFF_SIZE)
                data += part
                recv_end = data.decode('utf-8').find('\n')
                if recv_end != -1:
                        break
        return data.decode('utf-8')[:-1]

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
