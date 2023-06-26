#include <jni.h>
#include <string>
#include <iostream>
#include <cstdlib>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <type_traits>
#include <unistd.h>

#define PORT_NUMBER 8080
#define BUFFER_SIZE 1024

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_native_1cpp_1chat_1app_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

// Checks if the parameter (c-style string) is an integer
bool is_int(char *c) {
    while( *c != '\0' ) {
        if (*c < '0' || *c > '9') {
            return false;
        }
        c ++;
    }

    return true;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_native_1cpp_1chat_1app_MainActivity_createSocket(
        JNIEnv* env,
        jobject) {

    return (jstring) "Socket has been created successfully";
}


