package com.jameswolcott.omgandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by James on 7/26/2015.
 */
public class BRRead {
    private URL path;
    private static final int OFFSET = 2;
    private static final int COLUMNMOD = 253;
    private int emptyNum = 0;

    public BRRead(HttpURLConnection filePath) {
        path = filePath.getURL();
    }

    public double OpenFile() throws IOException {
        InputStreamReader fr = new InputStreamReader(path.openStream());
        BufferedReader textReader = new BufferedReader(fr);

        int count = 0;
        int numLines = readLines();
        String[] lastColumnString = new String[numLines];
        double[] lastColumn = new double[numLines];
        String[] eachLine = new String[numLines];

        for (int i = 0; i < numLines; i++) {
            eachLine[i] = textReader.readLine();
            //regEx to check for spaces and column divider text
            String pattern = "(\\s*)([\\|])";
            String plusPattern = "(\\s*)";
            String[] textData = eachLine[i].split(pattern + plusPattern);
            for (int j = 0; j < textData.length; j++) {
                if (j % COLUMNMOD == 0) {
                    lastColumnString[count] = textData[j];
                }
            }
            count++;
        }
        textReader.close();
        collectAndParse(lastColumnString, lastColumn);
       return sumAndAverager(numLines, lastColumn);
    }

    private void collectAndParse(String[] lastColumnString, double[] lastColumn) {
        for (int k = 0; k < lastColumnString.length; k++) {
            String temp = lastColumnString[k];
            if (k >= OFFSET) {
                double num = Double.parseDouble(temp);
                lastColumn[k] = num;
                if (lastColumn[k] > 0) {
                    System.out.println(lastColumn[k]);
                }
            } else {
                lastColumn[k] = 0;
            }
        }
    }

    private double sumAndAverager(double numLines, double[] lastColumn) {
        double sum = 0;
        for (int l = OFFSET; l < lastColumn.length; l++) {
            if (lastColumn[l] < 0) {
                emptyNum++;
            } else {
                sum = lastColumn[l] + sum;
            }
        }

        double average = sum / (numLines - (OFFSET + emptyNum));
        System.out.println("numlines = " + (numLines - (OFFSET + emptyNum)));
        return average;
    }

    protected int readLines() throws IOException {
        InputStreamReader fileToRead = new InputStreamReader(path.openStream());
        BufferedReader bf = new BufferedReader(fileToRead);
        String aLine;
        int numLines = 0;

        while ((aLine = bf.readLine()) != null) {
            numLines++;
        }

        bf.close();
        return numLines;
    }

}

