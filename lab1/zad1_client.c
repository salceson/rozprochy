#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

void usage();

void menu();

void receive_num();

//Socket variables
int sck;

int main(int argc, char **argv) {
    printf("Zad 1 Client\n(C) 2015 Michał Ciołczyk\n\n");

    if (argc != 2) {
        usage();
        return 1;
    }

    //Socket variables
    struct sockaddr_in sockaddrIn;

    //Program variables
    unsigned long long num;
    char *buffer = malloc(255 * sizeof(char));

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

    //Main loop

    menu();

    scanf("%s", buffer);

    if (0 == strcmp(buffer, "1")) {
        printf("Enter number to send: ");
        scanf("%llu", &num);
        int8_t toSend = (int8_t) num;
        send(sck, &toSend, sizeof(int8_t), 0);
        receive_num();
    }
    else if (0 == strcmp(buffer, "2")) {
        printf("Enter number to send: ");
        scanf("%llu", &num);
        int16_t toSend = htobe16((int16_t) num);
        send(sck, &toSend, sizeof(int16_t), 0);
        receive_num();
    }
    else if (0 == strcmp(buffer, "4")) {
        printf("Enter number to send: ");
        scanf("%llu", &num);
        int32_t toSend = htobe32((int32_t) num);
        send(sck, &toSend, sizeof(int32_t), 0);
        receive_num();
    }
    else if (0 == strcmp(buffer, "8")) {
        printf("Enter number to send: ");
        scanf("%llu", &num);
        int64_t toSend = htobe64((int64_t) num);
        send(sck, &toSend, sizeof(int64_t), 0);
        receive_num();
    }

    free(buffer);
    close(sck);

    return 0;
}

void receive_num() {
    uint8_t buffer;
    recv(sck, &buffer, sizeof(uint8_t), 0);
    printf("Got number: %d\n\n", (int) buffer);
}

void menu() {
    printf("\nMenu: \n");
    printf("1: send 1-byte number\n");
    printf("2: send 2-byte number\n");
    printf("4: send 4-byte number\n");
    printf("8: send 8-byte number\n");
    printf("q: quit the program\n");
    printf("\n");
    printf("Your choice: ");
}

void usage() {
    printf("Usage:\n");
    printf("./zad1_client <SERVER ADDRESS>\n");
}