package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resultFileName){
        Path TPO1dir = Paths.get(dirName);
        Path resultFile = Paths.get(resultFileName);
        Charset inputCharset = Charset.forName("Cp1250");
        Charset outputCharset = StandardCharsets.UTF_8;

        try {
            FileChannel resultChannel = FileChannel.open(resultFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            Files.walkFileTree(TPO1dir, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    System.out.println("Wchodze do katalogu "+dir.getFileName());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if(Files.isRegularFile(file)){
                        try{
                            FileChannel fileChannel = FileChannel.open(file, StandardOpenOption.READ);
                            ByteBuffer bb = ByteBuffer.allocate((int)fileChannel.size());
                            while(fileChannel.read(bb)>0){
                                bb.flip();
                                CharBuffer cb = inputCharset.decode(bb);
                                resultChannel.write(outputCharset.encode(cb));
                                bb.clear();
                            }
                            fileChannel.close();
                        }
                        catch (IOException e){
                            System.out.println(e);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.out.println("Nie da sie otworzyc pliku "+file);
                    System.out.println(exc);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    System.out.println("Wychodze z katalogu "+dir.getFileName());
                    return FileVisitResult.CONTINUE;
                }
            });
            resultChannel.close();
        }
        catch (IOException e){
            System.out.println(e);
        }


    }
}
