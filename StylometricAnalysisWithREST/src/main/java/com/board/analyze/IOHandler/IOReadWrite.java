/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.IOHandler;

import com.board.analyze.model.Alias;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ITE
 */
public class IOReadWrite {
    
    public Alias convertTxtFileToAliasObj(String basePath, String fileName, String extension) throws FileNotFoundException, IOException {
        String userPostAsString = readTxtFileAsString(basePath, fileName, extension);
        String temp[] = null;
        Alias alias = new Alias();
        List<String> postList = new ArrayList<String>();
        List<String> timeList = new ArrayList<String>();
        alias.setUserID(fileName);
        if (userPostAsString.contains(IOProperties.DATA_SEPERATOR)) {
            temp = userPostAsString.split(IOProperties.DATA_SEPERATOR);
        } else {
            temp = new String[1];
            temp[0] = userPostAsString;
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].toString().matches("[0-9]{2}:[0-9]{2}:[0-9]{2}")
                    || temp[i].toString().length() == 8) {
                temp[i] = temp[i].toString() + "  ";
            }
            String date = temp[i].substring(0, 8);
            if (date.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}")) {
                timeList.add(date);
                postList.add(temp[i].substring(9, temp[i].length()));
            } else {
                continue;
            }
        }
        alias.setPostTime(timeList);
        alias.setPosts(postList);
        return alias;
    }
    
    public String readTxtFileAsString(String basePath, String fileName, String extension) throws FileNotFoundException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(basePath + "/" + fileName + extension);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            if (file.exists()) {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        }
        String a = stringBuilder.substring(0, (stringBuilder.length() - (IOProperties.DATA_SEPERATOR).length())).toString();
        return a;
    }
    
}
