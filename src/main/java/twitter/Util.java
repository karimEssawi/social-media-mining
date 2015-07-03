package twitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static void writeStringToFile(String filePath, String stringToBeWritten) throws IOException {
        try
        {
            String filename= filePath;
            boolean append = true;
            FileWriter fw = new FileWriter(filename,append);
            fw.write(stringToBeWritten);
            fw.write("\n");
            fw.close();
        }
        catch(IOException e)
        {
            System.err.println("IOException: " + e.getMessage());
        }
    }

//    public static void writeStringToCSVFile(String filePathAndName, String stringToBeWritten) throws IOException{
//        try
//        {
//            String filename= filePathAndName;
//            boolean append = true;
//            FileWriter fw = new FileWriter(filename,append);
//            fw.write(stringToBeWritten.replaceAll(","," "));
//            fw.write("\n");
//            fw.close();
//        }
//        catch(IOException e)
//        {
//            System.err.println("IOException: " + e.getMessage());
//        }
//    }

    public static String[] readKeyWords(String fileName){
        String[] keyWords = null;
        List<String> lines;
        File f = new File(fileName);

//        URL url = Util.class.getClass().getResource(fileName);
//        File file;
//        try{
//            file = new File(url.toURI());
//            lines = Files.readAllLines(Paths.get(file.getCanonicalPath()), Charset.defaultCharset());
//            keyWords = lines.toArray(new String[lines.size()]);
//        }catch(IOException | URISyntaxException e){e.printStackTrace();}

        try{
//            lines = Files.readAllLines(Paths.get(f.getCanonicalPath()), Charset.defaultCharset());
            lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
            keyWords = lines.toArray(new String[lines.size()]);
        }catch(IOException e){e.printStackTrace();}

        return keyWords;
    }

    public static void cleanUpTweets(String filePath, String distFilePath) {
        List<String> list = new ArrayList<>();
        try {
            FileWriter writer = new FileWriter(distFilePath);
            list = Files.lines(Paths.get(filePath))
                        .map(s -> s.trim())
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
            int counter = 1;
            for(String l:list){
                String tweet = l.replaceAll("@\\w+|#\\w+|\\bRT\\b", "")
                                .replaceAll("[^\\p{L}\\p{N} ]+", " ")
                                .replaceAll(" +", " ")
                                .trim();
                writer.append(String.valueOf(counter));
                writer.append(',');
                writer.append("NGV");
                writer.append(',');
                writer.append(tweet + "\n");
                counter++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        cleanUpTweets("tweetsNegative.txt", "tweetsNegativeClean.csv");
    }
}