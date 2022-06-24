/* */

import java.net.*;
import java.io.*;

public class WordleClient {

    public static void main(String[] args) {

        // initial message
        System.out.println("\n****Wordle Server****\n");

        // get server name
        String server = args[0];

        // get port number (must be converted to int)
        int port = Integer.parseInt(args[1]);

        try {
            // https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
            Socket socket = new Socket(server, port);                    // 1st statement
            System.out.println("Connected");
            PrintWriter out =                                            // 2nd statement
                    new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in =                                          // 3rd statement
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =                                       // 4th statement
                    new BufferedReader(new InputStreamReader(System.in));

            String userIn;

            // get rotation
            System.out.println("Would you like to play Wordle? (Enter Y or N) \n");
            System.out.flush();
            userIn = stdIn.readLine();

            // check for valid user input
            while ( !( userIn.equalsIgnoreCase("Y") )
                    && !( userIn.equalsIgnoreCase("N") ) )
            {
                System.out.println( "Invalid answer: Please enter Y or N) \n");
                System.out.flush();
                userIn = stdIn.readLine();
            }

            if ( userIn.equalsIgnoreCase( "N" ) )
            {
                System.out.println( "Have a nice day! \n" + "\n****Quitting Wordle Server****\n");
                System.out.flush();

                // close streams and socket
                out.close();
                in.close();
                stdIn.close();
                socket.close();

                // exit
                System.exit(0);
            }

            int count = 0;

            // begin reading string inputs from user
            while ( count < 5 ) {
                // prompt user for text in next while loop
                System.out.println("Enter a five letter word: ");
                System.out.flush();
                userIn = stdIn.readLine();

                // exit program if "quit" is entered
                // Reference: https://www.geeksforgeeks.org/compare-two-strings-in-java/#:~:text=Using%20String.,match%2C%20then%20it%20returns%20false.
                if (userIn.equals("quit")) {
                    System.out.println("\n****Quitting SJT Server****\n");
                    break;
                }

                // output to server
                out.println(userIn);

                String response = in.readLine();

                if ( response.equalsIgnoreCase("invalid") )
                {
                    System.out.println("Wordle Server: " + response + " is not a word, please try again \n");
                }

                else
                {
                    System.out.println("Wordle Server: " + response + "\n");
                    ++count;
                }
            }

            // close streams and socket
            out.close();
            in.close();
            stdIn.close();
            socket.close();

            // exit
            System.exit(0);

        } catch (IOException exc) {     // handle error exception
            System.out.println("I/O error: " + exc.getMessage());
        }
    }

}
