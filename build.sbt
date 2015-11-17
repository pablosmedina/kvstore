name := "kvstore"

organization := "io.ckite"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

val ckiteVersion = "0.2.1"

//resolvers ++= Seq("sonatype" at " https://oss.sonatype.org/content/repositories/public")

libraryDependencies ++= Seq(
"io.ckite"						          % 	"ckite-core" 			        % ckiteVersion,
"io.ckite"						          % 	"ckite-finagle" 		      % ckiteVersion,
"io.ckite"						          % 	"ckite-mapdb" 		    	  % ckiteVersion,
"com.twitter"					          %% 	"finagle-http" 			      % "6.27.0",
"com.fasterxml.jackson.module"	%% 	"jackson-module-scala" 		% "2.4.4",
"ch.qos.logback"				        % 	"logback-classic" 		  	% "1.1.1"
)

fork in run := false

unmanagedSourceDirectories in Compile <++= baseDirectory { base =>
  Seq(
    base / "src/main/resources"
  )
}
