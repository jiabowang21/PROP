/* Aquesta es una adaptacio d'un conjunt de classes
   per a fer entrada/sortida de texte amb Java.
   La versio original va ser creada per Per Brinch
   Hansen (s'inclou el copyright de la versio original)
*/

/*                 THE JAVA TEXT PROGRAM    
                       28 April 1999
            Copyright (c) 1999 Per Brinch Hansen

This program defines a set of Java classes for text
processing. These text classes enable Java programs to
output text to screen and disk files and input text from
keyboard and disk files. The present version runs on
Macintosh, Unix, and Windows 95 systems.
*/

package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InOut {
    private final char cr = '\r', eof = '\uFFFF',
            nl = '\n', sp = ' ';
    private final String EOF = "eof";
    private final BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in));

    /* instruccions d'escriptura */
    private final char[] buffer = new char[80];
    private int typed = 0, used = 0;
    private boolean ahead = false;
    private char ch /*next character (if ahead)*/;

    public InOut() {

    }

    public void write(boolean value) throws Exception {
        write(value, 1);
    }

    public void write(boolean value, int width)
            throws Exception {
        String word = value ? "true" : "false";
        writespace(width - word.length());
        write(word);
    }

    public void write(char value) throws Exception {
        System.out.print(value);
    }

    public void write(char value, int width) throws Exception {
        write(value);
        writespace(width - 1);
    }

    public void write(double value) throws Exception {
        write(value, 1);
    }

    public void write(double value, int width)
            throws Exception {
        String numeral = String.valueOf(value);
        writespace(width - numeral.length());
        write(numeral);
    }

    public void write(int value) throws Exception {
        write(value, 1);
    }

    public void write(int value, int width) throws Exception {
        String numeral = String.valueOf(value);
        writespace(width - numeral.length());
        write(numeral);
    }

    public void write(String value) throws Exception {
        write(value, 1);
    }

    public void write(String value, int width)
            throws Exception {
        int length = value.length();
        for (int i = 0; i < length; i++)
            write(value.charAt(i));
        writespace(width - length);
    }

    public void writeln() throws Exception {
        write(nl);
    }

    public void writeln(boolean value) throws Exception {
        writeln(value, 1);
    }

    public void writeln(boolean value, int width)
            throws Exception {
        write(value, width);
        writeln();
    }

    public void writeln(char value) throws Exception {
        writeln(value, 1);
    }

    public void writeln(char value, int width)
            throws Exception {
        write(value, width);
        writeln();
    }

    public void writeln(double value) throws Exception {
        writeln(value, 1);
    }

    public void writeln(double value, int width)
            throws Exception {
        write(value, width);
        writeln();
    }

    public void writeln(int value) throws Exception {
        writeln(value, 1);
    }


    /* instruccions de lectura */

    public void writeln(int value, int width) throws Exception {
        write(value, width);
        writeln();
    }

    public void writeln(String value) throws Exception {
        writeln(value, 1);
    }

    public void writeln(String value, int width)
            throws Exception {
        write(value, width);
        writeln();
    }

    public void writesame(char value, int number)
            throws Exception {
        for (int i = 1; i <= number; i++) write(value);
    }

    public void writespace(int number) throws Exception {
        writesame(sp, number);
    }

    private char readkey() throws Exception {
        if (used == typed) {
            String line = readkeyline();
            if (line.equals(EOF)) {
                typed = 0;
                buffer[typed] = eof;
            } else {
                typed = line.length();
                for (int i = 0; i < typed; i++)
                    buffer[i] = line.charAt(i);
                buffer[typed] = nl;
            }
            typed = typed + 1;
            used = 0;
        }
        char ch = buffer[used];
        if (ch == cr) ch = nl;
        used = used + 1;
        return ch;
    }

    private String readkeyline() throws Exception {
        String line = in.readLine();
        return (line == null ? "" : line);
    }

    public boolean blank() throws Exception {
        getahead();
        return (ch == cr) | (ch == nl) | (ch == sp);
    }

    public boolean digit() throws Exception {
        getahead();
        return ('0' <= ch) & (ch <= '9');
    }

    private void getahead() throws Exception {
        if (!ahead) readnext();
    }

    public boolean letter() throws Exception {
        getahead();
        return (('a' <= ch) & (ch <= 'z')) |
                (('A' <= ch) & (ch <= 'Z'));
    }

    public boolean more() throws Exception {
        getahead();
        return ch != eof;
    }

    public char next() throws Exception {
        getahead();
        return ch;
    }

    public char read() throws Exception {
        getahead();
        ahead = false;
        return ch;
    }

    public void readblanks() throws Exception {
        while (blank()) readnext();
    }

    public boolean readboolean() throws Exception {
        boolean value;
        String symbol = readname();
        if (symbol.equals("false")) value = false;
        else if (symbol.equals("true")) value = true;
        else {
            String error = "boolean format: ";
            throw new Exception(error + symbol);
        }
        return value;
    }

    public double readdouble() throws Exception {
        StringBuilder symbol = new StringBuilder();
        //----------MODIFICACIO------------
        readblanks();
        if (ch == '+') readnext();
        else if (ch == '-')
            symbol.append(read());
        while (digit()) symbol.append(read());
        //---------------------------------
        // hem substiuit el readint per aquest boci de codi.
        if (ch == '.') {
            symbol.append(read());
            while (digit()) symbol.append(read());
        }
        if ((ch == 'e') | (ch == 'E')) {
            symbol.append(read());
            symbol.append(readint());
        }
        return Double.parseDouble(symbol.toString());
    }

    public int readint() throws Exception {
        StringBuilder symbol = new StringBuilder();
        readblanks();
        if (ch == '+') readnext();
        else if (ch == '-')
            symbol.append(read());
        while (digit()) symbol.append(read());
        return Integer.parseInt(symbol.toString());
    }

    public String readline() throws Exception {
        StringBuilder line = new StringBuilder();
        getahead();
        while (ch != nl) {
            line.append(ch);
            readnext();
        }
        ahead = false;
        return line.toString();
    }

    public void readln() throws Exception {
        String skipped = readline();
    }

    public String readname() throws Exception {
        StringBuilder letters = new StringBuilder();
        readblanks();
        while (letter() | digit() | (ch == '_')) {
            letters.append(ch);
            readnext();
        }
        return letters.toString();
    }

    public void readnext() throws Exception {
        ch = readkey();
        ahead = true;
    }

    public String readword() throws Exception {
        StringBuilder word = new StringBuilder();
        readblanks();
        while (!blank()) {
            word.append(ch);
            readnext();
        }
        return word.toString();
    }


    // Instruccions per llegir vector i matrius d'enters i reals

    public int[] read_int_array() throws Exception {
        int[] v = new int[100];
        int[] f;
        int i = 0;
        readblanks();
        while (next() != ';') {
            v[i] = readint();
            readblanks();
            i = i + 1;
        }
        read();
        f = new int[i];
        for (i = 0; i < f.length; i++) f[i] = v[i];
        return f;
    }

    public void write(int[] v) throws Exception {
        for (int j : v) write(" " + j);
    }

    public void writeln(int[] v) throws Exception {
        write(v);
        writeln();
    }

    public double[] read_double_array() throws Exception {
        double[] v = new double[100];
        double[] f;
        int i = 0;
        readblanks();
        while (next() != ';') {
            v[i] = readdouble();
            readblanks();
            i = i + 1;
        }
        read();
        f = new double[i];
        for (i = 0; i < f.length; i++) f[i] = v[i];
        return f;
    }

    public void write(double[] v) throws Exception {
        for (double value : v) write(" " + value);
    }

    public void writeln(double[] v) throws Exception {
        write(v);
        writeln();
    }

    public int[][] read_int_matrix() throws Exception {
        int[][] v = new int[100][100];
        int[][] f;
        int i = 0;
        readblanks();
        while (next() != ';') {
            v[i] = read_int_array();
            readblanks();
            i = i + 1;
        }
        read();
        f = new int[i][];
        for (i = 0; i < f.length; i++) {
            f[i] = new int[v[i].length];
            System.arraycopy(v[i], 0, f[i], 0, v[i].length);
        }
        return f;
    }

    public void write(int[][] m) throws Exception {
        for (int[] ints : m) {
            write(ints);
            write(";");
        }
    }

    public void writeln(int[][] m) throws Exception {
        for (int[] ints : m) {
            write(ints);
            writeln(";");
        }
    }

    public double[][] read_double_matrix() throws Exception {
        double[][] v = new double[100][100];
        double[][] f;
        int i = 0;
        readblanks();
        while (next() != ';') {
            v[i] = read_double_array();
            readblanks();
            i = i + 1;
        }
        read();
        f = new double[i][];
        for (i = 0; i < f.length; i++) {
            f[i] = new double[v[i].length];
            System.arraycopy(v[i], 0, f[i], 0, v[i].length);
        }
        return f;
    }

    public void write(double[][] m) throws Exception {
        for (double[] doubles : m) {
            write(doubles);
            write(";");
        }
    }

    public void writeln(double[][] m) throws Exception {
        for (double[] doubles : m) {
            write(doubles);
            writeln(";");
        }
    }
}