pipeline {
	agent {
		label "docker"	
	}
	
	stages {
	    stage ('Java Build') {
			agent {
        		label "docker"
     		}
		    
		    steps {
			    sh "printenv | sort"
				configFileProvider([configFile(fileId: 'f1a589f6-439e-4614-9bda-6ebe4387a535', variable: 'MAVEN_SETTINGS')]) {
          			container('docker') {
                    	withCredentials([usernamePassword(credentialsId: 'nexus-functional-es-user', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
				  			sh """
		     	     			mvn clean package -B -Dmaven.test.skip=true
				                java -cp ${WORKSPACE}/testng.jar -jar ${WORKSPACE}/target/APITesting-jar-with-dependencies.jar ${WORKSPACE}/src/main/resources/Sanity_prod/runner_prod.xml
				    		"""   
						}
					}
				}
			}
		}
	}
}
