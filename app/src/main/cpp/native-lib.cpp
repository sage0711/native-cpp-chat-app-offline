#include <jni.h>
#include <iostream>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <cstring>

// this is the cpp echo server socket

extern
"C"
JNIEXPORT
jstring
JNICALL
Java_com_example_native_1cpp_1chat_1app_MainActivity_echoServerStart
(
        JNIEnv *env,
        jobject
        ) {
        //creates server socket
        int server_socket = socket(AF_INET, SOCK_STREAM, 0);
        if (server_socket == -1) {
            std::cerr << "Failed to create socket" << std::endl;
            return env->NewStringUTF("Socket Create Failure");
        }

        // binds socket to address and port
        struct sockaddr_in server_address;
        server_address.sin_family = AF_INET;
        server_address.sin_addr.s_addr = INADDR_ANY;
        server_address.sin_port = htons(8080);  //to port 8080
        if(bind(server_socket, (struct sockaddr * ) &server_address, sizeof(server_address)) == -1) {
            std::cerr << "Failed to bind socket" << std::endl;
            close(server_socket);
            return env->NewStringUTF("Socket Bind Failure");
        }

        // listens for incoming connections
        if (listen(server_socket, 3) == -1) {
            std::cerr << "Failed to listen for incoming connections." << std::endl;
            close(server_socket);
            return env->NewStringUTF("Socket Listen Failure");
        }

        struct sockaddr_in client_address;
        socklen_t addrlen = sizeof(client_address);
        while(true) {
            //accepts incoming connections
            int client_socket = accept(server_socket, (struct sockaddr *) &client_address,
                                       &addrlen);
            if (client_socket == -1) {
                std::cerr << "Failed to accept incoming connection." << std::endl;
                close(server_socket);
                return env->NewStringUTF("Client socket accept failure");
            }

            // echos client's messages
            char buffer[1024] = {0};
            int bytesRead ;
            while(0 < (bytesRead = recv(client_socket, buffer, 1024, 0))) {
                send(client_socket, buffer, bytesRead, 0);
            }
        }

        std::string hello_str = "Hello everyone";
        return env->NewStringUTF(hello_str.c_str());
}