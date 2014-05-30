package uk.gov.ida

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenSeqDiagramTask extends DefaultTask {
    String inFile;

    @TaskAction
    def greet() {
        createSequenceDiagrams();
    }

    private void createSequenceDiagrams() {
        File diagramDir = new File("build/diagrams")
        String[] inputFiles = diagramDir.list(new FilenameFilter() {
            boolean accept(File dir, String name) {
                return name.endsWith(".txt")
            }
        })
        for (String inputFile : inputFiles) {
            createSequenceDiagram(inputFile)
        }
    }

    private void createSequenceDiagram(String inFile) {
        def wsdServerUrl = "http://web-sequence-diagrams.ida.digital.cabinet-office.gov.uk/"

        //Build parameter string
        String text = new File("build/diagrams", inFile).getText('UTF-8')

        def data = "style=modern-blue&message=" +
                URLEncoder.encode(text, "UTF-8") +
                "&apiVersion=1&format=svg";

        // Send the request
        def url = new URL(wsdServerUrl);
        def conn = url.openConnection();
        conn.setDoOutput(true);
        def writer = new OutputStreamWriter(conn.getOutputStream());

        //write parameters
        writer.write(data);
        writer.flush();

        // Get the response
        def answer = new StringBuffer();
        def reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        def line;
        while ((line = reader.readLine()) != null) {
            answer.append(line);
        }
        writer.close();
        reader.close();

        def json = answer.toString();
        int start = json.indexOf("?svg=");
        int end = json.indexOf("\"", start);

        url = new URL(wsdServerUrl + json.substring(start, end));

        String outFile = inFile.substring(0, inFile.lastIndexOf('.')) + ".svg";
        File outputDir = new File("build/diagrams/out");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        def out = new BufferedOutputStream(new FileOutputStream(new File(outputDir, outFile)));
        def input = url.openConnection().getInputStream();
        byte[] buffer = new byte[1024];
        int numRead;
        long numWritten = 0;
        while ((numRead = input.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
            numWritten += numRead;
        }

        input.close();
        out.close();
    }
}
