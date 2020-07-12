mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/small-tol1 2000 4 0 HPF 300 0.025"
mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/medium-tol1 10000 4 0 HPF 300 0.025"
mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/large-tol1 30000 4 0 HPF 300 0.025"
mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/medium-tol0.4 10000 4 0 HPF 300 0.025"
#mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/small-tol1 2000 2 0 PL 250 0.025"
#mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/small-tol1 2000 2 0 PD 250 0.025"
#mvn clean install exec:java -Dexec.mainClass="uy.udelar.fing.dras.DatacenterControllerDiscrete" -Dexec.args="instances/small-tol1 2000 2 0 DL 250 0.025"






