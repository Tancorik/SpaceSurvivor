package com.tancorikworld.spacesurvivor.data.demo;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.tancorikworld.spacesurvivor.application.SpaceApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawFileUtils {

    @WorkerThread
    public static String readTextFormRaw(int resourceId) {
        Context context = SpaceApplication.getAppComponent().getContext();
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = context.getResources().openRawResource(resourceId)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
