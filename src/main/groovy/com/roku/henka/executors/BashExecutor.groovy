/*
* Copyright (c) 2016, Henka Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and limitations under the License.
*/
package com.roku.henka.executors

class BashExecutor {

    public int execute(String command, String tfDir) {
        println command

        def pb = new ProcessBuilder("bash", "-c", command)

        def process = pb
                .directory(new File(tfDir))
                .redirectErrorStream(true)
                .redirectInput(ProcessBuilder.Redirect.INHERIT)
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
