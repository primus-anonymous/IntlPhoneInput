package net.rimoto.android.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import net.rimoto.android.R;
import net.rimoto.android.activity.LoginActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.fragment_wizard_roaming)
public class WizardRoamingFragment extends Fragment {

    @Click
    protected void openSettingsBtn() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
        startActivity(intent);
    }



    @Click
    protected void gotoLoginBtn() {
        Intent intent = new Intent(getActivity(), LoginActivity_.class);
        startActivity(intent);
        getActivity().finish();
    }
}