# Vault Kanban Board Demo
A [Web Tab](https://vaulthelp2.vod309.com/wordpress/?p=23516#defining_web_tabs) allows a user to view a web service that is
hosted externally from Vault. This page describes an example
use case of an integration between Vault and an external
system.

## Introduction
The Vault Kanban Board project provides sample code and Vault
Configuration. Additionally, this project gives step-by-step 
instructions on how to set up the following Amazon Web Services (AWS)
components and Vault configuration:

- Amazon Web Services
  - Amazon Cloudfront
  - Amazon S3 Bucket
  - Amazon API Gateway
  - Amazon Lambda

- Vault
  - Web Tab

## Business Use Case
### Access Request
A system administrator needs an easy way to track proposed
access for new users in Vault. This includes prioritization, different
types of security profiles, and user types. The configured web tabs allows
the use of a Kanban board to enable drag and drop functionality to update
Access Request records, instead of editing and saving the actual record in Vault.

## Setup
### Prerequisites
1. An AWS account. If you don't have one sign up for an account at [AWS Free Tier](https://aws.amazon.com/free/free-tier/).
2. Download the [executable jar file](https://github.com/veeva/Vault-Kanban-Board/blob/main/vault-kanban-board-1.0.jar).
3. Import [Vault Package](https://github.com/veeva/Vault-Kanban-Board/blob/main/KANBAN-BOARD-CONFIG.vpk) to create necessary components.
4. Install [Nodejs](https://nodejs.org/en/) and [Yarn package manager](https://yarnpkg.com/getting-started/install). Depending on the installation, the following command may need to be executed:
>yarn add env-cmd
5. [Download](https://maven.apache.org/download.cgi) and [Install](https://maven.apache.org/install.html) Maven.

The project contains a [Vault Packages (VPK)](https://vaulthelp2.vod309.com/wordpress/admin-user-help/admin-vault-loader/using-configuration-migration-packages/#how_to_import_validate_packages) in the "deploy-vpk" directory with the 
necessary configuration and Vault Java SDK code.
### Creating Backend Package
**Note**: An executable jar file is present in the project, so this step is only needed if additional changes were made within the code
and compilation and packaging is necessary.

1. Open the terminal and navigate to /vault-kanban-board/backend/ folder.
2. Execute the following for either sandbox or production: **mvn clean compile package**.
3. Once the packaging has completed, take note of the vault-kanban-board-0.2.jar file that was generated and that is located
in the /vault-kanban-board/backend/target/ folder.
### Create AWS Services
#### Create AWS S3 Bucket
1. In the [Amazon S3 console](https://console.aws.amazon.com/s3/), click **Create bucket**.
2. Configure the S3 bucket:
   - For Bucket name, enter **vsdk-kanban-board-aws-s3-bucket**.
   - For **Region**, choose your Region.
   - For **Default encryption**, enable **Server-side encryption** and select **Amazon S3 key (SSE-S3)** for 
   **Encryption key type**
3. Leave the other configuration settings to their default values and click
   **Create Bucket**. 
4. Record the bucket name for future reference.
#### Create AWS CloudFront Distribution
1. In the [Amazon CloudFront console](https://console.aws.amazon.com/cloudfront/), click **Create distribution**.
2. Configure the CloudFront distribution:
   - For **Origin domain**, enter the name of the previously created S3 bucket name.
   - For **Name**, enter **vsdk-kanban-board-aws-distribution**.
   - For **S3 bucket access**, select **Yes use OAI (bucket can restrict access to only CloudFront)** and configure the OAI:
     - For **Origin access identity**, click **Create new OAI**. For the name, enter **vsdk-kanban-board-aws-oai**.
     - For **Bucket policy**, select **Yes, update the bucket policy**.
   - In the **Default cache behavior** section and **Viewer** subsection, for **Viewer protocol policy**, click **Redirect HTTP to HTTPS**.
3. Leave the other configuration settings to their default values and click
   **Create distribution**. 
4. Once the distribution has been created and is active, click the **Invalidations** tab.
5. Click **Create invalidation**.
6. In the **Add object paths**, add "/*".
7. Click **Create invalidation**.
8. Record the **Distribution domain name** in the **General** tab for future use.
#### Create AWS Lambda Function and AWS API Gateway
1. In the [Amazon Lambda console](https://console.aws.amazon.com/lambda/), click **Create Function**.
2. Configure the Lambda function:
    - Ensure **Author from scratch** is selected.
    - For **Function name**, enter **vsdk-kanban-board-aws-lambda**.
    - For **Runtime**, select **Java 8 on Amazon Linux 2**.
3. Leave the other configuration settings to their default values and click
   **Create function**. 
4. Once the function has been successfully created, click on **+Add trigger** in the **Function Overview** section.
5. Configure the trigger:
    - For **Trigger configuration**, select **API Gateway**.
    - For **API**, select **Create an API**.
    - For **API type**, select **HTTP API**.
    - For **Security**, select **Open**.
    - Enable **Cross-origin resource sharing(CORS)**.
6. Leave the other configuration settings to their default values and click
   **Add**. 
7. In the **Code** tab, next to the **Code source** section label, for **Upload from** select **.zip or .jar file**.
8. Click **Upload** and select the vault-kanban-board-0.2.jar file provided or one that was previously packaged.
9. Click **Save**.
10. In the **Configuration** tab, navigate to the **Environment variables** section and click **Edit**. 
11. Configure the environment variable:
     - Click **Add environment variable** with the **Key** as **LOG_LEVEL** and **Value** as **DEBUG**
12. Leave all other environment variable settings to their default values and clikc **Save**.
### Creating Frontend Images
1. Replace the **{INSERT_API_GATEWAY_DNS}** with the AWS API Gateway DNS in the **env** files
2. Open terminal and navigate to /vault-kanban-board/frontend/
3. To build the environment execute the following command:
    1. For sandbox deployments use: **yarn build:sbx**
    2. For production deployments use: **yarn build**
4. Once the build execution has completed, there will be a new folder created name vault-kanban-board/frontend/build.
5. Navigate to the previously created AWS S3 Bucket.
6. Click the **Upload** button in the **Objects** tab.
7. Click **Add files**, and select all the single files in the /build folder.
8. Click **Add folder** and select the **static** folder within the /build folder.
9. Click **Upload**

#### Vault Web Tab
1. Log in to your Vault environment and click on the gear icon to go to the Admin side of your Vault.
2. Navigate to **Configuration** > **Tabs** and expand the **Access Request Admin** tab section.
3. For **Dashboard: Prioritization**, **Dashboard: Security Profiles**, and **Dashboard: User Type**, click **Edit** and 
   replace the **{CloudFront URL}** with the CloudFront Distribution domain name that was previously recorded.

Additional reference documents can be found on our [Developer Portal](https://developer.veevavault.com/sdk/#vault-java-sdk-overview) and in our [Javadocs](https://repo.veevavault.com/javadoc/vault-sdk-api/21.3.0/docs/api/index.html).
   



