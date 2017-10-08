package bemypet.com.br.bemypet_v1;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import bemypet.com.br.bemypet_v1.fragment.SlideIntro1;
import bemypet.com.br.bemypet_v1.utils.SharePreferencesInfo;

/**
 * Created by kassianesmentz on 08/10/17.
 */

public class NovaIntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyIntroActivity();

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.orangeBorder)
                        .buttonsColor(R.color.textoCheckbox)
                        .neededPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                        .image(R.drawable.logo_simb)
                        .description(getResources().getString(R.string.intro))
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Localização permitida com sucesso.");
                    }
                }, "Permitir GPS"));


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.orangeBorder)
                        .buttonsColor(R.color.textoCheckbox)
                        .image(R.drawable.logo_simb)
                        .title("Login")
                        .description(getResources().getString(R.string.login))
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.orangeBorder)
                .buttonsColor(R.color.textoCheckbox)
                .image(R.drawable.logo_simb)
                .title(getResources().getString(R.string.tituloAdocao))
                .description(getResources().getString(R.string.textoAdocao))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.orangeBorder)
                .buttonsColor(R.color.textoCheckbox)
                .image(R.drawable.logo_simb)
                .title(getResources().getString(R.string.tituloDoacao))
                .description(getResources().getString(R.string.textoDoacao))
                .build());

        addSlide(new SlideIntro1());
    }

    private void verifyIntroActivity() {
        if(SharePreferencesInfo.isIntroActivityShown(getApplicationContext())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
