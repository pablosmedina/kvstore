name := "kvstore"

organization := "io.ckite"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
	"io.ckite" % "ckite" % "0.1.4",
	"com.twitter" %% "finagle-http" % "6.6.2",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3",
    "ch.qos.logback" % "logback-classic" % "1.1.1"
)

EclipseKeys.withSource := true

unmanagedSourceDirectories in Compile <++= baseDirectory { base =>
  Seq(
    base / "src/main/resources"
  )
}
