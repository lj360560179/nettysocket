package main


import (
	"net"
	"log"
	"fmt"
	"os"
	"encoding/json"
)

func main() {
	Start("127.0.0.1:9090")
}

func Start(tcpAddrStr string) {
	tcpAddr, err := net.ResolveTCPAddr("tcp4", tcpAddrStr)
	if err != nil {
		log.Printf("Resolve tcp addr failed: %v\n", err)
		return
	}

	// 向服务器拨号
	conn, err := net.DialTCP("tcp", nil, tcpAddr)
	if err != nil {
		log.Printf("Dial to server failed: %v\n", err)
		return
	}

	// 向服务器发消息
	go SendMsg(conn)
	RzMsg(conn)

	// 接收来自服务器端的广播消息
	buf := make([]byte, 1024)
	for {
		length, err := conn.Read(buf)
		if err != nil {
			log.Printf("recv server msg failed: %v\n", err)
			conn.Close()
			os.Exit(0)
			break
		}

		fmt.Println(string(buf[0:length]))
	}
}

type Message struct {
	Uid, ReceiveId int
	MsgType,Msg string
}

//认证消息
func RzMsg(conn net.Conn){
	m:= Message{Uid:1002, ReceiveId:1001, MsgType:"认证消息", Msg:"",}
	data, _ := json.Marshal(m)
	_, err := conn.Write(data)
	if err != nil {
		conn.Close()
	}
}

// 向服务器端发消息
func SendMsg(conn net.Conn) {
	for {
		var input string
		// 接收输入消息，放到input变量中
		fmt.Scanln(&input)

		if input == "/q" || input == "/quit" {
			fmt.Println("Byebye ...")
			conn.Close()
			os.Exit(0)
		}
		// 只处理有内容的消息
		if len(input) > 0 {
			m:= Message{Uid:1002, ReceiveId:1001, MsgType:"文本消息", Msg:input,}
			data, _ := json.Marshal(m)
			_, err := conn.Write(data)
			if err != nil {
				conn.Close()
				break
			}
		}
	}
}