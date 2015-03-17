cmake_minimum_required(VERSION 2.8)
project(Ciolczyk_Michal_1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES zad1_client.c)
add_executable(Ciolczyk_Michal_1 ${SOURCE_FILES} zad1_client.c)