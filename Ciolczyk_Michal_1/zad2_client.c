#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

static const int BUFFER_SIZE = 255;

void usage();


int main(int argc, char **argv) {
    printf("Zad 2 Client\n(C) 2015 Michał Ciołczyk\n\n");

    if (argc != 3) {
        usage();
        return 1;
    }

    //Socket variables
    struct sockaddr_in sockaddrIn;
    int sck;

    //Program variables
    char *buffer = malloc(BUFFER_SIZE * sizeof(char));
    int got = 0, fd;

    //Initializing socket & connecting
    sck = socket(AF_INET, SOCK_STREAM, 0);

    bzero((char *) &sockaddrIn, sizeof(sockaddrIn));

    sockaddrIn.sin_family = AF_INET;
    sockaddrIn.sin_port = htons((uint16_t) atoi("6666"));
    sockaddrIn.sin_addr.s_addr = inet_addr(argv[1]);

    if (0 != connect(sck, (struct sockaddr *) &sockaddrIn, sizeof(struct sockaddr_in))) {
        perror("Cannot connect to address");
        return 2;
    }

    //Sending filename
    send(sck, argv[2], strlen(argv[2]) * sizeof(char), 0);

    fd = open(argv[2], O_WRONLY | O_CREAT, S_IRUSR | S_IWUSR | S_IROTH | S_IRGRP);

    //Receiving file
    do {
        bzero(buffer, BUFFER_SIZE * sizeof(char));
        got = (int) recv(sck, buffer, BUFFER_SIZE * sizeof(char), 0);
        printf("Got %d bytes\n", got);
        write(fd, buffer, got * sizeof(char));
    }
    while (got != 0);

    printf("File written\n");

    free(buffer);
    close(sck);
    close(fd);

    return 0;
}

void usage() {
    printf("Usage:\n");
    printf("./zad2_client <SERVER ADDRESS> <FILE NAME>\n");
}