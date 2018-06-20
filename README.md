# nettysocket
nettysocket
- 建立tcp连接
- 发送认证信息
- 根据uid保存连接
- json传输

# nodeclient
```js
var net = require('net')
const HOST = '127.0.0.1'
const PORT = 9090
const msg = {
    id: 1004,
    receiveId: 1001,
    msgType: '认证消息',
    msg: ''
}
var client = new net.Socket()
client.connect(PORT, HOST, function () {
    client.write(JSON.stringify(msg))
})

client.on('data', function (data) {
    console.log('DATA: ' + data)
})

client.on('close', function () {
    console.log('Connection closed')
})
```
