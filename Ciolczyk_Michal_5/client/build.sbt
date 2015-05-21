name := "client-scala"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "ZeroC" at "https://repo.zeroc.com/nexus/content/repositories/releases"

libraryDependencies ++= Seq(
  "com.zeroc" % "ice" % "3.5.0",
  "com.zeroc" % "icestorm" % "3.5.0"
)
