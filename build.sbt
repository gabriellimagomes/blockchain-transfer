ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "blockchain-transfer",
    idePackagePrefix := Some("com.gabriellimagomes.blockchaintransfer")
  )

resolvers += "Akka library repository" at "https://repo.akka.io/maven"

val akkaVersion = "2.9.3"
val akkaHttpVersion = "10.6.3"
val bouncyCastleVersion = "1.78.1"
val scalaTestVersion = "3.2.19"
val scalaMockVersion = "6.0.0"

libraryDependencies += "org.bouncycastle" % "bcprov-jdk18on" % bouncyCastleVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test
libraryDependencies += "org.scalamock" %% "scalamock" % scalaMockVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test