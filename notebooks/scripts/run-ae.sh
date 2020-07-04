cd ../jMetal/jmetal-fse/
mvn exec:java -Dexec.mainClass="org.uma.jmetal.runner.multiobjective.NSGAIIDRASv1Runner" -Dexec.args="$1 $2 $3"
