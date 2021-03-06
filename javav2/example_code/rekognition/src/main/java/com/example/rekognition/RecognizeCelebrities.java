// snippet-sourcedescription:[RecognizeCelebrities.java demonstrates how to recognize celebrities in a given image.]
// snippet-service:[Amazon Rekognition]
// snippet-keyword:[Java]
// snippet-keyword:[Amazon Rekognition]
// snippet-keyword:[Code Sample]
// snippet-sourcetype:[full-example]
// snippet-sourcedate:[6-10-2020]
// snippet-sourceauthor:[scmacdon - AWS]

/**
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.example.rekognition;

// snippet-start:[rekognition.java2.recognize_celebs.import]
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.core.SdkBytes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import software.amazon.awssdk.services.rekognition.model.RecognizeCelebritiesRequest;
import software.amazon.awssdk.services.rekognition.model.RecognizeCelebritiesResponse;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Celebrity;
// snippet-end:[rekognition.java2.recognize_celebs.import]

public class RecognizeCelebrities {

    public static void main(String[] args) {

        final String USAGE = "\n" +
                "RecognizeCelebrities -  recognize celebrities in a given image\n\n" +
                "Usage: RecognizeCelebrities <path>\n\n" +
                "Where:\n" +
                "path - the path to the image (i.e., C:\\AWS\\pic1.png) \n\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String sourceImage = args[0];

        Region region = Region.US_EAST_2;
        RekognitionClient rekClient = RekognitionClient.builder()
                .region(region)
                .build();

        System.out.println("Locating celebrities in " + sourceImage);
        recognizeAllCelebrities(rekClient, sourceImage);
    }

    // snippet-start:[rekognition.java2.recognize_celebs.main]
    public static void recognizeAllCelebrities(RekognitionClient rekClient, String sourceImage) {

        try {

            InputStream sourceStream = new FileInputStream(new File(sourceImage));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

            // Create an Image object for the source image
            Image souImage = Image.builder()
                .bytes(sourceBytes)
                .build();

            RecognizeCelebritiesRequest request = RecognizeCelebritiesRequest.builder()
                    .image(souImage)
                    .build();

            RecognizeCelebritiesResponse result = rekClient.recognizeCelebrities(request) ;

            List<Celebrity> celebs=result.celebrityFaces();
            System.out.println(celebs.size() + " celebrity(s) were recognized.\n");

            for (Celebrity celebrity: celebs) {
                System.out.println("Celebrity recognized: " + celebrity.name());
                System.out.println("Celebrity ID: " + celebrity.id());

                System.out.println("Further information (if available):");
                for (String url: celebrity.urls()){
                    System.out.println(url);
                }
                System.out.println();
            }
            System.out.println(result.unrecognizedFaces().size() + " face(s) were unrecognized.");

        } catch (RekognitionException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        // snippet-end:[rekognition.java2.recognize_celebs.main]
    }
}
