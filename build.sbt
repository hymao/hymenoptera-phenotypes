import com.typesafe.sbt.SbtNativePackager._
import NativePackagerKeys._

organization  := "org.hymao"

name          := "hymenoptera-phenotypes"

version       := "0.2"

packageArchetype.java_application

mainClass in Compile := Some("org.hymao.CreatePhenotypeTable")

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Phenoscape Maven repository" at "http://phenoscape.svn.sourceforge.net/svnroot/phenoscape/trunk/maven/repository"

resolvers += "Bigdata releases" at "http://www.systap.com/maven/releases"

resolvers += "NXParser repository" at "http://nxparser.googlecode.com/svn/repository"

resolvers += "BBOP repository" at "http://code.berkeleybop.org/maven/repository"

javaOptions += "-Xmx12G"

libraryDependencies ++= {
  Seq(
    "net.sourceforge.owlapi" %   "owlapi-distribution"    % "3.5.0",
	"com.hermit-reasoner"    %   "org.semanticweb.hermit" % "1.3.8.4",
	"org.phenoscape"         %   "scowl"                  % "0.9",
	"org.phenoscape"         %   "owlet"                  % "1.3",
	"org.phenoscape"         %   "kb-owl-tools"           % "1.1"
  )
}
