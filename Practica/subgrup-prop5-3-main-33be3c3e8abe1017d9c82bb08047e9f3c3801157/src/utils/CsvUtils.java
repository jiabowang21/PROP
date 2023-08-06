package utils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe amb dos mètodes encarregats de la codificació i de codificació de
 * fitxers en format CSV.
 *
 * @author Hector Godoy Creus
 */
public class CsvUtils {

    //-----------------------------------------------
    //Funcions per fer parse de strings en format CSV
    //-----------------------------------------------

    public static List<List<String>> parse(Reader file, List<String> messages) throws IOException {
        List<List<String>> result = new ArrayList<>();
        int n = 10;
        for (int i = 0; ; ++i) {
            List<String> row = parseRecord(file, messages, i, n);
            if (row == null) break;
            if (i > 0 && row.size() != result.get(0).size()) break;
            result.add(row);
            if (row.size() > n) n = Math.min(row.size(), 2 * n);
        }
        return result;
    }

    private static List<String> parseRecord(Reader file, List<String> messages, int rowNumber, int hint) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> row = new ArrayList<>(Math.min(Math.max(10, hint), 1000));
        for (int i = 0; ; ) {
            int c = parseField(file, sb, messages, rowNumber, i);
            if (i == 0 && sb.length() == 0) {
                if (c == -1)
                    return null;        // end of the file
                if (c == '\n')
                    continue;        // ignore blank lines in the middle of the file
            }
            row.add(sb.toString());
            if (c != ',') break;
            ++i;
        }
        return row;
    }

    private static int parseField(Reader file, StringBuilder result, List<String> messages, int row, int col) throws IOException {
        StringBuilder dump = null;
        result.setLength(0);
        boolean needsRead = true;
        int c = file.read();
        int numChars = 0;      // number of non-white chars that are added to the result
        for (; ; ) {
            if (c == '\n' || c == ',' || c == -1) {
                if (dump != null)
                    messages.add("WARN: Ignoring data in record " + row + " field " + (col + 1) + ": " + dump);
                if (numChars > 0) {
                    // trim trailing whitespace for non-quoted fields
                    while (numChars > 1 && Character.isWhitespace(result.charAt(numChars - 1))) --numChars;
                    result.setLength(numChars);
                }
                return c;
            }
            if (numChars == 0 && c == '"') {
                result.setLength(0);    // Ignore whitespace in front of quotes
                for (; ; ) {
                    if (needsRead)
                        c = file.read();
                    else
                        needsRead = true;
                    if (c == -1) {
                        messages.add("WARN: Unexpected EOF in record " + row + " field " + (col + 1));
                        return c;
                    }
                    if (c == '"') {
                        // Interpret double quotes inside quotes, meaning single quote
                        c = file.read();
                        if (c == '\n' || c == ',' || c == -1) {
                            // It was the last quote of the field
                            return c;
                        } else if (c != '"')
                            // Next element is a normal character, skip reading and process as normal
                            // If it's a second quote, it'll be added as normal
                            needsRead = false;
                    }
                    if (c == '\\') {
                        c = file.read();
                        // Interpret \n, \t, \\, and \" in the strings
                        if (c == 'n') c = '\n';
                        else if (c == 't') c = '\t';
                        else if (c != '"' && c != '\\') result.append('\\');
                    }
                    result.append((char) c);
                }
            } else {
                if (numChars == -1) {
                    if (dump != null)
                        dump.append((char) c);
                    else if (!Character.isWhitespace(c))
                        dump = new StringBuilder("" + (char) c);
                } else {
                    if (numChars > 0 || !Character.isWhitespace(c)) {
                        result.append((char) c);
                        ++numChars;
                    }
                }
            }
            c = file.read();
        }
    }


    //-----------------------------------------------
    // Funcions per convertir una string a CSV
    //-----------------------------------------------

    public static String toCsv(List<String> attribs) {
        StringBuilder builder = new StringBuilder();
        for (String attrib : attribs) {
            if (attrib.contains(",") || attrib.contains("\"") || attrib.contains("\n") || attrib.contains("\r"))
                builder.append("\"").append(attrib.replaceAll("\"", "\"\"")).append("\"");
            else
                builder.append(attrib);

            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
