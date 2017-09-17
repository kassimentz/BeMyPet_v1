package bemypet.com.br.bemypet_v1.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.pojo.Pet;

/**
 * Created by kassianesmentz on 17/09/17.
 */

public class SocialMediaUtils {

    public static void SharingPetToSocialMedia(String application, Context context, Pet pet) {

        /**
         * Intent shareIntent;
         Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
         String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
         OutputStream out = null;
         File file=new File(path);
         try {
         out = new FileOutputStream(file);
         bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
         out.flush();
         out.close();
         } catch (Exception e) {
         e.printStackTrace();
         }
         path=file.getPath();
         Uri bmpUri = Uri.parse("file://"+path);
         shareIntent = new Intent(android.content.Intent.ACTION_SEND);
         shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
         shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +getPackageName());
         shareIntent.setType("image/png");
         startActivity(Intent.createChooser(shareIntent,"Share with"));
         */

        Bitmap bmImg = null;
        Intent intent = new Intent();
        try {
            bmImg = Ion.with(context).load(pet.imagens.get(0)).asBitmap().get();

            intent.setAction(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, bmImg);
            intent.putExtra(Intent.EXTRA_TEXT,"Esse pet está a procura de um novo amigo " + "https://play.google.com/store/apps/details?id=" +context.getPackageName());

            boolean installed = checkAppInstall(application, context);
            if (installed) {
                intent.setPackage(application);
                context.startActivity(intent);
            } else {
                Utils.showToastMessage(context,  "O aplicativo necessita ser instalado antes.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    private static boolean checkAppInstall(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
