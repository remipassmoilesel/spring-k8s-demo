package org.remipassmoilesel.k8sdemo.signature.gpg;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class GpgWrapper {

    private static final Logger logger = LoggerFactory.getLogger(GpgWrapper.class);

    public void importPublicKey(String key) throws IOException {
        this.executeCommand("gpg2 --import", key.getBytes());
    }

    public void importPrivateKey(String key) throws IOException {
        this.executeCommand("gpg2 --allow-secret-key-import --import", key.getBytes());
    }

    public String signDocument(String documentPath, String keyId) throws IOException {
        String command = "gpg2 ";
        command += "--detach-sign -a --output - ";
        command += "--default-key '" + keyId + "' ";
        command += "--sign '" + documentPath + "'";
        return this.executeCommand(command, null);
    }

    public void verifyDocument(String documentPath, String signaturePath, String keyId) throws IOException {
        String command = "gpg2 ";
        command += "--default-key '" + keyId + "' ";
        command += "--verify '" + signaturePath + "' ";
        command += "'" + documentPath + "'";
        this.executeCommand(command, null);
    }

    private String executeCommand(String rawCommand, byte[] input) throws IOException {
        logger.info("Executing command: " + rawCommand);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DefaultExecutor executor = this.getCommandExecutor(input, output);

        CommandLine cmdLine = CommandLine.parse(rawCommand);
        executor.execute(cmdLine);

        return output.toString();
    }

    private DefaultExecutor getCommandExecutor(byte[] input, ByteArrayOutputStream output) {
        ByteArrayInputStream inputStream = null;
        if (input != null) {
            inputStream = new ByteArrayInputStream(input);
        }

        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(output, null, inputStream));

        executor.setExitValue(0);

        ExecuteWatchdog watchdog = new ExecuteWatchdog(20000);
        executor.setWatchdog(watchdog);

        return executor;
    }
}
