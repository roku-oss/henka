package com.roku.terraform.executors

class BashExecutor {

    public int execute(String command, String tfDir) {
        println command

        def pb = new ProcessBuilder("bash", "-c", command)

        def process = pb
                .directory(new File(tfDir))
                .redirectErrorStream(true)
                .start()

        def reader = new BufferedReader(new InputStreamReader(process.inputStream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                println line;
            }
        } finally {
            reader.close()
        }
        process.waitFor()

        return process.exitValue();
    }

}
