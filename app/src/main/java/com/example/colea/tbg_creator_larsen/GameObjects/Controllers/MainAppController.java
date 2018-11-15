package com.example.colea.tbg_creator_larsen.GameObjects.Controllers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainAppController {

    //This controller handlers saving games and some misc functions

    //Safe a file
    public static void saveFile(Context context, String content, String fileName)
    {
        if(fileName.charAt(fileName.length()-1) == '\n')
        {
            fileName = fileName.substring(0, fileName.length()-1);
        }
        if(!content.isEmpty()) {
            File fileDirectory = context.getFilesDir();
            if (fileName.compareTo("GameNames") != 0) {
                FileOutputStream outputStream;
                try {
                    outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Update the GameNames to include the file
    public static void saveGameName(Context context, String content)
    {
        if(content.charAt(content.length()-1) == '\n')
        {
            content = content.substring(0, content.length()-1);
        }
        String[] ret = getGameNames(context);
        if(ret[0].compareTo("") != 0) {
            content = "@" + content;
        }

        File fileDirectory = context.getFilesDir();
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput("GameNames", Context.MODE_APPEND);
            outputStream.write(content.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    //Create a new GameNames file
    private static void makeNewGameNames(Context context, String content)
    {
        File fileDirectory = context.getFilesDir();
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput("GameNames", Context.MODE_APPEND);
            outputStream.write(content.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Delete all games contained withing GameNAmes
    public static void deleteAllGames(Context context)
    {
        File fileDirectory = context.getFilesDir();
        File file = new File(fileDirectory, "GameNames");
        file.delete();
    }

    //Delete fileName
    public static void deleteGame(Context context, String fileName)
    {
        File fileDirectory = context.getFilesDir();
        File file = new File(fileDirectory, fileName);
        file.delete();

        String[] gameNames = getGameNames(context);
        String newGameNames = "";

        deleteAllGames(context);

        for(String s : gameNames)
        {
            if(s.compareTo(fileName) != 0 && s.compareTo(fileName + "\n") != 0)
            {
                saveGameName(context, s);
            }
        }
    }

    //Test if string is an integer
    public static boolean stringIsInt(String s)
    {
        return s.matches("^[0-9]*$");
    }

    //Check in GameNames if a game with that name already exists
    public static boolean nameExists(Context context, String name)
    {
        String[] names = getGameNames(context);
        for(String s : names)
        {
            if(s.compareTo(name) == 0)
            {
                return true;
            }
        }
        return false;
    }

    //Returns the content of a file
    public static String getFileContent(Context context, String fileName)
    {
        String ret = "";
        File fileDirectory = context.getFilesDir();
        File file = new File(fileDirectory, fileName);
        if(file.exists())
        {
            FileInputStream inputStream;
            try
            {
                FileInputStream fileInputStream = new FileInputStream (file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ( (ret = bufferedReader.readLine()) != null )
                {
                    stringBuilder.append(ret + System.getProperty("line.separator"));
                }
                fileInputStream.close();
                ret = stringBuilder.toString();
                bufferedReader.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            FileOutputStream outputStream;
            try {
                outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                outputStream.write("".getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;

    }

    //http://instinctcoder.com/read-and-write-text-file-in-android-studio/
    //A lot of the reading come from this link
    public static String[] getGameNames(Context context)
    {
        String ret = "";
        File fileDirectory = context.getFilesDir();
        File file = new File(fileDirectory, "GameNames");
        if(file.exists())
        {
            FileInputStream inputStream;
            try
            {
                FileInputStream fileInputStream = new FileInputStream (file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ( (ret = bufferedReader.readLine()) != null )
                {
                    stringBuilder.append(ret + System.getProperty("line.separator"));
                }
                fileInputStream.close();
                ret = stringBuilder.toString();
                bufferedReader.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            FileOutputStream outputStream;
            try {
                outputStream = context.openFileOutput("GameNames", Context.MODE_PRIVATE);
                outputStream.write("".getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String[] games = ret.split("@");
        return games;

    }
}
