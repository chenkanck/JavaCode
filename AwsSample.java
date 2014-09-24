/*
 * Copyright 2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * Modified by Sambit Sahu
 * Modified by Kyung-Hwa Kim (kk2515@columbia.edu)
 * 
 * 
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;


public class AwsSample {

    /*
     * Important: Be sure to fill in your AWS access credentials in the
     *            AwsCredentials.properties file before you try to run this
     *            sample.
     * http://aws.amazon.com/security-credentials
     */

    static AmazonEC2      ec2;
    public static final String KEYNAME = "JavaEC2Key2";
    public static final String SG_NAME = "JavaSecurityGroup2";
    public static final String INS_NAME = "kc2386_FirstInstance";
    
    public static void main(String[] args) throws Exception {


    	 AWSCredentials credentials = new PropertiesCredentials(
    			 AwsSample.class.getResourceAsStream("AwsCredentials.properties"));

         /*********************************************
          * 
          *  #1 Create Amazon Client object
          *  
          *********************************************/
    	 System.out.println("#1 Create Amazon Client object");
         ec2 = new AmazonEC2Client(credentials);

         
       
        try {
        	
        	/*********************************************
        	 * 
             *  #2 Describe Availability Zones.
             *  
             *********************************************/
        	System.out.println("#2 Describe Availability Zones.");
            DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
            System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
                    " Availability Zones.");

            /*********************************************
             * 
             *  #3 Describe Available Images
             *  
             *********************************************/
//            System.out.println("#3 Describe Available Images");
//            DescribeImagesResult dir = ec2.describeImages();
//            List<Image> images = dir.getImages();
//            System.out.println("You have " + images.size() + " Amazon images");
            
            
            /*********************************************
             *                 
             *  #4 Describe Key Pair
             *                 
             *********************************************/
            System.out.println("#9 Describe Key Pair");
            DescribeKeyPairsResult dkr = ec2.describeKeyPairs();
            System.out.println(dkr.toString());
            
            /*********************************************
             * 
             *  #5 Describe Current Instances
             *  
             *********************************************/
            System.out.println("#4 Describe Current Instances");
            DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            Set<Instance> instances = new HashSet<Instance>();
            // add all instances to a Set.
            for (Reservation reservation : reservations) {
            	instances.addAll(reservation.getInstances());
            }
            
            System.out.println("You have " + instances.size() + " Amazon EC2 instance(s).");
            for (Instance ins : instances){
            	
            	// instance id
            	String instanceId = ins.getInstanceId();
            	
            	// instance state
            	InstanceState is = ins.getState();
            	System.out.println(instanceId+" "+is.getName());
            }
            
            /**
             *	Create a Security Group 
             */
            
            System.out.println("#5 kc Create Security Group");
            CreateSecurityGroupRequest createSecurityGroupRequest = new CreateSecurityGroupRequest();
            createSecurityGroupRequest.withGroupName(SG_NAME).withDescription("My Java Security Group");
            
            CreateSecurityGroupResult createSecurityGroupResult = ec2.createSecurityGroup(createSecurityGroupRequest);
            System.out.println("Security Group Created:"+createSecurityGroupResult.getGroupId());
            
            /**
             * Add Rules for Security Group
             */
            
            System.out.println("#5 kc Add Security Rules");
            //Add SSH Rule to SG
            IpPermission sshPermission = new IpPermission();
            sshPermission.withIpRanges("0.0.0.0/0")
            			.withIpProtocol("tcp")
            			.withFromPort(22)
            			.withToPort(22);
            
            AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest = new AuthorizeSecurityGroupIngressRequest();
            authorizeSecurityGroupIngressRequest.withGroupName(SG_NAME)
            									.withIpPermissions(sshPermission);
            
            ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
            //Add Http Rule to SG
            IpPermission httpPermission = new IpPermission();
            httpPermission.withIpRanges("0.0.0.0/0")
            			.withIpProtocol("tcp")
            			.withFromPort(80)
            			.withToPort(80);
            
            AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest2 = new AuthorizeSecurityGroupIngressRequest();
            authorizeSecurityGroupIngressRequest2.withGroupName(SG_NAME)
            									.withIpPermissions(httpPermission);
            
            ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest2);
            //Add Tcp Rule to SG
            IpPermission tcpPermission = new IpPermission();
            tcpPermission.withIpRanges("0.0.0.0/0")
            			.withIpProtocol("tcp")
            			.withFromPort(8080)
            			.withToPort(8080);
            
            AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest3 = new AuthorizeSecurityGroupIngressRequest();
            authorizeSecurityGroupIngressRequest3.withGroupName(SG_NAME)
            									.withIpPermissions(tcpPermission);
            
            ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest3);
            /**
             * Create Key Pair
             */
           
            System.out.println("#5 kc Create Key Pair");
            CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
            createKeyPairRequest.withKeyName(KEYNAME);
            
            CreateKeyPairResult createKeyPairResult = ec2.createKeyPair(createKeyPairRequest);
            
            KeyPair keyPair = new KeyPair();
            keyPair = createKeyPairResult.getKeyPair();
            
            String privateKey = keyPair.getKeyMaterial();

            File keyFile = new File("/Users/Modoka/Desktop/"+KEYNAME+".pem");
            try {
            	keyFile.createNewFile();
            }
            catch (Exception e) {
            	e.printStackTrace();
            }
            
            BufferedWriter output = new BufferedWriter(new FileWriter(keyFile));
            output.write(privateKey);
            output.close();
            System.out.println("PrivateKeyPair is downloaded");
            /*********************************************
             * 
             *  #6 Create an Instance
             *  
             *********************************************/
            System.out.println("#5 Create an Instance");
