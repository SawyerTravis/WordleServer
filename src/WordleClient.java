/* */

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class WordleClient
{

    public static void main( String[] args )
    {

        // initial message
        System.out.println( "\n****Wordle Server****\n" );

        // get server name
        String server = args[0];

        // get port number (must be converted to int)
        int port = Integer.parseInt( args[1] );

        try
        {
            // https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
            Socket socket = new Socket( server, port );                    // 1st statement
            System.out.println( "Connected" );
            PrintWriter out =                                            // 2nd statement
                    new PrintWriter( socket.getOutputStream( ), true );
            BufferedReader in =                                          // 3rd statement
                    new BufferedReader(
                            new InputStreamReader( socket.getInputStream( ) ) );
            BufferedReader stdIn =                                       // 4th statement
                    new BufferedReader( new InputStreamReader( System.in ) );

            String userIn;

            System.out.println( "Would you like to play Wordle? (Enter Y or N) \n" );
            System.out.flush( );
            userIn = stdIn.readLine( );
            String userAnswer = userIn.strip( );

            // check for valid user input
            while ( !( userAnswer.equalsIgnoreCase( "Y" ) )
                    && !( userAnswer.equalsIgnoreCase( "N" ) ) )
            {
                System.out.println( "Invalid answer: Please enter Y or N \n");
                System.out.flush( );
                userIn = stdIn.readLine( );
                userAnswer = userIn.strip( );
            }

            if ( userIn.equalsIgnoreCase( "N" ) )
            {
                System.out.println( "Have a nice day! \n" + "\n****Quitting Wordle Server****\n" );
                System.out.flush( );

                // close streams and socket
                out.close( );
                in.close( );
                stdIn.close( );
                socket.close( );

                // exit
                System.exit( 0 ) ;
            }

            int count = 0;
            StringBuilder strBuilder = new StringBuilder( );

            // begin reading string inputs from user
            while ( count < 6 )
            {
                // prompt user for text in next while loop
                System.out.println( "Enter a five letter word: " );
                System.out.flush( );
                userIn = stdIn.readLine( );
                userIn = userIn.toLowerCase( );

                // exit program if "quit" is entered
                // Reference: https://www.geeksforgeeks.org/compare-two-strings-in-java/#:~:text=Using%20String.,match%2C%20then%20it%20returns%20false.
                if ( userIn.equals( "quit" ) )
                {
                    System.out.println( "\n****Quitting Wordle Server****\n" );
                    break;
                }

                // output to server
                out.println( userIn );

                String response = in.readLine( );

                if ( response.equalsIgnoreCase( "invalid" ) )
                {
                    System.out.println( "Wordle Server: " + userIn + " is not a word, please try again \n" );

                    if ( count != 0 )
                    {
                        System.out.println( strBuilder );
                    }
                }

                else if ( response.equalsIgnoreCase( "correct" ) )
                {
                    System.out.println("Wordle Server: " + userIn + " is correct! \n");
                    System.out.println("\n****Quitting Wordle Server****\n");
                    break;
                }

                // TODO: add display for valid guesses
                else
                {
                    ++count;
                    System.out.println("Wordle Server: " + userIn + " is a word. " + ( 6 - count ) + " guesses left \n" );
                    System.out.println( updateDisplay( strBuilder, userIn, response ) );
                }
            }

            if ( count == 6 )
            {
                System.out.println( "\nNo more guesses. Try again later!" );
                System.out.println( "\n****Quitting Wordle Server****\n" );
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

    private static String updateDisplay( StringBuilder strBuilder, String str, String answer )
    {
        // initialize list to track repeating letters
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        HashMap<Character, Integer> numCharAnswer = new HashMap<>();
        HashMap<Character, Integer> numCharWord = new HashMap<>();
        for ( int y = 0; y < 26; ++y )
        {
            numCharAnswer.put( alphabet.charAt( y ), 0 );
            numCharWord.put( alphabet.charAt( y ), 0 );
        }

        // count each letter in the answer string
        char c;
        char d;
        for ( int j = 0; j < answer.length( ); ++j )
        {
            c = answer.charAt( j );
            d = str.charAt( j );
            for ( int x = 0; x < alphabet.length( ); ++x )
            {
                if ( c == alphabet.charAt( x ) )
                {
                    int z = numCharAnswer.get( alphabet.charAt( x ) );
                    ++z;
                    numCharAnswer.put( alphabet.charAt( x ) , z );
                    // System.out.println( alphabet.charAt(x) + ": " + numCharAnswer.get( alphabet.charAt( x ) ) );
                }

                if ( d == alphabet.charAt( x ) )
                {
                    int a = numCharWord.get( alphabet.charAt( x ) );
                    ++a;
                    numCharWord.put( alphabet.charAt( x ) , a );
                    // System.out.println( alphabet.charAt(x) + ": " + numCharWord.get( alphabet.charAt( x ) ) );
                }
            }
        }

        // update the display
        str = str.strip( );
        int length = str.length( );
        for (int i = 0; i < length; i++)
        {
            c = str.charAt( i );

            // if c is the correct letter in the correct position, add square brackets
            if ( c == answer.charAt( i ) )
            {
                strBuilder.append( " [" );
                strBuilder.append( c );
                strBuilder.append( "] " );
            }

            // if c is a correct letter in the wrong position, add squiggly brackets
            else if ( answer.contains( String.valueOf( c ) ) )
            {
                if ( numCharWord.get( c ) > numCharAnswer.get( c ) )
                {
                    System.out.println(c + ": " + numCharAnswer.get(c)+numCharWord.get(c));
                    strBuilder.append( " |" );
                    strBuilder.append( c );
                    strBuilder.append( "| " );
                    numCharWord.put( c, ( numCharWord.get( c ) - 1 ) );
                }

                else
                {
                    //System.out.println(c + ": " + numChar.get(c));
                    strBuilder.append( " {" );
                    strBuilder.append( c );
                    strBuilder.append( "} " );
                }
            }
            // if not contained
            else
            {
                strBuilder.append( " |" );
                strBuilder.append( c );
                strBuilder.append( "| " );
            }
        }
        strBuilder.append( "\n" );
        return strBuilder.toString( );
    }
}
