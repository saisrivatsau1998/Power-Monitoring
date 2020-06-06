# Power-Monitoring
Implementation Steps
1. User has to follow the circuit diagram as mentioned in the Figure 4.3 for
establishing communication between external devices and user application.
2. Develop an C program to communicate with External electrical devices us-
ing Arduino IDE.
3. Develop an application that can interact with Arduino using android studio.
4. Connect to backend MongoDB Atlas database for storing of each informa-
tion.

Steps to connect App to MongoDB atlas.
1. Create an Atlas Account.
(a) Click Sign up with Google.
(b) Enter one of the following identifiers for your Google account:
i. Your Gmail or Google Apps email address:
ii. The phone number associated with your Google account
(c) Click Next.
(d) Enter the password for your Google account.
(e) Click Next.
(f) Review and select the checkbox to accept the Terms of Service and
the Privacy Policy.
(g) Click Submit.

2. Log into Atlas.
(a) Click on Build a Cluster.
(b) Select starter cluster and click Create a Cluster.
(c) Select your preferred Cloud Provider and Region
(d) Select M0 sandbox for cluster tier.
(e) Enter a name for your cluster in the Cluster name field.
(f) Click create cluster to deploy the cluster.

3. White-list Your Connection IP Address.
(a) Open the connect dialog.
(b) Configure your white-list entry.
(c) Click Add IP Address.

4. Add Database user to your cluster.
(a) Open the connect Dialog.
(b) In the create a MongoDB User step of the dialog, enter Username and
a Password for your database user.
(c) Click Create MongoDB User.

5. Connect to your Cluster.

6. Add a Stitch App.
(a) In Atlas, click Stitch Apps in the left-hand navigation.
(b) Click the Create New Application.
(c) In the Create a new application dialog, enter an Application Name for
your Stitch app.
(d) Select a cluster in your project in your project from the Link to Cluster
dropdown. Stitch will automatically create a MongoDB service that is
linked to this cluster.
(e) Enter a name for the service that Stitch will create in the Stitch Service
Name field.
(f) Select a deployment model and deployment region for your applica-
tion.
(g) Click Create.
