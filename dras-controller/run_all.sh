#export JAVA_HOME=/home/jmurana/jdk1.8.0_212
#export M2_HOME=/home/jmurana/apache-maven-3.5.4
#export PATH=${M2_HOME}/bin:${PATH}

mvn clean install

#sh run_scenario.sh small-tol0.4 2000 600 0.025
#sh run_scenario.sh small-tol0.8 2000 600 0.025
#sh run_scenario.sh small-tol1 2000 600 0.025
#sh run_scenario.sh medium-tol0.4 10000 300 0.025
#sh run_scenario.sh medium-tol0.8 10000 300 0.025
#sh run_scenario.sh medium-tol1 10000 300 0.025
#sh run_scenario.sh large-tol0.4 30000 300 0.025
#sh run_scenario.sh large-tol0.8 30000 300 0.025
#sh run_scenario.sh large-tol1 30000 300 0.025

#sh run_scenario.sh DRD-5-0-0-het-1 2000 300 0.025
#sh run_scenario.sh DRD-5-0-0-het-2 2000 300 0.025
#sh run_scenario.sh DRD-5-0-0-het-3 2000 300 0.025
#sh run_scenario.sh DRD-0-10-0-het-1 10000 300 0.025
#sh run_scenario.sh DRD-0-10-0-het-2 10000 300 0.025
#sh run_scenario.sh DRD-0-10-0-het-3 10000 300 0.025
#sh run_scenario.sh DRD-0-0-30-het-1 30000 300 0.025
#sh run_scenario.sh DRD-0-0-30-het-2 30000 300 0.0255
#sh run_scenario.sh DRD-0-0-30-het-3 30000 300 0.025

sh run_scenario.sh DRD-3-1-1-het-1 3000 600 0.025
#sh run_scenario.sh DRD-1-3-1-het-1 4000 100 300 0.025
#sh run_scenario.sh DRD-1-1-3-het-1 4500 100 300 0.025

#sh run_scenario.sh DRD-6-2-2-het-1 6000 300 0.025
#sh run_scenario.sh DRD-2-6-2-het-1 8000 300 0.025
#sh run_scenario.sh DRD-2-2-6-het-1 8500 300 0.025

#sh run_scenario.sh DRD-20-5-5-het-1 15000 300 0.025
#sh run_scenario.sh DRD-5-20-5-het-1 25000 300 0.025
#sh run_scenario.sh DRD-5-5-20-het-1 25500 300 0.025

#sh run_scenario.sh DRD-3-1-1-hom-1 3000 300 0.025
#sh run_scenario.sh DRD-1-3-1-hom-1 4000 300 0.025
#sh run_scenario.sh DRD-1-1-3-hom-1 4500 300 0.025

#sh run_scenario.sh DRD-6-2-2-hom-1 6000 300 0.025
#sh run_scenario.sh DRD-2-6-2-hom-1 8000 300 0.025
#sh run_scenario.sh DRD-2-2-6-hom-1 8500 300 0.025

#sh run_scenario.sh DRD-20-5-5-hom-1 15000 300 0.025
#sh run_scenario.sh DRD-5-20-5-hom-1 25000 300 0.025
#sh run_scenario.sh DRD-5-5-20-hom-1 25500 300 0.025


