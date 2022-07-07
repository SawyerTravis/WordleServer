# WordleServer

Client and Server with Wordle gameplay

# Install

cd into desired directory and clone repo into desired directory 

```
cd ExampleDirectory
```

```
git clone https://github.com/SawyerTravis/WordleServer.git
```

# Compilation & Initialization of Wordle Server

First, using the original terminal, navigate into the src directory 
(You will need to repeat this step in a second terminal window for the client later on)

```
cd Wordle/src
```

Second, compile the server file

```
javac WordleServer.java
```

Thirdly, run the server on the desired port number (4444 is used in the example)

```
java WordleServer 4444
```

The following messages will be displayed to show that the server is running

```
Start server on port: 4444
Current Server IP address : 10.46.185.70

Server started successfully
```

# Compilation and Intialization of Wordle Client 

Once the server is running, open a second terminal window and cd to the same directory 

```
cd ExampleDirectory
```

Then, compile the Wordle client 

```
javac WordleClient.java
```
