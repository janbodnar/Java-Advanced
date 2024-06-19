# Lambda


## throw IOException

```
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    @Override
    default void accept(final T elem) {
        try {
            acceptThrows(elem);
        } catch (final Exception e) {
            // Implement your own exception handling logic here..
            // For example:
//            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T elem) throws Exception;

}

void main(String[] args) throws IOException {

    FileSystem fileSystem = FileSystems.getDefault();

    System.out.printf("%30s | %10s | %23s | %20s \n", "", "Type", "Total space", "Free space");
    System.out.println("-------------------------------------------------"
            + "----------------------------------------------------------");

    Iterable<FileStore> fileStores = fileSystem.getFileStores();

//    for (var fileStore : fileStores) {
//        System.out.printf("%30s | %10s | %20s GB | %20s GB\n", fileStore, fileStore.type(),
//                (fileStore.getTotalSpace() / 1073741824f),
//                (fileStore.getUsableSpace() / 1073741824f));
//    }

    fileStores.forEach((ThrowingConsumer<FileStore>) fileStore -> {

            System.out.printf("%30s | %10s | %20s GB | %20s GB\n", fileStore, fileStore.type(),
                    (fileStore.getTotalSpace() / 1073741824f),
                    (fileStore.getUsableSpace() / 1073741824f));
        });
}




//class MyConsumer implements Consumer<FileStore>
//{
//
//    @Override
//    public void accept(FileStore fileStore) throws IOException {
//        System.out.printf("%30s | %10s | %20s GB | %20s GB\n", fileStore, fileStore.type(),
//                (fileStore.getTotalSpace() / 1073741824f),
//                (fileStore.getUsableSpace() / 1073741824f));
//    }
//}
```
