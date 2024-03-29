**This project was made as a school assignment and has no real purpose!**

# OpiNet

OpiNet is an imaginary social media platform, where anyone can create an account and then upload posts and comment posts
of other people.

This project has no purpose as it directly interacts with the database, which makes no sense. Not for use!

## Screenshots

<a href="readme-res/screenshots/login.png">
  <img alt="login" src="readme-res/screenshots/login.png" width="300px" />
</a>
<a href="readme-res/screenshots/register.png">
  <img alt="register" src="readme-res/screenshots/register.png" width="300px" />
</a>
<a href="readme-res/screenshots/feed.png">
  <img alt="feed" src="readme-res/screenshots/feed.png" width="300px" />
</a>
<a href="readme-res/screenshots/newpost.png">
  <img alt="new post" src="readme-res/screenshots/newpost.png" width="300px" />
</a>

## Running

### From JAR

1. Go to [GitHub releases](https://github.com/Tomasan7/OpiNet/releases) open the latest release and download the JAR file.
2. Open a terminal and navigate to the directory where the JAR file is located.
3. Run the JAR file: `java -jar <jarfile>`
4. Or just double-click the JAR file in file explorer.

### From code

1. Clone the repository: `git clone https://github.com/Tomasan7/OpiNet.git`
2. Navigate to the project directory: `cd OpiNet`
3. Run the project: `./gradlew run`

## Importing

### Users

1. Open `opinet.conf` and configure your CSV delimiter in the `import` section.
2. Go to registration and click the import button at the bottom.
3. Select your CSV file.
4. The columns are in the following order: `username`, `first name`, `last name`, `password`
5. Check the console for any possible errors.

### Posts

1. Open `opinet.conf` and in the `import` section.
   1. Configure your CSV delimiter.
   2. Configure your date format.
2. Login with any account and go to create new post page.
3. Click the import button at the bottom.
4. Select your CSV file.
5. The columns are in the following order: `author username`, `upload date`, `title`, `content`
6. Check the console for any possible errors.
