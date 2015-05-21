#include <iostream>
#include <Ice/Ice.h>
#include "Bank.h"

using namespace std;

int main(int argc, char **argv) {
    Ice::CommunicatorPtr communicator = Ice::initialize(argc, argv);
    cout << "Hello, World!" << endl;
    return 0;
}