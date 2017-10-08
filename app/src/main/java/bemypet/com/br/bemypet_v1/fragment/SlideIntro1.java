package bemypet.com.br.bemypet_v1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import agency.tango.materialintroscreen.SlideFragment;
import bemypet.com.br.bemypet_v1.MainActivity;
import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.utils.SharePreferencesInfo;
import bemypet.com.br.bemypet_v1.utils.Utils;

/**
 * Created by kassianesmentz on 08/10/17.
 */

public class SlideIntro1 extends SlideFragment {

    private View rootView;
    private CheckBox chkTermos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.intro1, container, false);
        chkTermos = (CheckBox) rootView.findViewById(R.id.chkTermos);
        return rootView;
    }

    @Override
    public boolean canMoveFurther() {
//        return super.canMoveFurther();
        if(chkTermos.isChecked()) {
            SharePreferencesInfo.updateIntroStatus(getActivity(), Boolean.TRUE);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        }

        return chkTermos.isChecked();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Você precisa aceitar os termos.";
    }

    @Override
    public int backgroundColor() {
        return R.color.bgMain;
    }

    @Override
    public int buttonsColor() {
        return R.color.orangeBorder;
    }
}
