#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>

void usage();

void menu();

void receive_num();

//Socket variables
int sck;

int main(int argc, char **argv) {
    printf("Zad 1 Client\n(C) 2015 Michał Ciołczyk\n\n");

    if(argc != 2) {
        usage();
        return 1;
    }

    //Socket variables
    struct sockaddr_in serv_addr;

    //Program variables
    bool end = false;
    long long num;
    char *buffer = malloc(255 * sizeof(char));

    //Initializing socket & connecting
    sck = socket(AF_INET, SOCK_STREAM, 0);

    bzero((char*) &serv_addr, sizeof(serv_addr));

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons((uint16_t) atoi("6666"));
    serv_addr.sin_addr.s_addr = inet_addr(argv[1]);

    if (0 != connect(sck, (struct sockaddr *) &serv_addr, sizeof(struct sockaddr_in))){
        perror("Cannot connect to address");
        return 2;
    }

    //Main loop
    while(!end){
        menu();

        scanf("%s", buffer);

        if(0 == strcmp(buffer, "q")) {
            end = true;
        }
        else if(0 == strcmp(buffer, "1")) {
            scanf("%lld", &num);
            uint8_t toSend = (uint8_t) num;

            if(send(sck, &toSend, sizeof(uint8_t), 0) != sizeof(uint8_t)) {
                perror("Send failed");
                close(sck);
                return 3;
            }

            receive_num();
        }
        else if(0 == strcmp(buffer, "2")) {
            scanf("%lld", &num);
            uint16_t toSend = (uint16_t) num;

            if(send(sck, &toSend, sizeof(uint16_t), 0) != sizeof(uint16_t)) {
                perror("Send failed");
                close(sck);
                return 3;
            }

            receive_num();
        }
        else if(0 == strcmp(buffer, "4")) {
            scanf("%lld", &num);
            uint32_t toSend = (uint32_t) num;

            if(send(sck, &toSend, sizeof(uint32_t), 0) != sizeof(uint32_t)) {
                perror("Send failed");
                close(sck);
                return 3;
            }

            receive_num();
        }
        else if(0 == strcmp(buffer, "8")) {
            scanf("%lld", &num);
            uint64_t toSend = (uint64_t) num;

            if(send(sck, &toSend, sizeof(uint64_t), 0) != sizeof(uint64_t)) {
                perror("Send failed");
                close(sck);
                return 3;
            }

            receive_num();
        }
    }

    free(buffer);
    close(sck);

    return 0;
}

void receive_num() {
    uint8_t buffer;

    if(recv(sck, &buffer, sizeof(uint8_t), 0) != sizeof(uint8_t)) {
        perror("Recv failed");
        close(sck);
        exit(4);
    }

    printf("Got number: %d", (int) buffer);
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