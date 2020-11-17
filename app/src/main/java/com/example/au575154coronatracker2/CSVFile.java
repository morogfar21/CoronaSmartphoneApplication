package com.example.au575154coronatracker2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ArrayList readFile() {
        ArrayList result = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String Line;
            while ((Line = reader.readLine()) != null) {
                String[] row = Line.split(";");
                result.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the Countries from CSV file:" + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream for FileRepository: " + e);
            }
        }

        return result;
    }

    /*
    public static class getflags {
    private int flags;
        public getflags(int flags) {
            this.flags = flags;
        }

        //Assign flag to corresponding country
        public int insertflag(String countryflags) {
            int flagImage = 0;

            switch (countryflags) {
                case "ca":
                    flagImage = R.drawable.ca;
                    break;
                case "cn":
                    flagImage = R.drawable.cn;
                    break;
                case "de":
                    flagImage = R.drawable.de;
                    break;
                case "dk":
                    flagImage = R.drawable.dk;
                    break;
                case "fi":
                    flagImage = R.drawable.fi;
                    break;
                case "in":
                    flagImage = R.drawable.in;
                    break;
                case "jp":
                    flagImage = R.drawable.jp;
                    break;
                case "no":
                    flagImage = R.drawable.no;
                    break;
                case "ru":
                    flagImage = R.drawable.ru;
                    break;
                case "se":
                    flagImage = R.drawable.se;
                    break;
                case "us":
                    flagImage = R.drawable.us;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + countryflags);
            }

            return flagImage;
        }
    }
    */
}
