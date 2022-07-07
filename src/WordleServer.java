

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WordleServer extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    public WordleServer( int port ) {
        this.port = port;
    }

    public void startServer( )
    {
        try
        {
            serverSocket = new ServerSocket( port );
            this.start( );
        }

        catch (IOException e)
        {
            e.printStackTrace( );
        }
    }

    public void stopServer( )
    {
        running = false;
        this.interrupt( );
    }

    @Override
    public void run( )
    {
        running = true;
        while( running )
        {
            try
            {
                // accept() to receive the next connection
                Socket socket = serverSocket.accept( );

                // Pass the socket to RequestHandler thread
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start( );
            }

            catch ( IOException e )
            {
                e.printStackTrace( );
            }
        }
    }

    public static void main( String[] args )
    {
        if ( args.length == 0 )
        {
            System.out.println( "Usage: WordleServer <port>" );
            System.exit( 0 );
        }

        int port = Integer.parseInt( args[0] );
        System.out.println( "Start server on port: " + port );

        // display server address
        // https://mkyong.com/java/how-to-get-ip-address-in-java/#:~:text=In%20Java%2C%20you%20can%20use,Server%20running%20the%20Java%20app.
        try
        {
            InetAddress ip = InetAddress.getLocalHost( );
            System.out.println( "Current Server IP address : " + ip.getHostAddress( ) );
        }

        catch ( UnknownHostException e )
        {
            e.printStackTrace();
        }

        WordleServer server = new WordleServer( port );
        server.startServer( ) ;
        System.out.println( "\nServer started successfully\n" );

        server.stopServer( );
    }
}

class RequestHandler extends Thread
{
    // method to intialize socket
    private Socket socket;
    RequestHandler ( Socket socket ) { this.socket = socket; }

    @Override
    public void run( )
    {
        try {
            System.out.println( "Received a connection" );

            // get streams from client
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
            PrintWriter out = new PrintWriter( socket.getOutputStream( ) );

            printTime( );

            printIP( );

            //String answer = pickAnswer( );
            String answer = "bolos";
            System.out.println( "Answer: " + answer + "\n" );
            System.out.flush();

            String line = in.readLine();
            String finalWord;

            while ( line != null && line.length() > 0 )
            {

                finalWord = wordle( line, answer );   // process line

                out.println( finalWord );     // output the word to client
                out.flush( );

                line = in.readLine( );       // prompt again
            }

            // close connection
            in.close( );
            out.close( );
            socket.close( );
        }

        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }

    public static void printTime( )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
        Date date = new Date( );
        System.out.println( "Connection time: " + formatter.format( date ) );
    }

    public static void printIP( )
    {
        // output IP address
        // Reference: https://crunchify.com/how-to-get-server-ip-address-and-hostname-in-java/
        InetAddress ip;
        String hostname;
        try
        {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Client current IP address : " + ip);
            System.out.println("Client current Hostname : " + hostname);

        }

        catch ( UnknownHostException e )
        {
            e.printStackTrace();
        }
    }

    // method to check if str is a word and output the correct syntax
    private static String wordle( String str, String answer ) throws FileNotFoundException {
        String word = str.strip( );

        if ( word.equalsIgnoreCase( answer ) )
        {
            return "correct";
        }

        else if ( isWord( word ) )
        {
            return answer;
        }

        else
        {
            return "invalid";
        }
    }

    private static boolean isWord( String searchStr ) throws FileNotFoundException
    {
        Scanner scan = new Scanner( new File( "WordBank.txt" ) );

        while( scan.hasNext( ) )
        {
            String line = scan.nextLine( ).toLowerCase( );
            if ( line.equals( searchStr ) )
            {
                System.out.println( "\nUser word was found in WordBank.txt\n");
                return true;
            }
        }
        System.out.println( "\nUser word was NOT found in WordBank.txt\n");
        return false;
    }

    private static String pickAnswer( ) throws FileNotFoundException
    {
        Random random = new Random();
        File file = new File( "WordleBank.txt" );
        int rand_num = ThreadLocalRandom.current().nextInt( 0, 5757 );

        try ( BufferedReader br = new BufferedReader( new FileReader( "WordBank.txt" ) ) )
        {
            for ( int i = 0; i < rand_num; i++ )
            {
                br.readLine( );
            }
            return br.readLine( );
        }

        catch ( IOException e )
        {
            System.out.println( e );
        }

        return "Error in pickAnswer";
    }
}