cmake_minimum_required(VERSION 2.8)
project(client)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp Bank.cpp)
add_executable(client ${SOURCE_FILES})
add_custom_target(
    sliceCompileIce
    COMMAND ${CMAKE_CURRENT_SOURCE_DIR}/slice/compileIce.sh
    WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/slice)
target_link_libraries(Ice)
target_link_libraries(IceUtil)
target_link_libraries(IceSSL)
include_directories(/usr/include/)
include_directories(.)
link_directories(/usr/lib/)
link_libraries(Ice)
link_libraries(IceUtil)
link_libraries(IceSSL)
add_dependencies(client sliceCompileIce)