//            String imageId = "ami-76f0061f"; //Basic 32-bit Amazon Linux AMI
            String imageId = "ami-76817c1e";
            int minInstanceCount = 1; // create 1 instance
            int maxInstanceCount = 1;
//            RunInstancesRequest rir = new RunInstancesRequest(imageId, minInstanceCount, maxInstanceCount);
            RunInstancesRequest rir = new RunInstancesRequest();
            rir.withImageId(imageId)
               .withInstanceType("t2.micro")
               .withMinCount(minInstanceCount)
               .withMaxCount(maxInstanceCount)
               .withKeyName(KEYNAME)
               .withSecurityGroups(SG_NAME);
            RunInstancesResult result = ec2.runInstances(rir);
            
            //get instanceId from the result
            List<Instance> resultInstance = result.getReservation().getInstances();
            String createdInstanceId = null;
           
            for (Instance ins : resultInstance){
            	createdInstanceId = ins.getInstanceId();
            	System.out.println("New instance has been created: "+ins.getInstanceId());
            }	
            /*
             * Find the Information of 
             */
		    
            System.out.println("#6 Describe New Instances State");
            Instance targetInstance = null;
            while (true) {
	            Thread.sleep(3000);
	            targetInstance = getInstance(createdInstanceId);
	            String targetState = targetInstance.getState().getName();
	            System.out.println("## New instance is:"+ targetState);
	            if (targetState.equals("running"))
	            	break;
//            	boolean found = false;
//            	DescribeInstancesResult describeInstancesRequest1 = ec2.describeInstances();
//	            List<Reservation> reservations1 = describeInstancesRequest1.getReservations();
//	            Set<Instance> instances1 = new HashSet<Instance>();
//	            // add all instances to a Set.
//	            for (Reservation reservation : reservations1) {
//	            	instances1.addAll(reservation.getInstances());
//	            }
//	            String tmpinstanceId =null;
//	            System.out.println("You have " + instances1.size() + " Amazon EC2 instance(s).");
//	            for (Instance ins : instances1){
//	            	
//	            	// instance id
//	            	tmpinstanceId = ins.getInstanceId();
//	            	// instance state
//	            	InstanceState is = ins.getState();
//	            	System.out.println(tmpinstanceId+" "+is.getName());
//	            	if ((is.getName().equals("running"))&&(tmpinstanceId.equals(createdInstanceId)))
//	            		{
//	            			found = true;
//	            			targetInstance = ins;
//	            		}
//	            }
//	            if (found)
//	            	break;
            }
            
            
            System.out.println("Public DNS of Newinstance:"+ targetInstance.getPublicDnsName());
        	System.out.println("Public IP of Newinstance:"+ targetInstance.getPublicIpAddress());
        	System.out.println("Private IP of Newinstance:"+ targetInstance.getPrivateIpAddress());
            
            /*********************************************
             * 
             *  #7 Create a 'tag' for the new instance.
             *  
             *********************************************/
            System.out.println("#6 Create a 'tag' for the new instance.");
            List<String> resources = new LinkedList<String>();
            List<Tag> tags = new LinkedList<Tag>();
            Tag nameTag = new Tag("Name", "kc2386_FirstInstance");
            
            resources.add(createdInstanceId);
            tags.add(nameTag);
            
            CreateTagsRequest ctr = new CreateTagsRequest(resources, tags);
            ec2.createTags(ctr);
            
            /*
             * Programmatically SSH to instance 
             */
            System.out.println("#kc SSh");
                        
            /*********************************************
             * 
             *  #8 Stop/Start an Instance
             *  
             *********************************************/
            System.out.println("#7 Stop the Instance");
            List<String> instanceIds = new LinkedList<String>();
            instanceIds.add(createdInstanceId);
            
            //stop
            StopInstancesRequest stopIR = new StopInstancesRequest(instanceIds);
            //ec2.stopInstances(stopIR);
            
            //start
            StartInstancesRequest startIR = new StartInstancesRequest(instanceIds);
            //ec2.startInstances(startIR);
            
            
            /*********************************************
             * 
             *  #9 Terminate an Instance
             *  
             *********************************************/
            System.out.println("#8 Terminate the Instance");
            TerminateInstancesRequest tir = new TerminateInstancesRequest(instanceIds);
            //ec2.terminateInstances(tir);
            
                        
            /*********************************************
             *  
             *  #10 shutdown client object
             *  
             *********************************************/
            ec2.shutdown();
            
            
            
        } catch (AmazonServiceException ase) {
                System.out.println("Caught Exception: " + ase.getMessage());
                System.out.println("Reponse Status Code: " + ase.getStatusCode());
                System.out.println("Error Code: " + ase.getErrorCode());
                System.out.println("Request ID: " + ase.getRequestId());
        }

        
    }
    
    public static Instance getInstance (String instanceId){
    	 DescribeInstancesRequest describeInstanceRequest = new DescribeInstancesRequest().withInstanceIds(instanceId); 
    	 DescribeInstancesResult describeInstanceResult = ec2.describeInstances(describeInstanceRequest);
//    	 InstanceState state = describeInstanceResult.getReservations().get(0).getInstances().get(0).getState();
    	 Instance ins = describeInstanceResult.getReservations().get(0).getInstances().get(0);
    	 return	ins;
    }
   
}
