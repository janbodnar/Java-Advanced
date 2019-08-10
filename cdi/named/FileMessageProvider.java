package com.zetcode.renderer;

import com.zetcode.provider.MessageProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileMessageRenderer implements MessageRenderer {

    @Inject
    @Named("HelloMessageProvider")
    private MessageProvider messageProvider;

    @Override
    public void render() {

        var msg = messageProvider.getMessage();

        OpenOption[] options = {CREATE,  APPEND};

        try  {

            var string = String.format("%s%n", msg);

            Files.writeString(Paths.get("src/main/resources/log.txt"), string, options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
