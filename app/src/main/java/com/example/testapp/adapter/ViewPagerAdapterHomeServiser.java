package com.example.testapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testapp.fragments.CekanjeFragmentServiser;
import com.example.testapp.fragments.HomeFragmentServiser;
import com.example.testapp.fragments.LocationFragment;
import com.example.testapp.fragments.ObavljenoFragmentServiser;
import com.example.testapp.fragments.ProfileFragment;
import com.example.testapp.listeners.LoginListener;

public class ViewPagerAdapterHomeServiser extends FragmentStateAdapter implements LoginListener {
    String email;
    String password;

    public ViewPagerAdapterHomeServiser(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;

        switch (position)
        {
            case 1:
                fragment = new CekanjeFragmentServiser();
                ((CekanjeFragmentServiser)fragment).loginListener = this;
                break;
            case 2:
                fragment = new ObavljenoFragmentServiser();
                ((ObavljenoFragmentServiser)fragment).loginListener = this;
                break;
            case 3:
                fragment = new ProfileFragment();
                ((ProfileFragment)fragment).loginListener = this;
                break;
            default:
                fragment = new HomeFragmentServiser();
                ((HomeFragmentServiser)fragment).loginListener = this;
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getLozinka() {
        return password;
    }

}
