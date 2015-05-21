cmake_minimum_required(VERSION 3.2)
project(client)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(client ${SOURCE_FILES})
add_custom_target(
    sliceCompile
    COMMAND ${CMAKE_CURRENT_SOURCE_DIR}/slice/compileIce.sh
    WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/slice
)
add_dependencies(client sliceCompile)