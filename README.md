# Wordle Server

Wordle is a popular word game currently playable at https://www.nytimes.com/games/wordle/index.html

This project is a CLI version of the game that can be played as many times as desired.

# Rules 

* You have to guess the Wordle in six goes or less.
* Every word you enter must be in the word list ( see src/Wordbank.txt for list of words )
* A correct letter is displayed as ```[L]```
* A correct letter in the wrong place is displayed as ```{L}```
* An incorrect letter is displayed as ```|L|```
* Letters can be used more than once.
* Unlike the real Wordle, plurals can be used ( sorry lol )

# Install

cd into desired directory and clone repo into desired directory 

```
cd ExampleDirectory
```

```
git clone https://github.com/SawyerTravis/WordleServer.git
```

# Starting Wordle Server...

First, using the original terminal, navigate into the src directory 
* You will need to repeat this step in a second terminal window for the client later on

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

# Starting Wordle Client... 

Once the server is running, open a second terminal window and cd to the same directory 

```
cd ExampleDirectory
```

Then, compile the Wordle client 

```
javac WordleClient.java
```

Take the IP address and port of the server to connect the client
( IP address may not be the same as the example )
```
java WordleClient 10.46.185.70 4444
```

The client should display the following...
```
****Wordle Server****

Connected
Would you like to play Wordle? (Enter Y or N) 
````

The server should display the following...
* Hostname will actually be displayed  
```
Received a connection
Connection time: 07/07/2022 13:59:52
Client current IP address : Hostname/10.46.185.70
Client current Hostname : Hostname 
```

You are now ready to play Wordle! 

* All user inputs go through the client (typing in the server won't do anything)
* Once your game ends, you can follow these steps again to connect another client 
* The server does support multiple clients for "multiplayer" ;)  



