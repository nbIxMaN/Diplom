package Controllers;

import Models.User;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.net.URI;
import java.net.URL;

/**
 * Created by alex on 28.02.17.
 */
public class S3Controller {

    AmazonS3 s3;
    String bucketName = "meddipappbucket";
    

    public S3Controller(){
        s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
    }

    private void logASE(AmazonServiceException ase){
        System.out.println("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    }

    private void logACE(AmazonClientException ace){
        System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    }

    public boolean createBucket(){
        try {
            s3.createBucket(bucketName);
            return true;
        }
        catch (AmazonServiceException ase) {
            logASE(ase);
            return false;
        } catch (AmazonClientException ace) {
            logACE(ace);
            return false;
        }
    }

    public URL getUploadLink(User user, String fileName){
        try {


            System.out.println("Generating pre-signed URL.");
            java.util.Date expiration = new java.util.Date();
            long milliSeconds = expiration.getTime();
            milliSeconds += 1000 * 60 * 60; // Add 1 hour.
            expiration.setTime(milliSeconds);

//            GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                    new GeneratePresignedUrlRequest(bucketName, String.format("%s/%s", user.getLogin(), fileName), HttpMethod.POST);
//            generatePresignedUrlRequest.setExpiration(expiration);

            URL url = s3.generatePresignedUrl(bucketName, String.format("%s/%s", user.getLogin(), fileName), expiration, HttpMethod.PUT);

            //UploadObject(url);

            System.out.println("Pre-Signed URL = " + url.toString());


//            s3.putObject(new PutObjectRequest(bucketName, String.format("%s/%s", user.getLogin(), fileName), file, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
            return url;
        }
        catch (AmazonServiceException ase) {
            logASE(ase);
        } catch (AmazonClientException ace) {
            logACE(ace);
        }
        return null;
    }

    public URI getFileLink(User user, String fileName){
        try {
            return s3.getObject(new GetObjectRequest(bucketName, String.format("%s/%s", user.getLogin(), fileName))).getObjectContent().getHttpRequest().getURI();
        }
        catch (AmazonServiceException ase) {
            logASE(ase);
        } catch (AmazonClientException ace) {
            logACE(ace);
        }
        return null;
    }

    public boolean makeFilePublic(User user, String fileName){
        try {
            s3.setObjectAcl(bucketName, String.format("%s/%s", user.getLogin(), fileName), CannedAccessControlList.PublicRead);
            return true;
        }
        catch (AmazonServiceException ase) {
            logASE(ase);
        } catch (AmazonClientException ace) {
            logACE(ace);
        }
        return false;
    }

    public boolean removeFile(User user, String fileName){
        try {
            s3.deleteObject(bucketName, String.format("%s/%s", user.getLogin(), fileName));
            return true;
        }
        catch (AmazonServiceException ase) {
            logASE(ase);
        } catch (AmazonClientException ace) {
            logACE(ace);
        }
        return false;
    }

    public boolean removeBucket(){
        try {
            s3.deleteBucket(bucketName);
            return true;
        }
        catch (AmazonServiceException ase) {
            logASE(ase);
        } catch (AmazonClientException ace) {
            logACE(ace);
        }
        return false;
    }
}
