name := "kvstore"

organization := "io.ckite"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
	"io.ckite" % "ckite" % "0.1.4-LOCAL",
    "ch.qos.logback" % "logback-classic" % "1.1.1"
)

EclipseKeys.withSource := true

unmanagedSourceDirectories in Compile <++= baseDirectory { base =>
  Seq(
    base / "src/main/resources"
  )
}
