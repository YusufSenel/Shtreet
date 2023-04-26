package com.example.shtreet;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TextRecognitionTest {


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


}