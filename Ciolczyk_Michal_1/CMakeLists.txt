cmake_minimum_required(VERSION 2.8)
project(Ciolczyk_Michal_1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES_zad1 zad1_client.c)
add_executable(zad1_client ${SOURCE_FILES_zad1})

set(SOURCE_FILES_zad2 zad2_client.c)
add_executable(zad2_client ${SOURCE_FILES_zad2})