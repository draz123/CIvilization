package main

import (
	"fmt"
	"net"
	"log"
)

func main() {
    fmt.Println("start client")
    conn, err := net.Dial("tcp", "localhost:8888") //pamietaj o zmienie portow
    if err != nil {
        log.Fatal("Connection error", err)
    }
    conn.Write([]byte("hello world"))
    conn.Close()
    fmt.Println("done")
}



