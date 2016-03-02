# Deploy an EMR cluster

The current script will help you to deploy an EMR cluster. To raise clusters, you'll need to install AWS CLI on your computer and set your credentials on ~/.aws/credentials as explained in:
http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html
http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-welcome.html

## Arguments
* c (cluster): the configuration of the cluster you want to deploy, remember the configuration files can be located under src/resources folder
* p (properties-file): all the properties to launch the cluster, such as cluster's name, number of machines, etc...
* t (steps): configuration of the steps to run when bootstrapping the cluster, they can be comma separated values to add multiple steps when bootstrapping the cluster 
* f (files): files to upload to s3, the format should be: s3://bucket/folder:/local/path/to/folder,s3://other_bucket/:/local/path/to/file
* s (salt): salt to be used by the internal decrypter to decrypt the encrypted passwords set in the configuration & step files

#### How to call the script 
Using short arguments
````
java -jar deploy.jar -a xxxxxx -s xxxx -c configuration.json -p file.properties -t step1.json,step2.json -f /local/path/to/folder:s3://bucket1/folder,/local/path/to/file:s3://bucket2/other/folder
````

Using long arguments
````
java -jar deploy.jar --access xxxxxx --secret xxxx --cluster configuration.json --properties-file file.properties --steps step1.json,step2.json --files /local/path/to/folder:s3://bucket1/folder,/local/path/to/file:s3://bucket2/other/folder
````
