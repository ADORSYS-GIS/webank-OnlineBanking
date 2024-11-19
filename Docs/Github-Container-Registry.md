# Pushing Docker Images to GitHub Container Registry (GHCR)

## Table of Contents
1. [Introduction](#1-introduction)
2. [Prerequisites](#2-prerequisites)
3. [Creating a Dockerfile](#3-creating-a-dockerfile)
4. [Building the Docker Image](#4-building-the-docker-image)
5. [Authenticating with GHCR](#5-authenticating-with-ghcr)
6. [Pushing the Image to GHCR](#6-pushing-the-image-to-ghcr)
7. [Making Image Public](#7-making-the-image-public)
8. [Setting Up GitHub Actions Workflow](#8-setting-up-github-actions-workflow)
9. [Troubleshooting](#9-troubleshooting-docker-and-ghcr-deployment)
10. [Conclusion](#10-conclusion)

## 1. Introduction
### What is a Github Container Registry (GHCR)
GitHub Container Registry stores container images within your organization or personal account, and allows you to associate an image with a repository. It currently supports both the Docker Image Manifest V2, Schema 2 and Open Container Initiative (OCI) specifications.
In GitHub, we can build and push our docker images to GHCR within a GitHub Actions workflow file and make those images available either privately or publicly, depending on your needs.

This guide provides step-by-step instructions on how to push Docker images to GitHub Container Registry (GHCR) for both personal and organization repositories.
## 2. Prerequisites

**Docker Installed:** Docker must be installed and running on your machine. You can download it from Docker's official site [Docker Installation](https://docs.docker.com/get-docker/).

**A repository on GitHub (personal or organization):** You must have access to GitHub repository be it your personal or organization repository.

**GitHub Personal Access Token (PAT):** A GitHub PAT with the required permissions (``` write:packages```, ```read:packages```) is needed to authenticate with GitHub Container Registry.


## 3. Creating a Dockerfile

1. In your project's root directory, create a file named `Dockerfile`.
2. Add the necessary instructions to build your Docker image.

Example Dockerfile:
```dockerfile
# Run stage
FROM openjdk:17-jdk-slim

WORKDIR /webank-OnlineBanking

# Copy the JAR file from the online-banking-app module
COPY ./online-banking-app/target/online-banking-app-0.1-SNAPSHOT.jar /webank-OnlineBanking/online-banking-app-0.1-SNAPSHOT.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/webank-OnlineBanking/online-banking-app-0.1-SNAPSHOT.jar"]
```
## 4. Building the Docker Image

1. Open a terminal in your project's root directory.

2. Build the Docker image:

```bash
docker build -t your-image-name .
```
## 5. Authenticating with GHCR

To authenticate with GitHub Container Registry (GHCR), you need to create a Personal Access Token (PAT) and use it to log in. Follow these steps:

1. Create a Personal Access Token (PAT) on GitHub:
    - Go to GitHub Settings > Developer settings > Personal access tokens
    - Click on "Generate new token" (classic)
    - Provide a descriptive name for your token in the "Note" field
    - Set an expiration date (or choose "No expiration" if needed)
    - * ![reference image](/Docs/Images/PAT.png)
    - Select the required scopes: `read:packages` , `write:packages`, `repo`,`workflow`(if you need to setup a workflow)
  
    - Click "Generate token" at the bottom of the page
    - **Important**: Copy the generated token immediately, as you won't be able to see it again.

2. Navigate to your terminal of your project and Log in to GHCR using your PAT:

Save your personal access token (classic). It's always a good practice to save your token as an environment variable so as not to expose it .
```bash
export $GITHUB_PAT=YOUR_TOKEN
```
After saving your token in an environmental variable, you can log in to GHCR using the following command:

```bash
echo $GITHUB_PAT | docker login ghcr.io -u USERNAME(your GitHub username) --password-stdin`

> It should show Login Succeeded
```

## 6. Pushing the Image to GHCR

To push your Docker image to GHCR, follow these steps:
1. Tag your image for GHCR:
```bash
docker tag your-image-name ghcr.io/NAMESPACE/your-image-name:tag
```
Replace NAMESPACE with the name of the personal account or organization to which you want the image to be scoped..
The tag could be latest, v1.0, or any version you want to specify
Push the image:

```bash
docker push ghcr.io/USERNAME/your-image-name:tag
```

## 7. Making the Image Public

If you want to make your Docker image publicly accessible, follow these steps:
1. Go to your GitHub repository
2. Click on the "Packages" tab
3. Select your package (Docker image)
4. Navigate to "Package Settings"
5. In the "Danger Zone" section, change the visibility to "Public"
- * ![reference image](/Docs/Images/package-public.png)


#### Pulling the Image from GHCR

After the image has been pushed and made public, others can pull it by navigating to the repository, click on the image under packages:

- * ![reference image](/Docs/Images/pull_image.png)

For our case, we are pulling the image from Webank Online Banking repository, so you can use

```bash
docker pull ghcr.io/adorsys-gis/webank-online-banking:latest

```

#### Running the Image
To run the pulled image:

```bash
docker run -p 8080:8080 ghcr.io/NAMESPACE/your-image-name:tag
```
This command runs the container and maps port 8080 of the container to port 8080 on your host machine.

You can now access the application by opening a web browser and navigating to ```http://localhost:8080/swagger-ui/index.html```

**Alternatively**,you can use GITHUB_TOKEN to authenticate with GHCR.
You can authenticate to GHCR is to use the GITHUB_TOKEN, But this can be specified in the workflow file. GitHub provides you with a token that you can use to authenticate on behalf of GitHub Actions. At the start of each workflow run, GitHub will automatically create a unique GITHUB_TOKEN secret to use in the workflow, which you can use to authenticate.
## 8. Setting Up GitHub Actions Workflow

GitHub Actions allows you to automate your Docker image build and push process. Follow these steps to set up a workflow:

1. In your repository, create a `.github/workflows` directory if it doesn't already exist.

2. Create a YAML file (e.g., `docker-publish.yml`) in this directory.

3. Add the following content to the file:

```yaml
name: Build and Push Docker Image
on:
  # Trigger the workflow only when a pull request is merged into main
  pull_request:
    branches:
      - main
    types:
      - closed
jobs:
  build:
    # Run this job only if the pull request is actually merged
    if: github.event.pull_request.merged == true

    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Log in to GHCR
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Build Docker image
        run: |
          docker build -t ghcr.io/adorsys-gis/webank-online-banking:latest .

      - name: Push Docker image to GHCR
        run: |
          docker push ghcr.io/adorsys-gis/webank-online-banking:latest
```
### Explanation of Workflow
1. `name: Build and Push Docker Image` - The name of the workflow.
2. `on: pull_request` - Trigger the workflow only when a pull request is merged into main.
3. `jobs: build` - The job to build and push the Docker image.
4. `if: github.event.pull_request.merged == true` - Run the job only if the pull request is actually merged.
5. `runs-on: ubuntu-latest` - Run the job on the latest version of Ubuntu.
6. `steps:` - The steps to build and push the Docker image.
7. `name: Checkout code` - Checkout the code from the repository.
8. `uses: docker/login-action@v3` - Log in to Github Container Registry.
9. `with:` - Specify the registry, username, and password.
10. `name: Build Docker image` - Build the Docker image.
11. `run:` - Run the command to build the Docker image.
12. `name: Push Docker image to GHCR` - Push the Docker image to Github Container Registry.

## 9. Troubleshooting Docker and GHCR Deployment

### Common Issues and Solutions

**Authentication Problems**
- Ensure your Personal Access Token (PAT) has the correct permissions
- Verify the scopes include `read:packages` , `write:packages` and `repo`
- Double-check that the token is not expired

**Image Deployment Issues**
- Confirm you're using the correct image name and tag
- Verify that your Dockerfile is located in the root of your repository
- Check GitHub Actions logs for detailed error messages if the workflow fails

## 10. Conclusion
In this guide, you learned how to build a Docker image, authenticate to GitHub Container Registry, tag, and push images. Additionally, you set up GitHub Actions for automating the process and made your images publicly accessible, ensuring that others can use them.
Now, you have a well-structured and efficient way to deploy your Docker images to GitHub Container Registry, making it easier for others to use your services.

